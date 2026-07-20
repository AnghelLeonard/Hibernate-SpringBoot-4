package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.BatchedPost;
import com.bookstore.forum.entity.PooledPost;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Two mappings of the same {@code batch_seq_post} table, sharing the same
 * {@code batch_seq_post_seq} sequence: {@code Post} (plain {@code SEQUENCE},
 * {@code allocationSize = 1}) for the OLTP path and {@code BatchedPost}
 * ({@code @BatchSequence}) for the import path.
 *
 * <p>The tests prove the trade the pair buys you: one sequence roundtrip for a
 * whole import chunk <em>without</em> inflating the sequence's increment, so a
 * plain database script can keep calling {@code nextval} on it and the
 * identifiers stay dense. The {@code PooledPost} counter-example shows what the
 * classic {@code pooled} optimizer costs instead — an {@code INCREMENT BY 50}
 * sequence and blocks cached per {@code SessionFactory}, which two
 * {@code EntityManagerFactory} objects turn into a permanent gap.</p>
 */
@SpringBootTest(properties = "test.database=POSTGRESQL")
@EnabledIfDatabaseAvailable(DatabaseType.POSTGRESQL)
@Import(TestDataSourceConfiguration.class)
@ActiveProfiles("test")
class BatchSequenceGeneratorTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        forumService.deleteAll();
    }

    @Test
    public void oltpInsertCostsOneSequenceCallPerRow() {
        SQLStatementCountValidator.reset();

        List<Long> ids = IntStream.rangeClosed(1, 3)
            .mapToObj(i -> forumService.createPost("OLTP post " + i).getId())
            .toList();

        // One nextval SELECT and one INSERT per row: fine for a single-row
        // transaction, hopeless for an import.
        SQLStatementCountValidator.assertSelectCount(3);
        SQLStatementCountValidator.assertInsertCount(3);

        assertConsecutive(ids);
    }

    // tag::batch-import[]
    @Test
    public void batchImportCostsOneSequenceCallForTheWholeFetchSize() {
        SQLStatementCountValidator.reset();

        List<Long> ids = forumService
            .importPosts(IntStream.rangeClosed(1, 10).mapToObj(i -> "Imported post " + i).toList())
            .stream()
            .map(BatchedPost::getId)
            .toList();

        // fetchSize = 10 identifiers in ONE recursive-CTE roundtrip, and the ten
        // inserts collapse into a single JDBC batch.
        SQLStatementCountValidator.assertSelectCount(1);
        SQLStatementCountValidator.assertInsertCount(1);

        assertConsecutive(ids);
    }
    // end::batch-import[]

    /**
     * The interoperability payoff: because {@code batch_seq_post_seq} is still a
     * normal {@code INCREMENT BY 1} sequence, a script that knows nothing about
     * Hibernate can interleave its own {@code nextval} calls with the
     * application's and the identifiers stay dense.
     */
    // tag::interop[]
    @Test
    public void theSameSequenceServesHibernateAndPlainDatabaseScripts() {
        long fromHibernate = forumService.createPost("Written by the application").getId();
        long fromScript = forumService.insertPostAsADatabaseScriptWould("Written by an ETL job");
        long fromHibernateAgain = forumService.createPost("Written by the application again").getId();

        assertEquals(fromHibernate + 1, fromScript,
            "A non-Hibernate client must get the very next sequence value");
        assertEquals(fromScript + 1, fromHibernateAgain,
            "And the application must resume right after it");

        assertEquals(3, forumService.findAllPostsOrderedById().size());
    }
    // end::interop[]

    /**
     * The same fact, read straight off the catalog: the shared sequence is
     * {@code INCREMENT BY 1} (usable by anyone), the pooled one is
     * {@code INCREMENT BY 50} (a Hibernate-only sequence — any other client that
     * calls {@code nextval} takes one identifier and burns forty-nine).
     */
    // tag::increments[]
    @Test
    public void onlyThePooledOptimizerRedefinesTheSequenceIncrement() {
        assertEquals(1L, incrementOf("batch_seq_post_seq"));
        assertEquals(50L, incrementOf("pooled_post_seq"));
    }
    // end::increments[]

    /**
     * The pooled block is cached per {@code SessionFactory}, so a second
     * application instance — here a second {@code EntityManagerFactory} over the
     * same database — reserves a block of its own instead of continuing where
     * the first one left off. Each instance keeps whatever it did not use, and
     * nothing ever fills the hole in between.
     */
    @Test
    public void pooledOptimizerLeavesGapsAcrossEntityManagerFactories() {
        try (EntityManagerFactory firstInstance = newEntityManagerFactory();
             EntityManagerFactory secondInstance = newEntityManagerFactory()) {

            List<Long> fromFirst = persistPooledPosts(firstInstance, "First application instance", 2);
            List<Long> fromSecond = persistPooledPosts(secondInstance, "Second application instance", 2);

            long lastOfFirst = fromFirst.get(fromFirst.size() - 1);
            long firstOfSecond = fromSecond.get(0);

            // The first instance holds a block of 50 and used two of it; the
            // second instance starts a whole block later, and the 48 identifiers
            // the first one is sitting on are lost the moment it shuts down.
            assertEquals(lastOfFirst + 50, firstOfSecond,
                () -> "Expected a whole reserved block between the identifiers assigned by the two "
                    + "EntityManagerFactory instances, but got " + fromFirst + " and " + fromSecond);
        }
    }

    private long incrementOf(String sequenceName) {
        return jdbcTemplate.queryForObject(
            "select increment_by from pg_sequences where sequencename = ?", Long.class, sequenceName);
    }

    private List<Long> persistPooledPosts(EntityManagerFactory entityManagerFactory, String title, int count) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            List<Long> ids = new ArrayList<>(count);
            for (int i = 1; i <= count; i++) {
                entityManager.getTransaction().begin();
                PooledPost post = new PooledPost(title + " #" + i);
                entityManager.persist(post);
                entityManager.getTransaction().commit();
                ids.add(post.getId());
            }
            return ids;
        }
    }

    /**
     * A second {@code EntityManagerFactory} over the same {@link DataSource},
     * standing in for a second application instance. The schema already exists,
     * hence {@code hbm2ddl.auto = none}.
     */
    private EntityManagerFactory newEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.bookstore.forum.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaPropertyMap(Map.of("hibernate.hbm2ddl.auto", "none"));
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    private static void assertConsecutive(List<Long> ids) {
        for (int i = 1; i < ids.size(); i++) {
            assertEquals(ids.get(i - 1) + 1, ids.get(i),
                () -> "Expected gapless identifiers, but got " + ids);
        }
    }
}
