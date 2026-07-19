package com.bookstore.forum;

import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Plain unit test for the TSID value type itself — no Spring, no database.
 */
public class TsidApiTest {

    @Test
    public void tsidStructure() {
        Instant beforeGeneration = Instant.now();

        TSID tsid = TSID.Factory.INSTANCE.generate();

        assertEquals(8, tsid.toBytes().length);
        assertEquals(13, tsid.toString().length());
        assertTrue(TSID.isValid(tsid.toString()));

        assertEquals(tsid, TSID.from(tsid.toString()));
        assertEquals(tsid, TSID.from(tsid.toLong()));

        Instant creationTime = tsid.getInstant();
        assertFalse(creationTime.isBefore(beforeGeneration.minusSeconds(1)));
        assertFalse(creationTime.isAfter(Instant.now().plusSeconds(1)));
    }

    @Test
    public void tsidValuesAreTimeSorted() {
        TSID.Factory factory = TSID.Factory.builder()
            .withRandomFunction(TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION)
            .build();

        List<TSID> tsids = new ArrayList<>();
        for (int i = 0; i < 1_000; i++) {
            tsids.add(factory.generate());
        }

        for (int i = 1; i < tsids.size(); i++) {
            TSID previous = tsids.get(i - 1);
            TSID current = tsids.get(i);

            assertTrue(previous.toLong() < current.toLong());
            assertTrue(previous.toString().compareTo(current.toString()) < 0);
        }
    }

    @Test
    public void partitionedFactoriesGenerateUniqueValues() {
        TSID.Factory firstNodeFactory = TSID.Factory.newInstance256(1);
        TSID.Factory secondNodeFactory = TSID.Factory.newInstance256(2);

        Set<TSID> tsids = new HashSet<>();
        for (int i = 0; i < 10_000; i++) {
            assertTrue(tsids.add(firstNodeFactory.generate()));
            assertTrue(tsids.add(secondNodeFactory.generate()));
        }
    }
}
