package mujica.ds.of_int.map;

import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2025/3/26")
public class EmptyIntMap extends IterableIntMap {

    public static final EmptyIntMap INSTANCE = new EmptyIntMap();

    @NotNull
    @Override
    public EmptyIntMap duplicate() {
        return new EmptyIntMap();
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
    public int getInt(int key) {
        return 0;
    }

    @Override
    public int putInt(int key, int newValue) {
        if (newValue == 0) {
            return 0;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void forEach(Consumer<? super IntMapEntry> action) {
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
    public Iterator<IntMapEntry> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmptyIntMap;
    }
}
