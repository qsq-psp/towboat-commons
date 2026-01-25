package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * Created on 2026/1/4.
 */
@CodeHistory(date = "2026/1/4")
public abstract class JsonObject implements JsonContainer<String> {

    @NotNull
    public abstract Set<String> nameSet();

    public abstract Object getObject(@NotNull String name);

    public abstract void setObject(@NotNull String name, Object value);

    @NotNull
    @Override
    public Collection<String> keyCollection() {
        return nameSet();
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull String key) {
        return new JsonObjectSlot(this, key);
    }
}
