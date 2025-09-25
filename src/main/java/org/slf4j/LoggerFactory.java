package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2021/3/2", project = "webbiton")
@CodeHistory(date = "2025/8/1")
public final class LoggerFactory {

    @NotNull
    public static Logger getLogger(@NotNull String name) {
        return null;
    }

    @NotNull
    public static Logger getLogger(@NotNull Class<?> clazz) {
        return null;
    }
}
