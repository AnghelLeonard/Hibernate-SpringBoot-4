package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
     
    @Override
    default Optional<Book> findById(Long id) {
        Specification<Book> byId = (root, query, cb) -> cb.equal(root.get("id"), id);
        Specification<Book> notDeleted = (root, query, cb) -> cb.isNull(root.get("deleted"));

        return findOne(Specification.where(byId).and(notDeleted));
    }

    @Query(value = "SELECT b FROM Book b WHERE b.deleted IS NOT NULL")
    List<Book> findAllOnlyDeleted();
    
    @Transactional
    @Query(value = "UPDATE Book b SET b.deleted = NULL WHERE b.author.id = ?1")
    @Modifying    
    public void restoreByAuthorId(Long id);
    
    @Transactional
    @Query(value = "UPDATE Book b SET b.deleted = NULL WHERE b.id = ?1")
    @Modifying    
    public void restoreById(Long id);
}
