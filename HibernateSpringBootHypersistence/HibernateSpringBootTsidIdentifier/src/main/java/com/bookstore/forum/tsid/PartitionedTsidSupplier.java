package com.bookstore.forum.tsid;

import io.hypersistence.tsid.TSID;

import java.util.function.Supplier;

/**
 * Provides a {@link TSID.Factory} using 8 node bits, allowing 256 application
 * nodes to generate collision-free identifiers. The node id is read from the
 * {@code tsid.node} system property, so each deployment unit must set its own value.
 */
public class PartitionedTsidSupplier implements Supplier<TSID.Factory> {

    private final TSID.Factory factory = TSID.Factory.builder()
        .withNodeBits(8)
        .withNode(Integer.getInteger("tsid.node", 0))
        .withRandomFunction(TSID.Factory.THREAD_LOCAL_RANDOM_FUNCTION)
        .build();

    @Override
    public TSID.Factory get() {
        return factory;
    }
}
