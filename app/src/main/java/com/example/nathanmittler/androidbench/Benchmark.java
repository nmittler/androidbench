package com.example.nathanmittler.androidbench;

interface Benchmark extends Runnable {
    String getName();
    void setResultListener(BenchmarkResultListener listener);
}
