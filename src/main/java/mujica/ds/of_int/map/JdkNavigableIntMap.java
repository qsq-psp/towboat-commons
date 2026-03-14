package mujica.ds.of_int.map;

import mujica.ds.of_int.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created on 2026/1/15.
 */
@CodeHistory(date = "2026/1/15", name = "CompatibleNavigableIntMap")
@CodeHistory(date = "2026/1/23")
public class JdkNavigableIntMap extends NavigableIntMap {

    @NotNull
    private final NavigableMap<Integer, Integer> map;

    public JdkNavigableIntMap(@NotNull NavigableMap<Integer, Integer> map) {
        super();
        this.map = map;
    }

    public JdkNavigableIntMap() {
        this(new TreeMap<>());
    }

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
    @Override
    @NotNull
    protected JdkNavigableIntMap clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public JdkNavigableIntMap duplicate() {
        return new JdkNavigableIntMap(new TreeMap<>(map));
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
    public Iterator<Entry> iterator() {
        return new Iterator<>() {

            @NotNull
            final Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();

            @NotNull
            final SimpleIntMapEntry entry1 = new SimpleIntMapEntry();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry next() {
                final Map.Entry<Integer, Integer> entry0 = iterator.next();
                entry1.key = entry0.getKey();
                entry1.value = entry0.getValue();
                return entry1;
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JdkNavigableIntMap && this.map.equals(((JdkNavigableIntMap) obj).map);
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "CompatibleNavigableIntMap<" + map.getClass().getName() + "<entry*" + map.size() + ">>";
    }

    @NotNull
    @Override
    public String detailToString() {
        return map.toString();
    }
}
