package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/10/17")
@ReferencePage(title = "JVMS12 The CONSTANT_Dynamic_info and CONSTANT_InvokeDynamic_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.10")
class DynamicConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0x89D0C75C6F9C9EB6L;

    protected int bootstrapMethodAttributeIndex;

    @ConstantType(tags = ConstantPool.CONSTANT_NAMEANDTYPE)
    protected int nameAndTypeIndex;

    DynamicConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_DYNAMIC;
    }

    @Override
    protected int section() {
        return 10;
    }

    @Override
    protected int sinceVersion() {
        return 55 << Short.SIZE;
    }

    @Nullable
    @Override
    protected Class<?> loadedClass() {
        return null;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        bootstrapMethodAttributeIndex = in.readUnsignedShort();
        nameAndTypeIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        bootstrapMethodAttributeIndex = 0xffff & buffer.getShort();
        nameAndTypeIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeShort(bootstrapMethodAttributeIndex);
        out.writeShort(nameAndTypeIndex);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putShort((short) bootstrapMethodAttributeIndex);
        buffer.putShort((short) nameAndTypeIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " dynamic #" + bootstrapMethodAttributeIndex + " " + context.constantPool.getToString(context, nameAndTypeIndex);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
        context.constantPool.getAndRetain(context, nameAndTypeIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        nameAndTypeIndex = remap.applyAsInt(nameAndTypeIndex);
    }

    @Override
    public int hashCode() {
        return bootstrapMethodAttributeIndex * 0x1aa5f + nameAndTypeIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DynamicConstantInfo)) {
            return false;
        }
        final DynamicConstantInfo that = (DynamicConstantInfo) obj;
        return this.tag() == that.tag()
                && this.bootstrapMethodAttributeIndex == that.bootstrapMethodAttributeIndex
                && this.nameAndTypeIndex == that.nameAndTypeIndex;
    }
}
