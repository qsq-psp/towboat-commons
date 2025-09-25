package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created on 2025/9/16.
 */
public class SourceFileAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x8326D0B801E6B67BL;

    int sourceFileIndex; // CONSTANT_UTF8

    public static final String NAME = "SourceFile";

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
        sourceFileIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        sourceFileIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        super.write(context, out);
        out.writeShort(sourceFileIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) sourceFileIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context, int position) {
        return NAME + ' ' + context.constantPool.getUtf8(sourceFileIndex);
    }
}
