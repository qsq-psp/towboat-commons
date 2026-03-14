package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/9.
 */
@CodeHistory(date = "2026/1/9")
public class CharReversedCharSequence extends FilteredCharSequence {

    public CharReversedCharSequence(@NotNull CharSequence original) {
        super(original);
    }

    @Override
    public char charAt(int index) {
        return original.charAt(original.length() - 1 - index);
    }
}
