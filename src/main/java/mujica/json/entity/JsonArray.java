package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_int.list.CompatibleIntegerList;
import mujica.ds.of_int.list.NaturalIntList;
import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

@CodeHistory(date = "2025/12/29")
public abstract class JsonArray extends JsonContainer<Integer> {

    private static final long serialVersionUID = 0x0809917A2F8E2812L;

    @NotNull
    public static JsonArray newArrayList() {
        return new UtilListAsJsonArray<>(new ArrayList<>());
    }

    @NotNull
    public static JsonArray newLinkedList() {
        return new UtilListAsJsonArray<>(new LinkedList<>());
    }

    @NotNull
    public static JsonArray newTruncateList() {
        return new UtilListAsJsonArray<>(new TruncateList<>());
    }

    protected JsonArray(@NotNull ContainerConfig config) {
        super(config);
    }

    public abstract int size();

    public abstract Object getObject(int index);

    public abstract void setObject(int index, Object value);

    @NotNull
    public abstract Consumer<Object> consumer(@NotNull ContainerConfig.ArrayAction action);

    @NotNull
    @Override
    public Collection<Integer> keyCollection() {
        return new CompatibleIntegerList(new NaturalIntList(size()));
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull Integer key) {
        return new SlotImpl(key);
    }

    @CodeHistory(date = "2025/9/26")
    class SlotImpl implements Slot<Object>, Serializable {

        private static final long serialVersionUID = 0xb83ccb1367e96deeL;

        private final int index;

        SlotImpl(int index) {
            super();
            this.index = index;
        }

        @Override
        public Object get() {
            return getObject(index);
        }

        @Override
        public void set(@NotNull Object newValue) {
            setObject(index, newValue);
        }
    }
}
