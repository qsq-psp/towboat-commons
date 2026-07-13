package mujica.ds.i32.map;

import mujica.ds.i32.list.IntList;
import mujica.ds.i32.list.PublicIntList;
import mujica.ds.i32.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/3/26", name = "CompatibleIntArrayMap")
@CodeHistory(date = "2026/2/6")
public class LookUpI32Map extends IterableI32Map {

    @NotNull
    private final IntList primary;

    @NotNull
    private final IterableI32Map secondary;

    public LookUpI32Map(@NotNull IntList primary, @NotNull IterableI32Map secondary) {
        super();
        this.primary = primary;
        this.secondary = secondary;
    }

    public LookUpI32Map(@NotNull IntList primary) {
        this(primary, new JdkI32Map());
    }


    public LookUpI32Map(@NotNull int[] array) {
        this(new PublicIntList(array));
    }

    public LookUpI32Map(int length) {
        this(new PublicIntList(length));
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public LookUpI32Map clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public LookUpI32Map duplicate() {
        return new LookUpI32Map(primary.duplicate(), secondary.duplicate());
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
    public int getI32(int key) {
        if (0 <= key && key < primary.intLength()) {
            return primary.getInt(key);
        } else {
            return secondary.getI32(key);
        }
    }

    @Override
    public int putI32(int key, int newValue) {
        if (0 <= key && key < primary.intLength()) {
            return primary.setInt(key, newValue);
        } else {
            return secondary.putI32(key, newValue);
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
    public Iterator<Entry> iterator() {
        return new Iterator<>() {

            @NotNull
            final SimpleIntMapEntry entry = new SimpleIntMapEntry();

            int primaryIndex;

            Iterator<Entry> secondaryIterator = primary.intLength() == 0 ? secondary.iterator() : null;

            @Override
            public boolean hasNext() {
                if (secondaryIterator != null) {
                    return secondaryIterator.hasNext();
                } else {
                    return primaryIndex < primary.intLength();
                }
            }

            @Override
            public Entry next() {
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
        };
    }
}
