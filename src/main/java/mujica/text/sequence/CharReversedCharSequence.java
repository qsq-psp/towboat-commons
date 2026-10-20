package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/9")
public class CharReversedCharSequence extends FilteredCharSequence {

    private static final long serialVersionUID = 0x4625bf04d4d94aa8L;

    public CharReversedCharSequence(@NotNull CharSequence original) {
        super(original);
    }

    @Override
    public char charAt(int index) {
        return original.charAt(original.length() - 1 - index);
    }
}
