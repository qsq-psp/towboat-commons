package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/7/1")
public class Order3ResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0x5b6d827aad3d0339L;

    private final int p0, p3;

    public Order3ResizePolicy(int p0, int p3) {
        super();
        if (p0 < 0 || p3 <= 0) {
            throw new IllegalArgumentException();
        }
        this.p0 = p0;
        this.p3 = p3;
    }

    @Override
    public int intLength() {
        return 0;
    }

    @Override
    public int getInt(int index) {
        return Math.addExact(p0, Math.multiplyExact(Math.multiplyExact(p3, index), Math.multiplyExact(index, index)));
    }
}
