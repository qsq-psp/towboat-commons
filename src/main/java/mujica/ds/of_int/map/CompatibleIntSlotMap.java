package mujica.ds.of_int.map;

import mujica.ds.of_int.PublicIntSlot;
import mujica.reflect.function.IntEntryConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@CodeHistory(date = "2023/2/27", project = "Ultramarine", name = "IntMap.CompatibleFrom2")
@CodeHistory(date = "2025/3/26")
public class CompatibleIntSlotMap extends IterableIntMap {

    private static final long serialVersionUID = 0x2f21d0a99fc4ee5bL;

    @NotNull
    private final Map<Integer, PublicIntSlot> map;

    private int zeroCount;

    protected CompatibleIntSlotMap(@NotNull Map<Integer, PublicIntSlot> map, int zeroCount) {
        super();
        this.map = map;
        this.zeroCount = zeroCount;
    }

    public CompatibleIntSlotMap() {
        super();
        map = new HashMap<>();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NotNull
    @Override
    public CompatibleIntSlotMap clone() {
        return duplicate();
    }

    @NotNull
    @Override
    public CompatibleIntSlotMap duplicate() {
        return new CompatibleIntSlotMap(new HashMap<>(map), zeroCount);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (map.containsKey(null)) {
            consumer.accept(new RuntimeException("map contains null key"));
        }
        int actualZeroCount = 0;
        for (Map.Entry<Integer, PublicIntSlot> entry : map.entrySet()) {
            PublicIntSlot slot = entry.getValue();
            if (slot == null) {
                consumer.accept(new RuntimeException("map contains null slot, corresponding key is " + entry.getKey()));
                continue;
            }
            if (slot.value == 0) {
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
        for (PublicIntSlot slot : map.values()) {
            if (slot == null) {
                return false;
            }
            if (slot.value == 0) {
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
        for (PublicIntSlot slot : map.values()) {
            int value = slot.value;
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
        map.values().removeIf(slot -> slot.value == 0);
    }

    @Override
    public int getInt(int key) {
        final PublicIntSlot slot = map.get(key);
        if (slot != null) {
            return slot.value;
        } else {
            return 0;
        }
    }

    @Override
    public int putInt(int key, int newValue) {
        if (newValue == 0) {
            zeroCount++;
        }
        PublicIntSlot slot = map.get(key);
        if (slot != null) {
            int oldValue = slot.setInt(newValue);
            if (oldValue == 0) {
                zeroCount--;
            }
            return oldValue;
        }
        slot = new PublicIntSlot(newValue);
        map.put(key, slot);
        return 0;
    }

    @Override
    public void forEach(@NotNull IntEntryConsumer action) {
        for (Map.Entry<Integer, PublicIntSlot> entry : map.entrySet()) {
            int value = entry.getValue().value;
            if (value == 0) {
                continue;
            }
            action.accept(entry.getKey(), value);
        }
    }

    @Override
    public void forEachKey(@NotNull IntConsumer action) {
        for (Map.Entry<Integer, PublicIntSlot> entry : map.entrySet()) {
            if (entry.getValue().value == 0) {
                continue;
            }
            action.accept(entry.getKey());
        }
    }

    @Override
    public void forEachValue(@NotNull IntConsumer action) {
        for (PublicIntSlot slot : map.values()) {
            if (slot.value == 0) {
                continue;
            }
            action.accept(slot.value);
        }
    }

    @NotNull
    @Override
    public Iterator<IntMapEntry> iterator() {
        return new CompatibleIntSlotIterator();
    }

    private class CompatibleIntSlotIterator implements Iterator<IntMapEntry> {

        @NotNull
        final Iterator<Map.Entry<Integer, PublicIntSlot>> iterator = map.entrySet().iterator();

        @NotNull
        final SimpleIntMapEntry entry1 = new SimpleIntMapEntry();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public IntMapEntry next() {
            final Map.Entry<Integer, PublicIntSlot> entry0 = iterator.next();
            entry1.key = entry0.getKey();
            entry1.value = entry0.getValue().value;
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
        return obj instanceof CompatibleIntSlotMap && this.map.equals(((CompatibleIntSlotMap) obj).map);
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
