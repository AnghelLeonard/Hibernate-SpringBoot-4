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
       
    @Query(value = """                   
                 SELECT concat('Hibernate',' ','is',' ','fun!')
            """)
    String callConcat();
           
    // use function() only to remain strictly JPA-compliant and portable 
    @Query(value = """                   
                 SELECT cos(radians(45.0))
            """)
    double callCosRadians();
    
    // use function() only to remain strictly JPA-compliant and portable 
    @Query(value = """
            SELECT b.title, b.price,
                b.price * width_bucket(b.price, 20, 65, 3) AS loyaltyPoints
                FROM Book b ORDER BY loyaltyPoints DESC
            """)            
    List<Object[]> calculateBookLoyaltyPoints();
   
    // use function() only to remain strictly JPA-compliant and portable 
    @Query(value = """                   
            SELECT (apply_discount(b.price, 10) + 5) FROM Book b
            """)
    List<Integer> callApplyDiscount();
        
    // use function() only to remain strictly JPA-compliant and portable 
    @Query(value = """                   
            SELECT slugify(b.title) FROM Book b 
                   WHERE apply_discount(b.price, 10) > 25
            """)
    List<String> callApplyDiscountInWhere();
}
