package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // concat() is part of JPQL
    // SELECT 1 WHERE concat('Hibernate',' ','is',' ','fun!') = 'Hibernate is fun!' 
    @Query(value = """                   
                 SELECT concat('Hibernate',' ','is',' ','fun!')
            """)
    String callConcat();
    
    // SELECT cos(radians(45.0)) - works
    // SELECT function('cos', function('radians', 45.0)) - works
    
    // remain strictly JPA-compliant and portable 
    @Query(value = """                   
                 SELECT function('cos' as Double, function('radians' as Double, 45.0))
            """)
    double callCosRadians();
    
    // SELECT field('a', 'b', 'c', 'a', 'd') - this works, but Hibernate doesn't know the returned data type
    // SELECT (field('a', 'b', 'c', 'a', 'd') + field('d', 'b', 'c', 'a', 'd')) - doesn't work
    
    // use function() to indicate the returned data type to Hibernate
    @Query(value = """                   
            SELECT (function('field' as Integer, 'a', 'b', 'c', 'a', 'd') 
                   + function('field' as Integer, 'd', 'b', 'c', 'a', 'd'))
            """)
    int callField();
        
    // SELECT apply_discount(b.price, 10) FROM Book b - works, but Hibernate doesn't know the returned data type
    // SELECT sum(apply_discount(b.price, 10)) FROM Book b - works, but Hibernate doesn't know the returned data type
                                                          // MySQL uses implicit type conversion (coercion).
    // SELECT (apply_discount(b.price, 10) + 5) FROM Book b - doesn't work
    // SELECT (function('apply_discount', b.price, 10) + 5) FROM Book b - doesn't work
    
    // use function() to indicate the returned data type to Hibernate
    @Query(value = """                   
            SELECT (function('apply_discount' as Integer, b.price, 10) + 5) FROM Book b
            """)
    List<Integer> callApplyDiscount();
    
    // SELECT slugify(b.title) FROM Book b WHERE apply_discount(b.price, 10) > 30 - doesn't work
    // SELECT slugify(b.title) FROM Book b WHERE function('apply_discount' as Integer, b.price, 10) > 30 - works, but is not strictly JPA-compliant and portable 
    
    // remain strictly JPA-compliant and portable 
    @Query(value = """                   
            SELECT function('slugify' as String, b.title) FROM Book b 
                   WHERE function('apply_discount' as Integer, b.price, 10) > 25
            """)
    List<String> callApplyDiscountInWhere();
}
