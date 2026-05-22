package mujica.ds.of_char.sequence;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Created on 2026/5/9.
 */
public class JdkRegexStringSearchAlgorithm implements StringSearchAlgorithm {

    public static final JdkRegexStringSearchAlgorithm INSTANCE = new JdkRegexStringSearchAlgorithm();

    @NotNull
    @Override
    public JdkRegexStringSearch bind(@NotNull CharSequence pattern) {
        return new JdkRegexStringSearch(Pattern.compile(pattern.toString(), Pattern.LITERAL));
    }
}
