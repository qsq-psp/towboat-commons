package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@CodeHistory(date = "2026/5/9")
public class JdkRegexStringSearchAlgorithm implements StringSearchAlgorithm {

    public static final JdkRegexStringSearchAlgorithm INSTANCE = new JdkRegexStringSearchAlgorithm();

    @NotNull
    @Override
    public StringSearch bind(@NotNull CharSequence pattern) {
        return new JdkRegexStringSearch(Pattern.compile(pattern.toString(), Pattern.LITERAL));
    }
}
