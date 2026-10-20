package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntSupplier;

@CodeHistory(date = "2026/10/15")
@FunctionalInterface
public interface DistributedSizeSupplierProvider {

    @NotNull
    IntSupplier create(int left, int right);
}
