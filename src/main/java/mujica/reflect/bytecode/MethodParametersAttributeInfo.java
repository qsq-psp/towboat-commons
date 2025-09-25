package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/20.
 */
public class MethodParametersAttributeInfo extends AttributeInfo {

    public static final String NAME = "MethodParameters";

    static class MethodParameter implements Independent {

        int nameIndex;

        int accessFlags;

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
            nameIndex = in.readUnsignedShort();
            accessFlags = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            nameIndex = 0xffff & buffer.getShort();
            accessFlags = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(nameIndex);
            out.writeShort(accessFlags);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) nameIndex);
            buffer.putShort((short) accessFlags);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context, int position) {
            final StringBuilder sb = new StringBuilder();
            if ((accessFlags & Modifier.FINAL) != 0) {
                sb.append("final ");
            }
            if ((accessFlags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            sb.append(context.constantPool.getUtf8(nameIndex));
            return sb.toString();
        }
    }

    MethodParameter[] parameters;

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2 + 4 * parameters.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        final int tableLength = in.readUnsignedShort();
        parameters = new MethodParameter[tableLength];
        for (int index = 0; index < tableLength; index++) {
            MethodParameter parameter = new MethodParameter();
            parameter.read(in);
            parameters[index] = parameter;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        final int tableLength = 0xffff & buffer.getShort();
        parameters = new MethodParameter[tableLength];
        for (int index = 0; index < tableLength; index++) {
            MethodParameter parameter = new MethodParameter();
            parameter.read(buffer);
            parameters[index] = parameter;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(parameters.length);
        for (MethodParameter parameter : parameters) {
            parameter.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) parameters.length);
        for (MethodParameter parameter : parameters) {
            parameter.write(buffer);
        }
    }
}
