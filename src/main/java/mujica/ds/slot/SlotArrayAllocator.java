package mujica.ds.slot;

import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

/**
 * Created on 2026/6/25.
 */
public interface SlotArrayAllocator<S, A> extends SlotAllocator<S> {

    @NotNull
    @Override
    S newSlot();

    @Override
    void move(@NotNull S src, @NotNull S dst);

    @NotNull
    A newArray(int length);

    default int length(@NotNull A array) {
        return Array.getLength(array);
    }

    void load(@NotNull A array, @Index(of = "array") int index, @NotNull S slot);

    void store(@NotNull A array, @Index(of = "array") int index, @NotNull S slot);

    default void exchange(@NotNull A array, @Index(of = "array") int index, @NotNull S slot) {
        final S temp = newSlot();
        load(array, index, temp);
        exchange(temp, slot);
        store(array, index, temp);
    }

    default void exchange(@NotNull A array, @Index(of = "array") int i, @Index(of = "array") int j) {
        final S temp = newSlot();
        load(array, i, temp);
        exchange(array, j, temp);
        store(array, i, temp);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    default void copy(@NotNull A src, @Index(of = "src") int srcPosition, @NotNull A dst, @Index(of = "dst") int dstPosition, int length) {
        System.arraycopy(src, srcPosition, dst, dstPosition, length);
    }
}
