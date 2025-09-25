package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/3/7.
 */
public class BaselineIntegralMath extends IntegralMath {

    @Override
    public int abs(int a) {
        a = Math.abs(a);
        if (a < 0) {
            throw new ArithmeticException("Absolute value overflow");
        }
        return a;
    }

    @Override
    public long abs(long a) {
        a = Math.abs(a);
        if (a < 0L) {
            throw new ArithmeticException("Absolute value overflow");
        }
        return a;
    }

    @Override
    public int multiplyFraction(int number, int numerator, int denominator) {
        return divideExact(
                BigInteger.valueOf(number).multiply(BigInteger.valueOf(numerator)),
                BigInteger.valueOf(denominator)
        ).intValueExact();
    }

    @Override
    public long multiplyFraction(long number, long numerator, long denominator) {
        return divideExact(
                BigInteger.valueOf(number).multiply(BigInteger.valueOf(numerator)),
                BigInteger.valueOf(denominator)
        ).longValueExact();
    }

    @Override
    public int triangle(int x) {
        BigInteger bigX = BigInteger.valueOf(x);
        return divideExact(
                bigX.multiply(bigX.add(BigInteger.ONE)),
                BigInteger.TWO
        ).intValueExact();
    }

    @Override
    public long triangle(long x) {
        BigInteger bigX = BigInteger.valueOf(x);
        return divideExact(
                bigX.multiply(bigX.add(BigInteger.ONE)),
                BigInteger.TWO
        ).longValueExact();
    }

    @Override
    public int divideExact(int x, int y) {
        if (x % y != 0) {
            throw new ArithmeticException("Can not divide exact");
        }
        if (y == -1 && x == Integer.MIN_VALUE) {
            throw new ArithmeticException("Divide int overflow");
        }
        return x / y;
    }

    @Override
    public long divideExact(long x, long y) {
        if (x % y != 0L) {
            throw new ArithmeticException("Can not divide exact");
        }
        if (y == -1L && x == Long.MIN_VALUE) {
            throw new ArithmeticException("Divide long overflow");
        }
        return x / y;
    }

    @Override
    public int power(int base, int exponent) {
        if (exponent < 0) {
            throw new ArithmeticException("power negative");
        }
        if (base == 0 && exponent == 0) {
            throw new ArithmeticException("power zero");
        }
        int product = 1;
        for (int index = 0; index < exponent; index++) {
            product = Math.multiplyExact(product, base);
        }
        return product;
    }

    @Override
    public long power(long base, long exponent) {
        if (exponent < 0L) {
            throw new ArithmeticException("power negative");
        }
        if (base == 0L && exponent == 0L) {
            throw new ArithmeticException("power zero");
        }
        long product = 1L;
        for (int index = 0; index < exponent; index++) {
            product = Math.multiplyExact(product, base);
        }
        return product;
    }

    @NotNull
    @Override
    public BigInteger power(@NotNull BigInteger base, @NotNull BigInteger exponent) {
        return base.pow(exponent.intValueExact());
    }

    @Override
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        int f = 1;
        while (a > 1) { // multiply from the larger tail so it overflows earlier
            f = Math.multiplyExact(f, a);
            a--;
        }
        return f;
    }

    @Override
    public long factorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Factorial negative");
        }
        long f = 1L;
        while (a > 1L) { // multiply from the larger tail so it overflows earlier
            f = Math.multiplyExact(f, a);
            a--;
        }
        return f;
    }

    @Override
    public int doubleFactorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Double factorial negative");
        }
        int f = 1;
        while (a > 1) { // multiply from the larger tail so it overflows earlier
            f = Math.multiplyExact(f, a);
            a -= 2;
        }
        return f;
    }

    @Override
    public long doubleFactorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Double factorial negative");
        }
        long f = 1L;
        while (a > 1L) { // multiply from the larger tail so it overflows earlier
            f = Math.multiplyExact(f, a);
            a -= 2L;
        }
        return f;
    }

    @NotNull
    @Override
    public BigInteger doubleFactorial(@NotNull BigInteger a) {
        if (a.signum() < 0) {
            throw new ArithmeticException("Double factorial negative");
        }
        BigInteger product = BigInteger.ONE;
        for (; a.signum() > 0; a = a.subtract(BigInteger.TWO)) {
            product = product.multiply(a);
        }
        return product;
    }

    @Override
    public int fibonacci(int i) {
        if (i < 0) {
            throw new ArithmeticException("Negative fibonacci");
        }
        if (i < 2) {
            return i; // F(0) = 0, F(1) = 1
        }
        int a = 0;
        int b = 1;
        for (int j = 2; j <= i; j++) { // will not loop infinitely if i == Integer.MAX_VALUE, addExact(int, int) will overflow
            int c = Math.addExact(a, b);
            a = b;
            b = c;
        }
        return b;
    }

    @Override
    public long fibonacci(long i) {
        if (i < 0L) {
            throw new ArithmeticException("Negative fibonacci");
        }
        if (i < 2L) {
            return i; // F(0) = 0, F(1) = 1
        }
        long a = 0L;
        long b = 1L;
        for (long j = 2; j <= i; j++) { // will not loop infinitely if i == Long.MAX_VALUE, addExact(long, long) will overflow
            long c = Math.addExact(a, b);
            a = b;
            b = c;
        }
        return b;
    }

    @NotNull
    @Override
    public BigInteger fibonacci(@NotNull BigInteger i) {
        final int ii = i.intValueExact();
        if (ii < 0) {
            throw new ArithmeticException("Negative fibonacci");
        }
        if (ii <= 2) {
            if (ii == 0) {
                return BigInteger.ZERO;
            } else {
                return BigInteger.ONE;
            }
        }
        BigInteger a = BigInteger.ONE;
        BigInteger b = BigInteger.ONE;
        for (int j = 2; j < ii; j++) {
            BigInteger c = a.add(b);
            a = b;
            b = c;
        }
        return b;
    }

    public int arrangement(int n, int m) {
        // A(n, m) = n! / (n - m)!
        return divideExact(
                factorial(BigInteger.valueOf(n)),
                factorial(BigInteger.valueOf(n - m))
        ).intValueExact();
    }

    public long arrangement(long n, long m) {
        // A(n, m) = n! / (n - m)!
        return divideExact(
                factorial(BigInteger.valueOf(n)),
                factorial(BigInteger.valueOf(n - m))
        ).longValueExact();
    }

    public BigInteger arrangement(@NotNull BigInteger n, @NotNull BigInteger m) {
        // A(n, m) = n! / (n - m)!
        return divideExact(
                factorial(n),
                factorial(n.subtract(m))
        );
    }

    @Override
    public int combination(int n, int m) {
        return combination(BigInteger.valueOf(n), BigInteger.valueOf(m)).intValueExact();
    }

    @Override
    public long combination(long n, long m) {
        return combination(BigInteger.valueOf(n), BigInteger.valueOf(m)).longValueExact();
    }

    @Override
    public BigInteger combination(@NotNull BigInteger n, @NotNull BigInteger m) {
        // C(n, m) = n! / (m! * (n - m)!)
        return divideExact(
                factorial(n),
                factorial(m).multiply(factorial(n.subtract(m)))
        );
    }

    @Override
    public int combination(@NotNull int[] array) {
        // C(a0, a1, ..., an) = (a0 + a1 + ... + an)! / (a0! * a1! * ... * an!)
        BigInteger numerator = BigInteger.ZERO;
        BigInteger denominator = BigInteger.ONE;
        for (int value : array) {
            BigInteger bigValue = BigInteger.valueOf(value);
            numerator = numerator.add(bigValue);
            denominator = denominator.multiply(factorial(bigValue));
        }
        numerator = factorial(numerator);
        return divideExact(numerator, denominator).intValueExact();
    }

    @Override
    public long combination(@NotNull long[] array) {
        // C(a0, a1, ..., an) = (a0 + a1 + ... + an)! / (a0! * a1! * ... * an!)
        BigInteger numerator = BigInteger.ZERO;
        BigInteger denominator = BigInteger.ONE;
        for (long value : array) {
            BigInteger bigValue = BigInteger.valueOf(value);
            numerator = numerator.add(bigValue);
            denominator = denominator.multiply(factorial(bigValue));
        }
        numerator = factorial(numerator);
        return divideExact(numerator, denominator).longValueExact();
    }

    @NotNull
    @Override
    public BigInteger combination(@NotNull BigInteger[] array) {
        // C(a0, a1, ..., an) = (a0 + a1 + ... + an)! / (a0! * a1! * ... * an!)
        BigInteger numerator = BigInteger.ZERO;
        BigInteger denominator = BigInteger.ONE;
        for (BigInteger value : array) {
            numerator = numerator.add(value);
            denominator = denominator.multiply(factorial(value));
        }
        numerator = factorial(numerator);
        return divideExact(numerator, denominator);
    }

    private int recursiveGcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return recursiveGcd(b, a % b);
    }

    @Override
    public int gcd(int a, int b) {
        if (a <= 0 || b <= 0) {
            throw new ArithmeticException("non-positive gcd");
        }
        return recursiveGcd(a, b);
    }

    private long recursiveGcd(long a, long b) {
        if (b == 0L) {
            return a;
        }
        return recursiveGcd(b, a % b);
    }

    @Override
    public long gcd(long a, long b) {
        if (a <= 0L || b <= 0L) {
            throw new ArithmeticException("non-positive gcd");
        }
        return recursiveGcd(a, b);
    }
}
