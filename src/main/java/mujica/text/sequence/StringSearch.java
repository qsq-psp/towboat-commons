package mujica.text.sequence;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/26")
@FunctionalInterface
@DirectSubclass({JdkStringSearch.class, JdkRegexStringSearch.class, Hash32StringSearch.class, Hash64StringSearch.class})
public interface StringSearch {

    int firstIndexOf(@NotNull CharSequence string);
}
