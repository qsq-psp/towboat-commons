package mujica.json.container;

import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

@CodeHistory(date = "2022/6/4", project = "Ultramarine", name = "JsonArray")
@CodeHistory(date = "2025/9/23", name = "JsonArray")
@CodeHistory(date = "2025/12/29", name = "GeneralJsonArray")
@CodeHistory(date = "2026/1/6")
class UtilListAsJsonArray extends JsonArray {

    private static final long serialVersionUID = 0x326e01ada4ba3c92L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilListAsJsonArray.class);

    @NotNull
    List<Object> list;

    UtilListAsJsonArray(@NotNull List<Object> list) {
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

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Consumer<Object> consumer(@NotNull ContainerConfig.ArrayAction action) {
        switch (action) {
            case NEW:
                try {
                    list = (List<Object>) list.getClass().getConstructor().newInstance();
                } catch (Exception e) {
                    LOGGER.error("", e);
                    list.clear();
                    if (list instanceof ArrayList) {
                        ((ArrayList<Object>) list).trimToSize();
                    }
                }
                return new IteratorConsumer(list.listIterator());
            case CLEAR:
                list.clear();
                return new IteratorConsumer(list.listIterator());
            case COVER:
                return new IndexedConsumer(0);
            case APPEND:
                return new IteratorConsumer(list.listIterator(list.size()));
            default:
                throw new IllegalArgumentException();
        }
    }

    @CodeHistory(date = "2026/4/13")
    private class IndexedConsumer implements Consumer<Object> {

        int index; // no check for comodification, not safe

        IndexedConsumer(int index) {
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

    @CodeHistory(date = "2026/8/6")
    private static class IteratorConsumer implements Consumer<Object> {

        @NotNull
        final ListIterator<Object> iterator;

        private IteratorConsumer(@NotNull ListIterator<Object> iterator) {
            super();
            this.iterator = iterator;
        }

        @Override
        public void accept(Object value) {
            iterator.add(value);
        }
    }
}
