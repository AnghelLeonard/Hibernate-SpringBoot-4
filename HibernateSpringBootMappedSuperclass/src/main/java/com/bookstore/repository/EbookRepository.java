package com.bookstore.repository;

import com.bookstore.entity.Ebook;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface EbookRepository extends BookBaseRepository<Ebook> {
}
