package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;

import java.math.BigInteger;

@CodeHistory(date = "2025/3/7")
class BaselineClampedMath extends ClampedMath {

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

    @Override
    public int multiply(int x, int y) {
        final BigInteger p = BigInteger.valueOf(x).multiply(BigInteger.valueOf(y));
        if (p.compareTo(BigConstants.MIN_INT) < 0) {
            return Integer.MIN_VALUE;
        }
        if (p.compareTo(BigConstants.MAX_INT) > 0) {
            return Integer.MAX_VALUE;
        }
        return p.intValueExact();
    }

    @Override
    public long multiply(long x, long y) {
        final BigInteger p = BigInteger.valueOf(x).multiply(BigInteger.valueOf(y));
        if (p.compareTo(BigConstants.MIN_LONG) < 0) {
            return Long.MIN_VALUE;
        }
        if (p.compareTo(BigConstants.MAX_LONG) > 0) {
            return Long.MAX_VALUE;
        }
        return p.longValueExact();
    }
}
