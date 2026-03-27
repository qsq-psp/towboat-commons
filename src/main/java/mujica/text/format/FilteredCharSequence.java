package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/8")
public abstract class FilteredCharSequence extends TowboatCharSequence {

    private static final long serialVersionUID = 0x0F6535B784955850L;

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
        if (startIndex == endIndex) {
            return EmptyCharSequence.INSTANCE;
        }
        return new SlicedCharSequence(this, startIndex, endIndex);
    }
}
