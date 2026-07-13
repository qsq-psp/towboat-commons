package mujica.ds.i32.set;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/26")
public abstract class AbstractHashIntSet extends IntSet {

    private static final long serialVersionUID = 0xbd78ffd9afc07400L;

    @NotNull
    final CapacityPolicy policy;

    int size;

    transient int modCount;

    protected AbstractHashIntSet(@Nullable CapacityPolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceCapacityPolicy.INSTANCE;
        }
        this.policy = policy;
    }

    @Override
    public int intLength() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
