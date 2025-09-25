package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/20.
 */
public class ConstantValueAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xB6343F1560711CF1L;

    int constantValueIndex; // CONSTANT_INTEGER, CONSTANT_FLOAT, CONSTANT_LONG, CONSTANT_DOUBLE, CONSTANT_STRING

    public static final String NAME = "ConstantValue";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        constantValueIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        constantValueIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(constantValueIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) constantValueIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return NAME + " " + constantValueToString(context);
    }

    @NotNull
    public String constantValueToString(@NotNull ClassFile context) {
        return context.constantPool.constantValueToString(context, constantValueIndex);
    }
}
