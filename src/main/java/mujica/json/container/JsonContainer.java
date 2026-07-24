package mujica.json.container;

import mujica.ds.slot.TypeSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collection;

@CodeHistory(date = "2025/9/25")
public abstract class JsonContainer<K> implements Serializable {

    private static final long serialVersionUID = 0x6DFEFA8C7E0FE8ADL;

    protected JsonContainer() {
        super();
    }

    @NotNull
    public abstract Collection<K> keyCollection();

    @NotNull
    public abstract TypeSlot<Object> getSlot(@NotNull K key);
}
