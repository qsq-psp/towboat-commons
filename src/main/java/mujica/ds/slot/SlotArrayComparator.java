package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
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

    default int compareArray(@NotNull A left, @NotNull A right) {
        final int an = length(left);
        final int bn = length(right);
        final int n = Math.min(an, bn);
        if (n > 0) {
            S as = newSlot();
            S bs = newSlot();
            for (int i = 0; i < n; i++) {
                load(left, i, as);
                load(right, i, bs);
                int r = compareSlot(as, bs);
                if (r != 0) {
                    return r;
                }
            }
        }
        return Integer.compare(an, bn);
    }

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
}
