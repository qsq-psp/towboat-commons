package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@CodeHistory(date = "2026/3/14")
public class BatchIOException extends IOException {

    private static final long serialVersionUID = 0xafc160a839f35459L;

    public BatchIOException() {
        super();
    }

    public BatchIOException(@Nullable String message) {
        super(message);
    }
}
