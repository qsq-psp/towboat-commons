package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaConstantMethodHandle")
@CodeHistory(date = "2025/10/15")
@ReferencePage(title = "JVMS12 The CONSTANT_MethodHandle_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.8")
class MethodHandleConstantInfo extends ConstantInfo {

    static final String[] KIND_NAMES = {
            "get-field",
            "get-static",
            "put-field",
            "put-static",
            "invoke-virtual",
            "invoke-static",
            "invoke-special",
            "new-invoke-special",
            "invoke-interface"
    };

    int kind; // from 1 to 9

    @NotNull
    String kindToString() {
        if (1 <= kind && kind <= KIND_NAMES.length) {
            return KIND_NAMES[kind - 1];
        } else {
            return "unknown(" + kind + ")";
        }
    }

    @ConstantType(tags = {
            ConstantPool.CONSTANT_FIELDREF,
            ConstantPool.CONSTANT_METHODREF,
            ConstantPool.CONSTANT_INTERFACEMETHODREF
    })
    int referenceIndex;

    MethodHandleConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_METHODHANDLE;
    }

    protected int section() {
        return 8;
    }

    protected int sinceVersion() {
        return 51 << Short.SIZE;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return MethodHandle.class;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        kind = in.readUnsignedByte();
        referenceIndex = in.readUnsignedShort();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        kind = 0xff & buffer.get();
        referenceIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeByte(kind);
        out.writeShort(referenceIndex);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.put((byte) kind);
        buffer.putShort((short) referenceIndex);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " method-handle " + kindToString() + " " + context.constantPool.getToString(context, referenceIndex);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
        context.constantPool.getAndRetain(context, referenceIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        referenceIndex = remap.applyAsInt(referenceIndex);
    }

    @Override
    public int hashCode() {
        return kind * 0x157c7 + referenceIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MethodHandleConstantInfo)) {
            return false;
        }
        final MethodHandleConstantInfo that = (MethodHandleConstantInfo) obj;
        return this.kind == that.kind && this.referenceIndex == that.referenceIndex;
    }
}
