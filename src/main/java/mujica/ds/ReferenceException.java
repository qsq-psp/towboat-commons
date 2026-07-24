package mujica.ds;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/6")
@Deprecated // use RuntimeException instead
public class ReferenceException extends RuntimeException {

    private static final long serialVersionUID = 0xa4e2392d3e058095L;

    public ReferenceException() {
        super();
    }

    public ReferenceException(@Nullable String message) {
        super(message);
    }
}
