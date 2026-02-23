package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/10/2")
class JsonObjectSlot implements Slot<Object>, Serializable {

    private static final long serialVersionUID = 0xec1ecf6f2a21b14fL;

    @NotNull
    private final JsonObject jsonObject;

    @NotNull
    final String name;

    JsonObjectSlot(@NotNull JsonObject jsonObject, @NotNull String name) {
        super();
        this.jsonObject = jsonObject;
        this.name = name;
    }

    @Override
    public Object get() {
        return jsonObject.getObject(name);
    }

    @Override
    public void set(@NotNull Object newValue) {
        jsonObject.setObject(name, newValue);
    }
}
