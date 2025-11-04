package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/10/7")
@ReferencePage(title = "JVMS12 The EnclosingMethod Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.7")
class EnclosingMethodAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x72EA6F64A3D9179DL;

    @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
    int classIndex;

    /**
     * If the current class is not immediately enclosed by a method or constructor, then the value of the method_index
     * item must be zero.
     */
    @ConstantType(tags = ConstantPool.CONSTANT_NAMEANDTYPE, zero = true)
    int methodIndex;

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.HIGH;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    public static final String NAME = "EnclosingMethod";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 4;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        classIndex = in.readUnsignedShort();
        methodIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        classIndex = 0xffff & buffer.getShort();
        methodIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(classIndex);
        out.writeShort(methodIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        super.write(context, buffer);
        buffer.putShort((short) classIndex);
        buffer.putShort((short) methodIndex);
    }
}
