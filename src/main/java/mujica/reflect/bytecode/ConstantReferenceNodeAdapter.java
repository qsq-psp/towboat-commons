package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

/**
 * Created on 2025/11/11.
 */
@CodeHistory(date = "2025/11/11")
public class ConstantReferenceNodeAdapter extends ClassFileNodeAdapter {

    @ConstantType(zero = true)
    protected int referenceIndex;

    public ConstantReferenceNodeAdapter() {
        super();
    }

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
