package mujica.text.format;

import mujica.reflect.modifier.AccessStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/2")
public class UniversalStringifierUsingStringBuffer implements UniversalStringifier {

    @AccessStructure(online = false, local = true)
    @NotNull
    final StringBuffer sb = new StringBuffer();

    @NotNull
    final UniversalAppender appender;

    public UniversalStringifierUsingStringBuffer(@NotNull UniversalAppender appender) {
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
