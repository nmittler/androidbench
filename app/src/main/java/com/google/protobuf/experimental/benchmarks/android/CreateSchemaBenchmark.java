package com.google.protobuf.experimental.benchmarks.android;

import com.google.protobuf.experimental.PojoMessage;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.descriptor.BeanDescriptorFactory;
import com.google.protobuf.experimental.schema.SchemaFactory;
import com.google.protobuf.experimental.schema.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.schema.AndroidGenericSchemaFactory;
import com.google.protobuf.experimental.util.TestUtil;

public final class CreateSchemaBenchmark implements Benchmark {
    private final String name;
    private final BenchmarkBlackhole bh = new BenchmarkBlackhole();
    private final SchemaFactory factory;

    public CreateSchemaBenchmark(SchemaType schemaType, boolean useAnnotations) {
        name = String.format("Create (%s%s)", schemaType, (useAnnotations ? "" : "_NO_ANNOTATIONS"));
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
    public void doIteration() {
        bh.consume(factory.createSchema(PojoMessage.class));
    }
}
