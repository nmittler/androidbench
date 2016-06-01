package com.google.protobuf.experimental.benchmarks.android;

import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.Writer;
import com.google.protobuf.experimental.descriptor.AnnotationBeanDescriptorFactory;
import com.google.protobuf.experimental.example.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.example.PojoMessage;
import com.google.protobuf.experimental.schema.GenericSchemaFactory;
import com.google.protobuf.experimental.schema.Schema;
import com.google.protobuf.experimental.schema.SchemaFactory;
import com.google.protobuf.experimental.schema.asm.android.AndroidClassLoadingStrategy;
import com.google.protobuf.experimental.schema.asm.AsmSchemaFactory;
import com.google.protobuf.experimental.schema.asm.ClassLoadingStrategy;
import com.google.protobuf.experimental.schema.asm.SchemaNamingStrategy;

public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    GENERIC(new GenericSchemaFactory()),
    ASM_INLINE_SAFE(newAsmSchemaFactory(true, false)),
    ASM_INLINE_UNSAFE(newAsmSchemaFactory(false, true)),
    ASM_MINCODE_SAFE(newAsmSchemaFactory(true, false)),
    ASM_MINCODE_UNSAFE(newAsmSchemaFactory(false, true));

    SchemaType(SchemaFactory factory) {
        this.factory = factory;
        schema = factory.createSchema(PojoMessage.class);
    }

    final void mergeFrom(PojoMessage message, Reader reader) {
        schema.mergeFrom(message, reader);
    }

    final void writeTo(PojoMessage message, Writer writer) {
        schema.writeTo(message, writer);
    }

    final SchemaFactory factory;
    final Schema<PojoMessage> schema;

    private static AsmSchemaFactory newAsmSchemaFactory(boolean minimizeCodeGen, boolean preferUnsafeAccess) {
        ClassLoadingStrategy classLoadingStrategy = new AndroidClassLoadingStrategy(
                SchemaType.class.getClassLoader(), MainActivity.ASM_SCHEMA_DIR);
        String schemaName = PojoMessage.class.getName() +
                (minimizeCodeGen ? "Mincode" : "Inline") +
                (preferUnsafeAccess ? "Unsafe" : "Safe") + "Schema";
        return new AsmSchemaFactory(classLoadingStrategy,
                AnnotationBeanDescriptorFactory.getInstance(),
                new FixedSchemaNamingStrategy(schemaName),
                minimizeCodeGen,
                preferUnsafeAccess);
    }

    private static class FixedSchemaNamingStrategy implements SchemaNamingStrategy {
        private final String name;

        FixedSchemaNamingStrategy(String name) {
            this.name = name;
        }

        @Override
        public String schemaNameFor(Class<?> messageClass) {
            return name;
        }
    }
}
