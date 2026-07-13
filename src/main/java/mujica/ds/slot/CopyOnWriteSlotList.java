package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/12")
public class CopyOnWriteSlotList<S, A> extends SlotList<S, A> {

    @NotNull
    final SlotArrayAllocator<S, A> allocator;

    @NotNull
    A array;

    public CopyOnWriteSlotList(@NotNull SlotArrayAllocator<S, A> allocator) {
        super();
        this.allocator = allocator;
        this.array = allocator.newArray(0);
    }

    CopyOnWriteSlotList(@NotNull SlotArrayAllocator<S, A> allocator, @NotNull A array) {
        super();
        this.allocator = allocator;
        this.array = array;
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return allocator;
    }

    @NotNull
    @Override
    public CopyOnWriteSlotList<S, A> duplicate() {
        return new CopyOnWriteSlotList<>(allocator, array);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass
    }

    @Override
    public int size() {
        return allocator.length(array);
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        allocator.load(array, index, out);
    }
}
