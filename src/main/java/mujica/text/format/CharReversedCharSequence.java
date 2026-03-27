package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/9")
public class CharReversedCharSequence extends FilteredCharSequence {

    private static final long serialVersionUID = 0x4625BF04D4D94AA8L;

    public CharReversedCharSequence(@NotNull CharSequence original) {
        super(original);
    }

    @Override
    public char charAt(int index) {
        return original.charAt(original.length() - 1 - index);
    }
}
