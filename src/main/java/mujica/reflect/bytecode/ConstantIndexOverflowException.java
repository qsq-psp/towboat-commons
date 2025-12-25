package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/25")
public class ConstantIndexOverflowException extends BytecodeException {

    private static final long serialVersionUID = 0x25cfffe4c06ba3b2L;

    public ConstantIndexOverflowException() {
        super();
    }

    public ConstantIndexOverflowException(@NotNull String message) {
        super(message);
    }
}
