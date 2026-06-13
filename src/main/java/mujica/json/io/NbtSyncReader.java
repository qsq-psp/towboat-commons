package mujica.json.io;

import mujica.json.handler.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/1")
public interface NbtSyncReader extends Nbt {

    void read(@NotNull JsonHandler jh);
}
