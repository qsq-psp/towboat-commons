package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2020/12/16", project = "webbiton")
@CodeHistory(date = "2021/9/19", project = "infrastructure")
@CodeHistory(date = "2021/6/24", project = "Ultramarine")
@CodeHistory(date = "2026/4/5")
public interface JsonStructure {
    
    void json(@NotNull JsonHandler jh);

    @CodeHistory(date = "2026/4/5")
    interface ExposedEntries {}
}
