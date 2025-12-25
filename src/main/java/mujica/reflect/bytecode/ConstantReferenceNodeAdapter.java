package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/11/11")
class ConstantReferenceNodeAdapter extends ClassFileNodeAdapter {

    @DataType("u16")
    @ConstantType(zero = true)
    protected int referenceIndex;

    public ConstantReferenceNodeAdapter(int referenceIndex) {
        super();
        this.referenceIndex = referenceIndex;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return context.constantPool.getToString(context, referenceIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        referenceIndex = remap.applyAsInt(referenceIndex);
    }
}
