package mujica.ds.slot;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/27.
 */
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

    int compareSlot(@NotNull S a, @NotNull S b);

    default int compareArray(@NotNull A a, @NotNull A b) {
        final int an = length(a);
        final int bn = length(b);
        final int n = Math.min(an, bn);
        if (n > 0) {
            S as = newSlot();
            S bs = newSlot();
            for (int i = 0; i < n; i++) {
                load(a, i, as);
                load(b, i, bs);
                int r = compareSlot(as, bs);
                if (r != 0) {
                    return r;
                }
            }
        }
        return Integer.compare(an, bn);
    }
}
