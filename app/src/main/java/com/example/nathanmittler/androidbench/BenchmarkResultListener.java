package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

public interface BenchmarkResultListener {
    void onBenchmarkCancelled();
    void onBenchmarkCompleted(Histogram histogram);
}
