package mujica.ds.slot;

import mujica.ds.sort.Sort;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2025/6/14", name = "ArrayPriorityQueue")
@CodeHistory(date = "2026/8/6")
@DirectSubclass({BinaryPrioritySlotQueue.class, NAryPrioritySlotQueue.class})
public class SortedSlotList<S, A> extends VersionSlotList<S, A> {

    private static final long serialVersionUID = 0x18b9f59103109b81L;

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

    SortedSlotList(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator, @NotNull A array, int size) {
        super();
        this.comparator = comparator;
        this.array = array;
        this.size = size;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new SortedSlotList<>(comparator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        final int newCapacity = Math.max(comparator.getCapacityPolicy().min(), size);
        final A newArray = comparator.newArray(newCapacity);
        comparator.copy(array, 0, newArray, 0, size);
        return new SortedSlotList<>(comparator, newArray, size);
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
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean isFull() {
        final int capacity = comparator.length(array);
        return size == capacity && comparator.getCapacityPolicy().tryLargerCapacity(capacity) <= capacity;
    }

    @Override
    public boolean contains(@NotNull S in) {
        int lowIndex = 0;
        int highIndex = size - 1;
        if (lowIndex <= highIndex) {
            S midSlot = comparator.newSlot();
            try {
                do {
                    int midIndex = (lowIndex + highIndex) >>> 1;
                    comparator.load(array, midIndex, midSlot);
                    int compareResult = comparator.compareSlot(in, midSlot);
                    if (compareResult < 0) {
                        highIndex = midIndex - 1;
                    } else if (compareResult > 0) {
                        lowIndex = midIndex + 1;
                    } else {
                        return true;
                    }
                } while (lowIndex <= highIndex);
            } finally {
                comparator.releaseSlot(midSlot);
            }
        }
        return false;
    }

    @Override
    public int countItem(@NotNull S in) {
        // todo
        return super.countItem(in);
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        int lowIndex = 0;
        int highIndex = size - 1;
        boolean highEqual = false;
        if (lowIndex <= highIndex) {
            S midSlot = comparator.newSlot();
            try {
                do {
                    int midIndex = (lowIndex + highIndex) >>> 1;
                    comparator.load(array, midIndex, midSlot);
                    int compareResult = comparator.compareSlot(in, midSlot);
                    if (compareResult < 0) {
                        highIndex = midIndex - 1;
                        highEqual = false;
                    } else if (compareResult > 0) {
                        lowIndex = midIndex + 1;
                    } else {
                        highIndex = midIndex;
                        highEqual = true;
                    }
                } while (lowIndex <= highIndex);
                if (highEqual) {
                    return highIndex;
                }
            } finally {
                comparator.releaseSlot(midSlot);
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        int lowIndex = 0;
        int highIndex = size - 1;
        boolean lowEqual = false;
        if (lowIndex <= highIndex) {
            S midSlot = comparator.newSlot();
            try {
                do {
                    int midIndex = (lowIndex + highIndex) >>> 1;
                    comparator.load(array, midIndex, midSlot);
                    int compareResult = comparator.compareSlot(in, midSlot);
                    if (compareResult < 0) {
                        highIndex = midIndex - 1;
                        lowEqual = false;
                    } else if (compareResult > 0) {
                        lowIndex = midIndex + 1;
                    } else {
                        lowIndex = midIndex;
                        lowEqual = true;
                    }
                } while (lowIndex <= highIndex);
                if (lowEqual) {
                    return lowIndex;
                }
            } finally {
                comparator.releaseSlot(midSlot);
            }
        }
        return -1;
    }

    @Override
    public void getItemAt(int index, @NotNull S out) throws IndexOutOfBoundsException {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        comparator.load(array, index, out);
    }

    @Override
    public void setItemAt(int index, @NotNull S in) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        S neighborSlot = null;
        try {
            if (index > 0) {
                neighborSlot = comparator.newSlot();
                comparator.load(array, index - 1, neighborSlot);
                if (comparator.compareSlot(in, neighborSlot) < 0) {
                    throw new IllegalArgumentException();
                }
            }
            if (index + 1 < size) {
                if (neighborSlot == null) {
                    neighborSlot = comparator.newSlot();
                }
                comparator.load(array, index + 1, neighborSlot);
                if (comparator.compareSlot(in, neighborSlot) > 0) {
                    throw new IllegalArgumentException();
                }
            }
            comparator.store(array, index, in);
        } finally {
            if (neighborSlot != null) {
                comparator.releaseSlot(neighborSlot);
            }
        }
    }

    @Override
    public void exchangeItemAt(int index, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        final S neighborSlot = comparator.newSlot();
        try {
            if (index > 0) {
                comparator.load(array, index - 1, neighborSlot);
                if (comparator.compareSlot(inOut, neighborSlot) < 0) {
                    throw new IllegalArgumentException();
                }
            }
            if (index + 1 < size) {
                comparator.load(array, index + 1, neighborSlot);
                if (comparator.compareSlot(inOut, neighborSlot) > 0) {
                    throw new IllegalArgumentException();
                }
            }
            comparator.load(array, index, neighborSlot);
            comparator.store(array, index, inOut);
            comparator.move(neighborSlot, inOut);
        } finally {
            comparator.releaseSlot(neighborSlot);
        }
    }

    @Override
    public void removeItemAt(int index, @Nullable S out) throws IndexOutOfBoundsException {
        final int tailLength = size - index - 1;
        if (tailLength < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (out != null) {
            comparator.load(array, index, out);
        }
        if (tailLength > 0) {
            comparator.copyWithin(array, index + 1, index, tailLength);
        }
        comparator.releaseReference(array, --size);
        version++;
    }

    protected void enlarge() {
        final int oldCapacity = comparator.length(array);
        if (size == oldCapacity) {
            A newArray = comparator.newArray(comparator.getCapacityPolicy().largerCapacity(oldCapacity));
            comparator.copy(array, 0, newArray, 0, size);
            comparator.releaseArray(array);
            array = newArray;
        }
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        enlarge();
        int lowIndex = 0;
        int highIndex = size;
        if (lowIndex < highIndex) {
            S midSlot = comparator.newSlot();
            if (gapIndex < 0) {
                try {
                    do {
                        int midIndex = (lowIndex + highIndex) >>> 1;
                        comparator.load(array, midIndex, midSlot);
                        if (comparator.compareSlot(in, midSlot) < 0) {
                            highIndex = midIndex;
                        } else {
                            lowIndex = midIndex + 1;
                        }
                    } while (lowIndex < highIndex);
                } finally {
                    comparator.releaseSlot(midSlot);
                }
            } else {
                try {
                    do {
                        int midIndex = (lowIndex + highIndex) >>> 1;
                        comparator.load(array, midIndex, midSlot);
                        if (comparator.compareSlot(in, midSlot) <= 0) {
                            highIndex = midIndex;
                        } else {
                            lowIndex = midIndex + 1;
                        }
                    } while (lowIndex < highIndex);
                } finally {
                    comparator.releaseSlot(midSlot);
                }
            }
            if (lowIndex < size) {
                comparator.copyWithin(array, lowIndex, lowIndex + 1, size - lowIndex);
            }
        }
        comparator.store(array, lowIndex, in);
        size++;
        version++;
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        enlarge();
        int lowIndex = 0;
        int highIndex = size;
        if (lowIndex < highIndex) {
            S midSlot = comparator.newSlot();
            try {
                do {
                    int midIndex = (lowIndex + highIndex) >>> 1;
                    comparator.load(array, midIndex, midSlot);
                    if (comparator.compareSlot(in, midSlot) <= 0) {
                        highIndex = midIndex;
                    } else {
                        lowIndex = midIndex + 1;
                    }
                } while (lowIndex < highIndex);
            } finally {
                comparator.releaseSlot(midSlot);
            }
            if (lowIndex < size) {
                comparator.copyWithin(array, lowIndex, lowIndex + 1, size - lowIndex);
            }
        }
        comparator.store(array, lowIndex, in);
        size++;
        version++;
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        enlarge();
        int lowIndex = 0;
        int highIndex = size;
        if (lowIndex < highIndex) {
            S midSlot = comparator.newSlot();
            try {
                do {
                    int midIndex = (lowIndex + highIndex) >>> 1;
                    comparator.load(array, midIndex, midSlot);
                    if (comparator.compareSlot(in, midSlot) < 0) {
                        highIndex = midIndex;
                    } else {
                        lowIndex = midIndex + 1;
                    }
                } while (lowIndex < highIndex);
            } finally {
                comparator.releaseSlot(midSlot);
            }
            if (lowIndex < size) {
                comparator.copyWithin(array, lowIndex, lowIndex + 1, size - lowIndex);
            }
        }
        comparator.store(array, lowIndex, in);
        size++;
        version++;
    }

    @Override
    public void clear() {
        comparator.releaseReference(array, 0, size);
        size = 0;
        version++;
    }

    @Override
    public void reverse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sort(@NotNull Sort<A> sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sortPart(@NotNull Sort<A> sort, int newEndIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long sortUnique(@NotNull Sort<A> sort) {
        throw new UnsupportedOperationException();
    }
}
