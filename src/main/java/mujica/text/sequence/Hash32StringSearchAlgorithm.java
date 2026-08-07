package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/28")
public class Hash32StringSearchAlgorithm implements StringSearchAlgorithm {

    final int multiplier;

    public Hash32StringSearchAlgorithm(int multiplier) {
        super();
        this.multiplier = multiplier;
    }

    @NotNull
    @Override
    public StringSearch bind(@NotNull CharSequence pattern) {
        return new Hash32StringSearch(pattern.toString(), multiplier);
    }
}
