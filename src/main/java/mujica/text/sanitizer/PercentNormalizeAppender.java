package mujica.text.sanitizer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;

@CodeHistory(date = "2026/3/8")
public class PercentNormalizeAppender extends PercentEncodeAppender {

    public PercentNormalizeAppender(@NotNull IntPredicate noEscape) {
        super(noEscape);
    }
}
