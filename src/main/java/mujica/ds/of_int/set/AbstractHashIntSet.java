package mujica.ds.of_int.set;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/6/26.
 */
@CodeHistory(date = "2025/6/26")
public abstract class AbstractHashIntSet extends AbstractIntSet {

    private static final long serialVersionUID = 0xbd78ffd9afc07400L;

    @NotNull
    final ResizePolicy policy;

    int size;

    transient int modCount;

    protected AbstractHashIntSet(@Nullable ResizePolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
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
