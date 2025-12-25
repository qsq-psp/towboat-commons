package mujica.json.entity;

import mujica.ds.generic.Slot;
import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_int.list.CompatibleIntegerList;
import mujica.ds.of_int.list.NaturalIntList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@CodeHistory(date = "2022/6/4", project = "Ultramarine")
@CodeHistory(date = "2025/9/23")
public class JsonArray extends TruncateList<Object> implements JsonContainer<Integer> {

    private static final long serialVersionUID = 0x326e01ada4ba3c92L;

    @NotNull
    @Override
    public List<Integer> keys() {
        return new CompatibleIntegerList(new NaturalIntList(size()));
    }

    @CodeHistory(date = "2025/9/26")
    private class IndexedSlot implements Slot<Object>, Serializable {

        private static final long serialVersionUID = 0xb83ccb1367e96deeL;

        final int index;

        private IndexedSlot(int index) {
            super();
            this.index = index;
        }

        @Override
        public Object get() {
            return JsonArray.this.get(index);
        }

        @Override
        public Object set(@NotNull Object newValue) {
            return JsonArray.this.set(index, newValue);
        }
    }

    @NotNull
    @Override
    public Slot<Object> getSlot(@NotNull Integer key) {
        return new IndexedSlot(key);
    }
}
