package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/30")
public class JavaArrayAsJsonArray {

    private static final long serialVersionUID = 0xE9C4F1369C378A07L;

    @NotNull
    private final Object array;

    public JavaArrayAsJsonArray(@NotNull Object array) {
        super();
        this.array = array;
    }
}
