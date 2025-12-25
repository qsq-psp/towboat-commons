package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/23")
@ReferencePage(title = "JVMS12 The Exceptions Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.5")
class ExceptionsAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xd2e64b8b77cb428fL;

    @DataType("u16-{0}")
    @ConstantType(tags = ClassConstantInfo.TAG)
    private short[] indexes;

    ExceptionsAttributeInfo() {
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
            return ConstantReferenceNodeAdapter.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == ConstantReferenceNodeAdapter.class) {
            return indexes.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == ConstantReferenceNodeAdapter.class) {
            return new ConstantReferenceNodeAdapter(0xffff & indexes[nodeIndex]);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.HIGH;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    public static final String NAME = "Exceptions";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 2 * indexes.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int count = in.readUnsignedShort();
        indexes = new short[count];
        for (int index = 0; index < count; index++) {
            indexes[index] = in.readShort();
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int count = 0xffff & buffer.getShort();
        indexes = new short[count];
        for (int index = 0; index < count; index++) {
            indexes[index] = buffer.getShort();
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(indexes.length);
        for (short index : indexes) {
            out.writeShort(index);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) indexes.length);
        for (short index : indexes) {
            buffer.putShort(index);
        }
    }

    public void append(@NotNull ClassFile context, @NotNull StringBuilder out) {
        boolean subsequent = false;
        for (int index : indexes) {
            if (subsequent) {
                out.append(", ");
            }
            out.append(context.constantPool.getSourceClassName(index));
            subsequent = true;
        }
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        remapConstant(remap, indexes);
    }
}
