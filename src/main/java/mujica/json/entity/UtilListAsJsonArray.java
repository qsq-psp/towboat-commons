package mujica.json.entity;

import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

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
        list.set(index, value);
    }

    @NotNull
    @Override
    public Consumer<Object> consumer(@NotNull ContainerConfig.ArrayAction action) {
        switch (action) {
            case NEW:
            case CLEAR:
                list.clear();
                // no break here
            case COVER:
                return new ConsumerImpl(0);
            case APPEND:
                return new ConsumerImpl(size());
            default:
                throw new IllegalArgumentException();
        }
    }

    private class ConsumerImpl implements Consumer<Object> {

        int index;

        ConsumerImpl(int index) {
            super();
            this.index = index;
        }

        @Override
        public void accept(Object value) {
            if (index < list.size()) {
                list.set(index, value);
            } else {
                list.add(value);
            }
            index++;
        }
    }
}
