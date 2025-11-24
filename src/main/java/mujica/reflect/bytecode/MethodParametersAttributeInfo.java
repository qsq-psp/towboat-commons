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

@CodeHistory(date = "2025/9/20")
@ReferencePage(title = "JVMS12 The MethodParameters Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.24")
public class MethodParametersAttributeInfo extends AttributeInfo {

    static class MethodParameter extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        int nameIndex;

        int accessFlags;

        MethodParameter() {
            super();
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
        public String toString(@NotNull ClassFile context) {
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

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            nameIndex = remap.applyAsInt(nameIndex);
        }
    }

    private MethodParameter[] parameters;

    public MethodParametersAttributeInfo() {
        super();
    }

    public static final String NAME = "MethodParameters";

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
        out.writeShort(parameters.length);
        for (MethodParameter parameter : parameters) {
            parameter.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) parameters.length);
        for (MethodParameter parameter : parameters) {
            parameter.write(buffer);
        }
    }
}
