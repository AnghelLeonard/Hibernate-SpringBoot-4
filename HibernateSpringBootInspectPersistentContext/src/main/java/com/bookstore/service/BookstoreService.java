package com.bookstore.service;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository, EntityManager entityManager) {

        this.authorRepository = authorRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void sqlOperations() {
        briefOverviewOfPersistentContextContent("Start of transaction, PC should be empty", false);

        Author author = authorRepository.findByName("Joana Nimar");
        briefOverviewOfPersistentContextContent("After author 'Joana Nimar' was fetched via a SELECT", false);

        author.getBooks().get(0).setIsbn("not available");
        briefOverviewOfPersistentContextContent("After updating the first book of 'Joana Nimar'", false);

        authorRepository.delete(author);
        authorRepository.flush();
        briefOverviewOfPersistentContextContent("After deleting the author and flushing the changes", false);

        Author newAuthor = new Author();
        newAuthor.setId(1L);
        newAuthor.setName("Alicia Tom");
        newAuthor.setAge(38);
        newAuthor.setGenre("Anthology");

        Book book = new Book();
        book.setId(1L);
        book.setIsbn("001-AT");
        book.setTitle("The book of swords");

        newAuthor.addBook(book); // use addBook() helper

        authorRepository.saveAndFlush(newAuthor);
        briefOverviewOfPersistentContextContent("After creating and saving a new author ('Alicia Tom')", false);
    }

    private void briefOverviewOfPersistentContextContent(String checkpointLabel, boolean loadedStateSQL) {

        System.out.println("\n===================================================================");
        System.out.println(checkpointLabel);
        System.out.println("===================================================================");

        SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
        org.hibernate.engine.spi.PersistenceContext persistenceContext = session.getPersistenceContext();

        int managedEntitiesSize = persistenceContext.getNumberOfManagedEntities();
        int collectionEntriesSize = persistenceContext.getCollectionEntriesSize();

        System.out.println("Session is open: " + session.isOpen());
        System.out.println("Session ID: " + session.getSessionIdentifier().toString());
        System.out.println("Total number of managed entities: " + managedEntitiesSize);
        System.out.println("Total number of collection entries: " + collectionEntriesSize);
        System.out.println("Has pending changes (dirty checking): " + session.isDirty() + "\n");

        if (managedEntitiesSize > 0) {
            System.out.println("\nEntities entries (" + managedEntitiesSize + "):");

            // reentrantSafeEntityEntries() prevents ConcurrentModificationException during inspection        
            for (Map.Entry<Object, EntityEntry> entry : persistenceContext.reentrantSafeEntityEntries()) {

                EntityEntry entityEntry = entry.getValue();

                String safeKey = entry.getKey().getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(entry.getKey()));
                Object[] loadedState = entityEntry.getLoadedState();
                String state = loadedState == null
                        ? "N/A (Read-Only or Transient)"
                        : (loadedStateSQL ? Arrays.toString(loadedState) 
                        : exposeLoadedStateWithoutTriggeringSQL(loadedState));
                                
                System.out.printf("""
                              
        +------------------------------------------------------------------
        | Entity key  : %s
        | Entity name : %s (ID: %s)
        | Status      : %s
        | Loaded state: %s
        +------------------------------------------------------------------
        """,
                        safeKey,
                        entityEntry.getEntityName(),
                        entityEntry.getId(),
                        entityEntry.getStatus(),
                        state
                );
            }
        }

        if (collectionEntriesSize > 0) {
            System.out.println("\nCollection entries (" + collectionEntriesSize + "):");
            persistenceContext.forEachCollectionEntry((k, v) -> {
                String safeKey = k.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(k));
                String role = v.getRole() != null ? v.getRole() : "N/A (Unattached)";
                Object loadedKey = v.getLoadedKey();
                boolean initialized = k.wasInitialized();

                System.out.printf("""
                                  
                +------------------------------------------------------------------
                | Collection key : %s
                | Role           : %s
                | Owner ID       : %s
                | Initialized    : %s
                | Dirty          : %s
                +------------------------------------------------------------------
                """,
                        safeKey,
                        role,
                        loadedKey,
                        initialized,
                        initialized && k.isDirty()
                );
            }, false);
        }

        System.out.println("===================================================================\n");
    }

    public static String exposeLoadedStateWithoutTriggeringSQL(Object[] loadedState) {
        
        if (loadedState == null) {
            return "N/A (Read-Only or Transient)";
        }

        StringBuilder sb = new StringBuilder("[");
        
        for (int i = 0; i < loadedState.length; i++) {
            
            if (i > 0) {
                sb.append(", ");
            }
            
            Object val = loadedState[i];

            if (val == null) {
                sb.append("null");
            } else if (!Hibernate.isInitialized(val)) {
                // prevents triggering a SELECT on uninitialized collections or lazy proxies
                switch (val) {
                    case PersistentCollection<?> pc -> sb.append("<Uninitialized Collection: ").append(pc.getRole()).append(">");
                    case HibernateProxy proxy -> sb.append("<Uninitialized Proxy: ")
                            .append(proxy.getHibernateLazyInitializer().getEntityName()).append("#")
                            .append(proxy.getHibernateLazyInitializer().getIdentifier()).append(">");
                    default -> sb.append("<Uninitialized: ").append(val.getClass().getSimpleName()).append(">");
                }
            } else if (val instanceof PersistentCollection<?>) {
                // even if initialized, avoid dumping huge collection contents into logs
                sb.append("<Initialized ").append(val.getClass().getSimpleName()).append(">");
            } else {
                sb.append(val);
            }
        }
        
        sb.append("]");
        
        return sb.toString();
    }
}
