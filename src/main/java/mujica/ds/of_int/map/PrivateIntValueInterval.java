package mujica.ds.of_int.map;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/18", name = "SimpleIntValueInterval")
@CodeHistory(date = "2026/5/9")
public class PrivateIntValueInterval extends IntValueIntInterval {

    private static final long serialVersionUID = 0x374E49CEE7F80D88L;

    private int left, right, value;

    public PrivateIntValueInterval(int left, int right, int value) {
        super();
        if (left > right) {
            throw new IllegalArgumentException();
        }
        if (value == 0) {
            throw new IllegalArgumentException();
        }
        this.left = left;
        this.right = right;
        this.value = value;
    }

    @NotNull
    @Override
    public PrivateIntValueInterval duplicate() {
        return new PrivateIntValueInterval(left, right, value);
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
    public void setLeft(int newLeft) {
        if (newLeft > right) {
            throw new IllegalArgumentException();
        }
        left = newLeft;
    }

    @Override
    public void setRight(int newRight) {
        if (left > newRight) {
            throw new IllegalArgumentException();
        }
        right = newRight;
    }

    @Override
    public void setInt(int newValue) {
        if (newValue == 0) {
            throw new IllegalArgumentException();
        }
        value = newValue;
    }
}
