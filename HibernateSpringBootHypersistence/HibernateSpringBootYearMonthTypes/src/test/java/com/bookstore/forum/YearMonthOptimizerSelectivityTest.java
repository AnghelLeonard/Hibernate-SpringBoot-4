package com.bookstore.forum;

import com.bookstore.forum.config.DatabaseType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;

/**
 * Compares how the PostgreSQL and MySQL query optimizers estimate the number of
 * matching rows for the two {@code YearMonth} mappings offered by Hypersistence
 * Utils, and how those estimates hold up against the actual row counts:
 *
 * <ul>
 *   <li>{@code YearMonthIntegerType} stores the month as the integer
 *       {@code year * 100 + month} (e.g. {@code 202607}). This encoding is
 *       <em>not contiguous</em>: there are 88 impossible values
 *       ({@code 202613}&hellip;{@code 202700}) between every December and the
 *       following January, so a range that crosses a year boundary spans a much
 *       wider integer interval than the number of real months it contains.</li>
 *   <li>{@code YearMonthDateType} stores it as the {@code date} on the first of
 *       the month (e.g. {@code 2026-07-01}). The domain is linear (no
 *       year-boundary discontinuity), though only one day per month is used.</li>
 * </ul>
 *
 * <p>The table stores the <em>same</em> year-month twice per row (once as
 * {@code int}, once as {@code date}), so {@code IN}, {@code >}, {@code <} and
 * {@code BETWEEN} predicates can be compared apples-to-apples. Every query is run
 * through {@code EXPLAIN ANALYZE} (the <em>actual</em> execution plan on both
 * databases), and the plan text is printed so the estimated vs. actual row
 * counts can be read off directly.</p>
 *
 * <p><strong>Disabled by default</strong> — it seeds tens of thousands of rows
 * and is meant to be run on demand. Enable it with
 * {@code mvn test -Dtest=YearMonthOptimizerSelectivityTest -DyearMonthOptimizer=true}.</p>
 */
@EnabledIfSystemProperty(named = "yearMonthOptimizer", matches = "true")
public class YearMonthOptimizerSelectivityTest {

    private static final int FIRST_YEAR = 2000;
    private static final int LAST_YEAR = 2049;          // 50 years x 12 = 600 distinct year-months
    private static final int ROWS_PER_MONTH = 100;      // 600 x 100 = 60_000 rows

    @Test
    public void postgresql() throws SQLException {
        // On PostgreSQL the estimate comes from pg_statistic (histogram), regardless
        // of whether the column is indexed, so one indexed run is enough.
        run(DatabaseType.POSTGRESQL, true);
    }

    @Test
    public void mysqlWithIndex() throws SQLException {
        // Indexed column: MySQL uses index dives (it probes the B-tree), so the
        // range estimates are exact whatever the encoding.
        run(DatabaseType.MYSQL, true);
    }

    @Test
    public void mysqlWithHistogram() throws SQLException {
        // No index: MySQL falls back to a column histogram, which is where the
        // non-contiguous integer encoding can distort the range estimates.
        run(DatabaseType.MYSQL, false);
    }

    private void run(DatabaseType databaseType, boolean createIndexes) throws SQLException {
        DataSource dataSource = databaseType.provider().dataSource();
        try (Connection connection = dataSource.getConnection()) {
            seed(connection, createIndexes);
            analyze(connection, databaseType, createIndexes);

            banner(databaseType + (createIndexes ? " [indexed]" : " [no index, histogram]") +
                " — YearMonthIntegerType column (published_on: int year*100+month, e.g. 202607)");
            explain(connection, "IN (three exact months)",
                "select * from ym_optimizer_stats where published_on in (202606, 202612, 202701)");
            explain(connection, "> last-half of 2049 (open upper range)",
                "select * from ym_optimizer_stats where published_on > 204906");
            explain(connection, "< first month of 2000 (open lower range)",
                "select * from ym_optimizer_stats where published_on < 200002");
            explain(connection, "BETWEEN Dec 2026 and Jan 2027 (crosses a year boundary)",
                "select * from ym_optimizer_stats where published_on between 202612 and 202701");
            explain(connection, "BETWEEN Jan 2026 and Jun 2026 (within one year)",
                "select * from ym_optimizer_stats where published_on between 202601 and 202606");

            banner(databaseType + (createIndexes ? " [indexed]" : " [no index, histogram]") +
                " — YearMonthDateType column (archived_on: date on the 1st, e.g. 2026-07-01)");
            explain(connection, "IN (three exact months)",
                "select * from ym_optimizer_stats where archived_on in " +
                    "(date '2026-06-01', date '2026-12-01', date '2027-01-01')");
            explain(connection, "> last-half of 2049 (open upper range)",
                "select * from ym_optimizer_stats where archived_on > date '2049-06-01'");
            explain(connection, "< first month of 2000 (open lower range)",
                "select * from ym_optimizer_stats where archived_on < date '2000-02-01'");
            explain(connection, "BETWEEN Dec 2026 and Jan 2027 (crosses a year boundary)",
                "select * from ym_optimizer_stats where archived_on between date '2026-12-01' and date '2027-01-01'");
            explain(connection, "BETWEEN Jan 2026 and Jun 2026 (within one year)",
                "select * from ym_optimizer_stats where archived_on between date '2026-01-01' and date '2026-06-01'");
        }
    }

    private void seed(Connection connection, boolean createIndexes) throws SQLException {
        try (Statement ddl = connection.createStatement()) {
            ddl.execute("drop table if exists ym_optimizer_stats");
            ddl.execute("""
                create table ym_optimizer_stats (
                    id bigint not null,
                    published_on int not null,
                    archived_on date not null,
                    primary key (id)
                )
                """);
        }

        connection.setAutoCommit(false);
        try (PreparedStatement insert = connection.prepareStatement(
                "insert into ym_optimizer_stats (id, published_on, archived_on) values (?, ?, ?)")) {
            long id = 0;
            int batched = 0;
            for (int year = FIRST_YEAR; year <= LAST_YEAR; year++) {
                for (int month = 1; month <= 12; month++) {
                    YearMonth yearMonth = YearMonth.of(year, month);
                    int asInteger = year * 100 + month;
                    java.sql.Date asDate = java.sql.Date.valueOf(yearMonth.atDay(1));
                    for (int r = 0; r < ROWS_PER_MONTH; r++) {
                        insert.setLong(1, ++id);
                        insert.setInt(2, asInteger);
                        insert.setDate(3, asDate);
                        insert.addBatch();
                        if (++batched % 1000 == 0) {
                            insert.executeBatch();
                        }
                    }
                }
            }
            insert.executeBatch();
        }
        connection.commit();
        connection.setAutoCommit(true);

        if (createIndexes) {
            try (Statement index = connection.createStatement()) {
                index.execute("create index idx_ym_published_on on ym_optimizer_stats (published_on)");
                index.execute("create index idx_ym_archived_on on ym_optimizer_stats (archived_on)");
            }
        }
    }

    private void analyze(Connection connection, DatabaseType databaseType, boolean createIndexes)
            throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (databaseType == DatabaseType.POSTGRESQL) {
                statement.execute("vacuum analyze ym_optimizer_stats");
            } else {
                statement.execute("analyze table ym_optimizer_stats");
                if (!createIndexes) {
                    // With no index to dive into, MySQL relies on a column histogram.
                    statement.execute("analyze table ym_optimizer_stats " +
                        "update histogram on published_on, archived_on with 1024 buckets");
                }
            }
        }
    }

    private void explain(Connection connection, String label, String sql) throws SQLException {
        StringBuilder plan = new StringBuilder();
        try (PreparedStatement statement = connection.prepareStatement("explain analyze " + sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                plan.append(resultSet.getString(1)).append(System.lineSeparator());
            }
        }
        System.out.println("---- " + label + " ----");
        System.out.println("    " + sql);
        System.out.println(plan);
    }

    private void banner(String title) {
        System.out.println();
        System.out.println("==================================================================");
        System.out.println("  " + title);
        System.out.println("==================================================================");
    }
}
