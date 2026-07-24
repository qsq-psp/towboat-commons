package mujica.ds.i32;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2026/6/25")
public class SlotArrayAllocatorI32 implements SlotArrayAllocator<I32Slot, int[]> {

    public static final int[] EMPTY_ARRAY = new int[0];

    public SlotArrayAllocatorI32() {
        super();
    }

    @NotNull
    @Override
    public I32Slot newSlot() {
        return new I32();
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @NotNull
    @Override
    public I32Slot cloneSlot(@NotNull I32Slot original) {
        return new I32(original.getI32());
    }

    @Override
    public void exchange(@NotNull I32Slot a, @NotNull I32Slot b) {
        a.setI32(b.updateI32(a.getI32()));
    }

    @NotNull
    @Override
    public int[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new int[length];
    }

    @Override
    public int length(@NotNull int[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        slot.setI32(array[index]);
    }

    @Override
    public void store(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        array[index] = slot.getI32();
    }

    @Override
    public void exchange(@NotNull int[] array, int index, @NotNull I32Slot slot) {
        array[index] = slot.updateI32(array[index]);
    }

    @Override
    public void exchange(@NotNull int[] array, int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Override
    public int[] cloneArray(@NotNull int[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = i32, slotType = I32Slot, arrayType = int[]]";
    }

    @CodeHistory(date = "2026/7/16")
    public static class WithHashImpl extends SlotArrayAllocatorI32 implements WithHash<I32Slot, int[]> {

        public static final WithHashImpl TWICE = new WithHashImpl(TwiceCapacityPolicy.INSTANCE);

        @NotNull
        final CapacityPolicy capacityPolicy;

        public WithHashImpl(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull I32Slot left, @NotNull I32Slot right) {
            return left.getI32() == right.getI32();
        }

        @Override
        public boolean arrayEquals(@NotNull int[] leftArray, @NotNull int[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }

        @Override
        public int slotHash(@NotNull I32Slot slot) {
            return slot.getI32();
        }

        @Override
        public int arrayHash(@NotNull int[] array) {
            return Arrays.hashCode(array);
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = i32, slotType = I32Slot, arrayType = int[], capacityPolicy = " + capacityPolicy + "]";
        }
    }

    @CodeHistory(date = "2026/7/18")
    public static class WithNode2Impl extends SlotArrayAllocatorI32 implements WithNode2<I32Slot, I32.Node2> {

        public WithNode2Impl() {
            super();
        }

        @NotNull
        @Override
        public I32.Node2 newNode() {
            return new I32.Node2();
        }

        @Override
        public I32.Node2 getFirstLink(@NotNull I32.Node2 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I32.Node2 self, I32.Node2 node) {
            self.first = node;
        }

        @Override
        public I32.Node2 getSecondLink(@NotNull I32.Node2 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I32.Node2 self, I32.Node2 node) {
            self.second = node;
        }
    }

    @CodeHistory(date = "2026/7/19")
    public static class WithNode3Impl extends SlotArrayAllocatorI32 implements WithNode3<I32Slot, I32.Node3> {

        public WithNode3Impl() {
            super();
        }

        @NotNull
        @Override
        public I32.Node3 newNode() {
            return new I32.Node3();
        }

        @Override
        public I32.Node3 getFirstLink(@NotNull I32.Node3 self) {
            return self.first;
        }

        @Override
        public void setFirstLink(@NotNull I32.Node3 self, I32.Node3 node) {
            self.first = node;
        }

        @Override
        public I32.Node3 getSecondLink(@NotNull I32.Node3 self) {
            return self.second;
        }

        @Override
        public void setSecondLink(@NotNull I32.Node3 self, I32.Node3 node) {
            self.second = node;
        }

        @Override
        public I32.Node3 getThirdLink(@NotNull I32.Node3 self) {
            return self.third;
        }

        @Override
        public void setThirdLink(@NotNull I32.Node3 self, I32.Node3 node) {
            self.third = node;
        }
    }
}
