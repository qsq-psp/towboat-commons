package mujica.ds.i32.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/1/5")
@Deprecated
public class CyclicIndexedIntList extends CenterAlignedIntList {

    public CyclicIndexedIntList(@Nullable CapacityPolicy policy) {
        super(policy);
    }

    CyclicIndexedIntList(@NotNull CapacityPolicy policy, @NotNull int[] array) {
        super(policy, array);
    }
}
