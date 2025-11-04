package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/17")
@ReferencePage(title = "JVMS12 The RuntimeVisibleAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.16")
@ReferencePage(title = "JVMS12 The RuntimeInvisibleAnnotations Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.17")
public class MemberAnnotationsAttributeInfo extends AnnotationsAttributeInfo {

    private static final long serialVersionUID = 0x2841B67A2A148719L;

    private Annotation[] annotations;

    MemberAnnotationsAttributeInfo(boolean visible) {
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
            return Annotation.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == Annotation.class) {
            return annotations.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == Annotation.class) {
            return annotations[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final String VISIBLE_NAME = "RuntimeVisibleAnnotations";

    public static final String INVISIBLE_NAME = "RuntimeInvisibleAnnotations";

    @NotNull
    @Override
    public String attributeName() {
        return visible ? VISIBLE_NAME : INVISIBLE_NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (Annotation annotation : annotations) {
            size += annotation.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int length = in.readUnsignedShort();
        annotations = new Annotation[length];
        for (int index = 0; index < length; index++) {
            Annotation annotation = new Annotation();
            annotation.read(in);
            annotations[index] = annotation;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort();
        annotations = new Annotation[length];
        for (int index = 0; index < length; index++) {
            Annotation annotation = new Annotation();
            annotation.read(buffer);
            annotations[index] = annotation;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(annotations.length);
        for (Annotation annotation : annotations) {
            annotation.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) annotations.length);
        for (Annotation annotation : annotations) {
            annotation.write(buffer);
        }
    }
}
