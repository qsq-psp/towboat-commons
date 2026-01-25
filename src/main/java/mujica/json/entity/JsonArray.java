package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.ds.of_int.list.CompatibleIntegerList;
import mujica.ds.of_int.list.NaturalIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@CodeHistory(date = "2025/12/29")
public abstract class JsonArray implements JsonContainer<Integer> {

    public abstract int size();

    public abstract Object getObject(int index);

    public abstract void setObject(int index, Object value);

    @NotNull
    @Override
    public Collection<Integer> keyCollection() {
        return new CompatibleIntegerList(new NaturalIntList(size()));
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull Integer key) {
        return new JsonArraySlot(this, key);
    }
}
