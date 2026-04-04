package mujica.ds.of_char.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/3/15.
 */
@CodeHistory(date = "2026/3/15")
public class ArrayRepeatCharSequence extends TowboatCharSequence {

    private static final long serialVersionUID = 0xAF12376993E2D7E5L;

    @NotNull
    private final char[] array;

    private final int length;

    public ArrayRepeatCharSequence(@NotNull char[] array, int length) {
        super();
        if (array.length <= 0 || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.array = array;
        this.length = length;
    }

    public ArrayRepeatCharSequence(@NotNull char[] array) {
        this(array, array.length);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException();
        }
        return array[index % array.length];
    }
}
