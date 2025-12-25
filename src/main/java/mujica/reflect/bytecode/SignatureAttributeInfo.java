package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/16")
@ReferencePage(title = "JVMS12 The Signature Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.9")
class SignatureAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x30e93cfd17675433L;

    @DataType("u16-{0}")
    @ConstantType(tags = Utf8ConstantInfo.TAG)
    int signatureIndex;

    SignatureAttributeInfo() {
        super();
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        return ImportanceLevel.HIGH;
    }

    @Override
    public boolean isNecessary() {
        return true; // needed in generic reflection
    }

    public static final String NAME = "Signature";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 2; // fixed
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
        out.writeShort(signatureIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) signatureIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return NAME + " " + context.constantPool.getUtf8(signatureIndex);
    }
}
