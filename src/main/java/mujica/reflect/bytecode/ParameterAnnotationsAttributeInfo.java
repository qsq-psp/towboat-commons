package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/11/3")
@ReferencePage(title = "JVMS12 The RuntimeVisibleParameterAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.18")
@ReferencePage(title = "JVMS12 The RuntimeInvisibleParameterAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.19")
public class ParameterAnnotationsAttributeInfo extends AnnotationsAttributeInfo {

    private static final long serialVersionUID = 0x9E6E899594B31316L;

    private ParameterAnnotation[] annotations;

    ParameterAnnotationsAttributeInfo(boolean visible) {
        super(visible);
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return ParameterAnnotation.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == ParameterAnnotation.class) {
            return annotations.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == ParameterAnnotation.class) {
            return annotations[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final String VISIBLE_NAME = "RuntimeVisibleParameterAnnotations";

    public static final String INVISIBLE_NAME = "RuntimeInvisibleParameterAnnotations";

    @NotNull
    @Override
    public String attributeName() {
        return visible ? VISIBLE_NAME : INVISIBLE_NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (ParameterAnnotation annotation : annotations) {
            size += annotation.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int length = in.readUnsignedShort();
        annotations = new ParameterAnnotation[length];
        for (int index = 0; index < length; index++) {
            ParameterAnnotation annotation = new ParameterAnnotation();
            annotation.read(in);
            annotations[index] = annotation;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort();
        annotations = new ParameterAnnotation[length];
        for (int index = 0; index < length; index++) {
            ParameterAnnotation annotation = new ParameterAnnotation();
            annotation.read(buffer);
            annotations[index] = annotation;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(annotations.length);
        for (ParameterAnnotation annotation : annotations) {
            annotation.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) annotations.length);
        for (ParameterAnnotation annotation : annotations) {
            annotation.write(buffer);
        }
    }
}
