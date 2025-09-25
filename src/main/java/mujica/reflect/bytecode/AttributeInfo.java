package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/3.
 */
@CodeHistory(date = "2025/9/3")
public abstract class AttributeInfo implements ClassFileNode.Dependent {

    private static final long serialVersionUID = 0xceb6ef185534178aL;

    @NotNull
    public static AttributeInfo createByName(@NotNull String name) {
        switch (name) {
            case ConstantValueAttributeInfo.NAME:
                return new ConstantValueAttributeInfo();
            case CodeAttributeInfo.NAME:
                return new CodeAttributeInfo();
            // case StackMapTableAttributeInfo.NAME:
                // return new StackMapTableAttributeInfo();
            case ExceptionsAttributeInfo.NAME:
                return new ExceptionsAttributeInfo();
            case InnerClassesAttributeInfo.NAME:
                return new InnerClassesAttributeInfo();
            case SignatureAttributeInfo.NAME:
                return new SignatureAttributeInfo();
            case SourceFileAttributeInfo.NAME:
                return new SourceFileAttributeInfo();
            case LineNumberTableAttributeInfo.NAME:
                return new LineNumberTableAttributeInfo();
            case LocalVariableTableAttributeInfo.NAME:
                return new LocalVariableTableAttributeInfo();
            case RuntimeVisibleAnnotationsAttributeInfo.NAME:
                return new RuntimeVisibleAnnotationsAttributeInfo();
            default:
                return new NotParsedAttributeInfo(name);
        }
    }

    @NotNull
    public static AttributeInfo readNew(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final AttributeInfo attributeInfo = createByName(context.getUtf8(in.readUnsignedShort()));
        final long newLength = 0xffffffffL & in.readInt();
        final long oldLength = in.setRemaining(newLength);
        attributeInfo.read(context, in);
        final long remaining = in.setRemaining(oldLength - newLength);
        if (remaining != 0L) {
            throw new IOException();
        }
        return attributeInfo;
    }

    @NotNull
    public static AttributeInfo readNew(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final AttributeInfo attributeInfo = createByName(context.getUtf8(0xffff & buffer.getShort()));
        final int oldLimit = buffer.limit();
        final int newLimit = buffer.getInt();
        buffer.limit(buffer.position() + newLimit);
        attributeInfo.read(context, buffer);
        if (buffer.remaining() != 0) {
            throw new RuntimeException();
        }
        buffer.limit(oldLimit);
        return attributeInfo;
    }

    @NotNull
    public static AttributeInfo[] readArray(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int count = in.readUnsignedShort();
        final AttributeInfo[] array = new AttributeInfo[count];
        for (int index = 0; index < count; index++) {
            array[index] = readNew(context, in);
        }
        return array;
    }

    @NotNull
    public static AttributeInfo[] readArray(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int count = 0xffff & buffer.getShort();
        final AttributeInfo[] array = new AttributeInfo[count];
        for (int index = 0; index < count; index++) {
            array[index] = readNew(context, buffer);
        }
        return array;
    }

    public static void writeArray(@NotNull ConstantPool context, @NotNull DataOutput out, @NotNull AttributeInfo[] attributes) throws IOException {
        out.writeShort(attributes.length);
        for (AttributeInfo attribute : attributes) {
            attribute.write(context, out);
        }
    }

    public static void writeArray(@NotNull ConstantPool context, @NotNull ByteBuffer buffer, @NotNull AttributeInfo[] attributes) {
        buffer.putShort((short) attributes.length);
        for (AttributeInfo attribute : attributes) {
            attribute.write(context, buffer);
        }
    }

    public AttributeInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 0;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        return 0;
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    public abstract String attributeName();

    public abstract int byteSize();

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        while (true) {
            long remaining = in.getRemaining();
            if (remaining > 0L) {
                in.skipBytes((int) Math.min(remaining, Integer.MAX_VALUE));
            } else {
                break;
            }
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.position(buffer.limit());
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(context.putUtf8(attributeName()));
        out.writeInt(byteSize());
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) context.putUtf8(attributeName()));
        buffer.putInt(byteSize());
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return attributeName();
    }

    @Override
    @NotNull
    public String toString() {
        return attributeName();
    }
}
