package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/19.
 */
@CodeHistory(date = "2026/4/19")
@ReferencePage(title = "Algorithms Jeff Erickson", href = "http://jeffe.cs.illinois.edu/teaching/algorithms/")
@ReferencePage(title = "Algorithms Jeff Erickson", href = "http://algorithms.wtf")
public interface IntSelectAlgorithm {

    int select(@NotNull int[] array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int midIndex, @Index(of = "array") int endIndex);
}
