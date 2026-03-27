package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/1/9.
 */
@CodeHistory(date = "2026/1/9")
public class CodePointReversedCharSequence extends FilteredCharSequence {

    private static final long serialVersionUID = 0x49570B66ADD19DFAL;

    public CodePointReversedCharSequence(@NotNull CharSequence original) {
        super(original);
    }

    @Override
    public char charAt(int index) {
        index = original.length() - 1 - index;
        char ch = original.charAt(index);
        if (Character.isSurrogate(ch)) {
            try {
                if (Character.isHighSurrogate(ch)) {
                    ch = original.charAt(index + 1); // no index check, let it throw
                    if (Character.isLowSurrogate(ch)) {
                        return ch;
                    } else {
                        return '\ufffd'; // replacement character
                    }
                } else {
                    ch = original.charAt(index - 1); // no index check, let it throw
                    if (Character.isHighSurrogate(ch)) {
                        return ch;
                    } else {
                        return '\ufffd'; // replacement character
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                return '\ufffd'; // replacement character
            }
        }
        return ch;
    }
}
