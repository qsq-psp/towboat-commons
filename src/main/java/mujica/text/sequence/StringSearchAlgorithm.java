package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/26")
@FunctionalInterface
@DirectSubclass({JdkStringSearchAlgorithm.class, JdkRegexStringSearchAlgorithm.class, Hash32StringSearchAlgorithm.class, Hash64StringSearchAlgorithm.class})
public interface StringSearchAlgorithm {

    @NotNull
    StringSearch bind(@NotNull CharSequence pattern);
}
