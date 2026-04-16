package mujica.json.io;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/1.
 */
@CodeHistory(date = "2026/4/1")
public interface JsonSyncSkipReader extends JsonSyncReader {

    @Override
    void setFlags(int flags);

    @Override
    int getFlags();

    void skip();

    void read(@NotNull JsonHandler jh);
}
