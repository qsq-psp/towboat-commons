package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/9/24")
public class JsonObject extends HashMap<String, Object> implements JsonContainer<String> {

    private static final long serialVersionUID = 0x2c7f0fa3ea0712a5L;

    @NotNull
    @Override
    public Set<String> keys() {
        return keySet();
    }

    @CodeHistory(date = "2025/10/2")
    private class NamedSlot implements Slot<Object>, Serializable {

        private static final long serialVersionUID = 0xec1ecf6f2a21b14fL;

        @NotNull
        final String name;

        private NamedSlot(@NotNull String name) {
            super();
            this.name = name;
        }

        @Override
        public Object get() {
            return JsonObject.this.get(name);
        }

        @Override
        public Object set(@NotNull Object newValue) {
            return put(name, newValue);
        }
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull String key) {
        return new NamedSlot(key);
    }
}
