package mujica.text.sanitizer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/8")
public class ChainAppender extends ContainerAppender {

    public ChainAppender(@NotNull CharSequenceAppender[] appenderArray) {
        super(appenderArray);
    }
}
