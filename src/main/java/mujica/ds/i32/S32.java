package mujica.ds.i32;

import mujica.ds.bit.BitSlot;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.NoSuchElementException;

@CodeHistory(date = "2018/7/3", project = "existence", name = "MtInteger")
@CodeHistory(date = "2020/2/22", project = "coo", name = "IntegerModel")
@CodeHistory(date = "2022/7/2", project = "Ultramarine", name = "Counter")
@CodeHistory(date = "2024/1/20", project = "Ultramarine")
@CodeHistory(date = "2025/3/9", name = "PublicI32Slot")
@CodeHistory(date = "2026/6/30")
@FieldOrder("value") // only "value", not "int"
public class S32 extends Base2I32Slot implements Comparable<S32> {

    private static final long serialVersionUID = 0xf76e70d00282fbbdL;

    public S32() {
        super();
    }

    public S32(int value) {
        super(value);
    }

    @NotNull
    @Override
    public S32 clone() {
        return new S32(value);
    }

    @NotNull
    @Override
    public BitSlot signBit() {
        return new BitSlot() {

            @Override
            public boolean getBit() {
                return value < 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (value < 0) {
                    if (!newValue) {
                        if (value == Integer.MIN_VALUE) {
                            throw new ArithmeticException();
                        }
                        value = -value;
                    }
                } else {
                    if (newValue) {
                        value = -value;
                    }
                }
            }
        };
    }

    @NotNull
    @Override
    public ReadOnlyBase2Iterator readMagnitude(boolean msbFirst) {
        if (msbFirst) {
            return new ReadOnlyBase2Iterator() {

                int index = (value == Integer.MIN_VALUE) ? Integer.SIZE : Integer.SIZE - 1;

                @Override
                public boolean next() {
                    return index-- > 0;
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE || index == Integer.SIZE - 1 && value != Integer.MIN_VALUE) {
                        throw new NoSuchElementException();
                    }
                    int magnitude = value;
                    if (magnitude < 0) {
                        magnitude = -magnitude;
                    }
                    return (magnitude & (1 << index)) != 0;
                }
            };
        } else {
            return new ReadOnlyBase2Iterator() {

                int index = -1;

                @Override
                public boolean next() {
                    if (value == Integer.MIN_VALUE) {
                        return index++ < Integer.SIZE;
                    } else {
                        return index++ < Integer.SIZE - 1;
                    }
                }

                @Override
                public boolean getBit() {
                    if (index < 0  || index >= Integer.SIZE || index == Integer.SIZE - 1 && value != Integer.MIN_VALUE) {
                        throw new NoSuchElementException();
                    }
                    int magnitude = value;
                    if (magnitude < 0) {
                        magnitude = -magnitude;
                    }
                    return (magnitude & (1 << index)) != 0;
                }
            };
        }
    }

    @NotNull
    @Override
    public Base2Iterator magnitude() {
        return new Base2Iterator() {

            int index = -1;

            @Override
            public boolean next() {
                if (value == 0 || value == Integer.MIN_VALUE) {
                    return index++ < Integer.SIZE;
                } else {
                    return index++ < Integer.SIZE - 1;
                }
            }

            @Override
            public boolean getBit() {
                if (index < 0  || index >= Integer.SIZE || index == Integer.SIZE - 1 && value != 0 && value != Integer.MIN_VALUE) {
                    throw new NoSuchElementException();
                }
                int magnitude = value;
                if (magnitude < 0) {
                    magnitude = -magnitude;
                }
                return (magnitude & (1 << index)) != 0;
            }

            @Override
            public void setBit(boolean newValue) {
                if (index < 0  || index >= Integer.SIZE || index == Integer.SIZE - 1 && value != 0 && value != Integer.MIN_VALUE) {
                    throw new NoSuchElementException();
                }
                int magnitude = value;
                if (value < 0) {
                    magnitude = -magnitude;
                }
                if (newValue) {
                    magnitude |= 1 << index;
                } else {
                    magnitude &= ~(1 << index);
                }
                if (value < 0) {
                    magnitude = -magnitude;
                }
                value = magnitude;
            }
        };
    }

    @Override
    public int compareTo(@NotNull S32 that) {
        return Integer.compare(this.value, that.value);
    }

    @NotNull
    @Override
    public String toString() {
        return "(" + value + ")";
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param <T> counted item type
     */
    public static <T> int increase(@NotNull Map<T, S32> map, T t) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(1);
            map.put(t, slot);
        } else {
            slot.value++;
        }
        return slot.value;
    }

    public static <T> S32 remappingIncrease(T key, @Nullable S32 slot) {
        if (slot != null) {
            slot.value++;
        } else {
            slot = new S32(1);
        }
        return slot;
    }

    public static <T> int computeIncrease(@NotNull Map<T, S32> map, T t) {
        return map.compute(t, S32::remappingIncrease).value;
    }

    public void increaseExact() {
        value = Math.incrementExact(value);
    }

    public static <T> int increaseExact(@NotNull Map<T, S32> map, T t) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(1);
            map.put(t, slot);
        } else {
            slot.increaseExact();
        }
        return slot.value;
    }

    public static <T> S32 remappingIncreaseExact(T key, @Nullable S32 slot) {
        if (slot != null) {
            slot.increaseExact();
        } else {
            slot = new S32(1);
        }
        return slot;
    }

    public static <T> int computeIncreaseExact(@NotNull Map<T, S32> map, T t) {
        return map.compute(t, S32::remappingIncreaseExact).value;
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param <T> counted item type
     */
    public static <T> int decrease(@NotNull Map<T, S32> map, T t) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(-1);
            map.put(t, slot);
        } else {
            slot.value--;
        }
        return slot.value;
    }

    public static <T> S32 remappingDecrease(T key, @Nullable S32 slot) {
        if (slot != null) {
            slot.value--;
        } else {
            slot = new S32(-1);
        }
        return slot;
    }

    public static <T> int computeDecrease(@NotNull Map<T, S32> map, T t) {
        return map.compute(t, S32::remappingDecrease).value;
    }

    public void decreaseExact() {
        value = Math.decrementExact(value);
    }

    public static <T> int decreaseExact(@NotNull Map<T, S32> map, T t) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(-1);
            map.put(t, slot);
        } else {
            slot.decreaseExact();
        }
        return slot.value;
    }

    public static <T> S32 remappingDecreaseExact(T key, @Nullable S32 slot) {
        if (slot != null) {
            slot.decreaseExact();
        } else {
            slot = new S32(-1);
        }
        return slot;
    }

    public static <T> int computeDecreaseExact(@NotNull Map<T, S32> map, T t) {
        return map.compute(t, S32::remappingDecreaseExact).value;
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param delta added quantity
     * @param <T> counted item type
     */
    public static <T> int add(@NotNull Map<T, S32> map, T t, int delta) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(delta);
            map.put(t, slot);
        } else {
            slot.value += delta;
        }
        return slot.value;
    }

    public void addExact(int delta) {
        value = Math.addExact(value, delta);
    }

    public static <T> int addExact(@NotNull Map<T, S32> map, T t, int delta) {
        S32 slot = map.get(t);
        if (slot == null) {
            slot = new S32(delta);
            map.put(t, slot);
        } else {
            slot.addExact(delta);
        }
        return slot.value;
    }

    @CodeHistory(date = "2026/7/27")
    public static class S32Node1 extends S32 {

        private static final long serialVersionUID = 0x343DC7B058F2ACD3L;

        S32Node1 first;

        int tag;
    }

    @CodeHistory(date = "2026/8/1")
    public static class S32NodeAllocator1 implements NodeAllocator1<S32Node1> {

        private static final long serialVersionUID = 0x70DABD7C25E1B893L;

        public static final S32NodeAllocator1 INSTANCE = new S32NodeAllocator1();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public S32Node1 newNode() {
            return new S32Node1();
        }

        @Override
        public S32Node1[] newNodeArray(int length) {
            return new S32Node1[length];
        }

        @Override
        public int getTag(@NotNull S32Node1 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S32Node1 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S32Node1 getFirstLink(@NotNull S32Node1 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S32Node1 self, S32Node1 node) {
            self.first = node;
        }
    }

    @CodeHistory(date = "2026/7/27")
    public static class S32Node2 extends S32 {

        private static final long serialVersionUID = 0xBBB5F867CC6A6CCCL;

        S32Node2 first, second;

        int tag;
    }

    @CodeHistory(date = "2026/8/4")
    public static class S32NodeAllocator2 implements NodeAllocator2<S32Node2> {

        private static final long serialVersionUID = 0xFE752163463EDA84L;

        public static final S32NodeAllocator2 INSTANCE = new S32NodeAllocator2();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public S32Node2 newNode() {
            return new S32Node2();
        }

        @Override
        public S32Node2[] newNodeArray(int length) {
            return new S32Node2[length];
        }

        @Override
        public int getTag(@NotNull S32Node2 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S32Node2 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S32Node2 getFirstLink(@NotNull S32Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S32Node2 self, S32Node2 node) {
            self.first = node;
        }

        @Override
        public S32Node2 getSecondLink(@NotNull S32Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull S32Node2 self, S32Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/27")
    public static class S32Node3 extends S32 {

        private static final long serialVersionUID = 0xC98E2D2ED4E15143L;

        S32Node3 first, second, third;

        int tag;
    }

    @CodeHistory(date = "2026/8/4")
    public static class S32NodeAllocator3 implements NodeAllocator3<S32Node3> {

        private static final long serialVersionUID = 0xD49D5CAB2E0DCC51L;

        public static final S32NodeAllocator3 INSTANCE = new S32NodeAllocator3();

        @NotNull
        private Object readResolve() {
            return INSTANCE;
        }

        @NotNull
        @Override
        public S32Node3 newNode() {
            return new S32Node3();
        }

        @Override
        public S32Node3[] newNodeArray(int length) {
            return new S32Node3[length];
        }

        @Override
        public int getTag(@NotNull S32Node3 self) {
            return self.tag;
        }

        @Override
        public void setTag(@NotNull S32Node3 self, int newTag) {
            self.tag = newTag;
        }

        @Override
        public S32Node3 getFirstLink(@NotNull S32Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull S32Node3 self, S32Node3 node) {
            self.first = node;
        }

        @Override
        public S32Node3 getSecondLink(@NotNull S32Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull S32Node3 self, S32Node3 node) {
            self.second = node;
        }

        @Override
        public S32Node3 getThirdLink(@NotNull S32Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull S32Node3 self, S32Node3 node) {
            self.third = node;
        }
    }
}
