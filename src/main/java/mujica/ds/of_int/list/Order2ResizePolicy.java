package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/7/1")
public class Order2ResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0x7e5c74e47d6fc6dfL;

    private final int p0, p1, p2;

    public Order2ResizePolicy(int p0, int p1, int p2) {
        super();
        if (p0 < 0 || p1 < 0 || p2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public int intLength() {
        return 0;
    }

    @Override
    public int getInt(int index) {
        return Math.addExact(p0, Math.multiplyExact(Math.addExact(p1, Math.multiplyExact(p2, index)), index));
    }
}
