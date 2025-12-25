package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaConstantFloat")
@CodeHistory(date = "2025/9/6")
@ReferencePage(title = "JVMS12 The CONSTANT_Integer_info and CONSTANT_Float_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.4")
class FloatConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0xf8c5bd1075c263d0L;

    float value;

    FloatConstantInfo() {
        super();
    }

    public static final int TAG = 4;

    @Override
    protected int tag() {
        return TAG;
    }

    protected int section() {
        return 4;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return float.class;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        value = in.readFloat();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        value = buffer.getFloat();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeFloat(value);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putFloat(value);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " float " + value;
    }

    @NotNull
    @Override
    public String constantValueToString(@NotNull ClassFile context) {
        if (Float.isNaN(value)) {
            return "Float.NaN";
        } else if (value == Float.POSITIVE_INFINITY) {
            return "Float.POSITIVE_INFINITY";
        } else if (value == Float.NEGATIVE_INFINITY) {
            return "Float.NEGATIVE_INFINITY";
        } else {
            return value + "F";
        }
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FloatConstantInfo && Float.floatToRawIntBits(value) == Float.floatToRawIntBits(((FloatConstantInfo) obj).value);
    }
}
