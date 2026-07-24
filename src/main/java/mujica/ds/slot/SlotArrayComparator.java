package mujica.ds.slot;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.CapacityPolicyProvider;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@CodeHistory(date = "2025/6/14", name = "ComparableContext")
@CodeHistory(date = "2026/6/27")
public interface SlotArrayComparator<S, A> extends SlotArrayAllocator<S, A> {

    @NotNull
    @Override
    S newSlot();

    @Override
    void move(@NotNull S src, @NotNull S dst);

    @NotNull
    @Override
    A newArray(int length);

    @Override
    void load(@NotNull A array, int index, @NotNull S slot);

    @Override
    void store(@NotNull A array, int index, @NotNull S slot);

    int compareSlot(@NotNull S left, @NotNull S right);

    default void min(@NotNull S result) {
        throw new UnsupportedOperationException();
    }

    default void max(@NotNull S result) {
        throw new UnsupportedOperationException();
    }

    default void min(@NotNull S left, @NotNull S right, @NotNull S result) {
        if (compareSlot(left, right) <= 0) {
            move(left, result);
        } else {
            move(right, result);
        }
    }

    default void max(@NotNull S left, @NotNull S right, @NotNull S result) {
        if (compareSlot(left, right) >= 0) {
            move(left, result);
        } else {
            move(right, result);
        }
    }

    default void median(@NotNull S first, @NotNull S second, @NotNull S third, @NotNull S result) {
        if (compareSlot(first, second) <= 0) {
            if (compareSlot(second, third) <= 0) {
                move(second, result);
            } else if (compareSlot(third, first) <= 0) {
                move(first, result);
            } else {
                move(third, result);
            }
        } else {
            if (compareSlot(first, third) < 0) {
                move(first, result);
            } else if (compareSlot(third, second) < 0) {
                move(second, result);
            } else {
                move(third, result);
            }
        }
    }

    default int compareArray(@NotNull A left, @NotNull A right) {
        final int leftLength = length(left);
        final int rightLength = length(right);
        final int minLength = Math.min(leftLength, rightLength);
        if (minLength > 0) {
            S as = newSlot();
            S bs = newSlot();
            for (int index = 0; index < minLength; index++) {
                load(left, index, as);
                load(right, index, bs);
                int result = compareSlot(as, bs);
                if (result != 0) {
                    return result;
                }
            }
        }
        return Integer.compare(leftLength, rightLength);
    }

    default int compareAdjacent(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return 0; // todo
    }

    default int compareAdjacent(@NotNull A array) {
        return compareAdjacent(array, 0, length(array));
    }

    default boolean isAscending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return false;
    }

    default boolean isAscending(@NotNull A array) {
        return isAscending(array, 0, length(array));
    }

    default boolean isDescending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return false;
    }

    default boolean isDescending(@NotNull A array) {
        return isDescending(array, 0, length(array));
    }

    default boolean isStrictAscending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return false;
    }

    default boolean isStrictAscending(@NotNull A array) {
        return isStrictAscending(array, 0, length(array));
    }

    default boolean isStrictDescending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return false;
    }

    default boolean isStrictDescending(@NotNull A array) {
        return isStrictDescending(array, 0, length(array));
    }

    @CodeHistory(date = "2026/7/7")
    interface WithPolicy<S, A> extends SlotArrayComparator<S, A>, CapacityPolicyProvider {

        @NotNull
        @Override
        S newSlot();

        @Override
        void move(@NotNull S src, @NotNull S dst);

        @NotNull
        @Override
        A newArray(int length);

        @Override
        void load(@NotNull A array, int index, @NotNull S slot);

        @Override
        void store(@NotNull A array, int index, @NotNull S slot);

        @Override
        int compareSlot(@NotNull S left, @NotNull S right);

        @NotNull
        @Override
        CapacityPolicy getCapacityPolicy();
    }

    @CodeHistory(date = "2026/7/22")
    class OfComparable<T extends Comparable<T>> extends OfType<T> implements SlotArrayComparator<TypeSlot<T>, T[]> {

        public OfComparable(@NotNull Class<T> classT) {
            super(classT);
        }

        @Override
        public int compareSlot(@NotNull TypeSlot<T> left, @NotNull TypeSlot<T> right) {
            return left.get().compareTo(right.get());
        }
    }

    @CodeHistory(date = "2026/7/22")
    class OfComparator<T> extends OfType<T> implements SlotArrayComparator<TypeSlot<T>, T[]> {

        @NotNull
        final Comparator<T> comparator;

        public OfComparator(@NotNull Class<T> classT, @NotNull Comparator<T> comparator) {
            super(classT);
            this.comparator = comparator;
        }

        @Override
        public int compareSlot(@NotNull TypeSlot<T> left, @NotNull TypeSlot<T> right) {
            return comparator.compare(left.get(), right.get());
        }
    }
}
