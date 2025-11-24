package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/15")
public interface SortingAlgorithm<T> extends OrderingConstants {

    /**
     * A sorting algorithm is stable if equal elements always reserve order after sorting
     */
    @ReferencePage(title = "稳定性", href = "https://oi-wiki.org/basic/sort-intro/#%E7%A8%B3%E5%AE%9A%E6%80%A7")
    boolean stable();

    int COMPOSITION_ASCENDING = (1 << STRICT_ASCENDING) | (1 << EQUAL) | (1 << (STRICT_ASCENDING | EQUAL)) | (1 << NAN) | 1;

    int COMPOSITION_DESCENDING = (1 << STRICT_DESCENDING) | (1 << EQUAL) | (1 << (STRICT_DESCENDING | EQUAL)) | (1 << NAN) | 1;

    int orderingComposition();

    long apply(@NotNull T target, @Index(of = "target") int fromIndex, @Index(of = "target", inclusive = false) int toIndex);
}
