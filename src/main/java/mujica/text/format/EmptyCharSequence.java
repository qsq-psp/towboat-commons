package mujica.text.format;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/3/7.
 */
public class EmptyCharSequence implements CharSequence {

    public static final EmptyCharSequence INSTANCE = new EmptyCharSequence();

    public EmptyCharSequence() {
        super();
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    @NotNull
    public EmptyCharSequence subSequence(int startIndex, int endIndex) {
        if (startIndex == 0 && endIndex == 0) {
            return this;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    @NotNull
    public String toString() {
        return "";
    }
}
