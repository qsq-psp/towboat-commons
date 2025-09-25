package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/15")
public class InnerClassesAttributeInfo extends AttributeInfo {

    @CodeHistory(date = "2025/9/15")
    private static class InnerClassEntry implements Dependent {

        int innerClassIndex; // CONSTANT_CLASS

        int outerClassIndex; // CONSTANT_CLASS

        int innerNameIndex; // CONSTANT_UTF8

        int innerAccessFlags;

        InnerClassEntry() {
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
            innerClassIndex = in.readUnsignedShort();
            outerClassIndex = in.readUnsignedShort();
            innerNameIndex = in.readUnsignedShort();
            innerAccessFlags = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            innerClassIndex = 0xffff & buffer.getShort();
            outerClassIndex = 0xffff & buffer.getShort();
            innerNameIndex = 0xffff & buffer.getShort();
            innerAccessFlags = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeShort(innerClassIndex);
            out.writeShort(outerClassIndex);
            out.writeShort(innerNameIndex);
            out.writeShort(innerAccessFlags);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.putShort((short) innerClassIndex);
            buffer.putShort((short) outerClassIndex);
            buffer.putShort((short) innerNameIndex);
            buffer.putShort((short) innerAccessFlags);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            final StringBuilder sb = new StringBuilder();
            if ((innerAccessFlags & Modifier.PUBLIC) != 0) {
                sb.append("public ");
            }
            if ((innerAccessFlags & Modifier.PROTECTED) != 0) {
                sb.append("protected ");
            }
            if ((innerAccessFlags & Modifier.PRIVATE) != 0) {
                sb.append("private ");
            }
            if ((innerAccessFlags & Modifier.STATIC) != 0) {
                sb.append("static ");
            }
            if ((innerAccessFlags & Modifier.FINAL) != 0) {
                sb.append("final ");
            }
            if ((innerAccessFlags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            if ((innerAccessFlags & 0x4000) != 0) {
                sb.append("enum ");
            } else if ((innerAccessFlags & 0x2000) != 0) {
                sb.append("annotation ");
            } else if ((innerAccessFlags & Modifier.INTERFACE) != 0) {
                sb.append("interface ");
            } else {
                if ((innerAccessFlags & Modifier.ABSTRACT) != 0) {
                    sb.append("abstract ");
                }
                sb.append("class ");
            }
            if (innerNameIndex == 0) {
                sb.append("? ");
            } else {
                sb.append(context.constantPool.getUtf8(innerNameIndex).replace('/', '.')).append(' ');
            }
            sb.append("inner ");
            if (innerClassIndex == 0) {
                sb.append("? ");
            } else {
                sb.append(context.constantPool.getSourceClassName(innerClassIndex)).append(' ');
            }
            sb.append("outer ");
            if (outerClassIndex == 0) {
                sb.append('?');
            } else {
                sb.append(context.constantPool.getSourceClassName(outerClassIndex));
            }
            return sb.toString();
        }
    }

    InnerClassEntry[] entries;

    public InnerClassesAttributeInfo() {
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
            return InnerClassEntry.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        if (group == InnerClassEntry.class) {
            return entries.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == InnerClassEntry.class) {
            return entries[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final String NAME = "InnerClasses";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 8 * entries.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int entryCount = in.readUnsignedShort();
        entries = new InnerClassEntry[entryCount];
        for (int index = 0; index < entryCount; index++) {
            InnerClassEntry entry = new InnerClassEntry();
            entry.read(context, in);
            entries[index] = entry;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int entryCount = 0xffff & buffer.getShort();
        entries = new InnerClassEntry[entryCount];
        for (int index = 0; index < entryCount; index++) {
            InnerClassEntry entry = new InnerClassEntry();
            entry.read(context, buffer);
            entries[index] = entry;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(entries.length);
        for (InnerClassEntry entry : entries) {
            entry.write(context, out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) entries.length);
        for (InnerClassEntry entry : entries) {
            entry.write(context, buffer);
        }
    }
}
