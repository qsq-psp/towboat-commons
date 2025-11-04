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

@CodeHistory(date = "2025/9/21")
@ReferencePage(title = "JVMS12 The CONSTANT_NameAndType_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.6")
class NameAndTypeConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0xBF311DDBE2FD62BFL;

    @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
    private int nameIndex;

    @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
    private int typeIndex;

    NameAndTypeConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_NAMEANDTYPE;
    }

    protected int section() {
        return 6;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return null;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        nameIndex = in.readUnsignedShort();
        typeIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        nameIndex = 0xffff & buffer.getShort();
        typeIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeShort(nameIndex);
        out.writeShort(typeIndex);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putShort((short) nameIndex);
        buffer.putShort((short) typeIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " name-and-type #" + nameIndex + " " + context.constantPool.getUtf8(nameIndex)
                + " #" + typeIndex + " " + context.constantPool.getUtf8(typeIndex);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
        context.constantPool.getAndRetain(context, nameIndex);
        context.constantPool.getAndRetain(context, typeIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        nameIndex = remap.applyAsInt(nameIndex);
        typeIndex = remap.applyAsInt(typeIndex);
    }

    @Override
    public int hashCode() {
        return nameIndex * 0x1055f + typeIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NameAndTypeConstantInfo)) {
            return false;
        }
        final NameAndTypeConstantInfo that = (NameAndTypeConstantInfo) obj;
        return this.nameIndex == that.nameIndex && this.typeIndex == that.typeIndex;
    }
}
