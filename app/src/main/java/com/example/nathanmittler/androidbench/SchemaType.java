package com.example.nathanmittler.androidbench;

import io.nproto.PojoMessage;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaFactory;
import io.nproto.schema.HandwrittenSchemaFactory;
import io.nproto.schema.AndroidGenericSchemaFactory;

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
