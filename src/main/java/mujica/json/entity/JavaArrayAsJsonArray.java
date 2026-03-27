package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;

@CodeHistory(date = "2025/12/30")
class JavaArrayAsJsonArray extends JsonArray {

    private static final long serialVersionUID = 0xE9C4F1369C378A07L;

    @NotNull
    private final Object array;

    JavaArrayAsJsonArray(@NotNull Object array) {
        super();
        this.array = array;
    }

    @Override
    public int size() {
        return Array.getLength(array);
    }

    @Override
    public Object getObject(int index) {
        return Array.get(array, index);
    }

    @Override
    public void setObject(int index, Object value) {
        Array.set(array, index, value);
    }
}
