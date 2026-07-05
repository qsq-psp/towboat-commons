package mujica.ds.i32.set;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/8.
 */
public class ImmutableIntInterval extends IntInterval {

    public final int left, right;

    public ImmutableIntInterval(int left, int right) {
        super();
        if (left > right) {
            throw new IllegalArgumentException();
        }
        this.left = left;
        this.right = right;
    }

    @NotNull
    @Override
    public ImmutableIntInterval duplicate() {
        return this;
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
    public void setLeft(int newLeft) {
        if (left != newLeft) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setRight(int newRight) {
        if (right != newRight) {
            throw new UnsupportedOperationException();
        }
    }
}
