package mujica.json.entity;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/4")
public abstract class JsonPathSegment implements JsonPath {

    private static final long serialVersionUID = 0x366D0A83001BB5BBL;

    @Override
    public int length() {
        return 1;
    }

    @NotNull
    @Override
    public JsonPathSegment get(int index) {
        if (index == 0) {
            return this;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
