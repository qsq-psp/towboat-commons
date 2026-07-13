package mujica.ds.sort;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/5/14", name = "PivotSelector")
@CodeHistory(date = "2026/7/11")
public interface SelectPivot<S, A> {

    void selectPivot(@NotNull A target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int endIndex, @NotNull S out);
}
