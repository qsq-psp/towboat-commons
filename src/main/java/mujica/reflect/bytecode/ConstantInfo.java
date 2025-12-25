package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2019", project = "bone", name = "JavaConstantPoolEntry")
@CodeHistory(date = "2025/9/13")
@ReferencePage(title = "JVMS12 The Constant Pool", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4")
abstract class ConstantInfo extends ClassFileNodeAdapter implements ClassFileNode.Independent {

    private static final long serialVersionUID = 0x80a41766d5ea67c0L;

    @DataType("u16")
    @ConstantType(zero = true)
    protected int newIndex;

    protected int referenceCount;

    @DataType("u8")
    protected abstract int tag();

    /**
     * @return section 4.4.x in JVMS
     */
    protected abstract int section();

    /**
     * @return (major << Short.SIZE) | minor;
     */
    protected abstract int sinceVersion();

    @Nullable
    protected abstract Class<?> loadedClass();

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex;
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
}
