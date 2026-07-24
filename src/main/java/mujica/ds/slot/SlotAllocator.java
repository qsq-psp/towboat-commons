package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/14")
public interface SlotAllocator<S> {

    @NotNull
    S newSlot();

    default void releaseSlot(@NotNull S slot) {
        // pass
    }

    void move(@NotNull S src, @NotNull S dst);

    @NotNull
    default S cloneSlot(@NotNull S original) {
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

    @CodeHistory(date = "2026/7/18")
    interface WithNode2<S, N extends S> extends SlotAllocator<S>, NodeAccess2<N> {}

    @CodeHistory(date = "2026/7/19")
    interface WithNode3<S, N extends S> extends SlotAllocator<S>, NodeAccess3<N> {}
}
