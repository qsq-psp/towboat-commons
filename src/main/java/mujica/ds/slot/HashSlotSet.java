package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2025/6/7", name = "AbstractHashSet")
@CodeHistory(date = "2026/7/21")
public abstract class HashSlotSet<S, A> extends GapIteratorBasedSlotCollection<S, A> {

    private static final long serialVersionUID = 0xBA83386C30B6A735L;

    @NotNull
    final SlotArrayAllocator.HashSAA<S, A> hashFunction;

    int size;

    @Name(value = "modCount", language = "en")
    transient int version;

    protected HashSlotSet(@NotNull SlotArrayAllocator.HashSAA<S, A> hashFunction) {
        super();
        this.hashFunction = hashFunction;
    }

    @NotNull
    @Override
    public abstract SlotCollection<S, A> duplicateNew();

    @Override
    @NotNull
    public abstract SlotCollection<S, A> duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return hashFunction;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @NotNull
    @Override
    public abstract GapIterator<S> iterator(int startGapIndex);

    @NotNull
    @Override
    public String summaryToString() {
        return "size = " + size + ", version = " + version;
    }
}
