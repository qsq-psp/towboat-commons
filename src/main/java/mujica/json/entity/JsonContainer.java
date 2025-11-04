package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created on 2025/9/25.
 */
@CodeHistory(date = "2025/9/25")
public interface JsonContainer<K> {

    @NotNull
    Collection<K> keys();

    @NotNull
    Slot<Object> getSlot(@NotNull K key);
}
