package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.InterpretAsByte;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/14.
 */
@CodeHistory(date = "2025/9/14")
public class StackMapTableAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xECE64000722FC308L;

    @CodeHistory(date = "2025/9/14")
    private static class VerificationTypeInfo implements Independent {

        private static final long serialVersionUID = 0x96CA6590D0241D72L;

        static final int ITEM_TOP = 0;
        static final int ITEM_INTEGER = 1;
        static final int ITEM_FLOAT = 2;
        static final int ITEM_DOUBLE = 3;
        static final int ITEM_LONG = 4;
        static final int ITEM_NULL = 5;
        static final int ITEM_UNINITIALIZED_THIS = 6;
        static final int ITEM_OBJECT = 7;
        static final int ITEM_UNINITIALIZED = 8;

        @NotNull
        static VerificationTypeInfo createByTag(int tag) {
            switch (tag) {
                case ITEM_TOP:
                case ITEM_INTEGER:
                case ITEM_FLOAT:
                case ITEM_DOUBLE:
                case ITEM_LONG:
                case ITEM_NULL:
                case ITEM_UNINITIALIZED_THIS:
                    return new VerificationTypeInfo(tag);
                case ITEM_OBJECT:
                    return new ObjectVariableInfo(tag);
                case ITEM_UNINITIALIZED:
                    return new UninitializedVariableInfo(tag);
                default:
                    throw new BytecodeException("StackMapTable VerificationTypeInfo tag = " + tag);
            }
        }

        @InterpretAsByte(unsigned = true)
        final int tag;

        VerificationTypeInfo(int tag) {
            super();
            this.tag = tag;
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
            // pass
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            // pass
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(tag);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) tag);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return toString();
        }

        @Override
        @NotNull
        public String toString() {
            switch (tag) {
                case ITEM_TOP:
                    return "top";
                case ITEM_INTEGER:
                    return "int";
                case ITEM_FLOAT:
                    return "float";
                case ITEM_DOUBLE:
                    return "double";
                case ITEM_LONG:
                    return "long";
                case ITEM_NULL:
                    return "null";
                case ITEM_UNINITIALIZED_THIS:
                    return "uninitialized-this";
                case ITEM_OBJECT:
                    return "object";
                case ITEM_UNINITIALIZED:
                    return "uninitialized";
                default:
                    throw new BytecodeException("StackMapTable VerificationTypeInfo tag = " + tag);
            }
        }
    }

    @CodeHistory(date = "2025/9/14")
    private static class ObjectVariableInfo extends VerificationTypeInfo {

        private static final long serialVersionUID = 0x5B1F6398ECD93414L;

        int classIndex; // CONSTANT_CLASS

        ObjectVariableInfo(int tag) {
            super(tag);
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            classIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            classIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeShort(classIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.putShort((short) classIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return toString() + " " + context.constantPool.getSourceClassName(classIndex);
        }
    }

    @CodeHistory(date = "2025/9/14")
    private static class UninitializedVariableInfo extends VerificationTypeInfo {

        private static final long serialVersionUID = 0x8DDBBD032DEA1B4BL;

        int newCP; // the PC (program counter) value of the corresponding new instruction

        UninitializedVariableInfo(int tag) {
            super(tag);
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            newCP = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            newCP = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            super.write(out);
            out.writeShort(newCP);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            super.write(buffer);
            buffer.putShort((short) newCP);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return toString() + " " + newCP;
        }
    }

    /**
     * The base class is same_frame, frame_type 0-63
     */
    @CodeHistory(date = "2025/9/14")
    private static class StackMapFrame implements Independent {

        private static final long serialVersionUID = 0x8DDBBD032DEA1B4BL;

        @InterpretAsByte(unsigned = true)
        final int frameType;

        StackMapFrame(int frameType) {
            super();
            this.frameType = frameType;
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
            // pass
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            // pass
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "same-frame " + frameType;
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame, frame_type 64-127
     */
    @CodeHistory(date = "2025/9/14")
    private static class SameLocalsOneStackItemFrame extends StackMapFrame {

        private static final long serialVersionUID = 0x8D5692A62C622B6DL;

        VerificationTypeInfo stack;

        SameLocalsOneStackItemFrame(int frameType) {
            super(frameType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "same-locals-1-stack-item-frame " + (frameType - 64);
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame_extended, frame_type 247
     */
    @CodeHistory(date = "2025/9/14")
    private static class SameLocalsOneStackItemFrameExtended extends SameLocalsOneStackItemFrame {

        private static final long serialVersionUID = 0x7EABE48E44C503DAL;

        int offsetDelta;

        SameLocalsOneStackItemFrameExtended(int frameType) {
            super(frameType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "same-locals-1-stack-item-extended " + offsetDelta;
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame, frame_type 248-250
     * Or same_frame_extended, frame_type 251
     */
    @CodeHistory(date = "2025/9/14")
    private static class ChopFrameOrSameFrameExtended extends StackMapFrame {

        private static final long serialVersionUID = 0xF31C986FBE577603L;

        int offsetDelta;

        ChopFrameOrSameFrameExtended(int frameType) {
            super(frameType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            if (frameType == 251) {
                return "same-frame-extended " + offsetDelta;
            } else {
                return "chop-" + (251 - frameType) + "-frame " + offsetDelta;
            }
        }
    }

    /**
     * This class is append_frame, frame_type 252-254
     */
    @CodeHistory(date = "2025/9/14")
    private static class AppendFrame extends StackMapFrame {

        private static final long serialVersionUID = 0x4F4DFFC2D3B0C00DL;

        int offsetDelta;

        VerificationTypeInfo[] locals;

        AppendFrame(int frameType) {
            super(frameType);
        }
    }

    /**
     * This class is full_frame, frame_type 255
     */
    @CodeHistory(date = "2025/9/14")
    private static class FullFrame extends StackMapFrame {

        private static final long serialVersionUID = 0x1CAE38435F2A1568L;

        int offsetDelta;

        VerificationTypeInfo[] locals;

        VerificationTypeInfo[] stack;

        FullFrame(int frameType) {
            super(frameType);
        }
    }

    StackMapFrame[] entries;

    public static final String NAME = "StackMapTable";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int numberOfEntries = in.readUnsignedShort();
        entries = new StackMapFrame[numberOfEntries];
        for (int index = 0; index < numberOfEntries; index++) {
            int frameType = in.readUnsignedByte();
            StackMapFrame frame;
            if (frameType < 64) {
                frame = new StackMapFrame(frameType); // SameFrame
            } else if (frameType < 128) {
                frame = new SameLocalsOneStackItemFrame(frameType);
            } else if (frameType < 247) {
                throw new IOException();
            } else if (frameType == 247) {
                frame = new SameLocalsOneStackItemFrameExtended(frameType);
            } else if (frameType < 251) {
                frame = new ChopFrameOrSameFrameExtended(frameType);
            } else if (frameType < 255) {
                frame = new AppendFrame(frameType);
            } else {
                frame = new FullFrame(frameType);
            }
            // read
            entries[index] = frame;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {

    }
}
