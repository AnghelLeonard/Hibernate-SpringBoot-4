package com.bookstore.repository;

import com.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findFirstByGenre(String genre);

    @NativeQuery(value = """                 
                         SELECT tx.trx_id FROM information_schema.innodb_trx tx 
                         WHERE tx.trx_mysql_thread_id = connection_id()
                         """)
    public Object getTransactionId();
}
