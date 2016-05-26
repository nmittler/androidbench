package com.google.protobuf.experimental.benchmarks.android;

import java.util.Random;

/**
 * JMH Blackhole does not allow direct instantiation. Copying some of the internals here to
 * allow use with android benchmarks.
 */
public final class BenchmarkBlackhole {
    public int tlr;
    public volatile boolean bool1;
    public volatile int i1;
    public volatile long l1;
    public volatile float f1;
    public volatile double d1;
    public boolean bool2;
    public int i2;
    public long l2;
    public float f2;
    public double d2;
    public volatile Object obj1;
    public volatile int tlrMask;

    BenchmarkBlackhole() {
        Random r = new Random(System.nanoTime());
        tlr = r.nextInt();
        tlrMask = 1;
        obj1 = new Object();
    }

    void consume(Object obj) {
        int tlrMask = this.tlrMask; // volatile read
        int tlr = (this.tlr = (this.tlr * 1664525 + 1013904223));
        if ((tlr & tlrMask) == 0) {
            // SHOULD ALMOST NEVER HAPPEN IN MEASUREMENT
            this.obj1 = obj;
            this.tlrMask = (tlrMask << 1) + 1;
        }
    }

    public final void consume(boolean bool) {
        boolean bool1 = this.bool1;
        boolean bool2 = this.bool2;
        if((bool ^ bool1) == (bool ^ bool2)) {
            this.bool1 = bool;
        }

    }

    public final void consume(int i) {
        int i1 = this.i1;
        int i2 = this.i2;
        if((i ^ i1) == (i ^ i2)) {
            this.i1 = i;
        }

    }

    public final void consume(long l) {
        long l1 = this.l1;
        long l2 = this.l2;
        if((l ^ l1) == (l ^ l2)) {
            this.l1 = l;
        }

    }

    public final void consume(float f) {
        float f1 = this.f1;
        float f2 = this.f2;
        if(f == f1 & f == f2) {
            this.f1 = f;
        }

    }

    public final void consume(double d) {
        double d1 = this.d1;
        double d2 = this.d2;
        if(d == d1 & d == d2) {
            this.d1 = d;
        }

    }
}
