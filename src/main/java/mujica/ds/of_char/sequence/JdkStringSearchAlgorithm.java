package mujica.ds.of_char.sequence;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/29.
 */
public class JdkStringSearchAlgorithm implements StringSearchAlgorithm {

    public static final JdkStringSearchAlgorithm INSTANCE = new JdkStringSearchAlgorithm();

    @NotNull
    @Override
    public JdkStringSearch bind(@NotNull CharSequence pattern) {
        return new JdkStringSearch(pattern.toString());
    }
}
