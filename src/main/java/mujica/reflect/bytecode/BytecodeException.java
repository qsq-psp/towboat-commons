package mujica.reflect.bytecode;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/9/24.
 */
public class BytecodeException extends RuntimeException {

    public BytecodeException() {
        super();
    }

    public BytecodeException(@NotNull String message) {
        super(message);
    }
}
