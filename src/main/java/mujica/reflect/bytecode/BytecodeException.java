package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/9/24.
 */
@CodeHistory(date = "2025/9/24")
public class BytecodeException extends RuntimeException {

    private static final long serialVersionUID = 0xC866F0DAD19DE6D9L;

    public BytecodeException() {
        super();
    }

    public BytecodeException(@NotNull String message) {
        super(message);
    }
}
