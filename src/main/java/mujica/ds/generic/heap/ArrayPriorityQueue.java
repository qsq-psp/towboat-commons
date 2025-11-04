package mujica.ds.generic.heap;

import mujica.ds.of_int.list.ResizePolicy;
import mujica.ds.of_int.list.TwiceResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/6/14")
public class ArrayPriorityQueue<E> extends AbstractPriorityQueue<E> {

    private static final long serialVersionUID = 0x18b9f59103109b81L;

    @NotNull
    final ResizePolicy policy;

    @NotNull
    transient Object[] elements;

    @Index(of = "elements")
    transient int head;

    @Index(of = "elements", inclusive = false)
    transient int tail;

    public ArrayPriorityQueue(@Nullable Comparator<E> comparator, @Nullable ResizePolicy policy) {
        super(comparator);
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
        }
        this.policy = policy;
        final int capacity = policy.initialCapacity();
        this.elements = new Object[capacity];
        final int half = capacity >> 1;
        head = half;
        tail = half;
    }

    public ArrayPriorityQueue(@Nullable Comparator<E> comparator, @Nullable ResizePolicy policy, @NotNull List<E> list) {
        super(comparator);
        if (policy == null) {
            policy = TwiceResizePolicy.INSTANCE;
        }
        list.sort(this.comparator);
        this.policy = policy;
        final int size = list.size();
        final int capacity = policy.notSmallerCapacity(size);
        this.elements = new Object[capacity];
        final int offset = (capacity - size) >> 1;
        for (int index = 0; index < size; index++) {
            this.elements[offset + index] = list.get(index);
        }
        head = offset;
        tail = offset + size;
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        final int size = ois.readInt();
        final int capacity = Math.max(size, policy.initialCapacity());
        elements = new Object[capacity];
        head = (capacity - size) >> 1;
        tail = head + size;
        for (int index = head; index < tail; index++) {
            elements[index] = ois.readObject();
        }
    }

    private void writeObject(@NotNull ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeInt(tail - head); // size
        for (int index = head; index < tail; index++) {
            oos.writeObject(elements[index]);
        }
    }

    @NotNull
    @Override
    public ArrayPriorityQueue<E> clone() {
        try {
            ArrayPriorityQueue<E> that = (ArrayPriorityQueue<E>) super.clone();
            that.elements = this.elements.clone();
            return that;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public ArrayPriorityQueue<E> duplicate(@NotNull UnaryOperator<E> operator) {
        final ArrayPriorityQueue<E> that = new ArrayPriorityQueue<>(comparator, policy);
        final int capacity = elements.length;
        that.elements = new Object[capacity];
        for (int index = 0; index < capacity; index++) {
            Object object = this.elements[index];
            if (object == null) {
                continue;
            }
            object = operator.apply((E) object);
            that.elements[index] = object;
        }
        that.head = this.head;
        that.tail = this.tail;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (!(0 <= head && head <= tail && tail <= elements.length)) {
            consumer.accept(new IndexOutOfBoundsException("head = " + head + ", tail = " + tail + ", capacity = " + elements.length));
        }
    }

    @Override
    public int size() {
        return tail - head; // no go around
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    @Override
    public long sumOfDepth() {
        return size(); // only one layer
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object element) {
        int low = head;
        int high = tail;
        while (low < high) {
            int mid = (low + high) >>> 1;
            int sign = comparator.compare((E) element, (E) elements[mid]);
            if (sign < 0) {
                high = mid;
            } else if (sign > 0) {
                low = mid + 1;
            } else {
                low = mid - head;
                high = tail - mid - 1;
                if (low < high) {
                    System.arraycopy(elements, head, elements, head + 1, low);
                    elements[head] = null;
                    head++;
                } else {
                    System.arraycopy(elements, mid, elements, mid - 1, high);
                    tail--;
                    elements[tail] = null;
                }
                modCount++;
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean offer(E element) {
        int low = head;
        int high = tail;
        while (low < high) {
            int mid = (low + high) >>> 1;
            int sign = comparator.compare(element, (E) elements[mid]);
            if (sign < 0) {
                high = mid;
            } else if (sign > 0) {
                low = mid + 1;
            } else {
                low = mid;
                break;
            }
        }
        final int index = low;
        low = index - head;
        high = tail - index;
        int capacity = elements.length;
        if (head == 0) {
            if (tail == capacity) {
                int newCapacity = policy.nextCapacity(capacity);
                if (!(capacity < newCapacity)) {
                    return false;
                }
                Object[] newElements = new Object[newCapacity];
                int newSize = tail - head + 1;
                int newHead = (newCapacity - newSize) >>> 1;
                System.arraycopy(elements, head, newElements, newHead, low);
                System.arraycopy(elements, index, newElements, newHead + low + 1, high);
                newElements[newHead + low] = element;
                elements = newElements;
                head = newHead;
                tail = newHead + newSize;
                return true;
            }
            low = Integer.MAX_VALUE;
        } else if (tail == capacity) {
            high = Integer.MAX_VALUE;
        }
        if (low < high) {
            System.arraycopy(elements, head, elements, head - 1, low);
            elements[index - 1] = element;
            head--;
        } else {
            System.arraycopy(elements, index, elements, index + 1, high);
            elements[index] = element;
            tail++;
        }
        modCount++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove() {
        if (head == tail) {
            throw new NoSuchElementException();
        }
        final E removed = (E) elements[head];
        elements[head] = null;
        head++;
        if (head == tail) {
            int half = elements.length >> 1;
            head = half;
            tail = half;
        }
        modCount++;
        return removed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (head == tail) {
            return null;
        }
        final E removed = (E) elements[head];
        elements[head] = null;
        head++;
        if (head == tail) {
            int half = elements.length >> 1;
            head = half;
            tail = half;
        }
        modCount++;
        return removed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E element() {
        if (head == tail) {
            throw new NoSuchElementException();
        }
        return (E) elements[head];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        return (E) elements[head];
    }

    class SafeIterator implements Iterator<E> {

        int index = head;

        int lastRemove = head; // if remove() is not used, there is no extra processing

        int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return index < tail;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (index < tail) {
                return (E) elements[index++];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (index == lastRemove) {
                throw new IllegalStateException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            int low = index - head - 1;
            int high = tail - index;
            assert low >= 0;
            assert high >= 0;
            if (low < high) {
                System.arraycopy(elements, head, elements, head + 1, low);
                elements[head] = null;
                head++;
                lastRemove = index;
            } else {
                System.arraycopy(elements, index, elements, index - 1, high);
                tail--;
                elements[tail] = null;
                lastRemove = --index;
            }
            if (head == tail) {
                int half = elements.length >> 1;
                head = half;
                tail = half;
                index = half;
                lastRemove = half;
            }
            expectedModCount = ++modCount;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new SafeIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(elements, head, tail);
    }
}
