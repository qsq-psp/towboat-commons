package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/4/15.
 */
@CodeHistory(date = "2025/4/15")
public interface CharSequenceConsumer extends CharConsumer {

    @Override
    void accept(char value);

    default void accept(@NotNull CharSequence string) {
        final int length = string.length();
        for (int index = 0; index < length; index++) {
            accept(string.charAt(index));
        }
    }

    default void accept(@NotNull char[] array, @Index(of = "array") int offset, int length) {
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
