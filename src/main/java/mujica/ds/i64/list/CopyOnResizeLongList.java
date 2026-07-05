package mujica.ds.i64.list;

import mujica.ds.i32.list.ResizePolicy;
import mujica.ds.i32.list.TwiceResizePolicy;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/5/12.
 */
public class CopyOnResizeLongList extends AbstractLongList {

    @NotNull
    final ResizePolicy policy;

    @NotNull
    long[] array;

    @Index(of = "array", inclusive = false)
    int endIndex;

    int modCount;

    public CopyOnResizeLongList(@Nullable ResizePolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
        }
        this.policy = policy;
        this.array = new long[policy.initialCapacity()];
    }

    @Override
    public int longLength() {
        return endIndex;
    }

    @Override
    public long getLong(int index) {
        return array[index];
    }
}
