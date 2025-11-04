package mujica.io.function;

import mujica.ds.of_boolean.BitSequence;
import mujica.reflect.function.BooleanConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/**
 * Call accept(boolean) method sequentially, not necessarily consuming the {@code BitSequence} object
 */
@CodeHistory(date = "2025/4/10")
public interface BitSequenceConsumer extends BooleanConsumer {

    @Override
    void accept(boolean value);

    default void accept(@NotNull BitSequence sequence) {
        final int length = sequence.bitLength();
        for (int index = 0; index < length; index++) {
            accept(sequence.getBit(index));
        }
    }

    default void accept(@NotNull BooleanSupplier supplier, int count) {
        while (count-- > 0) {
            accept(supplier.getAsBoolean());
        }
    }

    default void accept(@NotNull boolean[] array, @Index(of = "array") int offset, int length) {
        if (offset < 0 || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int limit = offset + length;
        if (limit < 0 || limit > array.length) {
            throw new IndexOutOfBoundsException();
        }
        while (offset < limit) {
            accept(array[offset++]);
        }
    }
}
