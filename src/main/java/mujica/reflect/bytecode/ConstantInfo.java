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

@CodeHistory(date = "2019", project = "bone", name = "JavaConstantPoolEntry")
@CodeHistory(date = "2025/9/13")
@ReferencePage(title = "JVMS12 The Constant Pool", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4")
class ConstantInfo implements ClassFileNode.Independent {

    private static final long serialVersionUID = 0x80A41766D5EA67C0L;

    @ConstantType(zero = true)
    protected int newIndex;

    protected int referenceCount;

    protected int tag() {
        return 0;
    }

    /**
     * @return section 4.4.x in JVMS
     */
    protected int section() {
        return 0;
    }

    /**
     * @return (major << Short.SIZE) | minor;
     */
    protected int sinceVersion() {
        return 0;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return null;
    }

    @Override
    public int groupCount() {
        return 0;
    }

    @NotNull
    @Override
    public Class<?> getGroup(int groupIndex) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int nodeCount(@NotNull Class<?> group) {
        return 0;
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        throw new BytecodeException();
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        throw new BytecodeException();
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        throw new BytecodeException();
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        throw new BytecodeException();
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " slot";
    }

    @NotNull
    protected String constantValueToString(@NotNull ClassFile context) {
        throw new BytecodeException();
    }

    public void retain(@NotNull ClassFile context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        // pass
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return ConstantInfo.class == obj.getClass();
    }

    @Override
    public String toString() {
        return "slot";
    }
}
