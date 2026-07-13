package mujica.ds.any.set;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/7")
public abstract class AbstractHashSet<E> extends AbstractAxiomSet<E> {

    private static final long serialVersionUID = 0x42e7f0ff6fed0940L;

    @NotNull
    final CapacityPolicy policy;

    int size;

    transient int modCount;

    protected AbstractHashSet(@Nullable CapacityPolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceCapacityPolicy.INSTANCE;
        }
        this.policy = policy;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size != 0;
    }

    @Override
    public abstract boolean contains(Object element);

    @Override
    public abstract boolean add(E element);

    @Override
    public abstract boolean remove(Object element);

    @Override
    public abstract void clear();

    @NotNull
    @Override
    public String summaryToString() {
        return "<size = " + size + ", modification = " + modCount + ">";
    }
}
