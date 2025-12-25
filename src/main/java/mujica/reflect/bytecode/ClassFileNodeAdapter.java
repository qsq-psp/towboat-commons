package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/11/9")
class ClassFileNodeAdapter implements ClassFileNode {

    private static final long serialVersionUID = 0xfde60b3660e3fa1bL;

    ClassFileNodeAdapter() {
        super();
    }

    @Override
    public int groupCount() {
        return 0;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        return 0;
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "";
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        // pass
    }
}
