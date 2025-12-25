package mujica.ds.of_int.map;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

/**
 * Created on 2025/3/27.
 */
@CodeHistory(date = "2025/3/27")
class SimpleIntMapEntry implements IntMapEntry, Serializable {

    private static final long serialVersionUID = 0xf306c658f3ec17b5L;

    int key, value;

    @Override
    public int getIntKey() {
        return key;
    }

    @Override
    public int getIntValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + "->" + value;
    }
}
