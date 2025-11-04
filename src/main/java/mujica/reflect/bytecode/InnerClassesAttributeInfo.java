package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/15")
@ReferencePage(title = "JVMS12 The InnerClasses Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.6")
public class InnerClassesAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xBA27CD966BC157F7L;

    @CodeHistory(date = "2025/9/15")
    private static class InnerClassEntry implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xAB333D9BE3F93997L;

        @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
        int innerClassIndex;

        @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
        int outerClassIndex;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int innerNameIndex;

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
        public void read(@NotNull LimitedDataInput in) throws IOException {
            innerClassIndex = in.readUnsignedShort();
            outerClassIndex = in.readUnsignedShort();
            innerNameIndex = in.readUnsignedShort();
            innerAccessFlags = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            innerClassIndex = 0xffff & buffer.getShort();
            outerClassIndex = 0xffff & buffer.getShort();
            innerNameIndex = 0xffff & buffer.getShort();
            innerAccessFlags = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(innerClassIndex);
            out.writeShort(outerClassIndex);
            out.writeShort(innerNameIndex);
            out.writeShort(innerAccessFlags);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) innerClassIndex);
            buffer.putShort((short) outerClassIndex);
            buffer.putShort((short) innerNameIndex);
            buffer.putShort((short) innerAccessFlags);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
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

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            innerClassIndex = remap.applyAsInt(innerClassIndex);
            outerClassIndex = remap.applyAsInt(outerClassIndex);
            innerNameIndex = remap.applyAsInt(innerNameIndex);
        }
    }

    private InnerClassEntry[] entries;

    InnerClassesAttributeInfo() {
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

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.HIGH;
    }

    @Override
    public boolean isNecessary() {
        return true;
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
            entry.read(in);
            entries[index] = entry;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int entryCount = 0xffff & buffer.getShort();
        entries = new InnerClassEntry[entryCount];
        for (int index = 0; index < entryCount; index++) {
            InnerClassEntry entry = new InnerClassEntry();
            entry.read(buffer);
            entries[index] = entry;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(entries.length);
        for (InnerClassEntry entry : entries) {
            entry.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) entries.length);
        for (InnerClassEntry entry : entries) {
            entry.write(buffer);
        }
    }
}
