package mujica.ds.i32.map;

import mujica.ds.i32.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/3/26")
public class EmptyI32Map extends IterableI32Map {

    public static final EmptyI32Map INSTANCE = new EmptyI32Map();

    @NotNull
    @Override
    public EmptyI32Map duplicate() {
        return new EmptyI32Map();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    @Override
    public void clear() {
        // NOP
    }

    @Override
    public long nonZeroKeyCount() {
        return 0L;
    }

    @Override
    public long sumOfValues() {
        return 0L;
    }

    @Override
    public int getI32(int key) {
        return 0;
    }

    @Override
    public int putI32(int key, int newValue) {
        if (newValue == 0) {
            return 0;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super Entry> action) {
        // pass
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        // pass
    }

    @Override
    public void forEachKey(@NotNull IntConsumer action) {
        // pass
    }

    @Override
    public void forEachValue(@NotNull IntConsumer action) {
        // pass
    }

    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyI32Map;
    }
}
