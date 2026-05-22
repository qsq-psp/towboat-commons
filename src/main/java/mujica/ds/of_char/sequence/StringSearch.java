package mujica.ds.of_char.sequence;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/26.
 */
@CodeHistory(date = "2026/4/26")
@FunctionalInterface
public interface StringSearch {

    int firstIndexOf(@NotNull CharSequence string);
}
