package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/3/3.
 */
@CodeHistory(date = "2025/3/3")
public class CastToFloor implements IntegralCast {

    public static final CastToFloor INSTANCE = new CastToFloor();

    @Override
    public int divide(int a, int b) {
        return Math.floorDiv(a, b);
    }

    @Override
    public long divide(long a, long b) {
        return Math.floorDiv(a, b);
    }

    public int d2i(double v) {
        if (!(Integer.MIN_VALUE <= v && v < (Integer.MAX_VALUE + 1.0))) {
            throw new ArithmeticException("Overflow casting double to int");
        }
        return (int) Math.floor(v);
    }

    @Override
    public long d2l(double v) {
        if (!(Long.MIN_VALUE <= v && v < (Long.MAX_VALUE + 1.0))) {
            throw new ArithmeticException("Overflow casting double to long");
        }
        return (long) Math.floor(v);
    }
}
