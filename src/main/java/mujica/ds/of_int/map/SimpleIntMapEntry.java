package mujica.ds.of_int.map;

/**
 * Created on 2025/3/27.
 */
class SimpleIntMapEntry implements IntMapEntry {

    int key, value;

    @Override
    public int getIntKey() {
        return key;
    }

    @Override
    public int getIntValue() {
        return value;
    }
}
