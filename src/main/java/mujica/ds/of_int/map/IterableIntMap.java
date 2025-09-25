package mujica.ds.of_int.map;

import mujica.reflect.function.IntEntryConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Created on 2025/3/27.
 */
public abstract class IterableIntMap extends IntMap implements Iterable<IntMapEntry> {

    @NotNull
    @Override
    public abstract IterableIntMap duplicate();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    @Override
    public abstract Iterator<IntMapEntry> iterator();

    /**
     * @return -1 for unknown
     */
    public long nonZeroKeyCount() {
        long count = 0L;
        for (IntMapEntry ignore : this) {
            count++;
        }
        return count;
    }

    /**
     * @return -1 for unknown
     */
    public long sumOfValues() {
        long sum = 0L;
        for (IntMapEntry entry : this) {
            sum += entry.getIntValue();
        }
        return sum;
    }

    public void forEach(@NotNull IntEntryConsumer action) {
        for (IntMapEntry entry : this) {
            action.accept(entry.getIntKey(), entry.getIntValue());
        }
    }

    public void forEachKey(@NotNull IntConsumer action) {
        for (IntMapEntry entry : this) {
            action.accept(entry.getIntKey());
        }
    }

    public void forEachValue(@NotNull IntConsumer action) {
        for (IntMapEntry entry : this) {
            action.accept(entry.getIntValue());
        }
    }

    @NotNull
    public IntSummaryStatistics keyStatistics() {
        final IntSummaryStatistics statistics = new IntSummaryStatistics();
        forEachKey(statistics);
        return statistics;
    }

    @NotNull
    public IntSummaryStatistics valueStatistics() {
        final IntSummaryStatistics statistics = new IntSummaryStatistics();
        forEachValue(statistics);
        return statistics;
    }

    public void removeIfKey(@NotNull IntPredicate predicate) {
        for (Iterator<IntMapEntry> iterator = this.iterator(); iterator.hasNext();) {
            if (predicate.test(iterator.next().getIntKey())) {
                iterator.remove();
            }
        }
    }

    public void removeIfValue(@NotNull IntPredicate predicate) {
        for (Iterator<IntMapEntry> iterator = this.iterator(); iterator.hasNext();) {
            if (predicate.test(iterator.next().getIntValue())) {
                iterator.remove();
            }
        }
    }

    public void trimTrie() {
        removeIfKey(key -> {
            key &= TYPE_MASK;
            return key == TYPE_TRIE || key == TYPE_ACCEPT;
        });
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<entry*" + nonZeroKeyCount() + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        final StringBuilder sb = new StringBuilder("(");
        boolean subsequent = false;
        for (IntMapEntry entry : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(entry.getIntKey()).append("=").append(entry.getIntValue());
            subsequent = true;
        }
        return sb.append(")").toString();
    }

    @Override
    public String toString() {
        return summaryToString() + detailToString();
    }
}
