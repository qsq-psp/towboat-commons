package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/14.
 */
public class ModuloSomeInt extends ModularMath {

    public final int mod;

    public ModuloSomeInt(int mod) {
        super();
        if (mod <= 0) {
            throw new IllegalArgumentException();
        }
        this.mod = mod;
    }

    @Override
    protected int modLevel() {
        return mod == 1 ? ONE : SMALL;
    }

    @Override
    @NotNull
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
        return (int) value;
    }

    @Override
    public int increment(int a) {
        if (a == Integer.MAX_VALUE) {
            a -= mod;
        }
        // now a is not Integer.MAX_VALUE
        a = (a + 1) % mod;
        if (a < 0) {
            a += mod;
        }
        return a;
    }

    @Override
    public long increment(long a) {
        a = a % mod + 1L;
        if (a < 0L) {
            a += mod;
        } else if (a == mod) {
            a -= mod;
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
        if (a == Integer.MIN_VALUE) {
            a += mod;
        }
        // now a is not Integer.MIN_VALUE
        a = (a - 1) % mod;
        if (a < 0) {
            a += mod;
        }
        return a;
    }

    @Override
    public long decrement(long a) {
        a = a % mod - 1L;
        if (a < 0L) {
            a += mod;
        }
        return a;
    }

    @NotNull
    @Override
    public BigInteger decrement(@NotNull BigInteger a) {
        return a.subtract(BigInteger.ONE).mod(BigInteger.valueOf(mod)); // always non-negative
    }

    @Override
    public int add(int x, int y) {
        int s = (int) ((((long) x) + ((long) y)) % mod);
        if (s < 0) {
            s += mod;
        }
        return s;
    }

    @Override
    public long add(long x, long y) {
        long s = x + y;
        if (((x ^ s) & (y ^ s)) < 0L) {
            s = x % mod + y % mod;
        }
        s %= mod;
        if (s < 0L) {
            s += mod;
        }
        return s;
    }

    @NotNull
    @Override
    public BigInteger add(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.add(y).mod(BigInteger.valueOf(mod));
    }

    @Override
    public int subtract(int x, int y) {
        int d = (int) ((((long) x) - ((long) y)) % mod);
        if (d < 0) {
            d += mod;
        }
        return d;
    }

    @Override
    public long subtract(long x, long y) {
        long d = x - y;
        if (((x ^ y) & (x ^ d)) < 0L) {
            d = x % mod - y % mod;
        }
        d %= mod;
        if (d < 0L) {
            d += mod;
        }
        return d;
    }

    @NotNull
    @Override
    public BigInteger subtract(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.subtract(y).mod(BigInteger.valueOf(mod));
    }

    @Override
    public int multiply(int x, int y) {
        int p = (int) ((((long) x) * ((long) y)) % mod);
        if (p < 0) {
            p += mod;
        }
        return p;
    }

    @Override
    public long multiply(long x, long y) {
        long p = ((x % mod) * (y % mod)) % mod;
        if (p < 0L) {
            p += mod;
        }
        return p;
    }

    @NotNull
    @Override
    public BigInteger multiply(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.multiply(y).mod(BigInteger.valueOf(mod));
    }

    @Override
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a < mod) {
            long product = 1L;
            for (int i = 2; i <= a; i++) {
                product = (product * i) % mod;
            }
            return (int) product;
        } else {
            return 0;
        }
    }

    @Override
    public long factorial(long a) {
        return factorial(cast2i(a));
    }

    @NotNull
    @Override
    public BigInteger factorial(@NotNull BigInteger a) {
        return BigInteger.valueOf(factorial(a.intValueExact()));
    }

    @Override
    public int doubleFactorial(int a) {
        if (a < 2) {
            if (a < 0) {
                throw new ArithmeticException("Double factorial negative");
            }
            return 1;
        }
        if (a < mod || ((mod - a) & 1) != 0) {
            long product = a;
            for (a -= 2; a > 1; a -= 2) {
                product = (product * a) % mod;
            }
            return (int) product;
        } else {
            return 0;
        }
    }

    @Override
    public long doubleFactorial(long a) {
        return doubleFactorial(cast2i(a));
    }

    @NotNull
    @Override
    public BigInteger doubleFactorial(@NotNull BigInteger a) {
        return BigInteger.valueOf(doubleFactorial(a.intValueExact()));
    }
}
