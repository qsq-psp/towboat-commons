package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "JsonArray")
@CodeHistory(date = "2025/9/23", name = "JsonArray")
@CodeHistory(date = "2025/12/29", name = "GeneralJsonArray")
@CodeHistory(date = "2026/1/6")
class UtilListAsJsonArray<T extends List<Object>> extends JsonArray {

    private static final long serialVersionUID = 0x326e01ada4ba3c92L;

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
