package com.example.nathanmittler.androidbench;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import io.nproto.PojoMessage;
import io.nproto.ProtoField;
import io.nproto.descriptor.AnnotationBeanDescriptorFactory;
import io.nproto.descriptor.PropertyDescriptor;
import io.nproto.util.TestUtil;

public class AnnotationBenchmark implements Benchmark {
    private final AnnotationBeanDescriptorFactory factory = AnnotationBeanDescriptorFactory.getInstance();
    private final BenchmarkBlackhole bh = new BenchmarkBlackhole();


    @Override
    public String getName() {
        return "Annotations";
    }

    @Override
    public void doIteration() {
        bh.consume(TestUtil.PojoDescriptorFactory.getInstance().descriptorFor(PojoMessage.class));
        //consumeFields();
        //bh.consume(factory.descriptorFor(PojoMessage.class));
        //bh.consume(PojoMessage.class.getDeclaredFields());
        /*final Field[] fields = PojoMessage.class.getDeclaredFields();
        final int numFields = fields.length;

        for(int i = 0; i < numFields; ++i) {
            Field f = fields[i];
            int mod = f.getModifiers();
            //ProtoField protoField = (ProtoField)f.getAnnotation(ProtoField.class);
            Annotation[] annotations = f.getAnnotations();
            bh.consume(annotations);
            if(!Modifier.isStatic(mod) && !Modifier.isTransient(mod)) {// && protoField != null) {
                //fields.add(new PropertyDescriptor(f, protoField));
                bh.consume(f);
            }
        }*/
    }

    private void consumeFields() {
        bh.consume(pojoField("enumField"));
        bh.consume(pojoField("boolField"));
        bh.consume(pojoField("uint32Field"));
        bh.consume(pojoField("int32Field"));
        bh.consume(pojoField("sInt32Field"));
        bh.consume(pojoField("fixedInt32Field"));
        bh.consume(pojoField("sFixedInt32Field"));
        bh.consume(pojoField("uint64Field"));
        bh.consume(pojoField("int64Field"));
        bh.consume(pojoField("sInt64Field"));
        bh.consume(pojoField("fixedInt64Field"));
        bh.consume(pojoField("sFixedInt64Field"));
        bh.consume(pojoField("stringField"));
        bh.consume(pojoField("bytesField"));
        bh.consume(pojoField("messageField"));
        bh.consume(pojoField("enumListField"));
        bh.consume(pojoField("boolListField"));
        bh.consume(pojoField("uint32ListField"));
        bh.consume(pojoField("int32ListField"));
        bh.consume(pojoField("sInt32ListField"));
        bh.consume(pojoField("fixedInt32ListField"));
        bh.consume(pojoField("sFixedInt32ListField"));
        bh.consume(pojoField("uint64ListField"));
        bh.consume(pojoField("int64ListField"));
        bh.consume(pojoField("sInt64ListField"));
        bh.consume(pojoField("fixedInt64ListField"));
        bh.consume(pojoField("sFixedInt64ListField"));
        bh.consume(pojoField("stringListField"));
        bh.consume(pojoField("bytesListField"));
        bh.consume(pojoField("messageListField"));
    }
    private static Field pojoField(String name) {
        try {
            return PojoMessage.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
