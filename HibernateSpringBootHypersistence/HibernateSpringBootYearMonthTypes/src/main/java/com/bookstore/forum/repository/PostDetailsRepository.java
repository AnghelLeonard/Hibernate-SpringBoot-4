package com.bookstore.forum.repository;

import com.bookstore.forum.entity.PostDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface PostDetailsRepository extends JpaRepository<PostDetails, Long> {

    /**
     * Derived query over a {@link YearMonth} attribute. Hibernate binds the
     * parameter through {@link io.hypersistence.utils.hibernate.type.basic.YearMonthIntegerType},
     * so the comparison happens against the stored integer form &mdash; no manual
     * year/month arithmetic in the query.
     */
    List<PostDetails> findByPublishedOn(YearMonth publishedOn);
}
