package mujica.ds.i32.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/7")
@FunctionalInterface
public interface CapacityPolicyProvider {

    @NotNull
    CapacityPolicy getCapacityPolicy();
}
