package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import mujica.text.number.HexEncoder;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/5")
@ReferencePage(title = "JVMS12 The Code Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.3")
public class CodeAttributeInfo extends AttributeInfo implements Consumer<CodeAttributeInfo.Statistics> {

    public static int dumpInstruction(@NotNull ByteBuffer code, int pc, @NotNull StringBuilder out) {
        final int opcode = 0xff & code.get(pc);
        out.append(Opcode.NAMES[opcode]);
        switch (opcode) {
            default:
                pc++;
                break;
            case Opcode.BIPUSH:
            case Opcode.LDC:
            case Opcode.ILOAD:
            case Opcode.LLOAD:
            case Opcode.FLOAD:
            case Opcode.DLOAD:
            case Opcode.ALOAD:
            case Opcode.ISTORE:
            case Opcode.LSTORE:
            case Opcode.FSTORE:
            case Opcode.DSTORE:
            case Opcode.ASTORE:
            case Opcode.RET:
            case Opcode.NEWARRAY:
                pc += 2;
                break;
            case Opcode.SIPUSH:
            case Opcode.LDC_W:
            case Opcode.LDC2_W:
            case Opcode.IINC:
            case Opcode.IFEQ:
            case Opcode.IFNE:
            case Opcode.IFLT:
            case Opcode.IFGE:
            case Opcode.IFGT:
            case Opcode.IFLE:
            case Opcode.IF_ICMPEQ:
            case Opcode.IF_ICMPNE:
            case Opcode.IF_ICMPLT:
            case Opcode.IF_ICMPGE:
            case Opcode.IF_ICMPGT:
            case Opcode.IF_ICMPLE:
            case Opcode.IF_ACMPEQ:
            case Opcode.IF_ACMPNE:
            case Opcode.GOTO:
            case Opcode.JSR:
            case Opcode.GETSTATIC:
            case Opcode.PUTSTATIC:
            case Opcode.GETFIELD:
            case Opcode.PUTFIELD:
            case Opcode.INVOKEVIRTUAL:
            case Opcode.INVOKESPECIAL:
            case Opcode.INVOKESTATIC:
            case Opcode.NEW:
            case Opcode.ANEWARRAY:
            case Opcode.CHECKCAST:
            case Opcode.INSTANCEOF:
            case Opcode.IFNULL:
            case Opcode.IFNONNULL:
                pc += 3;
                break;
            case Opcode.MULTIANEWARRAY:
                pc += 4;
                break;
            case Opcode.INVOKEINTERFACE:
            case Opcode.INVOKEDYNAMIC:
            case Opcode.GOTO_W:
            case Opcode.JSR_W:
                pc += 5;
                break;
            case Opcode.TABLESWITCH: {
                pc = (pc & 0x3) + 0x8;
                int low = code.getInt(pc);
                pc += 0x4;
                int high = code.getInt(pc);
                pc += (high - low + 2) * 0x4;
                break;
            }
            case Opcode.LOOKUPSWITCH: {
                pc = (pc & 0x3) + 0x8;
                pc += 0x4 + (code.getInt(pc) << 3);
                break;
            }
            case Opcode.WIDE:
                if ((0xff & code.get(pc + 1)) == Opcode.IINC) {
                    pc += 6;
                } else {
                    pc += 4;
                }
                break;
        }
        return pc;
    }

    public static void dumpCode(@NotNull ByteBuffer code, @NotNull StringBuilder out) {
        for (int pc = code.position(); pc < code.limit(); pc = dumpInstruction(code, pc, out)) {
            out.append(System.lineSeparator());
        }
    }

    private static final long serialVersionUID = 0x526ebb1319642acbL;

    int maxStack;

    int maxLocals;

    byte[] code; // wrap on need

    @CodeHistory(date = "2025/9/5")
    private static class CodeException implements Independent {

        private static final long serialVersionUID = 0x4f5e8887fd9f3da5L;

        int startPC;

        int endPC;

        int handlerPC;

        @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
        int catchType;

        CodeException() {
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

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            startPC = in.readUnsignedShort();
            endPC = in.readUnsignedShort();
            handlerPC = in.readUnsignedShort();
            catchType = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            endPC = 0xffff & buffer.getShort();
            handlerPC = 0xffff & buffer.getShort();
            catchType = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(endPC);
            out.writeShort(handlerPC);
            out.writeShort(catchType);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) endPC);
            buffer.putShort((short) handlerPC);
            buffer.putShort((short) catchType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "try from " + startPC + " to " + endPC + " catch " + context.constantPool.getSourceClassName(catchType) + " goto " + handlerPC;
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            catchType = remap.applyAsInt(catchType);
        }
    }

    private CodeException[] exceptions;

    private AttributeInfo[] attributes;

    CodeAttributeInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 2;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return CodeException.class;
        } else if (groupIndex == 1) {
            return AttributeInfo.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == CodeException.class) {
            return exceptions.length;
        } else if (group == AttributeInfo.class) {
            return attributes.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == CodeException.class) {
            return exceptions[nodeIndex];
        } else if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.CRITICAL;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    public static final String NAME = "Code";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 12;
        size += code.length;
        size += 8 * exceptions.length;
        for (AttributeInfo attribute : attributes) {
            size += 6 + attribute.byteSize();
        }
        return size;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        maxStack = in.readUnsignedShort();
        maxLocals = in.readUnsignedShort();
        code = new byte[in.readInt()];
        in.readFully(code);
        final int exceptionTableLength = in.readUnsignedShort();
        exceptions = new CodeException[exceptionTableLength];
        for (int index = 0; index < exceptionTableLength; index++) {
            CodeException exception = new CodeException();
            exception.read(in);
            exceptions[index] = exception;
        }
        attributes = AttributeInfo.readArray(context, in);
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        maxStack = 0xffff & buffer.getShort();
        maxLocals = 0xffff & buffer.getShort();
        code = new byte[buffer.getInt()];
        buffer.get(code);
        final int exceptionTableLength = 0xffff & buffer.getShort();
        exceptions = new CodeException[exceptionTableLength];
        for (int index = 0; index < exceptionTableLength; index++) {
            CodeException exception = new CodeException();
            exception.read(buffer);
            exceptions[index] = exception;
        }
        attributes = AttributeInfo.readArray(context, buffer);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(maxStack);
        out.writeShort(maxLocals);
        out.writeInt(code.length);
        out.write(code);
        out.writeShort(exceptions.length);
        for (CodeException exception : exceptions) {
            exception.write(out);
        }
        AttributeInfo.writeArray(attributes, context, out);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) maxStack);
        buffer.putShort((short) maxLocals);
        buffer.putInt(code.length);
        buffer.put(code);
        buffer.putShort((short) exceptions.length);
        for (CodeException exception : exceptions) {
            exception.write(buffer);
        }
        AttributeInfo.writeArray(attributes, context, buffer);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return NAME + " " + code.length;
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        super.accept(statistics, prefix);
        prefix = prefix + NAME + ".";
        for (AttributeInfo attribute : attributes) {
            attribute.accept(statistics, prefix);
        }
    }

    @CodeHistory(date = "2025/10/4")
    public static class Statistics {

        final int[] opcodeCount = new int[Opcode.BREAKPOINT]; // breakpoint is not counted
    }

    public static int[] INT_ALIGN = {4, 7, 6, 5};

    public static int next(@NotNull ByteBuffer buffer) {
        final int opcode = 0xff & buffer.get();
        switch (opcode) {
            default:
                break;
            case Opcode.BIPUSH:
            case Opcode.LDC:
            case Opcode.ILOAD:
            case Opcode.LLOAD:
            case Opcode.FLOAD:
            case Opcode.DLOAD:
            case Opcode.ALOAD:
            case Opcode.ISTORE:
            case Opcode.LSTORE:
            case Opcode.FSTORE:
            case Opcode.DSTORE:
            case Opcode.ASTORE:
            case Opcode.RET:
            case Opcode.NEWARRAY:
                buffer.get();
                break;
            case Opcode.SIPUSH:
            case Opcode.LDC_W:
            case Opcode.LDC2_W:
            case Opcode.IINC:
            case Opcode.IFEQ:
            case Opcode.IFNE:
            case Opcode.IFLT:
            case Opcode.IFGE:
            case Opcode.IFGT:
            case Opcode.IFLE:
            case Opcode.IF_ICMPEQ:
            case Opcode.IF_ICMPNE:
            case Opcode.IF_ICMPLT:
            case Opcode.IF_ICMPGE:
            case Opcode.IF_ICMPGT:
            case Opcode.IF_ICMPLE:
            case Opcode.IF_ACMPEQ:
            case Opcode.IF_ACMPNE:
            case Opcode.GOTO:
            case Opcode.JSR:
            case Opcode.GETSTATIC:
            case Opcode.PUTSTATIC:
            case Opcode.GETFIELD:
            case Opcode.PUTFIELD:
            case Opcode.INVOKEVIRTUAL:
            case Opcode.INVOKESPECIAL:
            case Opcode.INVOKESTATIC:
            case Opcode.NEW:
            case Opcode.ANEWARRAY:
            case Opcode.CHECKCAST:
            case Opcode.INSTANCEOF:
            case Opcode.IFNULL:
            case Opcode.IFNONNULL:
                buffer.getShort();
                break;
            case Opcode.MULTIANEWARRAY:
                buffer.get();
                buffer.getShort();
                break;
            case Opcode.INVOKEINTERFACE:
            case Opcode.INVOKEDYNAMIC:
            case Opcode.GOTO_W:
            case Opcode.JSR_W:
                buffer.getInt();
                break;
            case Opcode.TABLESWITCH: {
                buffer.position(buffer.position() + INT_ALIGN[buffer.position() & 0x3]);
                int low = buffer.getInt();
                int high = buffer.getInt();
                buffer.position(buffer.position() + ((high - low + 1) << 2));
                break;
            }
            case Opcode.LOOKUPSWITCH: {
                buffer.position(buffer.position() + INT_ALIGN[buffer.position() & 0x3]);
                int pairs = buffer.getInt();
                buffer.position(buffer.position() + (pairs << 3));
                break;
            }
            case Opcode.WIDE:
                if ((0xff & buffer.get()) == Opcode.IINC) {
                    buffer.getInt();
                } else {
                    buffer.getShort();
                }
                break;
        }
        return opcode;
    }

    @Override
    public void accept(Statistics statistics) {
        final ByteBuffer buffer = ByteBuffer.wrap(code);
        buffer.order(ByteOrder.BIG_ENDIAN);
        while (buffer.hasRemaining()) {
            statistics.opcodeCount[next(buffer)]++;
        }
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        // todo
        for (CodeException exception : exceptions) {
            exception.remapConstant(remap);
        }
        for (AttributeInfo attribute : attributes) {
            attribute.remapConstant(remap);
        }
    }

    public void writeAssemble(@NotNull ClassFile context, @NotNull IndentWriter writer) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(code);
        buffer.order(ByteOrder.BIG_ENDIAN);
        while (buffer.hasRemaining()) {
            writer.newLine();
            int opcode = next(buffer);
            StringBuilder sb = new StringBuilder();
            sb.append("0x");
            HexEncoder.LOWER_ENCODER.hex8(sb, opcode);
            sb.append(" ").append(Opcode.NAMES[opcode]);
            writer.write(sb.toString());
        }
    }
}
