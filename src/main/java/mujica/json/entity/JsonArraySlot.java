package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2025/9/26")
class JsonArraySlot implements Slot<Object>, Serializable {

    private static final long serialVersionUID = 0xb83ccb1367e96deeL;

    @NotNull
    private final JsonArray jsonArray;

    @Index(of = "jsonArray")
    private final int index;

    JsonArraySlot(@NotNull JsonArray jsonArray, int index) {
        super();
        this.index = index;
        this.jsonArray = jsonArray;
    }

    @Override
    public Object get() {
        return jsonArray.getObject(index);
    }

    @Override
    public void set(@NotNull Object newValue) {
        jsonArray.setObject(index, newValue);
    }
}
