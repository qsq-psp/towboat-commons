package mujica.ds.of_int.map;

import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.list.PublicIntList;
import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/3/26")
public class CompatibleIntArrayMap extends IterableIntMap {

    @NotNull
    private final IntList primary;

    @NotNull
    private final IterableIntMap secondary;

    public CompatibleIntArrayMap(@NotNull IntList primary, @NotNull IterableIntMap secondary) {
        super();
        this.primary = primary;
        this.secondary = secondary;
    }

    public CompatibleIntArrayMap(@NotNull IntList primary) {
        this(primary, new CompatibleIntMap());
    }


    public CompatibleIntArrayMap(@NotNull int[] array) {
        this(new PublicIntList(array));
    }

    public CompatibleIntArrayMap(int length) {
        this(new PublicIntList(length));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public CompatibleIntArrayMap clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public CompatibleIntArrayMap duplicate() {
        return new CompatibleIntArrayMap(primary.duplicate(), secondary.duplicate());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {

    }

    @Override
    public long nonZeroKeyCount() {
        int count = 0;
        for (Integer value : primary) {
            if (value != 0) {
                count++;
            }
        }
        return count + secondary.nonZeroKeyCount();
    }

    @Override
    public long sumOfValues() {
        long sum = 0L;
        for (Integer value : primary) {
            sum += value;
        }
        return sum + secondary.sumOfValues();
    }

    @Override
    public void clear() {
        final int length = primary.intLength();
        for (int index = 0; index < length; index++) {
            primary.setInt(index, 0);
        }
        secondary.clear();
    }

    @Override
    public int getInt(int key) {
        if (0 <= key && key < primary.intLength()) {
            return primary.getInt(key);
        } else {
            return secondary.getInt(key);
        }
    }

    @Override
    public int putInt(int key, int newValue) {
        if (0 <= key && key < primary.intLength()) {
            return primary.setInt(key, newValue);
        } else {
            return secondary.putInt(key, newValue);
        }
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        primary.forEach((index, value) -> {
            if (value == 0) {
                return;
            }
            action.accept(index, value);
        });
        secondary.forEach(action);
    }

    @Override
    public void forEachKey(@NotNull IntConsumer action) {
        primary.forEach((index, value) -> {
            if (value == 0) {
                return;
            }
            action.accept(index);
        });
        secondary.forEachKey(action);
    }

    @Override
    public void forEachValue(@NotNull IntConsumer action) {
        primary.forEach((int value) -> {
            if (value != 0) {
                action.accept(value);
            }
        });
        secondary.forEachValue(action);
    }

    @NotNull
    @Override
    public Iterator<IntMapEntry> iterator() {
        return new TwoPhaseIterator();
    }

    private class TwoPhaseIterator implements Iterator<IntMapEntry> {

        @NotNull
        final SimpleIntMapEntry entry = new SimpleIntMapEntry();

        int primaryIndex;

        Iterator<IntMapEntry> secondaryIterator;

        TwoPhaseIterator() {
            super();
            if (primary.intLength() == 0) {
                secondaryIterator = secondary.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (secondaryIterator != null) {
                return secondaryIterator.hasNext();
            } else {
                return primaryIndex < primary.intLength();
            }
        }

        @Override
        public IntMapEntry next() {
            if (secondaryIterator != null) {
                return secondaryIterator.next();
            } else {
                entry.value = primary.getInt(primaryIndex);
                entry.key = primaryIndex;
                primaryIndex++;
                if (primaryIndex == primary.intLength()) {
                    secondaryIterator = secondary.iterator();
                }
                return entry;
            }
        }
    }
}
