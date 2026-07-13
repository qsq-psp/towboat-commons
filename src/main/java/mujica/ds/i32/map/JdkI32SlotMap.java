package mujica.ds.i32.map;

import mujica.ds.i32.I32;
import mujica.ds.i32.list.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2023/2/27", project = "Ultramarine", name = "IntMap.CompatibleFrom2")
@CodeHistory(date = "2025/3/26", name = "CompatibleI32SlotMap")
@CodeHistory(date = "2026/7/8")
public class JdkI32SlotMap extends IterableI32Map {

    private static final long serialVersionUID = 0x2f21d0a99fc4ee5bL;

    @NotNull
    private final Map<Integer, I32> map;

    private int zeroCount;

    protected JdkI32SlotMap(@NotNull Map<Integer, I32> map, int zeroCount) {
        super();
        this.map = map;
        this.zeroCount = zeroCount;
    }

    public JdkI32SlotMap() {
        super();
        map = new HashMap<>();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public JdkI32SlotMap clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public JdkI32SlotMap duplicate() {
        return new JdkI32SlotMap(new HashMap<>(map), zeroCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (map.containsKey(null)) {
            consumer.accept(new RuntimeException("map contains null key"));
        }
        int actualZeroCount = 0;
        for (Map.Entry<Integer, I32> entry : map.entrySet()) {
            I32 slot = entry.getValue();
            if (slot == null) {
                consumer.accept(new RuntimeException("map contains null slot, corresponding key is " + entry.getKey()));
                continue;
            }
            if (slot.getI32() == 0) {
                actualZeroCount++;
            }
        }
        if (zeroCount != actualZeroCount) {
            consumer.accept(new RuntimeException("zero count mismatch; expected = " + zeroCount + ", actual = " + actualZeroCount));
        }
    }

    public boolean isHealthy() {
        if (map.containsKey(null)) {
            return false;
        }
        int actualZeroCount = 0;
        for (I32 slot : map.values()) {
            if (slot == null) {
                return false;
            }
            if (slot.getI32() == 0) {
                actualZeroCount++;
            }
        }
        return zeroCount == actualZeroCount;
    }

    @Override
    public long nonZeroKeyCount() {
        return map.size() - zeroCount;
    }

    @Override
    public long sumOfValues() {
        long sum = 0L;
        for (I32 slot : map.values()) {
            int value = slot.getI32();
            assert value != 0;
            sum += value;
        }
        return sum;
    }

    @Override
    public void clear() {
        map.clear();
    }

    public void trim() {
        // map.entrySet().removeIf(entry -> entry.getValue().value == 0);
        map.values().removeIf(slot -> slot.getI32() == 0);
    }

    @Override
    public int getI32(int key) {
        final I32 slot = map.get(key);
        if (slot != null) {
            return slot.getI32();
        } else {
            return 0;
        }
    }

    @Override
    public int putI32(int key, int newValue) {
        if (newValue == 0) {
            zeroCount++;
        }
        I32 slot = map.get(key);
        if (slot != null) {
            int oldValue = slot.updateI32(newValue);
            if (oldValue == 0) {
                zeroCount--;
            }
            return oldValue;
        }
        slot = new I32(newValue);
        map.put(key, slot);
        return 0;
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        for (Map.Entry<Integer, I32> entry : map.entrySet()) {
            int value = entry.getValue().getI32();
            if (value == 0) {
                continue;
            }
            action.accept(entry.getKey(), value);
        }
    }

    @Override
    public void forEachKey(@NotNull IntConsumer action) {
        for (Map.Entry<Integer, I32> entry : map.entrySet()) {
            if (entry.getValue().getI32() == 0) {
                continue;
            }
            action.accept(entry.getKey());
        }
    }

    @Override
    public void forEachValue(@NotNull IntConsumer action) {
        for (I32 slot : map.values()) {
            if (slot.getI32() == 0) {
                continue;
            }
            action.accept(slot.getI32());
        }
    }

    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        return new Iterator<>() {

            @NotNull
            final Iterator<Map.Entry<Integer, I32>> iterator = map.entrySet().iterator();

            @NotNull
            final SimpleIntMapEntry entry1 = new SimpleIntMapEntry();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Entry next() {
                final Map.Entry<Integer, I32> entry0 = iterator.next();
                entry1.key = entry0.getKey();
                entry1.value = entry0.getValue().getI32();
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
        return obj instanceof JdkI32SlotMap && this.map.equals(((JdkI32SlotMap) obj).map);
    }

    @Override
    @NotNull
    public String summaryToString() {
        return "CompatibleIntSlotMap<" + map.getClass().getName() + "<entry*" + map.size() + ">>";
    }

    @Override
    @NotNull
    public String detailToString() {
        return map.toString();
    }
}
