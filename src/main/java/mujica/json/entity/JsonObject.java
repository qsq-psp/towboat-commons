package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

@CodeHistory(date = "2026/1/4")
public abstract class JsonObject extends JsonContainer<String> {

    private static final long serialVersionUID = 0x409C22961ED20306L;

    @NotNull
    public static JsonObject newHashMap() {
        return new UtilMapAsJsonObject<>(new HashMap<>());
    }

    @NotNull
    public static JsonObject newLinkedHashMap() {
        return new UtilMapAsJsonObject<>(new LinkedHashMap<>());
    }

    @NotNull
    public static JsonObject newTreeMap() {
        return new UtilMapAsJsonObject<>(new TreeMap<>());
    }

    protected JsonObject(@NotNull ContainerConfig config) {
        super(config);
    }

    @NotNull
    public abstract Set<String> nameSet();

    public abstract Object getObject(@NotNull String name);

    public abstract void setObject(@NotNull String name, Object value, @NotNull ContainerConfig.ObjectAction action);

    @NotNull
    @Override
    public Collection<String> keyCollection() {
        return nameSet();
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull String key) {
        return new JsonObjectSlot(key);
    }

    @CodeHistory(date = "2025/10/2")
    class JsonObjectSlot implements Slot<Object>, Serializable {

        private static final long serialVersionUID = 0xec1ecf6f2a21b14fL;

        @NotNull
        final String name;

        JsonObjectSlot(@NotNull String name) {
            super();
            this.name = name;
        }

        @Override
        public Object get() {
            return getObject(name);
        }

        @Override
        public void set(@NotNull Object newValue) {
            setObject(name, newValue, ContainerConfig.ObjectAction.PUT);
        }
    }
}
