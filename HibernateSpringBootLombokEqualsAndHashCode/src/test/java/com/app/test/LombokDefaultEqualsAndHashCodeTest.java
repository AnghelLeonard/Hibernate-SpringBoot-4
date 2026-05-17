package com.app.test;

import com.app.LombokDefaultBook;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.junit.jupiter.api.AssertionsKt.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LombokDefaultEqualsAndHashCodeTest {

    @Autowired
    private TestEntityManager entityManager;

    private static final LombokDefaultBook book = new LombokDefaultBook();
    private static final Set<LombokDefaultBook> books = new HashSet<>();

    @BeforeAll
    public static void setUp() {
        book.setTitle("Modern History");
        book.setIsbn("001-100-000-111");

        books.add(book);
    }

    @Test
    // Find in Set the book that has never been persisted
    // State transition at assertion  point: NEW
    public void A_givenBookInSetWhenContainsThenTrue() throws Exception {

        assertTrue(books.contains(book));
    }

    @Test
    // Find in Set the book after it was persisted
    // State transition at first assertion point: NEW
    // State transition at second and third assertion point: MANAGED    
    public void B_givenBookWhenPersistThenSuccess() throws Exception {

        assertNull(book.getId());

        entityManager.persistAndFlush(book);
        assertNotNull(book.getId());
        
        assertTrue(books.contains(book));
    }

    @Test
    // Find in Set the book after merge() was called - SELECT and UPDATE statement   
    // State transition at assertion point: MANAGED
    public void C_givenBookWhenMergeThenSuccess() throws Exception {

        book.setTitle("New Modern History");
        LombokDefaultBook mergedBook = entityManager.merge(book);
        entityManager.flush();
        
        assertTrue(books.contains(mergedBook)); // this test fails
    }

    @Test
    // Find in Set the book after find() was called - SELECT statement   
    // State transition at assertion point: MANAGED
    public void D_givenBookWhenFindThenSuccess() throws Exception {

        LombokDefaultBook foundBook = entityManager
                .find(LombokDefaultBook.class, book.getId());
        entityManager.flush();

        assertTrue(books.contains(foundBook)); // this test fails
    }

    @Test
    // Find in Set the book after detach() was called
    // State transition at assertion point: DETACHED    
    public void E_givenBookWhenFindAndDetachThenSuccess() throws Exception {

        LombokDefaultBook foundBook = entityManager
                .find(LombokDefaultBook.class, book.getId());
        entityManager.detach(foundBook);
        
        assertTrue(books.contains(foundBook)); // this test fails
    }

    @Test
    // Find in Set the book after remove() was called - DELETE statement
    // State transition at assertion points: REMOVED    
    public void F_givenBookWhenFindAndRemoveThenSuccess() throws Exception {

        LombokDefaultBook foundBook = entityManager
                .find(LombokDefaultBook.class, book.getId());
        entityManager.remove(foundBook);
        entityManager.flush();

        assertTrue(books.contains(foundBook)); // this test fails

        books.remove(foundBook);

        assertFalse(books.contains(foundBook));
    }
}
