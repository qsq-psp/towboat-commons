package mujica.ds.of_int.heap;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/7/9")
public class ReversedIntQueue implements OrderedIntQueue {

    private static final long serialVersionUID = 0xb5860fc94b2dd029L;

    @NotNull
    private final OrderedIntQueue queue;

    public ReversedIntQueue(@NotNull OrderedIntQueue queue) {
        super();
        this.queue = queue;
    }

    @NotNull
    @Override
    public ReversedIntQueue duplicate() {
        return new ReversedIntQueue(queue.duplicate());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        queue.checkHealth(consumer);
    }

    @Override
    public void checkHealth() throws RuntimeException {
        queue.checkHealth();
    }

    @Override
    public boolean isHealthy() {
        return queue.isHealthy();
    }

    @Override
    public boolean isDescending() {
        return !queue.isDescending();
    }

    @Override
    public int intLength() {
        return queue.intLength();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean isFull() {
        return queue.isFull();
    }

    @Override
    public void offer(int t) {
        queue.offer(~t);
    }

    @Override
    public int remove() {
        return ~queue.remove();
    }

    @Override
    public int poll(int fallback) {
        return ~queue.poll(~fallback);
    }

    @Override
    public int element() {
        return ~queue.element();
    }

    @Override
    public int peek(int fallback) {
        return ~queue.peek(~fallback);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean contains(int t) {
        return queue.contains(~t);
    }

    @NotNull
    @Override
    public int[] toIntArray() {
        final int[] array = queue.toIntArray();
        final int n = array.length;
        for (int i = 0; i < n; i++) {
            array[i] = ~array[i];
        }
        return array;
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        queue.forEach((IntConsumer) (t -> action.accept(~t)));
    }

    private static class BitwiseNotIterator implements Iterator<Integer> {

        @NotNull
        private final Iterator<Integer> it;

        private BitwiseNotIterator(@NotNull Iterator<Integer> it) {
            super();
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Integer next() {
            Integer t = it.next();
            if (t != null) {
                t = ~t;
            }
            return t;
        }
    }

    private static class BitwiseNotPrimitiveIterator implements PrimitiveIterator.OfInt {

        @NotNull
        private final OfInt it;

        private BitwiseNotPrimitiveIterator(@NotNull OfInt it) {
            super();
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public int nextInt() {
            return ~it.nextInt();
        }
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        final Iterator<Integer> it = queue.iterator();
        if (it instanceof PrimitiveIterator.OfInt) {
            return new BitwiseNotPrimitiveIterator(((PrimitiveIterator.OfInt) it));
        } else {
            return new BitwiseNotIterator(it);
        }
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), queue.intLength(), Spliterator.SIZED);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof OrderedIntQueue)) {
            return false;
        }
        final OrderedIntQueue that = (OrderedIntQueue) object;
        if (this.intLength() != that.intLength() || this.isDescending() != that.isDescending()) {
            return false;
        }
        final int[] thisArray = this.toIntArray();
        final int[] thatArray = that.toIntArray();
        Arrays.sort(thisArray);
        Arrays.sort(thatArray);
        return Arrays.equals(thisArray, thatArray);
    }

    @Override
    public int hashCode() {
        final int[] array = toIntArray();
        Arrays.sort(array);
        return Arrays.hashCode(array);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<reversed " + queue.summaryToString() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder();
        stringifyDetail(sb);
        return sb.toString();
    }

    public void stringifyDetail(@NotNull StringBuilder sb) {
        sb.append("[");
        boolean subsequent = false;
        for (int v : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(~v);
            subsequent = true;
        }
        sb.append("]");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + summaryToString() + " " + detailToString();
    }
}
