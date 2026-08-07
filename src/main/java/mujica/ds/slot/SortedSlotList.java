package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/8/6")
public class SortedSlotList<S, A> extends VersionSlotList<S, A> {

    @NotNull
    final SlotArrayComparator.PolicySAC<S, A> comparator;

    @NotNull
    A array;

    int size;

    public SortedSlotList(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator) {
        super();
        this.comparator = comparator;
        this.array = comparator.newArray(comparator.getCapacityPolicy().initialCapacity());
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new SortedSlotList<>(comparator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        return null;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (size < 2) {
            return;
        }
        S previousSlot = comparator.newSlot();
        S currentSlot = comparator.newSlot();
        try {
            comparator.load(array, 0, previousSlot);
            for (int index = 1; index < size; index++) {
                comparator.load(array, index, currentSlot);
                if (comparator.compareSlot(previousSlot, currentSlot) > 0) {
                    consumer.accept(new RuntimeException("not sorted between " + (index - 1) + " and " + index));
                }
                S tempSlot = previousSlot;
                previousSlot = currentSlot;
                currentSlot = tempSlot;
            }
        } finally {
            comparator.releaseSlot(previousSlot);
            comparator.releaseSlot(currentSlot);
        }
    }

    @NotNull
    @Override
    protected SlotArrayAllocator<S, A> getAllocator() {
        return comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        comparator.load(array, index, out);
    }
}
