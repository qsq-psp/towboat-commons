package mujica.ds.i32.list;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/7/7.
 */
@FunctionalInterface
public interface CapacityPolicyProvider {

    @NotNull
    CapacityPolicy getCapacityPolicy();
}
