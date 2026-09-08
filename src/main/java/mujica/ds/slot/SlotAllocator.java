package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/6/14")
public interface SlotAllocator<S> extends Serializable {

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

    default void exchange(@NotNull S first, @NotNull S second) {
        final S third = newSlot();
        move(first, third);
        move(second, first);
        move(third, second);
    }
}
