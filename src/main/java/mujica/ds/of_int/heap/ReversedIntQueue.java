package mujica.ds.of_int.heap;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created on 2025/7/9.
 */
public class ReversedIntQueue implements OrderedIntQueue {

    private static final long serialVersionUID = 0xb5860fc94b2dd029L;

    @NotNull
    private final OrderedIntQueue ordered;

    public ReversedIntQueue(@NotNull OrderedIntQueue ordered) {
        super();
        this.ordered = ordered;
    }

    @NotNull
    @Override
    public ReversedIntQueue duplicate() {
        return new ReversedIntQueue(ordered.duplicate());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        ordered.checkHealth(consumer);
    }

    @Override
    public void checkHealth() throws RuntimeException {
        ordered.checkHealth();
    }

    @Override
    public boolean isHealthy() {
        return ordered.isHealthy();
    }

    @Override
    public boolean isDescending() {
        return !ordered.isDescending();
    }

    @Override
    public int intLength() {
        return ordered.intLength();
    }

    @Override
    public boolean isEmpty() {
        return ordered.isEmpty();
    }

    @Override
    public boolean isFull() {
        return ordered.isFull();
    }

    @Override
    public void offer(int t) {
        ordered.offer(~t);
    }

    @Override
    public int remove() {
        return ~ordered.remove();
    }

    @Override
    public int poll(int fallback) {
        return ~ordered.poll(~fallback);
    }

    @Override
    public int element() {
        return ~ordered.element();
    }

    @Override
    public int peek(int fallback) {
        return ~ordered.peek(~fallback);
    }

    @Override
    public void clear() {
        ordered.clear();
    }

    @Override
    public boolean contains(int t) {
        return ordered.contains(~t);
    }

    @NotNull
    @Override
    public int[] toArray() {
        final int[] array = ordered.toArray();
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            array[i] = ~array[i];
        }
        return array;
    }

    @Override
    public void forEach(@NotNull IntConsumer action) {
        ordered.forEach((IntConsumer) (t -> action.accept(~t)));
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
        final Iterator<Integer> it = ordered.iterator();
        if (it instanceof PrimitiveIterator.OfInt) {
            return new BitwiseNotPrimitiveIterator(((PrimitiveIterator.OfInt) it));
        } else {
            return new BitwiseNotIterator(it);
        }
    }

    @NotNull
    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), ordered.intLength(), Spliterator.SIZED);
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
        final int[] thisArray = this.toArray();
        final int[] thatArray = that.toArray();
        Arrays.sort(thisArray);
        Arrays.sort(thatArray);
        return Arrays.equals(thisArray, thatArray);
    }

    @Override
    public int hashCode() {
        final int[] array = toArray();
        Arrays.sort(array);
        return Arrays.hashCode(array);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<ordered = " + ordered.summaryToString() + ">";
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
