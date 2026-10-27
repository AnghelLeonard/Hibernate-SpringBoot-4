package com.bookstore.repository;
 
import java.util.List;
import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.dto.AuthorNameAge;
import com.bookstore.dto.AuthorNameAgeDefaults;
import org.springframework.data.jpa.repository.Query;

@Repository
 @Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {
   
    @Query("SELECT a.name AS name, a.age AS age FROM Author a WHERE a.age >= ?1")
    List<AuthorNameAge> fetchByAge(int age);
    
    @Query("SELECT a.name AS name, a.age AS age FROM Author a WHERE a.age >= ?1")
    List<AuthorNameAgeDefaults> fetchByAgeDefaults(int age);
}
