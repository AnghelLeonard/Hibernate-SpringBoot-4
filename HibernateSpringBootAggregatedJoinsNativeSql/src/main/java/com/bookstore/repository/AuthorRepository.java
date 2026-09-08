package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
        
    @NativeQuery("""
           SELECT 
              a.id as author_id,a.name,a.age,a.genre,
              t.id as tag_id, t.tag,   
              b.id as book_id,b.title,b.isbn,
              p.id as publisher_id,p.company,p.address,
              rw.id as reviewer_id,rw.reviewer_age,rw.reviewer_name,
              r.id as review_id,r.script,r.language
           FROM author a             
             LEFT JOIN book b ON b.author_id=a.id
             LEFT JOIN review r ON r.book_id=b.id                 
             LEFT JOIN review_reviewer rr ON rr.review_id=b.id
             LEFT JOIN reviewer rw ON rr.reviewer_id=rw.id
             LEFT JOIN author_tag at ON at.author_id=a.id
             LEFT JOIN tag t ON t.id=at.tag_id
             LEFT JOIN publisher p ON b.publisher_id=p.id
           ORDER BY a.id
           """)
    List<Object[]> findAuthorsBooksTagsPublishersReviewersReviews();            
}
