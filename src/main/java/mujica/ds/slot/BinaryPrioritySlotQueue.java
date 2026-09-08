package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@CodeHistory(date = "2024/4/27", project = "coo", name = "MinHeap")
@CodeHistory(date = "2025/5/27", project = "Ultramarine", name = "BinaryPriorityHeap")
@CodeHistory(date = "2025/6/9", name = "BinaryPriorityQueue")
@CodeHistory(date = "2026/8/6")
@ReferencePage(title = "二叉堆", href = "https://oi-wiki.org/ds/binary-heap/")
@Name(value = "二叉堆", language = "zh")
public class BinaryPrioritySlotQueue<S, A> extends SortedSlotList<S, A> {

    public BinaryPrioritySlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator) {
        super(comparator);
    }

    BinaryPrioritySlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator, @NotNull A array, int size) {
        super(comparator, array, size);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new BinaryPrioritySlotQueue<>(comparator);
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicate() {
        final int newCapacity = Math.max(comparator.getCapacityPolicy().min(), size);
        final A newArray = comparator.newArray(newCapacity);
        comparator.copy(array, 0, newArray, 0, size);
        return new BinaryPrioritySlotQueue<>(comparator, newArray, size);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final S currentSlot = comparator.newSlot();
        final S parentSlot = comparator.newSlot();
        try {
            for (int currentIndex = 1; currentIndex < size; currentIndex++) {
                int parentIndex = (currentIndex - 1) >>> 1;
                comparator.load(array, currentIndex, currentSlot);
                comparator.load(array, parentIndex, parentSlot);
                if (comparator.compareSlot(currentSlot, parentSlot) < 0) {
                    consumer.accept(new RuntimeException("node at " + currentIndex + " smaller than parent at " + parentIndex));
                }
            }
        } finally {
            comparator.releaseSlot(currentSlot);
            comparator.releaseSlot(parentSlot);
        }
    }

    @Override
    public boolean contains(@NotNull S in) {
        // super super
        return firstIndexOf(in) != -1;
    }

    @Override
    public int firstIndexOf(@NotNull S in) {
        final S scanSlot = getAllocator().newSlot();
        try {
            for (int index = 0; index < size; index++) {
                getItemAt(index, scanSlot);
                if (comparator.compareSlot(in, scanSlot) == 0) {
                    return index;
                }
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public int lastIndexOf(@NotNull S in) {
        final S scanSlot = getAllocator().newSlot();
        try {
            for (int index = size - 1; index >= 0; index--) {
                getItemAt(index, scanSlot);
                if (comparator.compareSlot(in, scanSlot) == 0) {
                    return index;
                }
            }
            return -1;
        } finally {
            getAllocator().releaseSlot(scanSlot);
        }
    }

    @Override
    public void setItemAt(int currentIndex, @NotNull S in) throws IndexOutOfBoundsException {
        if (currentIndex < 0 || currentIndex >= size) {
            throw new IndexOutOfBoundsException();
        }
        S neighborSlot = null;
        try {
            if (currentIndex > 0) {
                neighborSlot = comparator.newSlot();
                comparator.load(array, (currentIndex - 1) >>> 1, neighborSlot); // parent
                if (comparator.compareSlot(in, neighborSlot) < 0) {
                    throw new IllegalArgumentException();
                }
            }
            int childIndex = (currentIndex << 1) + 1;
            if (childIndex < size) {
                if (neighborSlot == null) {
                    neighborSlot = comparator.newSlot();
                }
                comparator.load(array, childIndex, neighborSlot);
                if (comparator.compareSlot(in, neighborSlot) > 0) {
                    throw new IllegalArgumentException();
                }
                childIndex++;
                if (childIndex < size) {
                    comparator.load(array, childIndex, neighborSlot);
                    if (comparator.compareSlot(in, neighborSlot) > 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            comparator.store(array, currentIndex, in);
        } finally {
            if (neighborSlot != null) {
                comparator.releaseSlot(neighborSlot);
            }
        }
    }

    @Override
    public void exchangeItemAt(int currentIndex, @NotNull S inOut) throws IndexOutOfBoundsException {
        if (currentIndex < 0 || currentIndex >= size) {
            throw new IndexOutOfBoundsException();
        }
        final S neighborSlot = comparator.newSlot();
        try {
            if (currentIndex > 0) {
                comparator.load(array, (currentIndex - 1) >>> 1, neighborSlot); // parent
                if (comparator.compareSlot(inOut, neighborSlot) < 0) {
                    throw new IllegalArgumentException();
                }
            }
            int childIndex = (currentIndex << 1) + 1;
            if (childIndex < size) {
                comparator.load(array, childIndex, neighborSlot);
                if (comparator.compareSlot(inOut, neighborSlot) > 0) {
                    throw new IllegalArgumentException();
                }
                childIndex++;
                if (childIndex < size) {
                    comparator.load(array, childIndex, neighborSlot);
                    if (comparator.compareSlot(inOut, neighborSlot) > 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            comparator.load(array, currentIndex, neighborSlot);
            comparator.store(array, currentIndex, inOut);
            comparator.move(neighborSlot, inOut);
        } finally {
            comparator.releaseSlot(neighborSlot);
        }
    }

    @Override
    public void removeItemAt(int currentIndex, @Nullable S out) throws IndexOutOfBoundsException {
        if (currentIndex < 0 || currentIndex >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (out != null) {
            comparator.load(array, currentIndex, out);
        }
        final S currentSlot = comparator.newSlot();
        final S nextSlot = comparator.newSlot();
        final S alternateSlot = comparator.newSlot();
        try {
            comparator.load(array, --size, currentSlot);
            version++;
            comparator.releaseReference(array, size);
            while (currentIndex > 0) {
                int parentIndex = (currentIndex - 1) >>> 1;
                comparator.load(array, parentIndex, nextSlot);
                comparator.store(array, currentIndex, nextSlot);
                currentIndex = parentIndex;
            }
            while (true) {
                int childIndex = (currentIndex << 1) + 1;
                if (childIndex < size) {
                    comparator.load(array, childIndex, nextSlot);
                    int alternateIndex = childIndex + 1;
                    if (alternateIndex < size) {
                        comparator.load(array, alternateIndex, alternateSlot);
                        if (comparator.compareSlot(alternateSlot, nextSlot) <= 0) {
                            childIndex = alternateIndex;
                            comparator.move(alternateSlot, nextSlot);
                        }
                    }
                    if (comparator.compareSlot(currentSlot, nextSlot) <= 0) {
                        break;
                    }
                    comparator.store(array, currentIndex, nextSlot);
                    currentIndex = childIndex;
                } else {
                    break;
                }
            }
            comparator.store(array, currentIndex, currentSlot);
        } finally {
            comparator.releaseSlot(currentSlot);
            comparator.releaseSlot(nextSlot);
            comparator.releaseSlot(alternateSlot);
        }
    }

    @Override
    public void insertItemBefore(int gapIndex, @NotNull S in) throws IndexOutOfBoundsException {
        insertLastItem(in);
    }

    @Override
    public void insertFirstItem(@NotNull S in) {
        insertLastItem(in);
    }

    @Override
    public void insertLastItem(@NotNull S in) {
        enlarge();
        int insertIndex = size;
        if (insertIndex > 0) {
            S parentSlot = comparator.newSlot();
            try {
                do {
                    int parentIndex = (insertIndex - 1) >>> 1;
                    comparator.load(array, parentIndex, parentSlot);
                    if (comparator.compareSlot(in, parentSlot) >= 0) {
                        break;
                    }
                    comparator.store(array, insertIndex, parentSlot);
                    insertIndex = parentIndex;
                } while (insertIndex > 0);
            } finally {
                comparator.releaseSlot(parentSlot);
            }
        }
        comparator.store(array, insertIndex, in);
        size++;
        version++;
    }
}
