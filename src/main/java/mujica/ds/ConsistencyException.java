package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/5/28")
public class ConsistencyException extends RuntimeException {

    private static final long serialVersionUID = 0x274924e04274b2b7L;

    public ConsistencyException() {
        super();
    }

    public ConsistencyException(@Nullable String message) {
        super(message);
    }

    public ConsistencyException(@NotNull String name, int expected, int actual) {
        this(name + " mismatch; expected = " + expected + ", actual = " + actual);
    }
}
