package com.bookstore.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookstoreService(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    public void persistAuthorJdbcTemplate() {             
        
        jdbcTemplate.update("INSERT INTO author (age,genre,name) VALUES (?, ?, ?)"
                , 34, "History", "Joana Nimar");
    }
    
    @Transactional
    public void persistAuthorNamedParameterJdbcTemplate() {       

        Map<String, Object> params = new HashMap<>();
        params.put("p1", 54);
        params.put("p2", "Anthology");
        params.put("p3", "John Voliu");
        
        namedParameterJdbcTemplate.update("INSERT INTO author (age,genre,name) VALUES (:p1, :p2, :p3)",
                params);
    }

    @Transactional(readOnly = true)
    public void displayAuthor() {    
      
        jdbcTemplate.queryForObject(
                "SELECT name FROM author WHERE id = ?", String.class, 1);        
    }
}
