package mujica.ds.of_int.map;

import java.io.Serializable;

/**
 * Created on 2026/1/18.
 */
public class SimpleIntValueInterval implements IntValueInterval, Serializable {

    int left, right, value;

    public SimpleIntValueInterval(int left, int right, int value) {
        super();
        if (left > right) {
            throw new IllegalArgumentException();
        }
        this.left = left;
        this.right = right;
        this.value = value;
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public int setInt(int newValue) {
        final int oldValue = value;
        value = newValue;
        return oldValue;
    }
}
