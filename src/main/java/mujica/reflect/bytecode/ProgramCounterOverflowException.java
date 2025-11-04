package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/10/26.
 */
@CodeHistory(date = "2025/10/26")
public class ProgramCounterOverflowException extends BytecodeException {

    public ProgramCounterOverflowException() {
        super();
    }

    public ProgramCounterOverflowException(@NotNull String message) {
        super(message);
    }
}
