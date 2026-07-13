package mujica.ds.i32.list;

import mujica.ds.sort.SelectFirstAsPivot;
import mujica.ds.sort.SelectThreeMedianAsPivot;
import mujica.ds.sort.SelectTwoMeanAsPivot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2023/4/2", project = "Ultramarine", name = "PivotSelector")
@CodeHistory(date = "2025/3/15", name = "PivotSelector")
@CodeHistory(date = "2025/12/4")
@FunctionalInterface
@DirectSubclass({SelectFirstAsPivot.class, SelectTwoMeanAsPivot.class, SelectThreeMedianAsPivot.class})
public interface IntPivotSelector {

    int select(@NotNull int[] array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex);
}
