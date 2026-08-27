package com.bookstore.service;

import com.bookstore.dao.Dao;
import com.bookstore.dto.AuthorDtoNoSetters;
import com.bookstore.dto.AuthorDtoWithSetters;
import com.bookstore.dto.AuthorRecord;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final Dao dao;

    public BookstoreService(Dao dao) {
        this.dao = dao;
    }

    public List<AuthorDtoNoSetters> fetchAuthorsNoSetters() {

        return dao.fetchAuthorsNoSetters();
    }
    
    public List<AuthorDtoWithSetters> fetchAuthorsWithSetters() {

        return dao.fetchAuthorsWithSetters();
    }
    
    public List<AuthorRecord> fetchAuthorsRecord() {

        return dao.fetchAuthorsRecord();
    }
}
