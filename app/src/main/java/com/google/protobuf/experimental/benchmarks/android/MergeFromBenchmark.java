package com.google.protobuf.experimental.benchmarks.android;

import com.google.protobuf.experimental.example.PojoMessage;
import com.google.protobuf.experimental.util.TestUtil;

public final class MergeFromBenchmark implements Benchmark {
    private final SchemaType schemaType;
    private final String name;
    private final TestUtil.PojoReader reader = new TestUtil.PojoReader(TestUtil.newTestMessage());

    public MergeFromBenchmark(SchemaType schemaType) {
        this.schemaType = schemaType;
        name = String.format("MergeFrom (%s)", schemaType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void doIteration() {
        schemaType.mergeFrom(new PojoMessage(), reader);
        reader.reset();
    }
}
