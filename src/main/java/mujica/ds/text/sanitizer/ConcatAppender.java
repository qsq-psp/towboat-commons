package mujica.ds.text.sanitizer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/30", name = "PatternConcatAppender")
@CodeHistory(date = "2026/6/1")
public class ConcatAppender extends ContainerAppender {

    public ConcatAppender(@NotNull CharSequenceAppender[] appenderArray) {
        super(appenderArray);
    }
}
