package mujica.ds.slot;

import mujica.ds.i32.list.CapacityPolicy;
import mujica.ds.i32.list.CapacityPolicyProvider;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

@CodeHistory(date = "2025/6/13", name = "OperationContext")
@CodeHistory(date = "2026/6/25")
public interface SlotArrayAllocator<S, A> extends SlotAllocator<S> {

    @NotNull
    @Override
    S newSlot();

    @Override
    void move(@NotNull S src, @NotNull S dst);

    @NotNull
    A newArray(int length);

    default void releaseArray(@NotNull A array) {
        // pass
    }

    default int length(@NotNull A array) {
        return Array.getLength(array);
    }

    void load(@NotNull A array, @Index(of = "array") int index, @NotNull S slot);

    void store(@NotNull A array, @Index(of = "array") int index, @NotNull S slot);

    default void releaseReference(@NotNull A array, @Index(of = "array") int index) {
        // pass
    }

    default void releaseReference(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        // pass
    }

    default void releaseReference(@NotNull A array) {
        // pass
    }

    default void exchange(@NotNull A array, @Index(of = "array") int index, @NotNull S slot) {
        final S tempSlot = newSlot();
        try {
            load(array, index, tempSlot);
            exchange(tempSlot, slot);
            store(array, index, tempSlot);
        } finally {
            releaseSlot(tempSlot);
        }
    }

    default void exchange(@NotNull A array, @Index(of = "array") int firstIndex, @Index(of = "array") int secondIndex) {
        final S tempSlot = newSlot();
        try {
            load(array, firstIndex, tempSlot);
            exchange(array, secondIndex, tempSlot);
            store(array, firstIndex, tempSlot);
        } finally {
            releaseSlot(tempSlot);
        }
    }

    default void reverse(@NotNull A array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        final S startSlot = newSlot();
        final S endSlot = newSlot();
        try {
            while (true) {
                endIndex--;
                if (startIndex >= endIndex) {
                    break;
                }
                load(array, startIndex, startSlot);
                load(array, endIndex, endSlot);
                store(array, endIndex, startSlot);
                store(array, startIndex, endSlot);
                startIndex++;
            }
        } finally {
            releaseSlot(startSlot);
            releaseSlot(endSlot);
        }
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    default void copySelf(@NotNull A array, @Index(of = "array") int srcPosition, @Index(of = "array") int dstPosition, int length) {
        System.arraycopy(array, srcPosition, array, dstPosition, length);
    }

    @SuppressWarnings("SuspiciousSystemArraycopy")
    default void copy(@NotNull A src, @Index(of = "src") int srcPosition, @NotNull A dst, @Index(of = "dst") int dstPosition, int length) {
        System.arraycopy(src, srcPosition, dst, dstPosition, length);
    }

    default A cloneArray(@NotNull A oldArray) {
        final int length = length(oldArray);
        final A newArray = newArray(length);
        copy(oldArray, 0, newArray, 0, length);
        return newArray;
    }

    @CodeHistory(date = "2026/7/7")
    interface WithPolicy<S, A> extends SlotArrayAllocator<S, A>, CapacityPolicyProvider {

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

        @NotNull
        @Override
        CapacityPolicy getCapacityPolicy();
    }

    @CodeHistory(date = "2026/7/21")
    interface WithHash<S, A> extends WithPolicy<S, A> {

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

        @NotNull
        @Override
        CapacityPolicy getCapacityPolicy();

        boolean slotEquals(@NotNull S left, @NotNull S right);

        default boolean arrayEquals(@NotNull A leftArray, @NotNull A rightArray) {
            final int length = length(leftArray);
            if (length != length(rightArray)) {
                return false;
            }
            final S leftSlot = newSlot();
            final S rightSlot = newSlot();
            try {
                for (int index = 0; index < length; index++) {
                    load(leftArray, index, leftSlot);
                    load(rightArray, index, rightSlot);
                    if (!slotEquals(leftSlot, rightSlot)) {
                        return false;
                    }
                }
            } finally {
                releaseSlot(leftSlot);
                releaseSlot(rightSlot);
            }
            return true;
        }

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

    @CodeHistory(date = "2026/7/22")
    class OfType<T> implements SlotArrayAllocator<TypeSlot<T>, T[]> {

        @NotNull
        final Class<T> classT;

        public OfType(@NotNull Class<T> classT) {
            super();
            this.classT = classT;
        }

        @NotNull
        @Override
        public TypeSlot<T> newSlot() {
            return new TypeSlot.Generic<>();
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
    }
}
