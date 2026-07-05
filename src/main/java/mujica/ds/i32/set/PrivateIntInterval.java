package mujica.ds.i32.set;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/6/7", project = "existence", name = "SimpleBoundI")
@CodeHistory(date = "2025/3/9", name = "PublicIntInterval")
@CodeHistory(date = "2026/1/18", name = "SimpleIntInterval")
@CodeHistory(date = "2026/5/5")
public class PrivateIntInterval extends IntInterval {

    private static final long serialVersionUID = 0xf2499a7fb880f2adL;

    private int left, right;

    public PrivateIntInterval() {
        super();
    }

    public PrivateIntInterval(int left, int right) {
        super();
        if (left > right) {
            throw new IllegalArgumentException();
        }
        this.left = left;
        this.right = right;
    }

    @NotNull
    @Override
    public PrivateIntInterval duplicate() {
        return new PrivateIntInterval(left, right);
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
}
