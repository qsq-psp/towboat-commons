package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/14.
 */
public class ModuloSomeLong extends ModularMath {

    protected static final long MAX_INT_SUM = ModuloI32.MASK;

    protected static final long MAX_INT_PRODUCT = ((long) Integer.MIN_VALUE) * ((long) Integer.MIN_VALUE);

    protected static int level(long value) {
        if (value > Integer.MAX_VALUE) {
            if (value > MAX_INT_PRODUCT) {
                return LARGER_THAN_MAX_INT_PRODUCT;
            } else if (value > MAX_INT_SUM) {
                return LARGER_THAN_MAX_INT_SUM;
            } else {
                return LARGER_THAN_MAX_INT;
            }
        } else if (value > 1) {
            return SMALL;
        } else {
            return ONE;
        }
    }

    private final long mod;

    private final int level;

    public ModuloSomeLong(long mod) {
        super();
        if (mod <= 0) {
            throw new IllegalArgumentException();
        }
        this.mod = mod;
        this.level = level(mod);
    }

    @Override
    protected int modLevel() {
        return level;
    }

    @NotNull
    @Override
    public Number modNumber() {
        return mod;
    }

    @Override
    @NotNull
    public BigInteger bigModNumber() {
        return BigInteger.valueOf(mod);
    }

    @Override
    public int cast2i(long value) {
        value %= mod;
        if (value < 0L) {
            value += mod;
        }
        return super.cast2i(value);
    }

    @Override
    public int increment(int a) {
        return cast2i(a + 1L);
    }

    @Override
    public long increment(long a) {
        if (a == Long.MAX_VALUE) {
            a -= mod;
        }
        // now a is not Long.MAX_VALUE
        a = (a + 1L) % mod;
        if (a < 0L) {
            a += mod;
        }
        return a;
    }

    @NotNull
    @Override
    public BigInteger increment(@NotNull BigInteger a) {
        return a.add(BigInteger.ONE).mod(BigInteger.valueOf(mod)); // always non-negative
    }

    @Override
    public int decrement(int a) {
        return cast2i(a - 1L);
    }

    @Override
    public long decrement(long a) {
        if (a == Long.MIN_VALUE) {
            a += mod;
        }
        // now a is not Long.MIN_VALUE
        a = (a - 1L) % mod;
        if (a < 0L) {
            a += mod;
        }
        return a;
    }

    @Override
    public int add(int x, int y) {
        if (level >= LARGER_THAN_MAX_INT_SUM) {
            int s = x + y;
            if (s < 0 || (x & y) < 0) {
                throw new ArithmeticException("Integer overflow");
            }
            return s;
        } else {
            return cast2i(((long) x) + ((long) y));
        }
    }

    @Override
    public long add(long x, long y) {
        long s = x + y;
        if (((x ^ s) & (y ^ s)) < 0L) {
            x %= mod;
            y %= mod;
            s = x + y;
            if (((x ^ s) & (y ^ s)) < 0L) {
                if (x >= 0L) {
                    assert y >= 0L;
                    y -= mod;
                } else {
                    assert y < 0L;
                    y += mod;
                }
                s = x + y;
                assert ((x ^ s) & (y ^ s)) >= 0L;
            }
        }
        s %= mod;
        if (s < 0L) {
            s += mod;
        }
        return s;
    }

    @Override
    public int subtract(int x, int y) {
        if (level >= LARGER_THAN_MAX_INT_SUM) {
            int d = Math.subtractExact(x, y);
            if (d < 0 || x < 0 && y >= 0) {
                throw new ArithmeticException("Integer overflow");
            }
            return d;
        } else {
            return cast2i(((long) x) - ((long) y));
        }
    }

    @Override
    public long subtract(long x, long y) {
        long d = x - y;
        if (((x ^ y) & (x ^ d)) < 0L) {
            x %= mod;
            y %= mod;
            d = x - y;
            if (((x ^ y) & (x ^ d)) < 0L) {
                if (x >= 0L) {
                    assert y < 0L;
                    y += mod;
                } else {
                    assert y >= 0L;
                    y -= mod;
                }
                d = x - y;
                assert ((x ^ y) & (x ^ d)) >= 0L;
            }
        }
        d %= mod;
        if (d < 0L) {
            d += mod;
        }
        return d;
    }
}
