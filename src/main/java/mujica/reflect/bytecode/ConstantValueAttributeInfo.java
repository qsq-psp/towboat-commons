package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/20")
@ReferencePage(title = "JVMS12 The ConstantValue Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.2")
class ConstantValueAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0xb6343f1560711cf1L;

    @DataType("u16-{0}")
    @ConstantType(tags = {
            IntegerConstantInfo.TAG,
            FloatConstantInfo.TAG,
            LongConstantInfo.TAG,
            DoubleConstantInfo.TAG,
            StringConstantInfo.TAG
    }) // constant value
    private int constantValueIndex;

    ConstantValueAttributeInfo() {
        super();
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.CRITICAL;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

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
        out.writeShort(constantValueIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) constantValueIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return NAME + " " + constantValueToString(context);
    }

    @NotNull
    public String constantValueToString(@NotNull ClassFile context) {
        return context.constantPool.constantValueToString(context, constantValueIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        constantValueIndex = remap.applyAsInt(constantValueIndex);
    }
}
