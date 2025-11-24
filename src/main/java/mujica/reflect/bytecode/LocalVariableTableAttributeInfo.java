package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.basic.BytecodeFieldType;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/17")
@ReferencePage(title = "JVMS12 The LocalVariableTable Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.13")
public class LocalVariableTableAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xB459F74DD30BA246L;

    @CodeHistory(date = "2025/9/17")
    private static class LocalVariableEntry extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xD1664378176A5AFBL;

        int startPC;

        int endPC;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int nameIndex;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int descriptorIndex;

        int localVariableIndex; // long or double occupy 2 indexes

        LocalVariableEntry() {
            super();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            startPC = in.readUnsignedShort();
            endPC = startPC + in.readUnsignedShort(); // read length
            nameIndex = in.readUnsignedShort();
            descriptorIndex = in.readUnsignedShort();
            localVariableIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            endPC = startPC + (0xffff & buffer.getShort());
            nameIndex = 0xffff & buffer.getShort();
            descriptorIndex = 0xffff & buffer.getShort();
            localVariableIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(endPC - startPC);
            out.writeShort(nameIndex);
            out.writeShort(descriptorIndex);
            out.writeShort(localVariableIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) (endPC - startPC));
            buffer.putShort((short) nameIndex);
            buffer.putShort((short) descriptorIndex);
            buffer.putShort((short) localVariableIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "from " + startPC + " to " + endPC + " "
                    + (new BytecodeFieldType(context.constantPool.getUtf8(descriptorIndex))).toSourceString() + " "
                    + context.constantPool.getUtf8(nameIndex) + " at " + localVariableIndex;
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            nameIndex = remap.applyAsInt(nameIndex);
            descriptorIndex = remap.applyAsInt(descriptorIndex);
        }
    }

    private LocalVariableEntry[] entries;

    LocalVariableTableAttributeInfo() {
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
            return LocalVariableEntry.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == LocalVariableEntry.class) {
            return entries.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == LocalVariableEntry.class) {
            return entries[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.HIGH;
    }

    public static final String NAME = "LocalVariableTable";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 10 * entries.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int tableLength = in.readUnsignedShort();
        entries = new LocalVariableEntry[tableLength];
        for (int index = 0; index < tableLength; index++) {
            LocalVariableEntry entry = new LocalVariableEntry();
            entry.read(in);
            entries[index] = entry;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int tableLength = 0xffff & buffer.getShort();
        entries = new LocalVariableEntry[tableLength];
        for (int index = 0; index < tableLength; index++) {
            LocalVariableEntry entry = new LocalVariableEntry();
            entry.read(buffer);
            entries[index] = entry;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(entries.length);
        for (LocalVariableEntry entry : entries) {
            entry.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) entries.length);
        for (LocalVariableEntry entry : entries) {
            entry.write(buffer);
        }
    }
}
