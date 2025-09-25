package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.ds.generic.list.TruncateList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on 2025/9/23.
 */
@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/9/23")
public class JsonArray extends TruncateList<Object> implements JsonContainer<Integer> {

    private static final long serialVersionUID = 0x326e01ada4ba3c92L;

    @NotNull
    @Override
    public List<Integer> keys() {
        return null;
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull Integer key) {
        return null;
    }
}
