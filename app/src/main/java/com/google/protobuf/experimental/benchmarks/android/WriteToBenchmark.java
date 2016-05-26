package com.google.protobuf.experimental.benchmarks.android;

import java.util.List;

import com.google.protobuf.experimental.ByteString;
import com.google.protobuf.experimental.PojoMessage;
import com.google.protobuf.experimental.Writer;
import com.google.protobuf.experimental.util.TestUtil;

public final class WriteToBenchmark implements Benchmark {
    private final SchemaType schemaType;
    private final String name;
    private final PojoMessage message = TestUtil.newTestMessage();
    private final Writer writer = new TestWriter();

    public WriteToBenchmark(SchemaType schemaType) {
        this.schemaType = schemaType;
        name = String.format("WriteTo (%s)", schemaType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void doIteration() {
        schemaType.writeTo(message, writer);
    }

    private static final class TestWriter implements Writer {
        private final BenchmarkBlackhole bh = new BenchmarkBlackhole();

        @Override
        public void writeSFixed32(int fieldNumber, int value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeInt64(int fieldNumber, long value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSFixed64(int fieldNumber, long value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFloat(int fieldNumber, float value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeDouble(int fieldNumber, double value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public <E extends Enum<E>> void writeEnum(int fieldNumber, E value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeUInt64(int fieldNumber, long value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeInt32(int fieldNumber, int value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFixed64(int fieldNumber, long value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFixed32(int fieldNumber, int value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeBool(int fieldNumber, boolean value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeString(int fieldNumber, String value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeBytes(int fieldNumber, ByteString value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeUInt32(int fieldNumber, int value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSInt32(int fieldNumber, int value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSInt64(int fieldNumber, long value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeMessage(int fieldNumber, Object value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeInt32List(int fieldNumber, List<Integer> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFixed32List(int fieldNumber, List<Integer> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeInt64List(int fieldNumber, List<Long> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeUInt64List(int fieldNumber, List<Long> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFixed64List(int fieldNumber, List<Long> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeFloatList(int fieldNumber, List<Float> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeDoubleList(int fieldNumber, List<Double> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public <E extends Enum<E>> void writeEnumList(int fieldNumber, List<E> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeBoolList(int fieldNumber, List<Boolean> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeStringList(int fieldNumber, List<String> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeBytesList(int fieldNumber, List<ByteString> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeUInt32List(int fieldNumber, List<Integer> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSFixed32List(int fieldNumber, List<Integer> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSFixed64List(int fieldNumber, List<Long> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSInt32List(int fieldNumber, List<Integer> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeSInt64List(int fieldNumber, List<Long> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }

        @Override
        public void writeMessageList(int fieldNumber, List<?> value) {
            bh.consume(fieldNumber);
            bh.consume(value);
        }
    }
}
