package mujica.ds.i32.map;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2025/3/27")
class SimpleIntMapEntry implements I32Map.Entry, Serializable {

    private static final long serialVersionUID = 0xf306c658f3ec17b5L;

    int key, value;

    @Override
    public int getI32Key() {
        return key;
    }

    @Override
    public int getI32Value() {
        return value;
    }

    @Override
    public String toString() {
        return key + "->" + value;
    }
}
