package mujica.ds.slot;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/29")
public class StringSlotArrayAllocator implements SlotArrayComparator<StringSlot, String[]> {

    public static final StringSlotArrayAllocator INSTANCE = new StringSlotArrayAllocator();

    public static final String[] EMPTY_ARRAY = new String[0];

    public StringSlotArrayAllocator() {
        super();
    }

    @NotNull
    @Override
    public StringSlot newSlot() {
        return new StringSlot("");
    }

    @Override
    public void move(@NotNull StringSlot src, @NotNull StringSlot dst) {
        dst.setString(src.getString());
    }

    @NotNull
    @Override
    public StringSlot cloneSlot(@NotNull StringSlot original) {
        return new StringSlot(original.getString());
    }

    @NotNull
    @Override
    public String[] newArray(int length) {
        if (length == 0) {
            return EMPTY_ARRAY;
        }
        return new String[length];
    }

    @Override
    public int length(@NotNull String[] array) {
        return array.length;
    }

    @Override
    public void load(@NotNull String[] array, int index, @NotNull StringSlot slot) {
        slot.setString(array[index]);
    }

    @Override
    public void store(@NotNull String[] array, int index, @NotNull StringSlot slot) {
        array[index] = slot.getString();
    }

    @Override
    public String[] cloneArray(@NotNull String[] original) {
        if (original.length == 0) {
            return EMPTY_ARRAY;
        }
        return original.clone();
    }

    @Override
    public int compareSlot(@NotNull StringSlot left, @NotNull StringSlot right) {
        return left.getString().compareTo(right.getString());
    }

    @Override
    @NotNull
    public String toString() {
        return "SlotTool[dataType = String, slotType = StringSlot, arrayType = String[]]";
    }

    @CodeHistory(date = "2026/7/29")
    public static class StringWithHash extends StringSlotArrayAllocator implements HashSAA<StringSlot, String[]> {

        @NotNull
        final CapacityPolicy capacityPolicy;

        public StringWithHash(@NotNull CapacityPolicy capacityPolicy) {
            super();
            this.capacityPolicy = capacityPolicy;
        }

        @NotNull
        @Override
        public CapacityPolicy getCapacityPolicy() {
            return capacityPolicy;
        }

        @Override
        public boolean slotEquals(@NotNull StringSlot left, @NotNull StringSlot right) {
            return left.getString().equals(right.getString());
        }

        @Override
        public int slotHash(@NotNull StringSlot slot) {
            return slot.getString().hashCode();
        }

        @Override
        @NotNull
        public String toString() {
            return "SlotTool[dataType = String, slotType = StringSlot, arrayType = String[], capacityPolicy = " + capacityPolicy + "]";
        }
    }
}
