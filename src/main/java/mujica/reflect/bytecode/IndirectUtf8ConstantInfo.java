package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/10/18")
abstract class IndirectUtf8ConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0x08f978bd495e0f62L;

    @DataType("u16-{0}")
    @ConstantType(tags = Utf8ConstantInfo.TAG)
    protected int utf8Index;

    IndirectUtf8ConstantInfo() {
        super();
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        utf8Index = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        utf8Index = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeShort(utf8Index);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putShort((short) utf8Index);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
        context.constantPool.getAndRetain(context, utf8Index);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        utf8Index = remap.applyAsInt(utf8Index);
    }

    @Override
    public int hashCode() {
        return utf8Index;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IndirectUtf8ConstantInfo)) {
            return false;
        }
        final IndirectUtf8ConstantInfo that = (IndirectUtf8ConstantInfo) obj;
        return this.tag() == that.tag() && this.utf8Index == that.utf8Index;
    }
}
