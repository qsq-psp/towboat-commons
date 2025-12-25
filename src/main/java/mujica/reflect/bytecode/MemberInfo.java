package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@CodeHistory(date = "2025/9/19")
public abstract class MemberInfo implements ClassFileNode.Dependent, BiConsumer<AttributeInfo.Statistics, String> {

    private static final long serialVersionUID = 0xd47608dcbc6ce532L;

    int accessFlags;

    @DataType("u16-{0}")
    @ConstantType(tags = Utf8ConstantInfo.TAG)
    String name;

    @DataType("u16-{0}")
    @ConstantType(tags = Utf8ConstantInfo.TAG)
    String descriptor;

    AttributeInfo[] attributes;

    transient SignatureAttributeInfo signature;

    public MemberInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return AttributeInfo.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == AttributeInfo.class) {
            return attributes.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        accessFlags = in.readUnsignedShort();
        name = context.getUtf8(in.readUnsignedShort());
        descriptor = context.getUtf8(in.readUnsignedShort());
        attributes = AttributeInfo.readArray(context, in);
        afterRead();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        accessFlags = 0xffff & buffer.getShort();
        name = context.getUtf8(0xffff & buffer.getShort());
        descriptor = context.getUtf8(0xffff & buffer.getShort());
        attributes = AttributeInfo.readArray(context, buffer);
        afterRead();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(accessFlags);
        out.writeShort(context.putUtf8(name));
        out.writeShort(context.putUtf8(descriptor));
        AttributeInfo.writeArray(attributes, context, out);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) accessFlags);
        buffer.putShort((short) context.putUtf8(name));
        buffer.putShort((short) context.putUtf8(descriptor));
        AttributeInfo.writeArray(attributes, context, buffer);
    }

    protected void afterRead() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof SignatureAttributeInfo) {
                signature = (SignatureAttributeInfo) attribute;
            }
        }
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        for (AttributeInfo attribute : attributes) {
            attribute.accept(statistics, prefix);
        }
    }

    public void filterAttributeInfo(@NotNull Predicate<AttributeInfo> predicate) {
        attributes = AttributeInfo.filterArray(attributes, predicate);
    }

    public void sortAttributeInfo(@NotNull Comparator<AttributeInfo> comparator) {
        Arrays.sort(attributes, comparator);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        for (AttributeInfo attribute : attributes) {
            attribute.remapConstant(remap);
        }
    }

}
