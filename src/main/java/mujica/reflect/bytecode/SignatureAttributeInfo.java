package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/16.
 */
public class SignatureAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x30E93CFD17675433L;

    int signatureIndex; // CONSTANT_UTF8

    public static final String NAME = "Signature";

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
        signatureIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        signatureIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(signatureIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) signatureIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return NAME + ' ' + context.constantPool.getUtf8(signatureIndex);
    }
}
