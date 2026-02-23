package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/28")
public class CharSequenceOperatorUsingStringBuilder implements CharSequenceOperator {

    @AccessStructure(online = false, local = true)
    @NotNull
    final StringBuilder sb = new StringBuilder();

    @NotNull
    final CharSequenceAppender appender;

    public CharSequenceOperatorUsingStringBuilder(@NotNull CharSequenceAppender appender) {
        super();
        this.appender = appender;
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string) {
        try {
            appender.append(string, sb);
            return sb.toString();
        } finally {
            sb.delete(0, sb.length());
        }
    }

    @NotNull
    @Override
    public String apply(@NotNull CharSequence string, int startIndex, int endIndex) {
        try {
            appender.append(string, startIndex, endIndex, sb);
            return sb.toString();
        } finally {
            sb.delete(0, sb.length());
        }
    }
}
