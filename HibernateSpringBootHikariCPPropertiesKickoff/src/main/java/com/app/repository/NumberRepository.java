package com.app.repository;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NumberRepository {

    private static final String SQL_INSERT = "INSERT INTO ints (nr) VALUES (?)";
    private final JdbcClient jdbcClient;

    public NumberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional
    public void insertRandomNumber() {
        jdbcClient.sql(SQL_INSERT)
                  .param(ThreadLocalRandom.current().nextInt())
                  .update();
    }
}
