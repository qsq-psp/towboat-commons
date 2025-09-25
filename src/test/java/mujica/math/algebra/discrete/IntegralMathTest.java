package mujica.math.algebra.discrete;

import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;

@CodeHistory(date = "2025/3/14")
public class IntegralMathTest {

    private static final int REPEAT = 228;

    private final FuzzyContext fc = new FuzzyContext();

    @Test(expected = ArithmeticException.class)
    public void caseCast2i1() {
        IntegralMath.INSTANCE.cast2i(Integer.MIN_VALUE - 1L);
    }

    @Test
    public void caseCast2i2() {
        Assert.assertEquals(Integer.MIN_VALUE, IntegralMath.INSTANCE.cast2i(Integer.MIN_VALUE));
    }

    @Test
    public void caseCast2i3() {
        Assert.assertEquals(Integer.MAX_VALUE, IntegralMath.INSTANCE.cast2i(Integer.MAX_VALUE));
    }

    @Test(expected = ArithmeticException.class)
    public void caseCast2i4() {
        IntegralMath.INSTANCE.cast2i(Integer.MAX_VALUE + 1L);
    }

    @Test(expected = ArithmeticException.class)
    public void caseAbsInt1() {
        IntegralMath.INSTANCE.abs(Integer.MIN_VALUE);
    }

    @Test
    public void caseAbsInt2() {
        Assert.assertEquals(Integer.MAX_VALUE, IntegralMath.INSTANCE.abs(Integer.MIN_VALUE + 1));
    }

    @Test
    public void caseAbsInt3() {
        Assert.assertEquals(Integer.MAX_VALUE, IntegralMath.INSTANCE.abs(Integer.MAX_VALUE));
    }

    @Test
    public void caseAbsInt4() {
        Assert.assertEquals(0, IntegralMath.INSTANCE.abs(0));
    }

    @Test(expected = ArithmeticException.class)
    public void caseAbsLong1() {
        IntegralMath.INSTANCE.abs(Long.MIN_VALUE);
    }

    @Test
    public void caseAbsLong2() {
        Assert.assertEquals(Long.MAX_VALUE, IntegralMath.INSTANCE.abs(Long.MIN_VALUE + 1L));
    }

    @Test
    public void caseAbsLong3() {
        Assert.assertEquals(Long.MAX_VALUE, IntegralMath.INSTANCE.abs(Long.MAX_VALUE));
    }

    @Test
    public void caseAbsLong4() {
        Assert.assertEquals(0L, IntegralMath.INSTANCE.abs(0L));
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactInt1() {
        IntegralMath.INSTANCE.divideExact(0, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactInt2() {
        IntegralMath.INSTANCE.divideExact(1, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactInt3() {
        IntegralMath.INSTANCE.divideExact(Integer.MIN_VALUE, 0);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactInt4() {
        IntegralMath.INSTANCE.divideExact(17, 3);
    }

    @Test
    public void caseDivideExactInt5() {
        Assert.assertEquals(0, IntegralMath.INSTANCE.divideExact(0, 20));
    }

    @Test
    public void caseDivideExactInt6() {
        Assert.assertEquals(-7, IntegralMath.INSTANCE.divideExact(63, -9));
    }

    @Test
    public void fuzzTriangleInt() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int x = fc.nextInt();
            Object expectResult;
            try {
                expectResult = expectAlgorithm.triangle(x);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.triangle(x);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            Assert.assertEquals(expectResult, actualResult);
        }
    }

    @Test
    public void fuzzTriangleLong() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long x = fc.nextLong();
            Object expectResult;
            try {
                expectResult = expectAlgorithm.triangle(x);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.triangle(x);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            Assert.assertEquals(expectResult, actualResult);
        }
    }

    @Test
    public void fuzzDivideExactInt() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int x = fc.nextInt();
            int y = fc.nextInt();
            Object expectResult;
            try {
                expectResult = expectAlgorithm.divideExact(x, y);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.divideExact(x, y);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            Assert.assertEquals(expectResult, actualResult);
        }
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactLong1() {
        IntegralMath.INSTANCE.divideExact(0L, 0L);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactLong2() {
        IntegralMath.INSTANCE.divideExact(-1L, 0L);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactLong3() {
        IntegralMath.INSTANCE.divideExact(Long.MIN_VALUE, 0L);
    }

    @Test(expected = ArithmeticException.class)
    public void caseDivideExactLong4() {
        IntegralMath.INSTANCE.divideExact(2025L, 2026L);
    }

    @Test
    public void caseDivideExactLong5() {
        Assert.assertEquals(0, IntegralMath.INSTANCE.divideExact(0L, 3L));
    }

    @Test
    public void caseDivideExactLong6() {
        Assert.assertEquals(-11L, IntegralMath.INSTANCE.divideExact(110L, -10L));
    }

    @Test
    public void fuzzDivideExactLong() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long x = fc.nextLong();
            long y = fc.nextLong();
            Object expectResult;
            try {
                expectResult = expectAlgorithm.divideExact(x, y);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.divideExact(x, y);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            Assert.assertEquals(expectResult, actualResult);
        }
    }

    @Test
    public void caseFibonacciInt() {
        final IntegralMath math = IntegralMath.INSTANCE;
        Assert.assertEquals(0, math.fibonacci(0));
        Assert.assertEquals(1, math.fibonacci(1));
        Assert.assertEquals(1, math.fibonacci(2));
        Assert.assertEquals(2, math.fibonacci(3));
        Assert.assertEquals(3, math.fibonacci(4));
        Assert.assertEquals(5, math.fibonacci(5));
        Assert.assertEquals(8, math.fibonacci(6));
        Assert.assertEquals(13, math.fibonacci(7));
        Assert.assertEquals(21, math.fibonacci(8));
    }

    @Test
    public void caseFibonacciLong() {
        final IntegralMath math = IntegralMath.INSTANCE;
        Assert.assertEquals(34L, math.fibonacci(9L));
        Assert.assertEquals(55L, math.fibonacci(10L));
        Assert.assertEquals(89L, math.fibonacci(11L));
        Assert.assertEquals(144L, math.fibonacci(12L));
        Assert.assertEquals(233L, math.fibonacci(13L));
        Assert.assertEquals(377L, math.fibonacci(14L));
    }

    @Test
    public void checkFibonacciCassiniIdentityInt() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (int i = 1; i <= 40; i++) {
            int s = math.fibonacci(i);
            s = math.fibonacci(i + 1) * math.fibonacci(i - 1) - s * s;
            Assert.assertEquals(i % 2 == 0 ? 1 : -1, s);
        }
    }

    @Test
    public void checkFibonacciCassiniIdentityLong() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (long i = 1L; i <= 90L; i++) {
            long s = math.fibonacci(i);
            s = math.fibonacci(i + 1L) * math.fibonacci(i - 1L) - s * s;
            Assert.assertEquals(i % 2L == 0L ? 1L : -1L, s);
        }
    }

    @Test
    public void checkFibonacciTwicePropertyInt() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (int i = 1; i <= 20; i++) {
            Assert.assertEquals(math.fibonacci(2 * i), math.fibonacci(i) * (math.fibonacci(i + 1) + math.fibonacci(i - 1)));
        }
    }

    @Test
    public void checkFibonacciTwicePropertyLong() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (long i = 1L; i <= 45L; i++) {
            Assert.assertEquals(math.fibonacci(2L * i), math.fibonacci(i) * (math.fibonacci(i + 1L) + math.fibonacci(i - 1L)));
        }
    }

    @Test
    public void checkFibonacciGcdInt() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (int a = 2; a <= 40; a++) {
            for (int b = a + 1; b <= 40; b++) {
                int c = math.gcd(a, b);
                if (c == 1) {
                    continue;
                }
                Assert.assertEquals(math.fibonacci(c), math.gcd(math.fibonacci(a), math.fibonacci(b)));
            }
        }
    }

    @Test
    public void checkFibonacciGcdLong() {
        final IntegralMath math = IntegralMath.INSTANCE;
        for (long a = 2L; a <= 90L; a++) {
            for (long b = a + 1L; b <= 90L; b++) {
                long c = math.gcd(a, b);
                if (c == 1L) {
                    continue;
                }
                Assert.assertEquals(math.fibonacci(c), math.gcd(math.fibonacci(a), math.fibonacci(b)));
            }
        }
    }

    @Test
    public void checkArrangementInt1() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int n = fc.nextInt(30);
            int m = fc.nextInt(30);
            Object expectResult;
            try {
                expectResult = expectAlgorithm.arrangement(n, m);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.arrangement(n, m);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            try {
                Assert.assertEquals(expectResult, actualResult);
            } catch (AssertionError e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    @Test
    public void checkArrangementInt2() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            int n = fc.nextInt(34);
            int m = fc.nextInt(n + 1);
            int expectResult;
            try {
                expectResult = expectAlgorithm.arrangement(n, m);
            } catch (ArithmeticException ignore) {
                continue;
            }
            repeatIndex++;
            try {
                Assert.assertEquals(expectResult, actualAlgorithm.arrangement(n, m));
            } catch (Throwable e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    @Test
    public void checkArrangementLong1() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long n = fc.nextLong(30L);
            long m = fc.nextLong(30L);
            Object expectResult;
            try {
                expectResult = expectAlgorithm.arrangement(n, m);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            try {
                actualResult = actualAlgorithm.arrangement(n, m);
            } catch (Throwable throwable) {
                actualResult = throwable.getClass();
            }
            try {
                Assert.assertEquals(expectResult, actualResult);
            } catch (AssertionError e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    @Test
    public void checkArrangementLong2() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            long n = fc.nextLong(34L);
            long m = fc.nextLong(n + 1L);
            long expectResult;
            try {
                expectResult = expectAlgorithm.arrangement(n, m);
            } catch (ArithmeticException ignore) {
                continue;
            }
            repeatIndex++;
            try {
                Assert.assertEquals(expectResult, actualAlgorithm.arrangement(n, m));
            } catch (Throwable e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationInt1() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int n = fc.nextInt(36);
            int m = fc.nextInt(36);
            Object expectResult;
            try {
                expectResult = expectAlgorithm.combination(n, m);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            Throwable suppressed = null;
            try {
                actualResult = actualAlgorithm.combination(n, m);
            } catch (Throwable throwable) {
                suppressed = throwable;
                actualResult = throwable.getClass();
            }
            try {
                Assert.assertEquals(expectResult, actualResult);
            } catch (AssertionError e) {
                System.out.println("n = " + n + ", m = " + m);
                if (suppressed != null) {
                    e.addSuppressed(suppressed);
                }
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationInt2() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            int n = fc.nextInt(40);
            int m = fc.nextInt(n + 1);
            int expectResult;
            try {
                expectResult = expectAlgorithm.combination(n, m);
            } catch (ArithmeticException ignore) {
                continue;
            }
            repeatIndex++;
            try {
                Assert.assertEquals(expectResult, actualAlgorithm.combination(n, m));
            } catch (Throwable e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationLong1() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long n = fc.nextLong(36L);
            long m = fc.nextLong(36L);
            Object expectResult;
            try {
                expectResult = expectAlgorithm.combination(n, m);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            Throwable suppressed = null;
            try {
                actualResult = actualAlgorithm.combination(n, m);
            } catch (Throwable throwable) {
                suppressed = throwable;
                actualResult = throwable.getClass();
            }
            try {
                Assert.assertEquals(expectResult, actualResult);
            } catch (AssertionError e) {
                System.out.println("n = " + n + ", m = " + m);
                if (suppressed != null) {
                    e.addSuppressed(suppressed);
                }
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationLong2() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            long n = fc.nextLong(40L);
            long m = fc.nextLong(n + 1L);
            long expectResult;
            try {
                expectResult = expectAlgorithm.combination(n, m);
            } catch (ArithmeticException ignore) {
                continue;
            }
            repeatIndex++;
            try {
                Assert.assertEquals(expectResult, actualAlgorithm.combination(n, m));
            } catch (Throwable e) {
                System.out.println("n = " + n + ", m = " + m);
                throw e;
            }
        }
    }

    /**
     * C(n, 0) = 1
     */
    @Test
    public void checkCombinationFormula1() {
        final IntegralMath algorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Assert.assertEquals(1, algorithm.combination(fc.nextInt(Integer.MAX_VALUE), 0));
            Assert.assertEquals(1L, algorithm.combination(fc.nextLong(Long.MAX_VALUE), 0));
            Assert.assertEquals(BigInteger.ONE, algorithm.combination(fc.nextBigInteger(128), BigInteger.ZERO));
        }
    }

    /**
     * C(n, n) = 1
     * C(n, 1) = C(n, n - 1) = n
     */
    @Test
    public void checkCombinationFormula2() {
        final IntegralMath algorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int n = fc.nextInt(Integer.MAX_VALUE) + 1;
            Assert.assertEquals(n, algorithm.combination(n, 1));
            Assert.assertEquals(n, algorithm.combination(n, n - 1));
            Assert.assertEquals(1, algorithm.combination(n, n));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long n = fc.nextLong(Long.MAX_VALUE) + 1L;
            Assert.assertEquals(n, algorithm.combination(n, 1L));
            Assert.assertEquals(n, algorithm.combination(n, n - 1L));
            Assert.assertEquals(1L, algorithm.combination(n, n));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            BigInteger n = fc.nextBigInteger(128).add(BigInteger.ONE);
            Assert.assertEquals(n, algorithm.combination(n, BigInteger.ONE));
            Assert.assertEquals(n, algorithm.combination(n, n.subtract(BigInteger.ONE)));
            Assert.assertEquals(BigInteger.ONE, algorithm.combination(n, n));
        }
    }

    /**
     * C(n, m) = C(n - 1, m - 1) + C(n - 1, m)
     */
    @Test
    public void checkCombinationFormula3() {
        final IntegralMath algorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int n = fc.nextInt(2000) + 4; // add 4 to ensure random range of m
            int m = fc.nextInt(2, n - 1); // avoid trivial cases like 0, 1, n - 1, n
            Assert.assertEquals(
                    algorithm.combination(BigInteger.valueOf(n), BigInteger.valueOf(m)),
                    algorithm.combination(BigInteger.valueOf(n - 1), BigInteger.valueOf(m - 1))
                            .add(algorithm.combination(BigInteger.valueOf(n - 1), BigInteger.valueOf(m)))
            );
        }
    }

    @Test
    public void caseCombinationIntArray1() {
        Assert.assertEquals(6, IntegralMath.INSTANCE.combination(new int[] {1, 1, 1}));
        Assert.assertEquals(90, IntegralMath.INSTANCE.combination(new int[] {2, 2, 2}));
        Assert.assertEquals(1680, IntegralMath.INSTANCE.combination(new int[] {3, 3, 3}));
    }

    @Test
    public void caseCombinationIntArray2() {
        Assert.assertEquals(12, IntegralMath.INSTANCE.combination(new int[] {1, 1, 2}));
        Assert.assertEquals(20, IntegralMath.INSTANCE.combination(new int[] {1, 1, 3}));
        Assert.assertEquals(210, IntegralMath.INSTANCE.combination(new int[] {2, 2, 3}));
        Assert.assertEquals(420, IntegralMath.INSTANCE.combination(new int[] {2, 2, 4}));
    }

    @Test
    public void checkCombinationIntArray1() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int length = fc.nextInt(10);
            int[] array = new int[length];
            for (int index = 0; index < length; index++) {
                array[index] = fc.nextMinInt(10, 4);
            }
            Object expectResult;
            try {
                expectResult = expectAlgorithm.combination(array);
            } catch (Throwable throwable) {
                expectResult = throwable.getClass();
            }
            Object actualResult;
            Throwable suppressed = null;
            try {
                actualResult = actualAlgorithm.combination(array);
            } catch (Throwable throwable) {
                suppressed = throwable;
                actualResult = throwable.getClass();
            }
            try {
                Assert.assertEquals(expectResult, actualResult);
            } catch (AssertionError e) {
                System.out.println("array = " + Arrays.toString(array));
                if (suppressed != null) {
                    e.addSuppressed(suppressed);
                }
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationIntArray2() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            int length = fc.nextInt(10);
            int[] array = new int[length];
            for (int index = 0; index < length; index++) {
                array[index] = fc.nextMinInt(10, 4);
            }
            int expectResult;
            try {
                expectResult = expectAlgorithm.combination(array);
            } catch (ArithmeticException ignore) {
                continue;
            }
            repeatIndex++;
            try {
                Assert.assertEquals(expectResult, actualAlgorithm.combination(array));
            } catch (Throwable e) {
                System.out.println("array = " + Arrays.toString(array));
                throw e;
            }
        }
    }

    @Test
    public void checkCombinationBigArray() {
        final IntegralMath expectAlgorithm = new BaselineIntegralMath();
        final IntegralMath actualAlgorithm = new IntegralMath();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int length = fc.nextInt(20);
            BigInteger[] array = new BigInteger[length];
            for (int index = 0; index < length; index++) {
                array[index] = BigInteger.valueOf(fc.nextMinInt(20, 4));
            }
            try {
                Assert.assertEquals(
                        expectAlgorithm.combination(array),
                        actualAlgorithm.combination(array)
                );
            } catch (AssertionError e) {
                System.out.println("array = " + Arrays.toString(array));
                throw e;
            }
        }
    }

    /**
     * OEIS A000166
     */
    @Test
    public void caseDerangementBig() {
        final IntegralMath algorithm = IntegralMath.INSTANCE;
        Assert.assertEquals(1, algorithm.derangement(BigInteger.ZERO).longValueExact());
        Assert.assertEquals(0, algorithm.derangement(BigInteger.ONE).longValueExact());
        Assert.assertEquals(1, algorithm.derangement(BigInteger.valueOf(2)).longValueExact());
        Assert.assertEquals(2, algorithm.derangement(BigInteger.valueOf(3)).longValueExact());
        Assert.assertEquals(9, algorithm.derangement(BigInteger.valueOf(4)).longValueExact());
        Assert.assertEquals(44, algorithm.derangement(BigInteger.valueOf(5)).longValueExact());
        Assert.assertEquals(265, algorithm.derangement(BigInteger.valueOf(6)).longValueExact());
    }
}
