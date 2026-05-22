package mujica.ds.generic.list;

import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/14.
 */
public interface PivotSelector<T> {

    void sortPart(@NotNull T target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int endIndex, @Index(of = "target", inclusive = false) int outIndex);
}
