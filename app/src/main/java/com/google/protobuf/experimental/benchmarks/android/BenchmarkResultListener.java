package com.google.protobuf.experimental.benchmarks.android;

import org.HdrHistogram.Histogram;

public interface BenchmarkResultListener {
    void onBenchmarkCancelled();
    void onBenchmarkCompleted(Histogram histogram);
}
