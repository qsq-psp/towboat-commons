package mujica.ds.of_int;

import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@CodeHistory(date = "2018/7/3", project = "existence", name = "MtInteger")
@CodeHistory(date = "2020/2/22", project = "coo", name = "IntegerModel")
@CodeHistory(date = "2022/7/2", project = "Ultramarine", name = "Counter")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9")
public class PublicIntSlot extends Number implements IntSlot, Comparable<PublicIntSlot>, Cloneable {

    private static final long serialVersionUID = 0xf76e70d00282fbbdL;

    public int value;

    public PublicIntSlot() {
        super();
    }

    public PublicIntSlot(int value) {
        super();
        this.value = value;
    }

    @NotNull
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public PublicIntSlot clone() {
        return new PublicIntSlot(value);
    }

    public void absExact() {
        value = IntegralMath.INSTANCE.abs(value);
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param <T> counted item type
     */
    public static <T> int increase(@NotNull Map<T, PublicIntSlot> map, T t) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(1);
            map.put(t, slot);
        } else {
            slot.value++;
        }
        return slot.value;
    }

    public static <T> PublicIntSlot remappingIncrease(T key, @Nullable PublicIntSlot slot) {
        if (slot != null) {
            slot.value++;
        } else {
            slot = new PublicIntSlot(1);
        }
        return slot;
    }

    public static <T> int computeIncrease(@NotNull Map<T, PublicIntSlot> map, T t) {
        return map.compute(t, PublicIntSlot::remappingIncrease).value;
    }

    public void increaseExact() {
        value = Math.incrementExact(value);
    }

    public static <T> int increaseExact(@NotNull Map<T, PublicIntSlot> map, T t) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(1);
            map.put(t, slot);
        } else {
            slot.increaseExact();
        }
        return slot.value;
    }

    public static <T> PublicIntSlot remappingIncreaseExact(T key, @Nullable PublicIntSlot slot) {
        if (slot != null) {
            slot.increaseExact();
        } else {
            slot = new PublicIntSlot(1);
        }
        return slot;
    }

    public static <T> int computeIncreaseExact(@NotNull Map<T, PublicIntSlot> map, T t) {
        return map.compute(t, PublicIntSlot::remappingIncreaseExact).value;
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param <T> counted item type
     */
    public static <T> int decrease(@NotNull Map<T, PublicIntSlot> map, T t) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(-1);
            map.put(t, slot);
        } else {
            slot.value--;
        }
        return slot.value;
    }

    public static <T> PublicIntSlot remappingDecrease(T key, @Nullable PublicIntSlot slot) {
        if (slot != null) {
            slot.value--;
        } else {
            slot = new PublicIntSlot(-1);
        }
        return slot;
    }

    public static <T> int computeDecrease(@NotNull Map<T, PublicIntSlot> map, T t) {
        return map.compute(t, PublicIntSlot::remappingDecrease).value;
    }

    public void decreaseExact() {
        value = Math.decrementExact(value);
    }

    public static <T> int decreaseExact(@NotNull Map<T, PublicIntSlot> map, T t) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(-1);
            map.put(t, slot);
        } else {
            slot.decreaseExact();
        }
        return slot.value;
    }

    public static <T> PublicIntSlot remappingDecreaseExact(T key, @Nullable PublicIntSlot slot) {
        if (slot != null) {
            slot.decreaseExact();
        } else {
            slot = new PublicIntSlot(-1);
        }
        return slot;
    }

    public static <T> int computeDecreaseExact(@NotNull Map<T, PublicIntSlot> map, T t) {
        return map.compute(t, PublicIntSlot::remappingDecreaseExact).value;
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param delta added quantity
     * @param <T> counted item type
     */
    public static <T> int add(@NotNull Map<T, PublicIntSlot> map, T t, int delta) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(delta);
            map.put(t, slot);
        } else {
            slot.value += delta;
        }
        return slot.value;
    }

    public void addExact(int delta) {
        value = Math.addExact(value, delta);
    }

    public static <T> int addExact(@NotNull Map<T, PublicIntSlot> map, T t, int delta) {
        PublicIntSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicIntSlot(delta);
            map.put(t, slot);
        } else {
            slot.addExact(delta);
        }
        return slot.value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public int setInt(int newValue) {
        final int oldValue = value;
        value = newValue;
        return oldValue;
    }

    @Override
    public int compareTo(@NotNull PublicIntSlot that) {
        return Integer.compare(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((PublicIntSlot) obj).value;
    }

    @Override
    public String toString() {
        return "(" + value + ")";
    }
}
