package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/17")
public class LineNumberTableAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xBE372F5329355F0DL;

    @CodeHistory(date = "2025/9/17")
    private static class LineNumberEntry implements Dependent {

        private static final long serialVersionUID = 0x3400DFA459BFA386L;

        int startPC; // u2

        int lineNumber;

        LineNumberEntry() {
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

        @Override
        public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
            startPC = in.readUnsignedShort();
            lineNumber = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            lineNumber = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(lineNumber);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) lineNumber);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "PC " + startPC + " line " + lineNumber;
        }
    }

    LineNumberEntry[] entries;

    LineNumberTableAttributeInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return LineNumberEntry.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == LineNumberEntry.class) {
            return entries.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == LineNumberEntry.class) {
            return entries[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
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
            entry.read(context, in);
            entries[index] = entry;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int tableLength = 0xffff & buffer.getShort();
        entries = new LineNumberEntry[tableLength];
        for (int index = 0; index < tableLength; index++) {
            LineNumberEntry entry = new LineNumberEntry();
            entry.read(context, buffer);
            entries[index] = entry;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(entries.length);
        for (LineNumberEntry entry : entries) {
            entry.write(context, out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) entries.length);
        for (LineNumberEntry entry : entries) {
            entry.write(context, buffer);
        }
    }
}
