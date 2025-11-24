package mujica.reflect.bytecode;

import mujica.ds.of_int.PublicIntSlot;
import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@CodeHistory(date = "2025/9/3")
@ReferencePage(title = "JVMS12 Attributes", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7")
public abstract class AttributeInfo implements ClassFileNode.Dependent, BiConsumer<AttributeInfo.Statistics, String> {

    private static final long serialVersionUID = 0xceb6ef185534178aL;

    @NotNull
    public static AttributeInfo createByName(@NotNull String name) {
        switch (name) {
            case ConstantValueAttributeInfo.NAME:
                return new ConstantValueAttributeInfo();
            case CodeAttributeInfo.NAME:
                return new CodeAttributeInfo();
            case StackMapTableAttributeInfo.NAME:
                return new StackMapTableAttributeInfo();
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
            case MemberAnnotationsAttributeInfo.VISIBLE_NAME:
                return new MemberAnnotationsAttributeInfo(true);
            case MemberAnnotationsAttributeInfo.INVISIBLE_NAME:
                return new MemberAnnotationsAttributeInfo(false);
            case ParameterAnnotationsAttributeInfo.VISIBLE_NAME:
                return new ParameterAnnotationsAttributeInfo(true);
            case ParameterAnnotationsAttributeInfo.INVISIBLE_NAME:
                return new ParameterAnnotationsAttributeInfo(false);
            case NestHostAttributeInfo.NAME:
                return new NestHostAttributeInfo();
            case NestMembersAttributeInfo.NAME:
                return new NestMembersAttributeInfo();
            case BootstrapMethodsAttributeInfo.NAME:
                return new BootstrapMethodsAttributeInfo();
            case EnclosingMethodAttributeInfo.NAME:
                return new EnclosingMethodAttributeInfo();
            case ModuleAttributeInfo.NAME:
                return new ModuleAttributeInfo();
            default:
                return new NotParsedAttributeInfo(name);
        }
    }

    @NotNull
    public static AttributeInfo readNew(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final String name = context.getUtf8(in.readUnsignedShort());
        final AttributeInfo attributeInfo = createByName(name);
        final long newLength = 0xffffffffL & in.readInt();
        final long oldLength = in.setRemaining(newLength);
        attributeInfo.read(context, in);
        final long remaining = in.setRemaining(oldLength - newLength);
        if (remaining != 0L) {
            throw new BytecodeException(name);
        }
        assert newLength == attributeInfo.byteSize() : name;
        return attributeInfo;
    }

    @NotNull
    public static AttributeInfo readNew(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final String name = context.getUtf8(0xffff & buffer.getShort());
        final AttributeInfo attributeInfo = createByName(name);
        final int oldLimit = buffer.limit();
        final int newLimit = buffer.getInt();
        buffer.limit(buffer.position() + newLimit);
        attributeInfo.read(context, buffer);
        if (buffer.remaining() != 0) {
            throw new BytecodeException(name);
        }
        buffer.limit(oldLimit);
        assert newLimit == attributeInfo.byteSize() : name;
        return attributeInfo;
    }

    public static void write(@NotNull AttributeInfo attribute, @NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(context.putUtf8(attribute.attributeName()));
        out.writeInt(attribute.byteSize());
        attribute.write(context, out);
    }

    public static void write(@NotNull AttributeInfo attribute, @NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) context.putUtf8(attribute.attributeName()));
        final int length = attribute.byteSize();
        buffer.putInt(length);
        final int start = buffer.position();
        attribute.write(context, buffer);
        if (start + length != buffer.position()) {
            throw new BytecodeException("byte size");
        }
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

    public static void writeArray(@NotNull AttributeInfo[] attributes, @NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(attributes.length);
        for (AttributeInfo attribute : attributes) {
            write(attribute, context, out);
        }
    }

    public static void writeArray(@NotNull AttributeInfo[] attributes, @NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) attributes.length);
        for (AttributeInfo attribute : attributes) {
            write(attribute, context, buffer);
        }
    }

    @NotNull
    public static AttributeInfo[] filterArray(@NotNull AttributeInfo[] attributes, @NotNull Predicate<AttributeInfo> predicate) {
        final int length = attributes.length;
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < length; readIndex++) {
            AttributeInfo attributeInfo = attributes[readIndex];
            if (predicate.test(attributeInfo)) {
                attributes[writeIndex++] = attributeInfo;
            }
        }
        if (writeIndex == length) {
            return attributes;
        } else {
            return Arrays.copyOf(attributes, writeIndex);
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
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        return 0;
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        throw new IndexOutOfBoundsException();
    }

    @CodeHistory(date = "2025/10/1")
    @ReferencePage(title = "JVMS12 Chapter 4. The class File Format", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7")
    public enum ImportanceLevel {

        /**
         * Six attributes are critical to correct interpretation of the class file by the Java Virtual Machine:
         * ConstantValue
         * Code
         * StackMapTable
         * BootstrapMethods
         * NestHost
         * NestMembers
         */
        CRITICAL,

        /**
         * Nine attributes are not critical to correct interpretation of the class file by the Java Virtual Machine,
         * but are either critical to correct interpretation of the class file by the class libraries of the Java SE
         * Platform, or are useful for tools (in which case the section that specifies an attribute describes it as
         * "optional"):
         * Exceptions
         * InnerClasses
         * EnclosingMethod
         * Synthetic
         * Signature
         * SourceFile
         * LineNumberTable
         * LocalVariableTable
         * LocalVariableTypeTable
         */
        HIGH,

        /**
         * Thirteen attributes are not critical to correct interpretation of the class file by the Java Virtual Machine,
         * but contain metadata about the class file that is either exposed by the class libraries of the Java SE
         * Platform, or made available by tools (in which case the section that specifies an attribute describes it as
         * "optional"):
         * SourceDebugExtension
         * Deprecated
         * RuntimeVisibleAnnotations
         * RuntimeInvisibleAnnotations
         * RuntimeVisibleParameterAnnotations
         * RuntimeInvisibleParameterAnnotations
         * RuntimeVisibleTypeAnnotations
         * RuntimeInvisibleTypeAnnotations
         * AnnotationDefault
         * MethodParameters
         * Module
         * ModulePackages
         * ModuleMainClass
         */
        MEDIUM,

        LOW;

        private static final long serialVersionUID = 0xC4AD5E7ECF8BCDC0L;
    }

    @NotNull
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.LOW;
    }

    public boolean isNecessary() {
        return false; // can be converted into an interface
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return attributeName();
    }

    @Override
    @NotNull
    public String toString() {
        return attributeName();
    }

    @CodeHistory(date = "2025/10/6")
    public static class Statistics {

        final HashMap<String, PublicIntSlot> map = new HashMap<>();
    }

    @Override
    public void accept(@NotNull Statistics statistics, @NotNull String prefix) {
        PublicIntSlot.increase(statistics.map, prefix + attributeName());
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        final int groupCount = groupCount();
        for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
            Class<? extends ClassFileNode> group = getGroup(groupIndex);
            int nodeCount = nodeCount(group);
            for (int nodeIndex = 0; nodeIndex < nodeCount; nodeIndex++) {
                getNode(group, nodeIndex).remapConstant(remap);
            }
        }
    }

    protected static void remapConstant(@NotNull IntUnaryOperator remap, @NotNull int[] array) {
        final int length = array.length;
        for (int index = 0; index < length; index++) {
            array[index] = remap.applyAsInt(array[index]);
        }
    }
}
