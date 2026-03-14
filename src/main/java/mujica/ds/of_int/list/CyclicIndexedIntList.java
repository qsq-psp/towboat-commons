package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/1/5")
public class CyclicIndexedIntList extends CenterAlignedIntList {

    public CyclicIndexedIntList(@Nullable ResizePolicy policy) {
        super(policy);
    }

    CyclicIndexedIntList(@NotNull ResizePolicy policy, @NotNull int[] array) {
        super(policy, array);
    }
}
