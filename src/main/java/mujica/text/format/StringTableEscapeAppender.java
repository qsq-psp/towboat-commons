package mujica.text.format;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/3/13.
 */
public class StringTableEscapeAppender extends CharSequenceAppender {

    @NotNull
    protected final String[] table = new String[0x100];

    public StringTableEscapeAppender() {
        super();
    }
}
