package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

/**
 * Created on 2025/9/24.
 */
@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/9/24")
public class JsonObject extends HashMap<String, Object> implements JsonContainer<String> {

    private static final long serialVersionUID = 0x2c7f0fa3ea0712a5L;

    @NotNull
    @Override
    public Set<String> keys() {
        return keySet();
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull String key) {
        return null;
    }
}
