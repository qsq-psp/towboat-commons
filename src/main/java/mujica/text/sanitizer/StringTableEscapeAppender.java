package mujica.text.sanitizer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/3/13")
public class StringTableEscapeAppender extends CharSequenceAppender {

    @NotNull
    protected final String[] table = new String[0x100];

    public StringTableEscapeAppender() {
        super();
    }
}
