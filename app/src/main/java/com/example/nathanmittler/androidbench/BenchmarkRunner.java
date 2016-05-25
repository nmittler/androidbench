package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

public class BenchmarkRunner<T extends Benchmark> implements Runnable {
    public static final int DEFAULT_WARMUP_ITERATIONS = 1000;
    public static final int DEFAULT_ITERATIONS = 5000;

    private final int iterations;
    private final int warmupIterations;
    private final T benchmark;
    private BenchmarkResultListener listener;

    public static <T extends Benchmark> BenchmarkRunner<T> forBenchmark(T benchmark) {
        return new BenchmarkRunner<T>(benchmark, DEFAULT_WARMUP_ITERATIONS, DEFAULT_ITERATIONS);
    }

    public static <T extends Benchmark> BenchmarkRunner<T> forBenchmark(T benchmark,
                                                                        int warmupIterations,
                                                                        int iterations) {
        return new BenchmarkRunner<T>(benchmark, warmupIterations, iterations);
    }

    private BenchmarkRunner(T benchmark, int warmupIterations, int iterations) {
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
        this.benchmark = benchmark;
    }

    public T getBenchmark() {
        return benchmark;
    }

    public void setResultListener(BenchmarkResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        for (int i = 0; !Thread.currentThread().isInterrupted() && i < warmupIterations; ++i) {
            benchmark.doIteration();
        }
        if (Thread.interrupted()) {
            listener.onBenchmarkCancelled();
        }

        final Histogram histogram = BenchmarkUtil.newHistogram();
        for (int i = 0; !Thread.currentThread().isInterrupted() && i < iterations; ++i) {
            long start = System.nanoTime();
            benchmark.doIteration();
            // Record time in microseconds.
            histogram.recordValue((System.nanoTime() - start) / 1000);
        }
        if (Thread.interrupted()) {
            listener.onBenchmarkCancelled();
        } else {
            listener.onBenchmarkCompleted(histogram);
        }
    }
}
