package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/6")
@ReferencePage(title = "JVMS12 The CONSTANT_Integer_info and CONSTANT_Float_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.4")
class IntegerConstantInfo extends ConstantInfo {

    private static final long serialVersionUID = 0x68f480567150ba16L;

    int value;

    IntegerConstantInfo() {
        super();
    }

    public static final int TAG = 3;

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
        return int.class;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        value = in.readInt();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        value = buffer.getInt();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeInt(value);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putInt(value);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " int " + value;
    }

    @NotNull
    @Override
    public String constantValueToString(@NotNull ClassFile context) {
        return Integer.toString(value);
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntegerConstantInfo && value == ((IntegerConstantInfo) obj).value;
    }
}
