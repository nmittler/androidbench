package com.example.nathanmittler.androidbench;

import io.nproto.PojoMessage;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.BeanDescriptorFactory;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.HandwrittenSchemaFactory;
import io.nproto.schema.AndroidGenericSchemaFactory;
import io.nproto.util.TestUtil;

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
