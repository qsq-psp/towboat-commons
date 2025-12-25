package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@CodeHistory(date = "2025/11/18", name = "CompressAlgorithmException")
public class CompressAlgorithmException extends IOException {

    private static final long serialVersionUID = 0x68adfe7ffd01a660L;

    public CompressAlgorithmException() {
        super();
    }

    public CompressAlgorithmException(@NotNull String message) {
        super(message);
    }
}
