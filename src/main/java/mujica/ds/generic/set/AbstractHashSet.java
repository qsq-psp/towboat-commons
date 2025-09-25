package mujica.ds.generic.set;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/7")
public abstract class AbstractHashSet<E> extends AbstractAxiomSet<E> {

    private static final long serialVersionUID = 0x42e7f0ff6fed0940L;

    @NotNull
    final ResizePolicy policy;

    int size;

    transient int modCount;

    protected AbstractHashSet(@Nullable ResizePolicy policy) {
        super();
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
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
