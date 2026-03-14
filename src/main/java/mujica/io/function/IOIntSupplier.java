package mujica.io.function;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;

/**
 * Created on 2026/2/25.
 */
@CodeHistory(date = "2026/2/25")
@FunctionalInterface
public interface IOIntSupplier {

    int getAsInt() throws IOException;
}
