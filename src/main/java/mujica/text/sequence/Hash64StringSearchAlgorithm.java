package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/5/12")
public class Hash64StringSearchAlgorithm implements StringSearchAlgorithm {

    final long multiplier;

    public Hash64StringSearchAlgorithm(long multiplier) {
        super();
        this.multiplier = multiplier;
    }

    @NotNull
    @Override
    public StringSearch bind(@NotNull CharSequence pattern) {
        return new Hash64StringSearch(pattern.toString(), multiplier);
    }
}
