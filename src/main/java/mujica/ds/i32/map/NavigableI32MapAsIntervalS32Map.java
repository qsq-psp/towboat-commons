package mujica.ds.i32.map;

import mujica.ds.i32.I32;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

@CodeHistory(date = "2024/2/8", project = "Ultramarine", name = "TreeIntSteps")
@CodeHistory(date = "2026/1/23")
public class NavigableI32MapAsIntervalS32Map extends IntervalS32Map {

    @NotNull
    private final NavigableS32Map steps;

    @NotNull
    private transient I32 slot;

    public NavigableI32MapAsIntervalS32Map(@NotNull NavigableS32Map steps) {
        super();
        this.steps = steps;
        this.slot = new I32();
    }

    @NotNull
    @Override
    public NavigableI32MapAsIntervalS32Map duplicate() {
        return new NavigableI32MapAsIntervalS32Map(steps.duplicate());
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final Iterator<Entry> iterator = steps.ascendingIterator();
        if (!iterator.hasNext()) {
            consumer.accept(new RuntimeException("empty steps"));
            return;
        }
        Entry entry = iterator.next();
        if (entry.getI32Key() != Integer.MIN_VALUE) {
            consumer.accept(new RuntimeException("first step key"));
        }
        int previousValue = entry.getI32Value();
        while (iterator.hasNext()) {
            entry = iterator.next();
            int currentValue = entry.getI32Value();
            if (previousValue == currentValue) {
                consumer.accept(new RuntimeException("same step value"));
            }
            previousValue = currentValue;
        }
    }

    @Override
    public void clear() {
        steps.clear();
        steps.putI32(Integer.MIN_VALUE, 0);
    }

    @Override
    public int getI32(int key) {
        slot.setI32(key);
        if (steps.floorKey(slot)) {
            return steps.getI32(slot.getI32());
        } else {
            throw new RuntimeException();
        }
    }

    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        return null;
    }
}
