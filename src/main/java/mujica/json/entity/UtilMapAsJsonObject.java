package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "JsonObject")
@CodeHistory(date = "2025/9/24", name = "JsonObject")
@CodeHistory(date = "2025/12/30")
class UtilMapAsJsonObject<T extends Map<String, Object>> extends JsonObject {

    private static final long serialVersionUID = 0x2c7f0fa3ea0712a5L;

    @NotNull
    protected T map;

    UtilMapAsJsonObject(@NotNull T map) {
        super();
        this.map = map;
    }

    @NotNull
    @Override
    public Set<String> nameSet() {
        return map.keySet();
    }

    @Override
    public Object getObject(@NotNull String name) {
        return map.get(name);
    }

    @Override
    public void setObject(@NotNull String name, Object value) {
        map.put(name, value);
    }
}
