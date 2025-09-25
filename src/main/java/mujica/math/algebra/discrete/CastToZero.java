package mujica.math.algebra.discrete;

/**
 * Created in Ultramarine on 2023/12/13, named IntegralCast.ToZero.
 * Created on 2025/3/3.
 */
public class CastToZero implements IntegralCast {

    public static final CastToZero INSTANCE = new CastToZero();

    @Override
    public int mean(int x, int y) {
        final int s = x + y;
        if (((x ^ s) & (y ^ s)) < 0) {
            if (s < 0) {
                return s >>> 1; // shift with sign is also OK
            } else {
                return Integer.MIN_VALUE | (s >>> 1);
            }
        }
        return s >> 1; // no overflow; shift with sign
    }

    @Override
    public long mean(long x, long y) {
        final long s = x + y;
        if (((x ^ s) & (y ^ s)) < 0L) {
            if (s < 0L) {
                return s >>> 1; // shift with sign is also OK
            } else {
                return Long.MIN_VALUE | (s >>> 1);
            }
        }
        return s >> 1; // no overflow; shift with sign
    }

    public int divide(int a, int b) {
        return a / b; // internally throws ArithmeticException if b == 0
    }

    public long divide(long a, long b) {
        return a / b; // internally throws ArithmeticException if b == 0
    }

    public int f2i(float v) {
        if (!(Math.abs(v) <= Integer.MAX_VALUE)) {
            throw new ArithmeticException("float to int overflow");
        }
        return (int) v;
    }

    @Override
    public long f2l(float v) {
        if (!(Math.abs(v) <= Long.MAX_VALUE)) {
            throw new ArithmeticException("float to long overflow");
        }
        return (long) v;
    }

    public int d2i(double v) {
        if (!(Math.abs(v) <= Integer.MAX_VALUE)) {
            throw new ArithmeticException("double to int overflow");
        }
        return (int) v;
    }

    @Override
    public long d2l(double v) {
        if (!(Math.abs(v) <= Long.MAX_VALUE)) {
            throw new ArithmeticException("float to long overflow");
        }
        return (long) v;
    }
}
