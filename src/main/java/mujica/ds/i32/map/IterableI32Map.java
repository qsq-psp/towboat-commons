package mujica.ds.i32.map;

import mujica.ds.i32.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

@CodeHistory(date = "2025/3/27")
@DirectSubclass({EmptyI32Map.class, JdkI32Map.class, JdkI32SlotMap.class, LookUpI32Map.class, NavigableS32Map.class})
public abstract class IterableI32Map extends I32Map implements Iterable<I32Map.Entry> {

    protected IterableI32Map() {
        super();
    }

    @NotNull
    @Override
    public abstract IterableI32Map duplicate();

    @NotNull
    @Override
    public abstract Iterator<Entry> iterator();

    /**
     * @return -1 for unknown
     */
    public long nonZeroKeyCount() {
        long count = 0L;
        for (Entry ignore : this) {
            count++;
        }
        return count;
    }

    /**
     * @return -1 for unknown
     */
    public long sumOfValues() {
        long sum = 0L;
        for (Entry entry : this) {
            sum += entry.getI32Value();
        }
        return sum;
    }

    public void forEach(@NotNull IntEntryConsumer action) {
        for (Entry entry : this) {
            action.accept(entry.getI32Key(), entry.getI32Value());
        }
    }

    public void forEachKey(@NotNull IntConsumer action) {
        for (Entry entry : this) {
            action.accept(entry.getI32Key());
        }
    }

    public void forEachValue(@NotNull IntConsumer action) {
        for (Entry entry : this) {
            action.accept(entry.getI32Value());
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
        for (Iterator<Entry> iterator = this.iterator(); iterator.hasNext();) {
            if (predicate.test(iterator.next().getI32Key())) {
                iterator.remove();
            }
        }
    }

    public void removeIfValue(@NotNull IntPredicate predicate) {
        for (Iterator<Entry> iterator = this.iterator(); iterator.hasNext();) {
            if (predicate.test(iterator.next().getI32Value())) {
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
        for (Entry entry : this) {
            if (subsequent) {
                sb.append(", ");
            }
            sb.append(entry.getI32Key()).append("=").append(entry.getI32Value());
            subsequent = true;
        }
        return sb.append(")").toString();
    }

    @Override
    public String toString() {
        return summaryToString() + detailToString();
    }
}
