package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/9/24")
public class BytecodeException extends RuntimeException {

    private static final long serialVersionUID = 0xc866f0dad19de6d9L;

    public BytecodeException() {
        super();
    }

    public BytecodeException(@NotNull String message) {
        super(message);
    }
}
