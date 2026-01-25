package mujica.ds.of_long.heap;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

/**
 * Created on 2026/1/15.
 */
@CodeHistory(date = "2026/1/15")
public class ReversedLongQueue implements OrderedLongQueue {

    @NotNull
    private final OrderedLongQueue queue;

    public ReversedLongQueue(@NotNull OrderedLongQueue queue) {
        super();
        this.queue = queue;
    }

    @NotNull
    @Override
    public OrderedLongQueue duplicate() {
        return new ReversedLongQueue(queue.duplicate());
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
    public int longLength() {
        return queue.longLength();
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
    public void offer(long t) {
        queue.offer(~t);
    }

    @Override
    public int remove() throws NoSuchElementException {
        return ~queue.remove();
    }

    @Override
    public int poll(long fallback) {
        return ~queue.poll(~fallback);
    }

    @Override
    public int element() throws NoSuchElementException {
        return ~queue.element();
    }

    @Override
    public int peek(long fallback) {
        return ~queue.peek(~fallback);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean contains(long t) {
        return queue.contains(~t);
    }

    @NotNull
    @Override
    public long[] toLongArray() {
        final long[] array = queue.toLongArray();
        final int n = array.length;
        for (int i = 0; i < n; i++) {
            array[i] = ~array[i];
        }
        return array;
    }

    @Override
    public void forEach(@NotNull LongConsumer action) {
        queue.forEach((LongConsumer) (t -> action.accept(~t)));
    }

    private static class BitwiseNotIterator implements Iterator<Long> {

        @NotNull
        private final Iterator<Long> it;

        private BitwiseNotIterator(@NotNull Iterator<Long> it) {
            super();
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Long next() {
            Long t = it.next();
            if (t != null) {
                t = ~t;
            }
            return t;
        }
    }

    private static class BitwiseNotPrimitiveIterator implements PrimitiveIterator.OfLong {

        @NotNull
        private final OfLong it;

        private BitwiseNotPrimitiveIterator(@NotNull OfLong it) {
            super();
            this.it = it;
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public long nextLong() {
            return ~it.nextLong();
        }
    }

    @NotNull
    @Override
    public Iterator<Long> iterator() {
        final Iterator<Long> it = queue.iterator();
        if (it instanceof PrimitiveIterator.OfLong) {
            return new BitwiseNotPrimitiveIterator(((PrimitiveIterator.OfLong) it));
        } else {
            return new BitwiseNotIterator(it);
        }
    }

    @NotNull
    @Override
    public Spliterator<Long> spliterator() {
        return Spliterators.spliterator(iterator(), queue.longLength(), Spliterator.SIZED);
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
        for (long v : this) {
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
