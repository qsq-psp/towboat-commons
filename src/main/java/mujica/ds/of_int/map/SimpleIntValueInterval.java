package mujica.ds.of_int.map;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

/**
 * Created on 2026/1/18.
 */
@CodeHistory(date = "2026/1/18")
public class SimpleIntValueInterval implements IntValueInterval, Serializable {

    private static final long serialVersionUID = 0x374E49CEE7F80D88L;

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
    public void setInt(int newValue) {
        value = newValue;
    }

    private static final int H = 59;

    @Override
    public int hashCode() {
        return ((left * H) + right) * H + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntValueInterval)) {
            return false;
        }
        final IntValueInterval that = (IntValueInterval) obj;
        return this.getLeft() == that.getLeft() && this.getRight() == that.getRight() && this.getInt() == that.getInt();
    }
}
