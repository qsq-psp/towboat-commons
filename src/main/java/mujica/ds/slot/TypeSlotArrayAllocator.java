package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Comparator;

@CodeHistory(date = "2026/7/22")
public class TypeSlotArrayAllocator<T> implements SlotArrayAllocator<TypeSlot<T>, T[]> {

    @NotNull
    final Class<T> classT;

    public TypeSlotArrayAllocator(@NotNull Class<T> classT) {
        super();
        this.classT = classT;
    }

    @NotNull
    @Override
    public TypeSlot<T> newSlot() {
        return new TypeSlot<>();
    }

    @Override
    public void move(@NotNull TypeSlot<T> src, @NotNull TypeSlot<T> dst) {
        dst.set(src.get());
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public T[] newArray(int length) {
        return (T[]) Array.newInstance(classT, length);
    }

    @Override
    public void load(@NotNull T[] array, int index, @NotNull TypeSlot<T> slot) {
        slot.set(array[index]);
    }

    @Override
    public void store(@NotNull T[] array, int index, @NotNull TypeSlot<T> slot) {
        array[index] = slot.get();
    }

    @CodeHistory(date = "2026/7/22")
    public static class WithComparable<T extends Comparable<T>> extends TypeSlotArrayAllocator<T> implements SlotArrayComparator<TypeSlot<T>, T[]> {

        public WithComparable(@NotNull Class<T> classT) {
            super(classT);
        }

        @Override
        public int compareSlot(@NotNull TypeSlot<T> left, @NotNull TypeSlot<T> right) {
            return left.get().compareTo(right.get());
        }
    }

    @CodeHistory(date = "2026/7/22")
    public static class WithComparator<T> extends TypeSlotArrayAllocator<T> implements SlotArrayComparator<TypeSlot<T>, T[]> {

        @NotNull
        final Comparator<T> comparator;

        public WithComparator(@NotNull Class<T> classT, @NotNull Comparator<T> comparator) {
            super(classT);
            this.comparator = comparator;
        }

        @Override
        public int compareSlot(@NotNull TypeSlot<T> left, @NotNull TypeSlot<T> right) {
            return comparator.compare(left.get(), right.get());
        }
    }
}
