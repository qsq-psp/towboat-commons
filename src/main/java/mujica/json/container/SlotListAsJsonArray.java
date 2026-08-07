package mujica.json.container;

import mujica.ds.slot.PlainObjectSlot;
import mujica.ds.slot.SlotCollection;
import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/8/6")
class SlotListAsJsonArray extends JsonArray {

    private static final long serialVersionUID = 0x230B3FC6045736E7L;

    @NotNull
    SlotCollection<PlainObjectSlot, Object[]> list;

    final PlainObjectSlot slot = new PlainObjectSlot();

    SlotListAsJsonArray(@NotNull SlotCollection<PlainObjectSlot, Object[]> list) {
        super();
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object getObject(int index) {
        list.getItemAt(index, slot);
        return slot.getObject();
    }

    @Override
    public void setObject(int index, Object value) {
        slot.setObject(value);
        list.setItemAt(index, slot);
    }

    @NotNull
    @Override
    public Consumer<Object> consumer(@NotNull ContainerConfig.ArrayAction action) {
        return null;
    }
}
