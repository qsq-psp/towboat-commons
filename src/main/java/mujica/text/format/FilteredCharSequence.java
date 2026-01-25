package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/8.
 */
@CodeHistory(date = "2026/1/8")
public abstract class FilteredCharSequence implements CharSequence {

    @NotNull
    protected final CharSequence original;

    public FilteredCharSequence(@NotNull CharSequence original) {
        super();
        this.original = original;
    }

    @Override
    public int length() {
        return original.length();
    }

    @Override
    public char charAt(int index) {
        return original.charAt(index);
    }

    @Override
    public CharSequence subSequence(@Index(of = "this") int startIndex, @Index(of = "this", inclusive = false) int endIndex) {
        return new SlicedCharSequence(this, startIndex, endIndex);
    }

    @NotNull
    @Override
    public String toString() {
        final int length = length();
        final char[] chars = new char[length];
        for (int index = 0; index < length; index++) {
            chars[index] = charAt(index);
        }
        return new String(chars);
    }
}
