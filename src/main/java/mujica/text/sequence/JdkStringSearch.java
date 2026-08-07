package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/29")
class JdkStringSearch implements StringSearch {

    @NotNull
    final String pattern;

    public JdkStringSearch(@NotNull String pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public int firstIndexOf(@NotNull CharSequence string) {
        return string.toString().indexOf(pattern);
    }
}
