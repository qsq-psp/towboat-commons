package indi.um.util.math;

import indi.um.util.value.PublicIntSlot;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by Hiro in mmc on 2018/11/29, named Factor.
 * Recreated in LeetInAction on 2021/7/17.
 * Moved here on 2023/4/1.
 */
@SuppressWarnings("SuspiciousNameCombination")
public final class DiscreteMath {

    private static int ensurePositive(int x) {
        if (x <= 0) {
            throw new IllegalArgumentException();
        }
        return x;
    }

    private static BigInteger ensurePositive(BigInteger x) {
        ensurePositive(x.signum());
        return x;
    }

    /**
     * GCD = greatest common divide
     */
    public static int gcd(int a, int b) {
        if (a <= 0 || b <= 0) {
            throw new IllegalArgumentException();
        }
        return uncheckedGcd(a, b);
    }

    /**
     * https://blog.csdn.net/Dobility/article/details/100608518
     */
    public static int gcd(int[] array) {
        final int len = array.length;
        if (len <= 2) {
            if (len == 2) {
                return gcd(array[0], array[1]);
            } else if (len == 1) {
                return ensurePositive(array[0]);
            } else {
                throw new IllegalArgumentException();
            }
        }
        while (true) {
            Arrays.sort(array);
            int x0 = array[0];
            if (x0 == array[len - 1]) {
                return x0;
            }
            for (int i = 1; i < len; i++) {
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

    private static int uncheckedGcd(int a, int b) {
        while (b != 0) {
            int r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    /**
     * For principal display only
     */
    static int recursiveGcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return recursiveGcd(b, a % b);
    }

    @NotNull
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return uncheckedGcd(ensurePositive(a), ensurePositive(b));
    }

    @NotNull
    public static BigInteger gcd(BigInteger[] array) {
        return array[0]; // todo
    }

    @NotNull
    private static BigInteger uncheckedGcd(BigInteger a, BigInteger b) {
        while (b.signum() != 0) {
            BigInteger r = a.remainder(b);
            a = b;
            b = r;
        }
        return a;
    }

    public static int extendedGreatestCommonDivisor(int a, int b, PublicIntSlot x, PublicIntSlot y) {
        return 0; // todo, or change signature
    }

    /**
     * For principal display only
     * Formula: a * x + b * y = gcd
     * C++ signature: int e_gcd(int a, int b, int &x, int &y)
     */
    static int recursiveExtendedGcd(int a, int b, PublicIntSlot x, PublicIntSlot y) {
        if (b == 0) {
            x.value = 1;
            y.value = 0;
            return a;
        }
        final int gcd = recursiveExtendedGcd(b, a % b, y, x);
        y.value -= a / b * x.value;
        return gcd;
    }

    /**
     * LCM = least common multiple
     */
    public static double leastCommonMultiple(int a, int b) {
        ensurePositive(a);
        ensurePositive(b);
        return ((double) a) * b / uncheckedGcd(a, b);
    }

    public static double leastCommonMultiple(int[] array) {
        final int len = array.length;
        if (len <= 2) {
            if (len == 2) {
                return leastCommonMultiple(array[0], array[1]);
            } else if (len == 1) {
                return ensurePositive(array[0]);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return 0; // todo
    }

    @NotNull
    public static BigInteger leastCommonMultiple(BigInteger a, BigInteger b) {
        ensurePositive(a);
        ensurePositive(b);
        return a.multiply(b).divide(uncheckedGcd(a, b));
    }

    public static int inverse(int value, int mod) {
        return 0;
    }

    public static long inverse(long value, long mod) {
        return 0;
    }

    public static int modMultiply(int a, int b, int mod) {
        return (int) ((long) a * b % mod);
    }

    public static long modMultiply(long a, long b, long mod) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

    public static int modPower(int base, int exponent, int mod) {
        if (mod <= 1) {
            if (mod == 1) {
                return 0;
            }
            throw new ArithmeticException();
        }
        if (exponent <= 0) {
            if (exponent == 0) {
                return 1;
            }
            return modPower(inverse(base, mod), exponent, mod);
        }
        int product = base % mod;
        for (int i = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product = modMultiply(product, product, mod);
            if ((exponent & (1 << i)) != 0) {
                product = modMultiply(product, base, mod);
            }
        }
        return product;
    }

    public static long modPower(long base, long exponent, long mod) {
        if (mod <= 1L) {
            if (mod == 1L) {
                return 0L;
            }
            throw new IllegalArgumentException();
        }
        if (exponent <= 0L) {
            if (exponent == 0L) {
                return 1L;
            }
            return modPower(inverse(base, mod), exponent, mod);
        }
        long product = base % mod;
        for (int i = Long.SIZE - 2 - Long.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product = modMultiply(product, product, mod);
            if ((exponent & (1L << i)) != 0) {
                product = modMultiply(product, base, mod);
            }
        }
        return product;
    }

    public static double power(double base, int exponent) {
        if (exponent <= 0) {
            if (exponent == 0) {
                return 1.0;
            } else if (exponent == Integer.MIN_VALUE) {
                return Math.pow(base, exponent);
            } else {
                return power(1.0 / base, -exponent);
            }
        }
        double product = base;
        for (int i = Integer.SIZE - 2 - Integer.numberOfLeadingZeros(exponent); i >= 0; i--) {
            product *= product;
            if ((exponent & (1 << i)) != 0) {
                product *= base;
            }
        }
        return product;
    }

    public static double factorial(int x) {
        double y = 1.0;
        while (x > 1) {
            y *= x;
            x--;
        }
        return y;
    }

    /**
     * For principal display only
     */
    @SuppressWarnings("unused")
    static double recursiveFactorial(int x) {
        if (x > 1) {
            return x * recursiveFactorial(x - 1);
        } else {
            return 1.0;
        }
    }

    public static int modFactorial(int x, int mod) {
        // todo
        return 0;
    }

    public static long modFactorial(int x, long mod) {
        // todo
        return 0;
    }

    @NotNull
    public static BigInteger bigFactorial(int x) {
        BigInteger y = BigInteger.ONE;
        while (x > 1) {
            y = y.multiply(BigInteger.valueOf(x));
            x--;
        }
        return y;
    }

    public static double doubleFactorial(int x) {
        double y = 1.0;
        while (x > 1) {
            y *= x;
            x -= 2;
        }
        return y;
    }

    @NotNull
    public static BigInteger bigDoubleFactorial(int x) {
        BigInteger y = BigInteger.ONE;
        while (x > 1) {
            y = y.multiply(BigInteger.valueOf(x));
            x -= 2;
        }
        return y;
    }

    public static double arrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new IllegalArgumentException();
        }
        double y = 1.0;
        for (int i = 0; i < m; i++) {
            y *= (n - i);
        }
        return y;
    }

    @NotNull
    public static BigInteger bigArrangement(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new IllegalArgumentException();
        }
        BigInteger y = BigInteger.ONE;
        for (int i = 0; i < m; i++) {
            y = y.multiply(BigInteger.valueOf(n - i));
        }
        return y;
    }

    public static double combination(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new IllegalArgumentException();
        }
        m = Math.min(m, n - m);
        double y = 1.0;
        for (int i = 0; i < m; i++) {
            y = y * (n - i) / (i + 1);
        }
        return y;
    }

    public static double combination(int[] array) {
        // todo
        return 0.0;
    }

    /**
     * For principal display only
     */
    @SuppressWarnings("unused")
    static double recursiveCombination(int n, int m) {
        if (m <= 0 || n <= m) { // the argument checking is different
            return 1.0;
        }
        n--;
        return recursiveCombination(n, m) + recursiveCombination(n, m - 1);
    }

    @NotNull
    public static BigInteger bigCombination(int n, int m) {
        if (!(0 <= m && m <= n)) {
            throw new IllegalArgumentException();
        }
        m = Math.min(m, n - m);
        BigInteger y = BigInteger.ONE;
        for (int i = 0; i < m; i++) {
            y = y.multiply(BigInteger.valueOf(n - i));
        }
        for (int i = 1; i <= m; i++) {
            y = y.divide(BigInteger.valueOf(i));
        }
        return y;
    }

    @NotNull
    public static BigInteger bigCombination(int[] array) {
        // todo
        return BigInteger.ZERO;
    }

    public static int intCombination(int n, int m) throws ArithmeticException {
        if (!(0 <= m && m <= n)) {
            throw new IllegalArgumentException();
        }
        m = Math.min(m, n - m);
        final int[] factors = new int[m + 1];
        int size = 0;
        for (int i = 2; i <= m; i++) {
            int value = i;
            for (int j = 0; j < size; j += 2) {
                int prime = factors[j];
                while (value % prime == 0) {
                    value /= prime;
                    factors[j + 1]++;
                }
            }
            if (value != 1) {
                factors[size++] = value;
                factors[size++] = 1;
            }
        }
        int y = 1;
        for (int i = 0; i < m; i++) {
            int value = n - i;
            for (int j = 0; j < size; j += 2) {
                while (factors[j + 1] != 0 && value % factors[j] == 0) {
                    value /= factors[j];
                    factors[j + 1]--;
                }
            }
            y = Math.multiplyExact(y, value);
        }
        return y;
    }

    public static int[][] pascalTriangle(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        final int[][] triangle = new int[n + 1][];
        int[] row0 = null;
        for (int i = 0; i <= n; i++) {
            int[] row1 = new int[i + 1];
            row1[0] = 1;
            for (int j = 1; j < i; j++) {
                row1[j] = row0[j - 1] + row0[j];
            }
            row1[i] = 1;
            triangle[i] = row1;
            row0 = row1;
        }
        return triangle;
    }

    @SuppressWarnings("ConstantConditions")
    public static int[][] halfPascalTriangle(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        final int[][] triangle = new int[n + 1][];
        int[] row0 = null;
        for (int i = 0; i <= n; i++) {
            int[] row1;
            if ((i & 1) == 0) {
                row1 = new int[(i >> 1) + 1];
            } else {
                row1 = new int[(i + 1) >> 1];
            }
            row1[0] = 1;
            for (int j = (i - 1) >> 1; j > 0; j--) {
                row1[j] = row0[j - 1] + row0[j];
            }
            if (i != 0 && (i & 1) == 0) {
                row1[i >> 1] = row0[(i >> 1) - 1] << 1;
            }
            triangle[i] = row1;
            row0 = row1;
        }
        return triangle;
    }

    public static double stirling1(int n, int k) {
        if (!(0 <= k && k <= n)) {
            throw new IllegalArgumentException();
        }
        return uncheckedStirling1(n, k);
    }

    private static double uncheckedStirling1(int n, int k) {
        if (k == n) {
            return 1.0;
        }
        if (k == 0) {
            return 0.0;
        }
        n--;
        return n * uncheckedStirling1(n, k) + uncheckedStirling1(n, k - 1);
    }

    public static double stirling2(int n, int k) {
        if (!(0 <= k && k <= n)) {
            throw new IllegalArgumentException();
        }
        if (k == n) {
            return 1.0;
        }
        if (k == 0) {
            return 0.0;
        }
        double s = 0;
        for (int i = 0; i < k; i++) {
            double t = combination(k, i) * power(k - i, n);
            if ((i & 1) != 0) {
                s -= t;
            } else {
                s += t;
            }
        }
        return s / factorial(k);
    }

    public static double catalan(int x) {
        double y = 1.0;
        for (int i = 1; i < x; i++) {
            y = y * (4 * i + 2) / (i + 2);
        }
        return y;
    }

    @NotNull
    public static BigInteger bigCatalan(int x) {
        BigInteger y = BigInteger.ONE;
        for (int i = 1; i < x; i++) {
            y = y.multiply(BigInteger.valueOf(4 * i + 2)).divide(BigInteger.valueOf(i + 2));
        }
        return y;
    }

    public static double bell(int n) {
        if (n < 2) {
            return 1;
        }
        final double[] array = new double[n + 1];
        array[0] = 1.0;
        array[1] = 1.0;
        for (int i = 1; i < n; i++) {
            double s = 0.0;
            for (int j = 0; j <= i; j++) {
                s += combination(i, j) * array[j];
            }
            array[i + 1] = s;
        }
        return array[n];
    }

    /**
     * For principal display only
     */
    @SuppressWarnings("unused")
    static double recursiveBell(int n) {
        if (n < 2) {
            return 1;
        }
        n--;
        double s = 0.0;
        for (int i = 0; i <= n; i++) {
            s += combination(n, i) * recursiveBell(i);
        }
        return s;
    }

    /**
     * No instance
     */
    private DiscreteMath() {}
}
