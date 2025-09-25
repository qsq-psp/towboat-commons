package mujica.ds.of_int.map;

import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2023/2/27", project = "Ultramarine", name = "IntMap.CompatibleFrom1")
@CodeHistory(date = "2025/3/26")
public class CompatibleIntMap extends IterableIntMap {

    private static final long serialVersionUID = 0xebe68e3c32d116a7L;

    @NotNull
    private final Map<Integer, Integer> map;

    public CompatibleIntMap(@NotNull Map<Integer, Integer> map) {
        super();
        this.map = map;
    }

    public CompatibleIntMap() {
        this(new HashMap<>());
    }

    @NotNull
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public CompatibleIntMap clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public CompatibleIntMap duplicate() {
        return new CompatibleIntMap(new HashMap<>(map));
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (map.containsKey(null)) {
            consumer.accept(new RuntimeException("map contains null key"));
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                consumer.accept(new RuntimeException("map contains null value, corresponding key is " + entry.getKey()));
            } else if (entry.getValue() == 0) {
                consumer.accept(new RuntimeException("map contains zero value, corresponding key is " + entry.getKey()));
            }
        }
    }

    public boolean isHealthy() {
        if (map.containsKey(null)) {
            return false;
        }
        for (Integer value : map.values()) {
            if (value == null || value == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public long nonZeroKeyCount() {
        return map.size();
    }

    @Override
    public long sumOfValues() {
        long sum = 0L;
        for (int value : map.values()) {
            assert value != 0;
            sum += value;
        }
        return sum;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int getInt(int key) {
        final Integer value = map.get(key);
        if (value != null) {
            assert value != 0;
            return value;
        } else {
            return 0;
        }
    }

    @Override
    public int putInt(int key, int newValue) {
        Integer oldValue;
        if (newValue != 0) {
            oldValue = map.put(key, newValue);
        } else {
            oldValue = map.remove(key);
        }
        if (oldValue != null) {
            assert oldValue != 0;
            return oldValue;
        } else {
            return 0;
        }
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void forEachKey(@NotNull IntConsumer action) {
        for (Integer key : map.keySet()) {
            action.accept(key);
        }
    }

    @Override
    public void forEachValue(@NotNull IntConsumer action) {
        for (Integer value : map.values()) {
            action.accept(value);
        }
    }

    @NotNull
    @Override
    public Iterator<IntMapEntry> iterator() {
        return new CompatibleIntIterator();
    }

    private class CompatibleIntIterator implements Iterator<IntMapEntry> {

        @NotNull
        final Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();

        @NotNull
        final SimpleIntMapEntry entry1 = new SimpleIntMapEntry();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public IntMapEntry next() {
            final Map.Entry<Integer, Integer> entry0 = iterator.next();
            entry1.key = entry0.getKey();
            entry1.value = entry0.getValue();
            return entry1;
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CompatibleIntMap && this.map.equals(((CompatibleIntMap) obj).map);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "CompatibleIntMap<" + map.getClass().getName() + "<entry*" + map.size() + ">>";
    }

    @NotNull
    @Override
    public String detailToString() {
        return map.toString();
    }
}
