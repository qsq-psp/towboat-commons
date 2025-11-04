package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2019/8/15", project = "bone", name = "JavaConstantLong")
@CodeHistory(date = "2025/9/21")
@ReferencePage(title = "JVMS12 The CONSTANT_Long_info and CONSTANT_Double_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.5")
class LongConstantInfo extends ConstantInfo implements ConstantPool.Large {

    long value;

    LongConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_LONG;
    }

    protected int section() {
        return 5;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return long.class;
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        value = in.readLong();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        value = buffer.getLong();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeLong(value);
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putLong(value);
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " long " + value;
    }

    @NotNull
    @Override
    public String constantValueToString(@NotNull ClassFile context) {
        return value + "L";
    }

    @Override
    public void retain(@NotNull ClassFile context) {
        referenceCount++;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LongConstantInfo && value == ((LongConstantInfo) obj).value;
    }
}
