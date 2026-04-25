package mujica.json.entity;

import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created on 2026/4/3.
 */
@CodeHistory(date = "2026/4/3")
@Deprecated
public abstract class JsonFlat extends JsonContainer<JsonPath> {

    protected JsonFlat(@NotNull ContainerConfig config) {
        super(config);
    }

    @NotNull
    @Override
    public abstract Collection<JsonPath> keyCollection();

    public abstract Object getObject(@NotNull JsonPath path);

    public abstract void setObject(@NotNull JsonPath path, Object value);
}
