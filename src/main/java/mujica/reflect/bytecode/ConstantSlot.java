package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/11/15")
class ConstantSlot extends ConstantInfo {

    private static final long serialVersionUID = 0x7783b830a7701947L;

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

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return ConstantSlot.class == obj.getClass();
    }

    @Override
    public String toString() {
        return "slot";
    }
}
