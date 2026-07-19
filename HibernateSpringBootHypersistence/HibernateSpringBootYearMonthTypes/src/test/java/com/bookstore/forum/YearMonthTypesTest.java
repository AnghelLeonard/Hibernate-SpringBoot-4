package com.bookstore.forum;

import com.bookstore.forum.config.TestDataSourceConfiguration;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Round-trips {@link YearMonth} on MySQL through both Hypersistence Utils
 * mappings: {@code YearMonthIntegerType} (a compact {@code mediumint} like
 * {@code 202607}) and {@code YearMonthDateType} (a {@code date} on the first of
 * the month). Both come back as the exact same {@link YearMonth}. A derived
 * query over the integer-mapped column confirms parameter binding works too.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ForumService.class, TestDataSourceConfiguration.class})
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class YearMonthTypesTest {

    @Autowired
    private ForumService forumService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void cleanUp() {
        jdbcTemplate.update("delete from year_month_post_details");
    }

    @Test
    public void integerAndDateMappingsRoundTripTheSameYearMonth() {
        PostDetails details = new PostDetails("Mapping YearMonth on MySQL");
        details.setPublishedOn(YearMonth.of(2026, 7));
        details.setArchivedOn(YearMonth.of(2026, 12));

        Long id = forumService.save(details).getId();

        // The integer mapping stores a compact numeric (202607), the date mapping
        // stores the first day of the month (2026-12-01) — verified at the raw
        // column level.
        Integer publishedOnColumn = jdbcTemplate.queryForObject(
            "select published_on from year_month_post_details where id = ?", Integer.class, id);
        String archivedOnColumn = jdbcTemplate.queryForObject(
            "select cast(archived_on as char) from year_month_post_details where id = ?", String.class, id);
        assertEquals(202607, publishedOnColumn);
        assertEquals("2026-12-01", archivedOnColumn);

        PostDetails loaded = forumService.findById(id);
        assertEquals(YearMonth.of(2026, 7), loaded.getPublishedOn());
        assertEquals(YearMonth.of(2026, 12), loaded.getArchivedOn());
    }

    @Test
    public void derivedQueryBindsYearMonthParameter() {
        forumService.save(withPublishedOn("Published in July", YearMonth.of(2026, 7)));
        forumService.save(withPublishedOn("Published in August", YearMonth.of(2026, 8)));

        List<PostDetails> july = forumService.findByPublishedOn(YearMonth.of(2026, 7));

        assertEquals(1, july.size());
        assertEquals("Published in July", july.get(0).getTitle());
    }

    private static PostDetails withPublishedOn(String title, YearMonth publishedOn) {
        PostDetails details = new PostDetails(title);
        details.setPublishedOn(publishedOn);
        return details;
    }
}
