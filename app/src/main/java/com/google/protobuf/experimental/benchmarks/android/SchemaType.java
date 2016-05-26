package com.google.protobuf.experimental.benchmarks.android;

import com.google.protobuf.experimental.PojoMessage;
import com.google.protobuf.experimental.Reader;
import com.google.protobuf.experimental.Writer;
import com.google.protobuf.experimental.schema.Schema;
import com.google.protobuf.experimental.schema.SchemaFactory;
import com.google.protobuf.experimental.schema.HandwrittenSchemaFactory;
import com.google.protobuf.experimental.schema.AndroidGenericSchemaFactory;

public enum SchemaType {
    HANDWRITTEN(new HandwrittenSchemaFactory()),
    GENERIC(new AndroidGenericSchemaFactory());

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
}
