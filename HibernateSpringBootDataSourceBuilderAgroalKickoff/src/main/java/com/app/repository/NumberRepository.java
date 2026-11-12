package com.app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NumberRepository {

    private static final Logger logger = LoggerFactory.getLogger(NumberRepository.class);
    private static final String SQL_INSERT = "INSERT INTO ints (nr) VALUES (?)";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertRandomNumber() {
        Session hibernateSession = entityManager.unwrap(Session.class);
        hibernateSession.doWork(this::insertSample);
    }

    private void insertSample(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
            int randomValue = ThreadLocalRandom.current().nextInt();
            preparedStatement.setInt(1, randomValue);
            preparedStatement.executeUpdate();

            logger.info("Inserted {} on thread {}", randomValue, Thread.currentThread().getName());
        }
    }
}

