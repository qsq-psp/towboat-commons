package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/12")
public interface MinimumJsonConsumer {

    void openArray();

    void closeArray();

    void openObject();

    void closeObject();

    void stringKey(@NotNull String key);

    void jsonValue(@NotNull String value);
}
