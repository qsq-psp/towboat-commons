package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/29")
public class JdkStringSearchAlgorithm implements StringSearchAlgorithm {

    public static final JdkStringSearchAlgorithm INSTANCE = new JdkStringSearchAlgorithm();

    @NotNull
    @Override
    public StringSearch bind(@NotNull CharSequence pattern) {
        return new JdkStringSearch(pattern.toString());
    }
}
