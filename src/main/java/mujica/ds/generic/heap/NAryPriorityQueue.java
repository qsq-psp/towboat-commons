package mujica.ds.generic.heap;

import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/5/29", project = "Ultramarine", name = "BinaryPriorityHeap")
@CodeHistory(date = "2025/6/9")
@Name(value = "多叉堆", language = "zh")
public class NAryPriorityQueue<E> extends ListPriorityQueue<E> {

    private static final long serialVersionUID = 0x1a5144a4bd5ef3feL;

    final int nAry;

    public NAryPriorityQueue(int nAry, @Nullable Comparator<E> comparator, int initialCapacity) {
        super(comparator, initialCapacity);
        if (nAry <= 0) {
            throw new IllegalArgumentException();
        }
        this.nAry = nAry;
    }

    public NAryPriorityQueue(int nAry, @Nullable Comparator<E> comparator) {
        super(comparator);
        if (nAry <= 0) {
            throw new IllegalArgumentException();
        }
        this.nAry = nAry;
    }

    @NotNull
    @Override
    public NAryPriorityQueue<E> duplicate() {
        final NAryPriorityQueue<E> that = new NAryPriorityQueue<>(nAry, comparator, this.list.size());
        that.list.addAll(this.list);
        return that;
    }

    @NotNull
    @Override
    public NAryPriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final NAryPriorityQueue<E> that = new NAryPriorityQueue<>(nAry, comparator, this.list.size());
        this.list.map(operator, that.list);
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int size = list.size();
        for (int index = 1; index < size; index++) {
            int parent = (index - 1) / nAry;
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
            int parent = (index - 1) / nAry;
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
            int child = nAry * parent + 1;
            if (child >= size) {
                break;
            }
            int limit = Math.min(child + nAry, size);
            int minIndex = child;
            E minElement = list.get(child);
            for (child++; child < limit; child++) {
                E element = list.get(child);
                if (comparator.compare(element, minElement) <= 0) {
                    minIndex = child;
                    minElement = element;
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
                int parent = (index - 1) / nAry;
                if (comparator.compare(list.get(parent), list.get(index)) <= 0) {
                    break;
                }
                list.set(index, list.set(parent, list.get(index))); // swap
                index = parent;
            } while (index != 0);
        }
    }
}
