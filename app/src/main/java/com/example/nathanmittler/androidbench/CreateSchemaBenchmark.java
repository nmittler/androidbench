package com.example.nathanmittler.androidbench;

import org.HdrHistogram.Histogram;

import io.nproto.PojoMessage;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.HandwrittenSchemaFactory;
import io.nproto.schema.AndroidGenericSchemaFactory;
import io.nproto.util.TestUtil;

public final class CreateSchemaBenchmark implements Benchmark {
    private final String name;
    private final int iterations;
    private final int warmupIterations;
    private final BenchmarkBlackhole bh = new BenchmarkBlackhole();
    private BenchmarkResultListener listener;
    private final SchemaFactory factory;

    public CreateSchemaBenchmark(SchemaType schemaType, boolean useAnnotations, int warmupIterations, int iterations) {
        this.iterations = iterations;
        this.warmupIterations = warmupIterations;
        name = String.format("Create (%s%s)", schemaType, (useAnnotations ? "_ANNOTATIONS" : ""));
        switch (schemaType) {
            case HANDWRITTEN:
                factory = new HandwrittenSchemaFactory();
                break;
            case GENERIC:
                BeanDescriptorFactory descFactory = useAnnotations ? AnnotationBeanDescriptorFactory.getInstance() : TestUtil.PojoDescriptorFactory.getInstance();
                factory = new AndroidGenericSchemaFactory(descFactory);
                break;
            default:
                throw new IllegalArgumentException("schemaType " + schemaType.name());
        }
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
        bh.consume(factory.createSchema(PojoMessage.class));
    }
}
