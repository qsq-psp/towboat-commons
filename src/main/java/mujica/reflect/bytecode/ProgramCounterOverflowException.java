package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/26")
public class ProgramCounterOverflowException extends BytecodeException {

    private static final long serialVersionUID = 0x249CDFD42D45C658L;

    public ProgramCounterOverflowException() {
        super();
    }

    public ProgramCounterOverflowException(@NotNull String message) {
        super(message);
    }
}
