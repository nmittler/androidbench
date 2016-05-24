package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

public final class BenchmarkUtil {
    private BenchmarkUtil() {
    }

    // The histogram can record values between 1 microsecond and 1 min.
    public static final long HISTOGRAM_MAX_VALUE = 60000000L;
    // Value quantization will be no larger than 1/10^3 = 0.1%.
    public static final int HISTOGRAM_PRECISION = 3;

    public static Histogram newHistogram() {
        return new Histogram(HISTOGRAM_MAX_VALUE, HISTOGRAM_PRECISION);
    }
}
