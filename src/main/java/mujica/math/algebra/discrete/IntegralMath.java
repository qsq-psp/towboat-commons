package mujica.math.algebra.discrete;

import mujica.ds.of_int.PublicIntSlot;
import mujica.math.algebra.prime.Decomposer;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hiro in mmc on 2018/11/29, named Factor.
 * Recreated in LeetInAction on 2021/7/17, named DiscreteMath.
 * Moved into Ultramarine on 2023/4/1.
 * Recreated on 2025/2/26.
 */
public class IntegralMath {

    public static final IntegralMath INSTANCE = new IntegralMath();

    public int cast2i(long value) {
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new ArithmeticException();
        }
        return (int) value;
    }

    public int abs(int a) {
        if (a < 0) {
            if (a == Integer.MIN_VALUE) {
                throw new ArithmeticException("Absolute value overflow");
            }
            return -a;
        } else {
            return a;
        }
    }

    public long abs(long a) {
        if (a < 0) {
            if (a == Long.MIN_VALUE) {
                throw new ArithmeticException("Absolute value overflow");
            }
            return -a;
        } else {
            return a;
        }
    }

    @NotNull
    public BigInteger abs(@NotNull BigInteger a) {
        return a.abs();
    }

    public int increment(int a) {
        return Math.incrementExact(a);
    }

    public long increment(long a) {
        return Math.incrementExact(a);
    }

    @NotNull
    public BigInteger increment(@NotNull BigInteger a) {
        return a.add(BigInteger.ONE);
    }

    public int decrement(int a) {
        return Math.decrementExact(a);
    }

    public long decrement(long a) {
        return Math.decrementExact(a);
    }

    @NotNull
    public BigInteger decrement(@NotNull BigInteger a) {
        return a.subtract(BigInteger.ONE);
    }

    public int add(int x, int y) {
        return Math.addExact(x, y);
    }

    public long add(long x, long y) {
        return Math.addExact(x, y);
    }

    @NotNull
    public BigInteger add(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.add(y);
    }

    public int subtract(int x, int y) {
        return Math.subtractExact(x, y);
    }

    public long subtract(long x, long y) {
        return Math.subtractExact(x, y);
    }

    @NotNull
    public BigInteger subtract(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.subtract(y);
    }

    public int multiply(int x, int y) {
        return Math.multiplyExact(x, y);
    }

    public long multiply(long x, long y) {
        return Math.multiplyExact(x, y);
    }

    @NotNull
    public BigInteger multiply(@NotNull BigInteger x, @NotNull BigInteger y) {
        return x.multiply(y);
    }

    public int dotMultiply(@NotNull int[] x, @NotNull int[] y) {
        final int n = Math.min(x.length, y.length);
        int s = 0;
        for (int i = 0; i < n; i++) {
            s = add(s, multiply(x[i], y[i]));
        }
        return s;
    }

    @NotNull
    public int[][] matMultiply(@NotNull int[][] x, @NotNull int[][] y) {
        return x; // ...
    }

    public int multiplyFraction(int number, int numerator, int denominator) {
        return cast2i(divideExact(
                ((long) number) * numerator,
                denominator
        ));
    }

    public long multiplyFraction(long number, long numerator, long denominator) {
        return 0L; // ...
    }

    /**
     * @return x * (x + 1) / 2
     * The result is always non-negative
     * 65535 * (65535 + 1) / 2 = 2147450880 < 2147483647 = Integer.MAX_VALUE
     * 65536 * (65536 + 1) / 2 = 2147516416 > 2147483647 = Integer.MAX_VALUE
     * 92681 * (92681 + 1) / 2 = 4294930221 < 4294967296 = 2 ** 32
     * 92682 * (92682 + 1) / 2 = 4295022903 > 4294967296 = 2 ** 32
     */
    public int triangle(int x) {
        if (-65536 <= x && x < 65536) {
            return (x * (x + 1)) >>> 1;
        } else {
            throw new ArithmeticException();
        }
    }

    /**
     * @return x * (x + 1) / 2
     * The result is always non-negative
     * 4294967295 * (4294967295 + 1) / 2 = 9223372034707292160 < 9223372036854775807 = Long.MAX_VALUE
     * 4294967296 * (4294967296 + 1) / 2 = 9223372039002259456 > 9223372036854775807 = Long.MAX_VALUE
     * 6074001000 * (6074001000 + 1) / 2 = 18446744070963499500 < 18446744073709551616 = 2 ** 64
     * 6074000999 * (6074000999 + 1) / 2 = 18446744077037500500 > 18446744073709551616 = 2 ** 64
     */
    public long triangle(long x) {
        if (-4294967296L <= x && x < 4294967296L) {
            return (x * (x + 1L)) >>> 1;
        } else {
            throw new ArithmeticException();
        }
    }

    public int divideExact(int x, int y) {
        final int q = x / y;
        if (x != Math.multiplyExact(q, y)) {
            throw new ArithmeticException();
        }
        return q;
    }

    public long divideExact(long x, long y) {
        final long q = x / y;
        if (x != Math.multiplyExact(q, y)) {
            throw new ArithmeticException();
        }
        return q;
    }

    @NotNull
    public BigInteger divideExact(@NotNull BigInteger x, @NotNull BigInteger y) {
        final BigInteger[] result = x.divideAndRemainder(y);
        if (result[1].signum() != 0) {
            throw new ArithmeticException("Can not divide exact");
        }
        return result[0];
    }

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
        for (int shift = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); shift >= 0; shift--) {
            product = Math.multiplyExact(product, product);
            if ((exponent & (1 << shift)) != 0) {
                product = Math.multiplyExact(product, base);
            }
        }
        return product;
    }

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
        for (int shift = Long.SIZE - 2 - Long.numberOfLeadingZeros(exponent); shift >= 0; shift--) {
            product = Math.multiplyExact(product, product);
            if ((exponent & (1L << shift)) != 0) {
                product = Math.multiplyExact(product, base);
            }
        }
        return product;
    }

    protected static final BigInteger BIG_MINUS_ONE = BigInteger.valueOf(-1L);

    private static final BigInteger BIG_POWER_THRESHOLD = BigInteger.valueOf(38064L); // Used by both power and factorial

    @NotNull
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
        if (BigInteger.ONE.equals(base) || BigInteger.ZERO.equals(base)) {
            return base;
        }
        if (BIG_MINUS_ONE.equals(base)) {
            if (exponent.testBit(0)) { // odd
                return BIG_MINUS_ONE;
            } else { // even
                return BigInteger.ONE;
            }
        }
        if (exponent.compareTo(BIG_POWER_THRESHOLD) > 0) {
            throw new ArithmeticException("Assumed power overflow");
        }
        return base.pow(exponent.intValue());
    }

    /**
     * 12! = 479001600 < 2147483647 = Integer.MAX_VALUE
     * 23! = 6227020800 > 2147483647 = Integer.MAX_VALUE
     */
    public int factorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a > 12) {
            throw new ArithmeticException("Factorial overflow");
        }
        int f = 1;
        for (int i = 2; i <= a; i++) {
            f *= i;
        }
        return f;
    }

    /**
     * 20! = 2432902008176640000 < 9223372036854775807 = Long.MAX_VALUE
     * 21! = 51090942171709440000 > 9223372036854775807 = Long.MAX_VALUE
     */
    public long factorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a > 20L) {
            throw new ArithmeticException("Factorial overflow");
        }
        long f = 1;
        for (long i = 2; i <= a; i++) {
            f *= i;
        }
        return f;
    }

    @NotNull
    public BigInteger factorial(@NotNull BigInteger a) {
        if (a.signum() < 0) {
            throw new ArithmeticException("Factorial negative");
        }
        if (a.compareTo(BIG_POWER_THRESHOLD) > 0) {
            throw new ArithmeticException("Assumed factorial overflow");
        }
        final int ia = a.intValue();
        BigInteger f = BigInteger.ONE;
        for (int i = 2; i <= ia; i++) {
            f = f.multiply(BigInteger.valueOf(i));
        }
        return f;
    }

    /**
     * 19!! = 654729075 < 2147483647 = Integer.MAX_VALUE
     * 20!! = 3715891200 > 2147483647 = Integer.MAX_VALUE
     */
    public int doubleFactorial(int a) {
        if (a < 0) {
            throw new ArithmeticException("Double factorial negative");
        }
        if (a > 19) {
            throw new ArithmeticException("Double factorial overflow");
        }
        int product = 1;
        for (; a >= 2; a -= 2) {
            product *= a;
        }
        return product;
    }

    /**
     * 33!! = 6332659870762850625 < 9223372036854775807 = Long.MAX_VALUE
     * 34!! = 46620662575398912000 > 9223372036854775807 = Long.MAX_VALUE
     */
    public long doubleFactorial(long a) {
        if (a < 0L) {
            throw new ArithmeticException("Double factorial negative");
        }
        if (a > 33L) {
            throw new ArithmeticException("Double factorial overflow");
        }
        long product = 1L;
        for (; a >= 2L; a -= 2L) {
            product *= a;
        }
        return product;
    }

    @NotNull
    public BigInteger doubleFactorial(@NotNull BigInteger a) {
        int ia = a.intValueExact();
        if (ia < 2) {
            if (ia < 0) {
                throw new ArithmeticException("Double factorial negative");
            }
            return BigInteger.ONE;
        }
        BigInteger product = a;
        for (ia -= 2; ia >= 2; ia -= 2) {
            product = product.multiply(BigInteger.valueOf(ia));
        }
        return a;
    }

    public int fibonacci(int i) {
        if (i < 2) {
            if (i < 0) {
                throw new ArithmeticException("fibonacci negative");
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
            int n10 = m10 * m00 + m11 * m10; // but always n01 == n10
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
                throw new ArithmeticException("fibonacci negative");
            }
            return i;
        }
        i--;
        long m00 = 1;
        long m01 = 1;
        long m10 = 1;
        long m11 = 0;
        for (int shift = Long.SIZE - 2 - Long.numberOfLeadingZeros(i); shift >= 0; shift--) {
            long n00 = m00 * m00 + m01 * m10;
            long n01 = m00 * m01 + m01 * m11;
            long n10 = m10 * m00 + m11 * m10; // but always n01 == n10
            long n11 = m10 * m01 + m11 * m11;
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
        return m00;
    }

    @NotNull
    public BigInteger fibonacci(@NotNull BigInteger i) {
        int ii = i.intValueExact();
        if (ii < 2) {
            if (ii < 0) {
                throw new ArithmeticException("Fibonacci negative");
            }
            return i;
        }
        ii--;
        BigInteger m00 = BigInteger.ONE;
        BigInteger m01 = BigInteger.ONE;
        BigInteger m10 = BigInteger.ONE;
        BigInteger m11 = BigInteger.ZERO;
        for (int shift = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(ii); shift >= 0; shift--) {
            BigInteger n00 = m00.multiply(m00).add(m01.multiply(m10));
            BigInteger n01 = m01.multiply(m00.add(m11));
            BigInteger n10 = m10.multiply(m00.add(m11)); // but always n01 == n10
            BigInteger n11 = m10.multiply(m01).add(m11.multiply(m11));
            if ((ii & (1 << shift)) != 0) {
                m00 = n00.add(n01);
                m01 = n00;
                m10 = n10.add(n11);
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

    public int arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        int product = 1;
        for (int i = n - m + 1; i <= n; i++) {
            product = Math.multiplyExact(product, i);
        }
        return product;
    }

    public long arrangement(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        long product = 1L;
        for (long i = n - m + 1; i <= n; i++) {
            product = Math.multiplyExact(product, i);
        }
        return product;
    }

    public BigInteger arrangement(@NotNull BigInteger n, @NotNull BigInteger m) {
        final int signM = m.signum();
        if (!(0 <= signM && m.compareTo(n) <= 0)) {
            throw new ArithmeticException("Bad arrangement arguments");
        }
        if (signM == 0) {
            return BigInteger.ONE;
        }
        BigInteger product = n;
        for (BigInteger i = n.subtract(m).add(BigInteger.ONE); i.compareTo(n) < 0; i = i.add(BigInteger.ONE)) {
            product = product.multiply(i);
        }
        return product;
    }

    public int combination(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        if (m == 0) {
            return 1;
        }
        int w = n - m + 1;
        for (int i = m - 2; i >= 0; i--) {
            w = multiplyFraction(w, n - i, m - i);
        }
        return w;
    }

    public long combination(long n, long m) {
        if (!(0L <= m && m <= n)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        m = Math.min(m, n - m); // C(n, m) = C(n, n - m)
        if (m <= 2L) { // long value can not use switch
            if (m == 0L) {
                return 1L;
            } else if (m == 1L) {
                return n;
            } else {
                return triangle(n - 1L);
            }
        }
        if (n > 28L) {
            if (n > 3810779L) {
                throw new ArithmeticException();
            }
            int in = (int) n;
            int im = (int) m;
            Decomposer math = Decomposer.INSTANCE;
            HashMap<Integer, PublicIntSlot> wm = new HashMap<>();
            for (int i = 0; i < im; i++) {
                math.factorize(in - i, ((factor, times) -> PublicIntSlot.add(wm, factor, times))); // multiply
                math.factorize(i + 1, ((factor, times) -> PublicIntSlot.add(wm, factor, -times))); // divide
            }
            long w = 1L;
            for (Map.Entry<Integer, PublicIntSlot> entry : wm.entrySet()) {
                w = multiply(w, power(entry.getKey(), entry.getValue().value)); // negative exponents detected and throw; no assert
            }
            return w;
        } else {
            long w = n;
            for (long i = 1; i < m; i++) {
                w *= n - i;
            }
            for (long i = 2; i <= m; i++) {
                assert w % i == 0;
                w /= i;
            }
            return w;
        }
    }

    public BigInteger combination(@NotNull BigInteger n, @NotNull BigInteger m) {
        if (!(0 <= m.signum() && m.compareTo(n) <= 0)) {
            throw new ArithmeticException("Bad combination arguments");
        }
        {
            BigInteger em = n.subtract(m);
            if (em.compareTo(m) < 0) {
                m = em;
            }
        }
        int im = m.intValueExact();
        if (im == 0) {
            return BigInteger.ONE;
        }
        BigInteger w = n.subtract(m).add(BigInteger.ONE);
        for (int i = im - 2; i >= 0; i--) {
            BigInteger bi = BigInteger.valueOf(i);
            w = w.multiply(n.subtract(bi)).divide(m.subtract(bi));
        }
        return w;
    }

    public int combination(@NotNull int[] array) {
        final int length = array.length;
        if (length < 2) {
            return 1;
        }
        int mk = 0;
        int s = array[0];
        for (int k = 1; k < length; k++) {
            int m = array[k];
            if (m < 0) {
                throw new ArithmeticException("Negative combination element");
            }
            if (m > s) {
                mk = k;
                s = m;
            }
        }
        int w = 1;
        for (int k = 0; k < length; k++) {
            if (k == mk) {
                continue;
            }
            int m = array[k];
            if (m == 0) {
                continue;
            }
            s = Math.incrementExact(s);
            w = Math.multiplyExact(w, s);
            for (int i = 2; i <= m; i++) {
                s = Math.incrementExact(s);
                w = multiplyFraction(w, s, i);
            }
        }
        return w;
    }

    public long combination(@NotNull long[] array) {
        final int length = array.length;
        long sum = 0;
        int index = 0;
        while (index < length) {
            long value = array[index++];
            if (value < 0L) {
                throw new ArithmeticException("Negative combination element");
            }
            if (value > 0L) {
                sum = value;
                break;
            }
        }
        long combination = 1L;
        return combination;
    }

    @NotNull
    public BigInteger combination(@NotNull BigInteger[] array) {
        int length = array.length;
        if (length < 2) {
            return BigInteger.ONE;
        }
        int mk = 0;
        BigInteger s = array[0];
        for (int k = 1; k < length; k++) {
            BigInteger m = array[k];
            if (m.signum() < 0) {
                throw new ArithmeticException("Negative combination element");
            }
            if (m.compareTo(s) > 0) {
                mk = k;
                s = m;
            }
        }
        BigInteger w = BigInteger.ONE;
        for (int k = 0; k < length; k++) {
            if (k == mk) {
                continue;
            }
            BigInteger m = array[k];
            int im = m.intValueExact();
            if (im == 0) {
                continue;
            }
            s = s.add(BigInteger.ONE);
            w = w.multiply(s);
            for (int i = 2; i <= im; i++) {
                s = s.add(BigInteger.ONE);
                w = w.multiply(s).divide(BigInteger.valueOf(i));
            }
        }
        return w;
    }

    /**
     * https://oi-wiki.org/math/combinatorics/derangement/
     */
    @NotNull
    public BigInteger derangement(@NotNull BigInteger i) {
        if (i.signum() < 0) {
            throw new ArithmeticException();
        }
        if (i.compareTo(BIG_POWER_THRESHOLD) > 0) {
            throw new ArithmeticException("Assumed derangement overflow");
        }
        final int ii = i.intValue();
        BigInteger sum = BigInteger.ZERO;
        BigInteger product = BigInteger.ONE;
        for (int k = ii; k > 0; k--) {
            if ((k & 1) == 0) { // even
                sum = sum.add(product);
            } else {
                sum = sum.subtract(product);
            }
            product = product.multiply(BigInteger.valueOf(k));
        }
        sum = sum.add(product);
        return sum;
    }

    public int gcd(int a, int b) {
        if (a <= 0 || b <= 0) {
            throw new ArithmeticException("non-positive gcd");
        }
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public long gcd(long a, long b) {
        if (a <= 0L || b <= 0L) {
            throw new ArithmeticException("non-positive gcd");
        }
        while (b != 0) {
            long r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public int gcd(int[] array) {
        final int length = array.length;
        if (length <= 2) {
            if (length == 2) {
                return gcd(array[0], array[1]);
            } else if (length == 1) {
                int value = array[0];
                if (value <= 0) {
                    throw new ArithmeticException("Non-positive gcd");
                }
                return value;
            } else {
                throw new ArithmeticException("Empty array gcd");
            }
        }
        while (true) {
            Arrays.sort(array);
            int x0 = array[0];
            if (x0 == array[length - 1]) {
                return x0;
            }
            for (int i = 1; i < length; i++) {
                int x1 = array[i];
                int c = x1 % x0;
                if (c == 0) {
                    c = x0;
                }
                array[i] = c;
                x0 = x1;
            }
        }
    }

    public int floorSquareRoot(int x) {
        if (x <= 0) {
            if (x == 0) {
                return 0;
            }
            throw new ArithmeticException();
        }
        int a = x;
        while (true) {
            int b = (a + x / a) >>> 1;
            if (b >= a) {
                return a;
            }
            a = b;
        }
    }

    public long floorSquareRoot(long x) {
        if (x <= 0L) {
            if (x == 0L) {
                return 0L;
            }
            throw new ArithmeticException();
        }
        long a = x;
        while (true) {
            long b = (a + x / a) >>> 1;
            if (b >= a) {
                return a;
            }
            a = b;
        }
    }
}
