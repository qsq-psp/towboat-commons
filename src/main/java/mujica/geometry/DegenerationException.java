package mujica.geometry;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2018/2/19", project = "aquarium", name = "GeometricException")
@CodeHistory(date = "2025/5/28")
public class DegenerationException extends RuntimeException {

    private static final long serialVersionUID = 0x07e1f9be707d445fL;

    public DegenerationException() {
        super();
    }

    public DegenerationException(@Nullable String message) {
        super(message);
    }
}
