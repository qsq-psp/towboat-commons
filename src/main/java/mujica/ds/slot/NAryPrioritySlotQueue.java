package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/5/29", project = "Ultramarine", name = "BinaryPriorityHeap")
@CodeHistory(date = "2025/6/9", name = "NAryPriorityQueue")
@CodeHistory(date = "2026/8/6")
@Name(value = "多叉堆", language = "zh")
public class NAryPrioritySlotQueue<S, A> extends BinaryPrioritySlotQueue<S, A> {

    public NAryPrioritySlotQueue(@NotNull SlotArrayComparator.PolicySAC<S, A> comparator) {
        super(comparator);
    }
}
