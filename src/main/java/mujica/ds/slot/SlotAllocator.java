package mujica.ds.slot;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/14.
 */
public interface SlotAllocator<S> {

    @NotNull
    S newSlot();

    void move(@NotNull S src, @NotNull S dst);

    @NotNull
    default S clone(@NotNull S original) {
        final S copy = newSlot();
        move(original, copy);
        return copy;
    }

    default void exchange(@NotNull S a, @NotNull S b) {
        final S temp = newSlot();
        move(a, temp);
        move(b, a);
        move(temp, b);
    }
}
