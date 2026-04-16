package mujica.json.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created on 2026/4/3.
 */
public abstract class JsonFlat extends JsonContainer<JsonPath> {

    @NotNull
    @Override
    public abstract Collection<JsonPath> keyCollection();

    public abstract Object getObject(@NotNull JsonPath path);

    public abstract void setObject(@NotNull JsonPath path, Object value);
}
