package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/1")
public class UniversalStringifierUsingStringBuilder implements UniversalStringifier {

    @AccessStructure(online = false, local = true)
    @NotNull
    final StringBuilder sb = new StringBuilder();

    @NotNull
    final UniversalAppender appender;

    public UniversalStringifierUsingStringBuilder(@NotNull UniversalAppender appender) {
        super();
        this.appender = appender;
    }

    @NotNull
    @Override
    public String apply(@NotNull Object object) {
        try {
            appender.append(object, sb);
            return sb.toString();
        } finally {
            sb.delete(0, sb.length());
        }
    }
}
