package mujica.ds.of_char.sequence;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/12.
 */
public class Hash64StringSearchAlgorithm implements StringSearchAlgorithm {

    final long multiplier;

    public Hash64StringSearchAlgorithm(long multiplier) {
        super();
        this.multiplier = multiplier;
    }

    @NotNull
    @Override
    public Hash64StringSearch bind(@NotNull CharSequence pattern) {
        return new Hash64StringSearch(pattern.toString(), multiplier);
    }
}
