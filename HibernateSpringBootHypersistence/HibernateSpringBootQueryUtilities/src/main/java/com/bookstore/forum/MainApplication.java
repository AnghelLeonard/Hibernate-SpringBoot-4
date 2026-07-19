package com.bookstore.forum;

import com.bookstore.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner init(ForumService forumService) {
        return args -> {
            String title = "Debugging Hibernate queries";
            forumService.createPostWithComments(title, "Great read", "Very useful");

            // Running the query triggers the QueryStackTraceLogger (DEBUG) and the
            // JfrQueryLogger. Launch with -XX:StartFlightRecording to capture the
            // 'Hibernate/Query' JFR events.
            LOGGER.info("Found {} comments (see the stack-trace log above)",
                forumService.findCommentsWithPost(title).size());
        };
    }
}
