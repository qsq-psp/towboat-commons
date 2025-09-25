package mujica.math.algebra.discrete;

import java.math.BigInteger;

/**
 * Created on 2025/3/7.
 */
public class BaselineClampedMath extends ClampedMath {

    @Override
    public int add(int x, int y) {
        final int s = x + y;
        if (s < 0) {
            if (x > 0 && y > 0) {
                return Integer.MAX_VALUE;
            }
        } else {
            if (x < 0 && y < 0) {
                return Integer.MIN_VALUE;
            }
        }
        return s;
    }

    @Override
    public long add(long x, long y) {
        final long s = x + y;
        if (s < 0L) {
            if (x > 0L && y > 0L) {
                return Long.MAX_VALUE;
            }
        } else {
            if (x < 0L && y < 0L) {
                return Long.MIN_VALUE;
            }
        }
        return s;
    }

    @Override
    public int subtract(int x, int y) {
        final int d = x - y;
        if (x < 0) {
            if (y > 0 && d > 0) {
                return Integer.MIN_VALUE;
            }
        } else {
            if (y < 0 && d < 0) {
                return Integer.MAX_VALUE;
            }
        }
        return d;
    }

    @Override
    public long subtract(long x, long y) {
        final long d = x - y;
        if (x < 0L) {
            if (y > 0L && d > 0L) {
                return Long.MIN_VALUE;
            }
        } else {
            if (y < 0L && d < 0L) {
                return Long.MAX_VALUE;
            }
        }
        return d;
    }

    private static final BigInteger BIG_MIN_INT = BigInteger.valueOf(Integer.MIN_VALUE);

    private static final BigInteger BIG_MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);

    @Override
    public int multiply(int x, int y) {
        final BigInteger p = BigInteger.valueOf(x).multiply(BigInteger.valueOf(y));
        if (p.compareTo(BIG_MIN_INT) < 0) {
            return Integer.MIN_VALUE;
        }
        if (p.compareTo(BIG_MAX_INT) > 0) {
            return Integer.MAX_VALUE;
        }
        return p.intValueExact();
    }

    private static final BigInteger BIG_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger BIG_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    @Override
    public long multiply(long x, long y) {
        final BigInteger p = BigInteger.valueOf(x).multiply(BigInteger.valueOf(y));
        if (p.compareTo(BIG_MIN_LONG) < 0) {
            return Integer.MIN_VALUE;
        }
        if (p.compareTo(BIG_MAX_LONG) > 0) {
            return Integer.MAX_VALUE;
        }
        return p.longValueExact();
    }
}
