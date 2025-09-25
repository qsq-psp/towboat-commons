package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/5")
public class CodeAttributeInfo extends AttributeInfo {

    public static int nextPC(@NotNull ByteBuffer code, int pc) {
        switch (0xff & code.get(pc)) {
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
    private static class CodeException implements Dependent {

        private static final long serialVersionUID = 0x4f5e8887fd9f3da5L;

        int startPC;

        int endPC;

        int handlerPC;

        int catchType; // CONSTANT_CLASS

        CodeException() {
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
            endPC = in.readUnsignedShort();
            handlerPC = in.readUnsignedShort();
            catchType = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            startPC = 0xffff & buffer.getShort();
            endPC = 0xffff & buffer.getShort();
            handlerPC = 0xffff & buffer.getShort();
            catchType = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
            out.writeShort(startPC);
            out.writeShort(endPC);
            out.writeShort(handlerPC);
            out.writeShort(catchType);
        }

        @Override
        public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
            buffer.putShort((short) startPC);
            buffer.putShort((short) endPC);
            buffer.putShort((short) handlerPC);
            buffer.putShort((short) catchType);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            return "try from " + startPC + " to " + endPC + " catch " + context.constantPool.getSourceClassName(catchType) + " goto " + handlerPC;
        }
    }

    CodeException[] exceptions;

    AttributeInfo[] attributes;

    public CodeAttributeInfo() {
        super();
    }

    @Override
    public int groupCount() {
        return 2;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return CodeException.class;
        } else if (groupIndex == 1) {
            return AttributeInfo.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
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
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        if (group == CodeException.class) {
            return exceptions[nodeIndex];
        } else if (group == AttributeInfo.class) {
            return attributes[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
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
            exception.read(context, in);
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
            exception.read(context, buffer);
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
            exception.write(context, out);
        }
        AttributeInfo.writeArray(context, out, attributes);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) maxStack);
        buffer.putShort((short) maxLocals);
        buffer.putInt(code.length);
        buffer.put(code);
        buffer.putShort((short) exceptions.length);
        for (CodeException exception : exceptions) {
            exception.write(context, buffer);
        }
        AttributeInfo.writeArray(context, buffer, attributes);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return NAME + " " + code.length;
    }

    void remapConstantPool(@NotNull IntUnaryOperator remap) {
        //
    }

    void floatToDouble() {
        //
    }

    void doubleToFloat() {
        //
    }
}
