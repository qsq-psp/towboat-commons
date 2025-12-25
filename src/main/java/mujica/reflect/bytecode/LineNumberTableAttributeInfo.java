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

@CodeHistory(date = "2025/9/17")
@ReferencePage(title = "JVMS12 The LineNumberTable Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.12")
class LineNumberTableAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xbe372f5329355f0dL;

    @CodeHistory(date = "2025/9/17")
    private static class LineNumberEntry extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x3400dfa459bfa386L;

        @DataType("u16")
        int startPC;

        @DataType("u16")
        int lineNumber;

        LineNumberEntry() {
            super();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            startPC = in.readUnsignedShort();
            lineNumber = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            lineNumber = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(lineNumber);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) lineNumber);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "PC " + startPC + " line " + lineNumber;
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            // pass
        }
    }

    private LineNumberEntry[] entries;

    LineNumberTableAttributeInfo() {
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
            return LineNumberEntry.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == LineNumberEntry.class) {
            return entries.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == LineNumberEntry.class) {
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

    public static final String NAME = "LineNumberTable";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 4 * entries.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int tableLength = in.readUnsignedShort();
        entries = new LineNumberEntry[tableLength];
        for (int index = 0; index < tableLength; index++) {
            LineNumberEntry entry = new LineNumberEntry();
            entry.read(in);
            entries[index] = entry;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int tableLength = 0xffff & buffer.getShort();
        entries = new LineNumberEntry[tableLength];
        for (int index = 0; index < tableLength; index++) {
            LineNumberEntry entry = new LineNumberEntry();
            entry.read(buffer);
            entries[index] = entry;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(entries.length);
        for (LineNumberEntry entry : entries) {
            entry.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) entries.length);
        for (LineNumberEntry entry : entries) {
            entry.write(buffer);
        }
    }
}
