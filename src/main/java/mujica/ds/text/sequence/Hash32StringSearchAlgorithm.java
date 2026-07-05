package mujica.ds.text.sequence;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/28.
 */
public class Hash32StringSearchAlgorithm implements StringSearchAlgorithm {

    final int multiplier;

    public Hash32StringSearchAlgorithm(int multiplier) {
        super();
        this.multiplier = multiplier;
    }

    @NotNull
    @Override
    public Hash32StringSearch bind(@NotNull CharSequence pattern) {
        return new Hash32StringSearch(pattern.toString(), multiplier);
    }
}
