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

@CodeHistory(date = "2025/9/14")
@ReferencePage(title = "JVMS12 The StackMapTable Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.4")
class StackMapTableAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xece64000722fc308L;

    @CodeHistory(date = "2025/9/14")
    private static class VerificationTypeInfo extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x96ca6590d0241d72L;

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
        static VerificationTypeInfo createByTag(@DataType("u8") int tag) {
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
                    throw new ClassFormatError("tag = " + tag);
            }
        }

        @NotNull
        static VerificationTypeInfo readNew(@NotNull LimitedDataInput in) throws IOException {
            final VerificationTypeInfo verificationTypeInfo = createByTag(in.readUnsignedByte());
            verificationTypeInfo.read(in);
            return verificationTypeInfo;
        }

        @NotNull
        static VerificationTypeInfo readNew(@NotNull ByteBuffer buffer) {
            final VerificationTypeInfo verificationTypeInfo = createByTag(0xff & buffer.get());
            verificationTypeInfo.read(buffer);
            return verificationTypeInfo;
        }

        @DataType("u8")
        final int tag;

        VerificationTypeInfo(int tag) {
            super();
            this.tag = tag;
        }

        int byteSize() {
            return 1;
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
        public String toString(@NotNull ClassFile context) {
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
                    return "(" + tag + ")";
            }
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            // pass
        }
    }

    @CodeHistory(date = "2025/9/14")
    private static class ObjectVariableInfo extends VerificationTypeInfo {

        private static final long serialVersionUID = 0x5b1f6398ecd93414L;

        @ConstantType(tags = ClassConstantInfo.TAG)
        int classIndex;

        ObjectVariableInfo(int tag) {
            super(tag);
        }

        int byteSize() {
            return 3;
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
        public String toString(@NotNull ClassFile context) {
            return toString() + " " + context.constantPool.getSourceClassName(classIndex);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            classIndex = remap.applyAsInt(classIndex);
        }
    }

    @CodeHistory(date = "2025/9/14")
    private static class UninitializedVariableInfo extends VerificationTypeInfo {

        private static final long serialVersionUID = 0x8ddbbd032dea1b4bL;

        int newCP; // the PC (program counter) value of the corresponding new instruction

        UninitializedVariableInfo(int tag) {
            super(tag);
        }

        int byteSize() {
            return 3;
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
        public String toString(@NotNull ClassFile context) {
            return toString() + " " + newCP;
        }
    }

    /**
     * The base class is same_frame, frame_type 0-63
     */
    @CodeHistory(date = "2025/9/14")
    private static class StackMapFrame implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x8ddbbd032dea1b4bL;

        @DataType("u8")
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

        int byteSize() {
            return 1;
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
        public String toString(@NotNull ClassFile context) {
            return "same-frame " + frameType;
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            // pass
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame, frame_type 64-127
     */
    @CodeHistory(date = "2025/9/14")
    private static class SameLocalsOneStackItemFrame extends StackMapFrame {

        private static final long serialVersionUID = 0x8d5692a62c622b6dL;

        VerificationTypeInfo stack;

        SameLocalsOneStackItemFrame(int frameType) {
            super(frameType);
        }

        @Override
        int byteSize() {
            return 1 + stack.byteSize();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            stack = VerificationTypeInfo.readNew(in);
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            stack = VerificationTypeInfo.readNew(buffer);
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
            stack.write(out);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
            stack.write(buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "same-locals-1-stack-item-frame " + (frameType - 64);
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            stack.remapConstant(remap);
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame_extended, frame_type 247
     */
    @CodeHistory(date = "2025/9/14")
    private static class SameLocalsOneStackItemFrameExtended extends SameLocalsOneStackItemFrame {

        private static final long serialVersionUID = 0x7eabe48e44c503daL;

        int offsetDelta;

        SameLocalsOneStackItemFrameExtended(int frameType) {
            super(frameType);
        }

        @Override
        int byteSize() {
            return 3 + stack.byteSize();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            offsetDelta = in.readUnsignedShort();
            super.read(in);
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            offsetDelta = 0xffff & buffer.getShort();
            super.read(buffer);
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
            out.writeShort(offsetDelta);
            stack.write(out);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
            buffer.putShort((short) offsetDelta);
            stack.write(buffer);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "same-locals-1-stack-item-extended " + offsetDelta;
        }
    }

    /**
     * This class is same_locals_1_stack_item_frame, frame_type 248-250
     * Or same_frame_extended, frame_type 251
     */
    @CodeHistory(date = "2025/9/14")
    private static class ChopFrameOrSameFrameExtended extends StackMapFrame {

        private static final long serialVersionUID = 0xf31c986fbe577603L;

        int offsetDelta;

        ChopFrameOrSameFrameExtended(int frameType) {
            super(frameType);
        }

        int byteSize() {
            return 3;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            offsetDelta = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            offsetDelta = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
            out.writeShort(offsetDelta);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
            buffer.putShort((short) offsetDelta);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
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

        private static final long serialVersionUID = 0x4f4dffc2d3b0c00dL;

        final VerificationTypeInfo[] locals;

        int offsetDelta;

        AppendFrame(int frameType) {
            super(frameType);
            locals = new VerificationTypeInfo[frameType - 251];
        }

        @Override
        int byteSize() {
            int size = 3;
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                size += verificationTypeInfo.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            offsetDelta = in.readUnsignedShort();
            for (int index = 0; index < locals.length; index++) {
                locals[index] = VerificationTypeInfo.readNew(in);
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            offsetDelta = 0xffff & buffer.getShort();
            for (int index = 0; index < locals.length; index++) {
                locals[index] = VerificationTypeInfo.readNew(buffer);
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
            out.writeShort(offsetDelta);
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
            buffer.putShort((short) offsetDelta);
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.write(buffer);
            }
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.remapConstant(remap);
            }
        }
    }

    /**
     * This class is full_frame, frame_type 255
     */
    @CodeHistory(date = "2025/9/14")
    private static class FullFrame extends StackMapFrame {

        private static final long serialVersionUID = 0x1cae38435f2a1568L;

        int offsetDelta;

        VerificationTypeInfo[] locals;

        VerificationTypeInfo[] stack;

        FullFrame(int frameType) {
            super(frameType);
        }

        @Override
        int byteSize() {
            int size = 7;
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                size += verificationTypeInfo.byteSize();
            }
            for (VerificationTypeInfo verificationTypeInfo : stack) {
                size += verificationTypeInfo.byteSize();
            }
            return size;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            offsetDelta = in.readUnsignedShort();
            int length = in.readUnsignedShort();
            locals = new VerificationTypeInfo[length];
            for (int index = 0; index < length; index++) {
                locals[index] = VerificationTypeInfo.readNew(in);
            }
            length = in.readUnsignedShort();
            stack = new VerificationTypeInfo[length];
            for (int index = 0; index < length; index++) {
                stack[index] = VerificationTypeInfo.readNew(in);
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            offsetDelta = 0xffff & buffer.getShort();
            int length = 0xffff & buffer.getShort();
            locals = new VerificationTypeInfo[length];
            for (int index = 0; index < length; index++) {
                locals[index] = VerificationTypeInfo.readNew(buffer);
            }
            length = 0xffff & buffer.getShort();
            stack = new VerificationTypeInfo[length];
            for (int index = 0; index < length; index++) {
                stack[index] = VerificationTypeInfo.readNew(buffer);
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeByte(frameType);
            out.writeShort(offsetDelta);
            out.writeShort(locals.length);
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.write(out);
            }
            out.writeShort(stack.length);
            for (VerificationTypeInfo verificationTypeInfo : stack) {
                verificationTypeInfo.write(out);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.put((byte) frameType);
            buffer.putShort((short) offsetDelta);
            buffer.putShort((short) locals.length);
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.write(buffer);
            }
            buffer.putShort((short) stack.length);
            for (VerificationTypeInfo verificationTypeInfo : stack) {
                verificationTypeInfo.write(buffer);
            }
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            for (VerificationTypeInfo verificationTypeInfo : locals) {
                verificationTypeInfo.remapConstant(remap);
            }
            for (VerificationTypeInfo verificationTypeInfo : stack) {
                verificationTypeInfo.remapConstant(remap);
            }
        }
    }

    private StackMapFrame[] entries;

    StackMapTableAttributeInfo() {
        super();
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.CRITICAL;
    }

    public static final String NAME = "StackMapTable";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 2;
        for (StackMapFrame item : entries) {
            size += item.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int numberOfEntries = in.readUnsignedShort();
        entries = new StackMapFrame[numberOfEntries];
        for (int index = 0; index < numberOfEntries; index++) {
            int frameType = in.readUnsignedByte();
            StackMapFrame item;
            if (frameType < 64) {
                item = new StackMapFrame(frameType); // SameFrame
            } else if (frameType < 128) {
                item = new SameLocalsOneStackItemFrame(frameType);
            } else if (frameType < 247) {
                throw new BytecodeException();
            } else if (frameType == 247) {
                item = new SameLocalsOneStackItemFrameExtended(frameType);
            } else if (frameType < 251) {
                item = new ChopFrameOrSameFrameExtended(frameType);
            } else if (frameType < 255) {
                item = new AppendFrame(frameType);
            } else {
                item = new FullFrame(frameType);
            }
            item.read(in);
            entries[index] = item;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int numberOfEntries = 0xffff & buffer.getShort();
        entries = new StackMapFrame[numberOfEntries];
        for (int index = 0; index < numberOfEntries; index++) {
            int frameType = 0xff & buffer.get();
            StackMapFrame item;
            if (frameType < 64) {
                item = new StackMapFrame(frameType); // SameFrame
            } else if (frameType < 128) {
                item = new SameLocalsOneStackItemFrame(frameType);
            } else if (frameType < 247) {
                throw new BytecodeException();
            } else if (frameType == 247) {
                item = new SameLocalsOneStackItemFrameExtended(frameType);
            } else if (frameType < 251) {
                item = new ChopFrameOrSameFrameExtended(frameType);
            } else if (frameType < 255) {
                item = new AppendFrame(frameType);
            } else {
                item = new FullFrame(frameType);
            }
            item.read(buffer);
            entries[index] = item;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(entries.length);
        for (StackMapFrame item : entries) {
            item.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) entries.length);
        for (StackMapFrame item : entries) {
            item.write(buffer);
        }
    }
}
