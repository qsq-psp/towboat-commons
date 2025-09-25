package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/3.
 */
@CodeHistory(date = "2025/3/3")
public class ModuloOne extends ModularMath {

    @Override
    protected int modLevel() {
        return ONE;
    }

    @NotNull
    @Override
    public Number modNumber() {
        return 1;
    }

    @Override
    @NotNull
    public BigInteger bigModNumber() {
        return BigInteger.ONE;
    }

    @Override
    public int cast2i(long value) {
        return 0;
    }

    @Override
    public int abs(int a) {
        return 0;
    }

    @Override
    public long abs(long a) {
        return 0L;
    }

    @Override
    public int increment(int a) {
        return 0;
    }

    @Override
    public long increment(long a) {
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger increment(@NotNull BigInteger a) {
        return BigInteger.ZERO;
    }

    @Override
    public int decrement(int a) {
        return 0;
    }

    @Override
    public long decrement(long a) {
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger decrement(@NotNull BigInteger a) {
        return BigInteger.ZERO;
    }

    @Override
    public int add(int x, int y) {
        return 0;
    }

    @Override
    public long add(long x, long y) {
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger add(@NotNull BigInteger x, @NotNull BigInteger y) {
        return BigInteger.ZERO;
    }

    @Override
    public int subtract(int x, int y) {
        return 0;
    }

    @Override
    public long subtract(long x, long y) {
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger subtract(@NotNull BigInteger x, @NotNull BigInteger y) {
        return BigInteger.ZERO;
    }

    @Override
    public int multiply(int x, int y) {
        return 0;
    }

    @Override
    public long multiply(long x, long y) {
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger multiply(@NotNull BigInteger x, @NotNull BigInteger y) {
        return BigInteger.ZERO;
    }

    @Override
    public int multiplyFraction(int number, int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Divide by zero");
        }
        return 0;
    }

    @Override
    public long multiplyFraction(long number, long numerator, long denominator) {
        if (denominator == 0L) {
            throw new ArithmeticException("Divide by zero");
        }
        return 0L;
    }

    @Override
    public int triangle(int x) {
        return 0;
    }

    @Override
    public long triangle(long x) {
        return 0L;
    }

    @Override
    public int power(int base, int exponent) {
        if (base == 0 && exponent == 0) {
            throw new ArithmeticException("power zero");
        }
        return 0;
    }

    @Override
    public long power(long base, long exponent) {
        if (base == 0L && exponent == 0L) {
            throw new ArithmeticException("power zero");
        }
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger power(@NotNull BigInteger base, @NotNull BigInteger exponent) {
        if (base.signum() == 0 && exponent.signum() == 0) {
            throw new ArithmeticException("power zero");
        }
        return BigInteger.ZERO;
    }

    @Override
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        return 0;
    }

    @Override
    public long factorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Factorial negative");
        }
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger factorial(@NotNull BigInteger a) {
        if (a.signum() < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        return BigInteger.ZERO;
    }

    @Override
    public int doubleFactorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Double factorial negative");
        }
        return 0;
    }

    @Override
    public long doubleFactorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Double factorial negative");
        }
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger doubleFactorial(@NotNull BigInteger a) {
        if (a.signum() < 0) {
            throw new ArithmeticException("Double factorial negative");
        }
        return BigInteger.ZERO;
    }

    @Override
    public int fibonacci(int i) {
        if (i < 0) {
            throw new ArithmeticException("Fibonacci negative");
        }
        return 0;
    }

    @Override
    public long fibonacci(long i) {
        if (i < 0L) {
            throw new ArithmeticException("Fibonacci negative");
        }
        return 0L;
    }

    @NotNull
    @Override
    public BigInteger fibonacci(@NotNull BigInteger i) {
        if (i.signum() < 0) {
            throw new ArithmeticException("Fibonacci negative");
        }
        return BigInteger.ZERO;
    }

    @Override
    public int arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        return 0;
    }

    @Override
    public long arrangement(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        return 0L;
    }

    @Override
    public BigInteger arrangement(@NotNull BigInteger n, @NotNull BigInteger m) {
        if (!(0 <= m.signum() && m.compareTo(n) <= 0)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        return BigInteger.ZERO;
    }

    @Override
    public int combination(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        return 0;
    }

    @Override
    public long combination(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        return 0L;
    }

    @Override
    public BigInteger combination(@NotNull BigInteger n, @NotNull BigInteger m) {
        if (!(0 <= m.signum() && m.compareTo(n) <= 0)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        return BigInteger.ZERO;
    }
}
