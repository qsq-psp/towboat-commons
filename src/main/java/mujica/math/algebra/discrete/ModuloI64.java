package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/3.
 */
@CodeHistory(date = "2025/3/3")
public class ModuloI64 extends ModularMath {

    protected static final BigInteger BIG_MASK = BigInteger.ONE.shiftLeft(Long.SIZE).subtract(BigInteger.ONE);

    @Override
    protected int modLevel() {
        return LARGER_THAN_MAX_LONG_SUM;
    }

    @NotNull
    @Override
    public Number modNumber() {
        return BigInteger.ONE.shiftLeft(Long.SIZE);
    }

    @NotNull
    @Override
    public BigInteger bigModNumber() {
        return BigInteger.ONE.shiftLeft(Long.SIZE);
    }

    @Override
    public int increment(int a) {
        if (a == Integer.MAX_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return a + 1;
    }

    @Override
    public long increment(long a) {
        return a + 1;
    }

    @NotNull
    @Override
    public BigInteger increment(@NotNull BigInteger a) {
        return a.add(BigInteger.ONE).and(BIG_MASK);
    }

    @Override
    public int decrement(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow");
        }
        return a - 1;
    }

    @Override
    public long decrement(long a) {
        return a - 1;
    }

    @NotNull
    @Override
    public BigInteger decrement(@NotNull BigInteger a) {
        return a.subtract(BigInteger.ONE).and(BIG_MASK);
    }

    @Override
    public int add(int x, int y) {
        return Math.addExact(x, y);
    }

    @Override
    @DataType("u64")
    public long add(long x, long y) {
        return x + y;
    }

    @NotNull
    @Override
    public BigInteger add(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.add(y).and(BIG_MASK);
    }

    @Override
    public int subtract(int x, int y) {
        return Math.subtractExact(x, y);
    }

    @Override
    @DataType("u64")
    public long subtract(long x, long y) {
        return x - y;
    }

    @NotNull
    @Override
    public BigInteger subtract(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.subtract(y).and(BIG_MASK);
    }

    @Override
    public int multiply(int x, int y) {
        return Math.multiplyExact(x, y);
    }

    @Override
    @DataType("u64")
    public long multiply(long x, long y) {
        return x * y;
    }

    @NotNull
    @Override
    public BigInteger multiply(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.multiply(y).and(BIG_MASK);
    }

    @Override
    public int power(int base, int exponent) {
        if (exponent <= 0) {
            if (exponent == 0) {
                if (base == 0) {
                    throw new ArithmeticException("Power zero");
                }
                return 1;
            }
            throw new ArithmeticException("Power negative");
        }
        long product = base;
        for (int i = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product *= product;
            if ((exponent & (1 << i)) != 0) {
                product *= base;
            }
        }
        if (Integer.MIN_VALUE <= product && product <= Integer.MAX_VALUE) {
            return (int) product;
        }
        throw new ArithmeticException("Overflow");
    }

    @Override
    public long power(long base, long exponent) {
        if (exponent <= 0L) {
            if (exponent == 0L) {
                if (base == 0L) {
                    throw new ArithmeticException("Power zero");
                }
                return 1L;
            }
            throw new ArithmeticException("Power negative");
        }
        long product = base;
        for (int i = Long.SIZE - 2 - Long.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product *= product;
            if ((exponent & (1L << i)) != 0) {
                product *= base;
            }
        }
        return product;
    }

    @NotNull
    @Override
    public BigInteger power(@NotNull BigInteger base, @NotNull BigInteger exponent) {
        final int sign = exponent.signum();
        if (sign <= 0) {
            if (sign == 0) {
                if (base.signum() == 0) {
                    throw new ArithmeticException("Power zero");
                }
                return BigInteger.ONE;
            }
            throw new ArithmeticException("Power negative");
        }
        if (BigInteger.ONE.equals(exponent)) {
            return base.and(BIG_MASK);
        }
        return base.modPow(exponent, BIG_MASK);
    }

    private static final int FACTORIAL_THRESHOLD = 66;

    private static final BigInteger BIG_FACTORIAL_THRESHOLD = BigInteger.valueOf(FACTORIAL_THRESHOLD);

    @Override
    @DataType("u64")
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a < FACTORIAL_THRESHOLD) {
            long product = 1;
            for (int i = 2; i <= a; i++) {
                product *= i;
            }
            if (Integer.MIN_VALUE <= product && product <= Integer.MAX_VALUE) {
                return (int) product;
            }
            throw new ArithmeticException("Overflow");
        } else {
            return 0;
        }
    }

    @Override
    public long factorial(long a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a < FACTORIAL_THRESHOLD) {
            long product = 1;
            for (long i = 2; i <= a; i++) {
                product *= i;
            }
            return product;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public BigInteger factorial(@NotNull BigInteger a) {
        if (a.signum() < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a.compareTo(BIG_FACTORIAL_THRESHOLD) < 0) {
            int ia = a.intValue();
            long product = 1;
            for (int i = 2; i <= ia; i++) {
                product *= i;
            }
            return BigInteger.valueOf(product).and(BIG_MASK);
        } else {
            return BigInteger.ZERO;
        }
    }

    @Override
    public int arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        long product = 1L;
        for (int i = n - m + 1; i <= n; i++) {
            product *= i;
            if (product == 0) {
                break;
            }
        }
        if (Integer.MIN_VALUE <= product && product <= Integer.MAX_VALUE) {
            return (int) product;
        }
        throw new ArithmeticException("Overflow");
    }

    @Override
    public long arrangement(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        long product = 1L;
        for (long i = n - m + 1; i <= n; i++) {
            product *= i;
            if (product == 0L) {
                break;
            }
        }
        return product;
    }
}
