package mujica.ds.text.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2026/5/9.
 */
public class JdkRegexStringSearch implements StringSearch {

    @NotNull
    final Pattern pattern;

    public JdkRegexStringSearch(@NotNull Pattern pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public int firstIndexOf(@NotNull CharSequence string) {
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.start();
        } else {
            return -1;
        }
    }
}
