package mujica.json.entity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on 2026/1/6.
 */
class UtilListAsJsonArray<T extends List<Object>> extends JsonArray {

    @NotNull
    protected T list;

    UtilListAsJsonArray(@NotNull T list) {
        super();
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Object getObject(int index) {
        return list.get(index);
    }

    @Override
    public void setObject(int index, Object value) {
        if (index == list.size()) {
            list.add(value);
        } else {
            list.set(index, value);
        }
    }
}
