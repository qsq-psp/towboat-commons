package mujica.ds.sort;

import mujica.ds.slot.SlotArrayComparator;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/2/10", name = "ListSelectionSort")
@CodeHistory(date = "2026/7/22")
@Name(value = "选择排序", language = "zh")
@ReferencePage(title = "选择排序", href = "https://oi-wiki.org/basic/selection-sort/")
public class SelectionSort<S, A> implements Sort<A> {

    @NotNull
    final SlotArrayComparator<S, A> comparator;

    public SelectionSort(@NotNull SlotArrayComparator<S, A> comparator) {
        super();
        this.comparator = comparator;
    }

    @Override
    public long sort(@NotNull A target, int startIndex, int endIndex) {
        return 0;
    }
}
