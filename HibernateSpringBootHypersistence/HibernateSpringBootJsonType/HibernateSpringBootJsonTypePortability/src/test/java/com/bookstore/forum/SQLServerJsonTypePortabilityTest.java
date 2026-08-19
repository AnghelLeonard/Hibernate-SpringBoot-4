package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import com.bookstore.forum.config.EnabledIfDatabaseAvailable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "test.database=SQLSERVER",
    "spring.sql.init.platform=sqlserver"
})
@EnabledIfDatabaseAvailable(DatabaseType.SQLSERVER)
class SQLServerJsonTypePortabilityTest extends AbstractJsonTypePortabilityTest {
}
