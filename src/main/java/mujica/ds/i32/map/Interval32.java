package mujica.ds.i32.map;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/18", name = "SimpleIntValueInterval")
@CodeHistory(date = "2026/5/9", name = "IntValueIntInterval")
@CodeHistory(date = "2026/7/9")
public class Interval32 extends I32ValueS32Interval {

    private static final long serialVersionUID = 0x374E49CEE7F80D88L;

    int left, right, value;

    public Interval32(int left, int right, int value) {
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
    public Interval32 duplicate() {
        return new Interval32(left, right, value);
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
    public int getI32() {
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
    public void setI32(int newValue) {
        if (newValue == 0) {
            throw new IllegalArgumentException();
        }
        value = newValue;
    }
}
