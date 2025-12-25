package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/3.
 */
@CodeHistory(date = "2025/3/3")
public class ModuloI32 extends ModularMath {

    protected static final long MASK = (1L << Integer.SIZE) - 1L;

    protected static final BigInteger BIG_MASK = BigInteger.valueOf(MASK);

    @Override
    protected int modLevel() {
        return LARGER_THAN_MAX_INT_SUM;
    }

    @Override
    @NotNull
    public Number modNumber() {
        return 1L << Integer.SIZE;
    }

    @NotNull
    @Override
    public BigInteger bigModNumber() {
        return BigInteger.ONE.shiftLeft(Integer.SIZE);
    }

    @Override
    public int increment(int a) {
        return a + 1;
    }

    @Override
    public long increment(long a) {
        return MASK & (a + 1L);
    }

    @NotNull
    @Override
    public BigInteger increment(@NotNull BigInteger a) {
        return a.add(BigInteger.ONE).and(BIG_MASK);
    }

    @Override
    public int decrement(int a) {
        return a - 1;
    }

    @Override
    public long decrement(long a) {
        return MASK & (a - 1L);
    }

    @NotNull
    @Override
    public BigInteger decrement(@NotNull BigInteger a) {
        return a.subtract(BigInteger.ONE).and(BIG_MASK);
    }

    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public long add(long x, long y) {
        return MASK & (x + y);
    }

    @NotNull
    @Override
    public BigInteger add(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.add(y).and(BIG_MASK);
    }

    @Override
    public int subtract(int x, int y) {
        return x - y;
    }

    @Override
    public long subtract(long x, long y) {
        return MASK & (x - y);
    }

    @NotNull
    @Override
    public BigInteger subtract(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.subtract(y).and(BIG_MASK);
    }

    @Override
    @DataType("u32")
    public int multiply(int x, int y) {
        return x * y;
    }

    @Override
    public long multiply(long x, long y) {
        return MASK & (x * y);
    }

    @NotNull
    @Override
    public BigInteger multiply(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.multiply(y).and(BIG_MASK);
    }

    @Override
    public int multiplyFraction(int number, int numerator, int denominator) {
        return (int) divideExact(
                ((long) number) * numerator,
                denominator
        );
    }

    @Override
    public long multiplyFraction(long number, long numerator, long denominator) {
        return 0L; // ...
    }

    @Override
    public int triangle(int x) {
        if ((x & 1) == 0) {
            return (x >>> 1) * (x + 1);
        } else {
            return x * ((x + 1) >>> 1);
        }
    }

    @Override
    public long triangle(long x) {
        if ((x & 1L) == 0L) {
            return MASK & ((x >>> 1) * (x + 1L));
        } else {
            return MASK & (x * ((x + 1L) >>> 1));
        }
    }

    @Override
    public long divideExact(long x, long y) {
        final long q = x / y;
        if (x != Math.multiplyExact(q, y)) {
            throw new ArithmeticException();
        }
        return MASK & q;
    }

    @NotNull
    @Override
    public BigInteger divideExact(@NotNull BigInteger x, @NotNull BigInteger y) {
        final BigInteger[] result = x.divideAndRemainder(y);
        if (result[1].signum() != 0) {
            throw new ArithmeticException("Can not divide exact");
        }
        return result[0].and(BIG_MASK);
    }

    @Override
    @DataType("u32")
    public int power(int base, int exponent) {
        if (exponent <= 0) {
            if (exponent == 0) {
                if (base == 0) {
                    throw new ArithmeticException("power zero");
                }
                return 1;
            }
            throw new ArithmeticException("power negative");
        }
        int product = base;
        for (int i = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product *= product;
            if ((exponent & (1 << i)) != 0) {
                product *= base;
            }
        }
        return product;
    }

    @Override
    public long power(long base, long exponent) {
        if (exponent <= 0L) {
            if (exponent == 0L) {
                if (base == 0L) {
                    throw new ArithmeticException("power zero");
                }
                return 1L;
            }
            throw new ArithmeticException("power negative");
        }
        long product = base;
        for (int i = Long.SIZE - 2 - Long.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product *= product;
            if ((exponent & (1L << i)) != 0) {
                product *= base;
            }
        }
        return MASK & product;
    }

    @NotNull
    @Override
    public BigInteger power(@NotNull BigInteger base, @NotNull BigInteger exponent) {
        final int sign = exponent.signum();
        if (sign <= 0) {
            if (sign == 0) {
                if (base.signum() == 0) {
                    throw new ArithmeticException("power zero");
                }
                return BigInteger.ONE;
            }
            throw new ArithmeticException("power negative");
        }
        if (BigInteger.ONE.equals(exponent)) {
            return base.and(BIG_MASK);
        }
        return base.modPow(exponent, BIG_MASK);
    }

    private static final int FACTORIAL_THRESHOLD = 34;

    private static final BigInteger BIG_FACTORIAL_THRESHOLD = BigInteger.valueOf(FACTORIAL_THRESHOLD);

    @Override
    @DataType("u32")
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a < FACTORIAL_THRESHOLD) {
            int product = 1;
            for (int i = 2; i <= a; i++) {
                product *= i;
            }
            return product;
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
            int ia = (int) a;
            int product = 1;
            for (int i = 2; i <= ia; i++) {
                product *= i;
            }
            return MASK & product;
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
            int product = 1;
            for (int i = 2; i <= ia; i++) {
                product *= i;
            }
            return BigInteger.valueOf(MASK & product);
        } else {
            return BigInteger.ZERO;
        }
    }

    private static final int DOUBLE_FACTORIAL_THRESHOLD = 34;

    private static final BigInteger BIG_DOUBLE_FACTORIAL_THRESHOLD = BigInteger.valueOf(DOUBLE_FACTORIAL_THRESHOLD);

    @Override
    public int doubleFactorial(int a) {
        if (a < 2) {
            if (a < 0) {
                throw new ArithmeticException("Double factorial negative");
            }
            return 1;
        }
        if (a < DOUBLE_FACTORIAL_THRESHOLD || (a & 1) != 0) {
            int product = a;
            for (a -= 2; a > 1; a -= 2) {
                product *= a;
            }
            return product;
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

    public int fibonacci(int i) {
        if (i < 2) {
            if (i < 0) {
                throw new ArithmeticException("Fibonacci negative");
            }
            return i;
        }
        i--;
        int m00 = 1;
        int m01 = 1;
        int m10 = 1;
        int m11 = 0;
        for (int shift = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(i); shift >= 0; shift--) {
            int n00 = m00 * m00 + m01 * m10;
            int n01 = m00 * m01 + m01 * m11;
            int n10 = m10 * m00 + m11 * m10;
            int n11 = m10 * m01 + m11 * m11;
            if ((i & (1 << shift)) != 0) {
                m00 = n00 + n01;
                m01 = n00;
                m10 = n10 + n11;
                m11 = n10;
            } else {
                m00 = n00;
                m01 = n01;
                m10 = n10;
                m11 = n11;
            }
        }
        return m00;
    }

    public long fibonacci(long i) {
        if (i < 2L) {
            if (i < 0L) {
                throw new ArithmeticException("Fibonacci negative");
            }
            return i;
        }
        i--;
        int m00 = 1;
        int m01 = 1;
        int m10 = 1;
        int m11 = 0;
        for (int shift = Long.SIZE - 2 - Long.numberOfLeadingZeros(i); shift >= 0; shift--) {
            int n00 = m00 * m00 + m01 * m10;
            int n01 = m00 * m01 + m01 * m11;
            int n10 = m10 * m00 + m11 * m10;
            int n11 = m10 * m01 + m11 * m11;
            if ((i & (1L << shift)) != 0L) {
                m00 = n00 + n01;
                m01 = n00;
                m10 = n10 + n11;
                m11 = n10;
            } else {
                m00 = n00;
                m01 = n01;
                m10 = n10;
                m11 = n11;
            }
        }
        return MASK & m00;
    }

    @NotNull
    @Override
    public BigInteger fibonacci(@NotNull BigInteger i) {
        return BigInteger.valueOf(fibonacci(i.longValueExact()));
    }

    @Override
    public int arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        int product = 1;
        for (int i = n - m + 1; i <= n; i++) {
            product *= i;
            if (product == 0) {
                break;
            }
        }
        return product;
    }

    @Override
    public long arrangement(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        int product = 1;
        for (long i = n - m + 1; i <= n; i++) {
            product *= i;
            if (product == 0L) {
                break;
            }
        }
        return MASK & product;
    }

    @Override
    public BigInteger arrangement(@NotNull BigInteger n, @NotNull BigInteger m) {
        return BigInteger.valueOf(arrangement(n.longValueExact(), m.longValueExact()));
    }
}
