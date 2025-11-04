package mujica.json.io;

import mujica.json.entity.JsonConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/10/27.
 */
@CodeHistory(date = "2022/1/9", project = "infrastructure", name = "SimpleReader")
@CodeHistory(date = "2022/6/5", project = "Ultramarine", name = "Reader")
@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "SyncReader")
@CodeHistory(date = "2025/10/27")
public interface JsonSyncReader extends JsonReader {

    @Override
    void config(int config);

    void read(@NotNull JsonConsumer jc);
}
