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
    
    // remain strictly JPA-compliant and portable, we still need a FROM clause
    @Query(value = """                   
                 SELECT cast(function('cos', cast(function('radians', 45.0) AS Double)) AS Double)
            """)
    double callCosRadians();
        
    // SELECT b.title, b.status, b.price, - doesn't work
    //               b.price * (1 - (field(b.status, 'bestseller', 'featured', 'standard', 'clearance') - 1) * 0.10) AS promoPrice
    //               FROM Book b ORDER BY promoPrice ASC
    
    // SELECT b.title, b.status, b.price, - doesn't work
    //               b.price * (1 - (function('field', b.status, 'bestseller', 'featured', 'standard', 'clearance') - 1) * 0.10) AS promoPrice
    //               FROM Book b ORDER BY promoPrice ASC
    
    // SELECT b.title, b.status, b.price, - works
    //               b.price * (1 - (function('field' as Integer, b.status, 'bestseller', 'featured', 'standard', 'clearance') - 1) * 0.10) AS promoPrice
    //               FROM Book b ORDER BY promoPrice ASC
    
    // use cast(function()) to indicate the returned data type to Hibernate
    @Query(value = """
            SELECT b.title, b.status, b.price,
                   b.price * (1 - (cast(function('field', b.status, 'bestseller', 'featured', 'standard', 'clearance')  as Integer) - 1) * 0.10) AS promoPrice
                   FROM Book b ORDER BY promoPrice ASC      
            """)            
    List<Object[]> callField();    
        
    // SELECT apply_discount(b.price, 10) FROM Book b - works, but Hibernate doesn't know the returned data type
    // SELECT sum(apply_discount(b.price, 10)) FROM Book b - works, but Hibernate doesn't know the returned data type
                                                          
    // SELECT (apply_discount(b.price, 10) + 5) FROM Book b - doesn't work
    // SELECT (function('apply_discount', b.price, 10) + 5) FROM Book b - doesn't work
    
    // SELECT (function('apply_discount' as Integer, b.price, 10) + 5) FROM Book b - work
    
    // use function() to indicate the returned data type to Hibernate
    @Query(value = """                   
            SELECT (cast(function('apply_discount', b.price, 10) AS Integer) + 5) FROM Book b
            """)
    List<Integer> callApplyDiscount();
    
    // SELECT slugify(b.title) FROM Book b WHERE apply_discount(b.price, 10) > 30 - doesn't work
    // SELECT slugify(b.title) FROM Book b WHERE function('apply_discount' as Integer, b.price, 10) > 30 - works, but is not strictly JPA-compliant and portable 
    
    // SELECT function('slugify' as String, b.title) FROM Book b - works
    // WHERE function('apply_discount' as Integer, b.price, 10) > 25
    
    // remain strictly JPA-compliant and portable 
    @Query(value = """                   
            SELECT cast(function('slugify', b.title) AS String) 
                   FROM Book b 
                   WHERE cast(function('apply_discount', b.price, 10) AS Integer) > 25
            """)
    List<String> callApplyDiscountInWhere();
}
