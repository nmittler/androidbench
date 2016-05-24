package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

import io.nproto.PojoMessage;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;

public class AnnotationBenchmark implements Benchmark {
    private final int iterations;
    private final int warmupIterations;
    private BenchmarkResultListener listener;
    private final AnnotationBeanDescriptorFactory factory = AnnotationBeanDescriptorFactory.getInstance();
    private final BenchmarkBlackhole bh = new BenchmarkBlackhole();

    public AnnotationBenchmark(int warmupIterations, int iterations) {
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
    }

    @Override
    public String getName() {
        return "Annotations";
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
        bh.consume(factory.descriptorFor(PojoMessage.class));
    }
}
