package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2024/4/27", project = "coo", name = "MinHeap")
@CodeHistory(date = "2025/5/27", project = "Ultramarine", name = "BinaryPriorityHeap")
@CodeHistory(date = "2025/6/9", name = "BinaryPriorityQueue")
@CodeHistory(date = "2026/8/6")
@ReferencePage(title = "二叉堆", href = "https://oi-wiki.org/ds/binary-heap/")
@Name(value = "二叉堆", language = "zh")
public class BinaryPrioritySlotQueue<S, A> extends SortedSlotList<S, A> {

    public BinaryPrioritySlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator) {
        super(comparator);
    }
}
