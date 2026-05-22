package mujica.ds.of_char.sequence;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/29.
 */
public class JdkStringSearch implements StringSearch {

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
