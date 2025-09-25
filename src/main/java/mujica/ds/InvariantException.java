package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/5/28")
public class InvariantException extends RuntimeException {

    private static final long serialVersionUID = 0xa7580d517a5c4bbbL;

    public InvariantException() {
        super();
    }

    public InvariantException(@Nullable String message) {
        super(message);
    }
}
