package mujica.ds.slot;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.CapacityPolicyProvider;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

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
            S leftSlot = newSlot();
            S rightSlot = newSlot();
            for (int index = 0; index < minLength; index++) {
                load(left, index, leftSlot);
                load(right, index, rightSlot);
                int result = compareSlot(leftSlot, rightSlot);
                if (result != 0) {
                    return result;
                }
            }
        }
        return Integer.compare(leftLength, rightLength);
    }

    int FLAG_EQUAL = 1;
    int FLAG_ASCENDING = 1 << 1;
    int FLAG_DESCENDING = 1 << 2;

    default int compareAdjacent(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        if (endIndex - startIndex < 2) {
            return 0;
        }
        S previousSlot = newSlot();
        S currentSlot = newSlot();
        try {
            load(array, startIndex, previousSlot);
            int flags = 0;
            for (int index = startIndex + 1; index < endIndex; index++) {
                load(array, index, currentSlot);
                int result = compareSlot(previousSlot, currentSlot);
                if (result < 0) {
                    flags |= FLAG_ASCENDING;
                } else if (result > 0) {
                    flags |= FLAG_DESCENDING;
                } else {
                    flags |= FLAG_EQUAL;
                }
                if (flags == (FLAG_EQUAL | FLAG_ASCENDING | FLAG_DESCENDING)) {
                    break;
                }
                S tempSlot = previousSlot;
                previousSlot = currentSlot;
                currentSlot = tempSlot;
            }
            return flags;
        } finally {
            releaseSlot(previousSlot);
            releaseSlot(currentSlot);
        }
    }

    default int compareAdjacent(@NotNull A array) {
        return compareAdjacent(array, 0, length(array));
    }

    default boolean isAscending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return (compareAdjacent(array, startIndex, endIndex) & FLAG_DESCENDING) == 0;
    }

    default boolean isAscending(@NotNull A array) {
        return isAscending(array, 0, length(array));
    }

    default boolean isDescending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        return (compareAdjacent(array, startIndex, endIndex) & FLAG_ASCENDING) == 0;
    }

    default boolean isDescending(@NotNull A array) {
        return isDescending(array, 0, length(array));
    }

    default boolean isStrictAscending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        final int flags = compareAdjacent(array, startIndex, endIndex);
        return (flags & FLAG_ASCENDING) == flags;
    }

    default boolean isStrictAscending(@NotNull A array) {
        return isStrictAscending(array, 0, length(array));
    }

    default boolean isStrictDescending(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        final int flags = compareAdjacent(array, startIndex, endIndex);
        return (flags & FLAG_DESCENDING) == flags;
    }

    default boolean isStrictDescending(@NotNull A array) {
        return isStrictDescending(array, 0, length(array));
    }

    @CodeHistory(date = "2026/7/7")
    interface PolicySAC<S, A> extends SlotArrayComparator<S, A>, CapacityPolicyProvider {

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

    @CodeHistory(date = "2026/8/6")
    interface HashSAC<S, A> extends PolicySAC<S, A> {

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

        // no slotEquals(), use compareSlot() instead

        int slotHash(@NotNull S slot);

        default int arrayHash(@NotNull A array) {
            final int length = length(array);
            int result = 1;
            final S slot = newSlot();
            try {
                for (int index = 0; index < length; index++) {
                    load(array, index, slot);
                    result = 31 * result + slotHash(slot);
                }
            } finally {
                releaseSlot(slot);
            }
            return result;
        }
    }
}
