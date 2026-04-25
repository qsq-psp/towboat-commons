package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

@CodeHistory(date = "2025/9/25")
public abstract class JsonContainer<K> implements Serializable {

    @NotNull
    protected ContainerConfig config;

    protected JsonContainer(@NotNull ContainerConfig config) {
        super();
        this.config = config;
    }

    @NotNull
    public ContainerConfig getConfig() {
        return config;
    }

    public void setConfig(@NotNull ContainerConfig config) {
        this.config = config;
    }

    @NotNull
    public abstract Collection<K> keyCollection();

    @NotNull
    public abstract Slot<Object> getSlot(@NotNull K key);
}
