package mujica.math.geometry;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Stable;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/5/28")
@Stable(date = "2025/8/4")
public class DegenerationException extends RuntimeException {

    private static final long serialVersionUID = 0x07e1f9be707d445fL;

    public DegenerationException() {
        super();
    }

    public DegenerationException(@Nullable String message) {
        super(message);
    }
}
