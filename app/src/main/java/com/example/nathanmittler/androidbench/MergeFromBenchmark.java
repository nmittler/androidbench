package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

import io.nproto.PojoMessage;
import io.nproto.util.TestUtil;

public final class MergeFromBenchmark implements Benchmark {
    private final SchemaType schemaType;
    private final String name;
    private final int iterations;
    private final int warmupIterations;
    private BenchmarkResultListener listener;
    private final TestUtil.PojoReader reader = new TestUtil.PojoReader(TestUtil.newTestMessage());

    public MergeFromBenchmark(SchemaType schemaType, int warmupIterations, int iterations) {
        this.schemaType = schemaType;
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
        name = String.format("MergeFrom (%s)", schemaType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setResultListener(BenchmarkResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        warmup();
        if (Thread.interrupted()) {
            listener.onBenchmarkCancelled();
        }

        final Histogram histogram = BenchmarkUtil.newHistogram();
        for (int i = 0; !Thread.currentThread().isInterrupted() && i < iterations; ++i) {
            long start = System.nanoTime();
            doIteration();
            // Record time in microseconds.
            histogram.recordValue((System.nanoTime() - start) / 1000);
        }
        if (Thread.interrupted()) {
            listener.onBenchmarkCancelled();
        } else {
            listener.onBenchmarkCompleted(histogram);
        }
    }

    private void warmup() {
        for (int i = 0; !Thread.currentThread().isInterrupted() && i < warmupIterations; ++i) {
            doIteration();
        }
    }

    private void doIteration() {
        schemaType.mergeFrom(new PojoMessage(), reader);
        reader.reset();
    }
}
