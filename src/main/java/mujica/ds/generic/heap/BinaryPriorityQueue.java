package mujica.ds.generic.heap;

import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2024/4/27", project = "coo", name = "MinHeap")
@CodeHistory(date = "2025/5/27", project = "Ultramarine", name = "BinaryPriorityHeap")
@CodeHistory(date = "2025/6/9")
@ReferencePage(title = "二叉堆", href = "https://oi-wiki.org/ds/binary-heap/")
@Name(value = "二叉堆", language = "zh")
public class BinaryPriorityQueue<E> extends ListPriorityQueue<E> {

    public BinaryPriorityQueue(@Nullable Comparator<E> comparator, int initialCapacity) {
        super(comparator, initialCapacity);
    }

    public BinaryPriorityQueue(@Nullable Comparator<E> comparator) {
        super(comparator);
    }

    @NotNull
    @Override
    public AbstractPriorityQueue<E> duplicate() {
        final BinaryPriorityQueue<E> that = new BinaryPriorityQueue<>(comparator, this.list.size());
        that.list.addAll(this.list);
        return that;
    }

    @NotNull
    @Override
    public BinaryPriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final BinaryPriorityQueue<E> that = new BinaryPriorityQueue<>(comparator, this.list.size());
        this.list.map(operator, that.list);
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int size = list.size();
        for (int index = 1; index < size; index++) {
            int parent = (index - 1) >>> 1;
            if (comparator.compare(list.get(index), list.get(parent)) < 0) {
                consumer.accept(new InvariantException("node at " + index + " smaller than parent"));
            }
        }
    }

    @Override
    public long sumOfDepth() {
        return 0;
    }

    @Override
    public boolean offer(E element) {
        int index = list.size();
        while (index != 0) {
            int parent = (index - 1) >>> 1;
            if (comparator.compare(list.get(parent), element) <= 0) {
                break;
            }
            list.addOrSet(index, list.get(parent));
            index = parent;
        }
        list.addOrSet(index, element);
        modCount++;
        return true;
    }

    @Override
    void removeRoot() {
        final E value = list.removeLast(); // size changes here
        final int size = list.size();
        if (size == 0) {
            return;
        }
        int parent = 0;
        while (true) {
            int minIndex = (parent << 1) + 1;
            if (minIndex >= size) {
                break;
            }
            E minElement = list.get(minIndex);
            int alternateIndex = minIndex + 1;
            if (alternateIndex < size) {
                E alternateElement = list.get(alternateIndex);
                if (comparator.compare(alternateElement, minElement) <= 0) {
                    minIndex = alternateIndex;
                    minElement = alternateElement;
                }
            }
            if (comparator.compare(value, minElement) <= 0) {
                break;
            }
            list.set(parent, minElement);
            parent = minIndex;
        }
        list.set(parent, value);
        modCount++;
    }

    @Override
    void buildHeap() {
        final int size = list.size();
        for (int start = 1; start < size; start++) {
            int index = start;
            do {
                int parent = (index - 1) >>> 1;
                if (comparator.compare(list.get(parent), list.get(index)) <= 0) {
                    break;
                }
                list.set(index, list.set(parent, list.get(index))); // swap
                index = parent;
            } while (index != 0);
        }
    }
}
