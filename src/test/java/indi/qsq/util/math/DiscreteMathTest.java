package indi.qsq.util.math;

import indi.qsq.util.random.FuzzyContext;
import indi.qsq.util.random.RandomContext;
import indi.qsq.util.value.PublicIntSlot;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created in LeetInAction on 2021/7/18.
 * Moved here on 2023/4/2.
 */
public class DiscreteMathTest {

    private final FuzzyContext fc = new FuzzyContext();

    private static final int REPEAT = 550;

    protected static final double EPSILON = 0x1P-4;

    @Test
    public void caseGcd() {
        assertEquals(1, DiscreteMath.gcd(1, 1));
        assertEquals(12, DiscreteMath.gcd(12, 12));
        assertEquals(30, DiscreteMath.gcd(30, 270));
        assertEquals(23, DiscreteMath.gcd(46, 23));
        assertEquals(8, DiscreteMath.gcd(16, 24));
        assertEquals(1, DiscreteMath.gcd(39, 22));
        assertEquals(5, DiscreteMath.gcd(25, 95));
    }

    @Test
    @Category(RandomContext.class)
    public void testGcd() {
        for (int index = 0; index < REPEAT; index++) {
            int a = fc.nextInt(1, Integer.MAX_VALUE);
            int b = fc.nextInt(1, Integer.MAX_VALUE);
            int gcd = DiscreteMath.gcd(a, b);
            assertEquals(0, a % gcd);
            assertEquals(0, b % gcd);
        }
    }

    @Test
    public void caseArrayGcd() {
        assertEquals(1, DiscreteMath.gcd(new int[] {1}));
        assertEquals(64, DiscreteMath.gcd(new int[] {64}));
        assertEquals(1, DiscreteMath.gcd(new int[] {1, 1}));
        assertEquals(7, DiscreteMath.gcd(new int[] {7, 7}));
        assertEquals(13, DiscreteMath.gcd(new int[] {13, 13, 13}));
        assertEquals(1, DiscreteMath.gcd(new int[] {1, 1, 1, 1}));
        assertEquals(2, DiscreteMath.gcd(new int[] {2, 4, 6}));
        assertEquals(1, DiscreteMath.gcd(new int[] {2, 4, 9}));
        assertEquals(42, DiscreteMath.gcd(new int[] {84, 42, 42}));
        assertEquals(7, DiscreteMath.gcd(new int[] {63, 21, 35}));
    }

    @Test
    public void caseRecursiveGcd() {
        assertEquals(1, DiscreteMath.recursiveGcd(1, 1));
        assertEquals(1, DiscreteMath.recursiveGcd(1, 2024));
        assertEquals(4, DiscreteMath.recursiveGcd(2024, 4));
        assertEquals(90, DiscreteMath.recursiveGcd(90, 90));
        assertEquals(30, DiscreteMath.recursiveGcd(30, 2700));
        assertEquals(23, DiscreteMath.recursiveGcd(460, 23));
        assertEquals(8, DiscreteMath.recursiveGcd(16, 40));
        assertEquals(1, DiscreteMath.recursiveGcd(47, 22));
        assertEquals(5, DiscreteMath.recursiveGcd(25, 115));
    }

    @Test
    @Category(RandomContext.class)
    public void testRecursiveExtendedGcd() {
        final PublicIntSlot x = new PublicIntSlot();
        final PublicIntSlot y = new PublicIntSlot();
        for (int index = 0; index < REPEAT; index++) {
            int a = fc.nextInt(1, Integer.MAX_VALUE);
            int b = fc.nextInt(1, Integer.MAX_VALUE);
            x.value = Integer.MIN_VALUE;
            y.value = Integer.MIN_VALUE;
            int gcd = DiscreteMath.recursiveExtendedGcd(a, b, x, y);
            assertEquals(0, a % gcd);
            assertEquals(0, b % gcd);
            assertEquals(gcd, a * x.value + b * y.value);
        }
    }

    @Test
    public void caseBigGcd() {
        assertEquals(BigInteger.ONE, DiscreteMath.gcd(BigInteger.ONE, BigInteger.ONE));
        assertEquals(BigInteger.ONE, DiscreteMath.gcd(BigInteger.ONE, BigInteger.TWO));
        assertEquals(BigInteger.ONE, DiscreteMath.gcd(BigInteger.TWO, BigInteger.ONE));
        assertEquals(BigInteger.TWO, DiscreteMath.gcd(BigInteger.TWO, BigInteger.TWO));
        assertEquals(BigInteger.TEN, DiscreteMath.gcd(BigInteger.TEN, BigInteger.TEN));
    }

    @Test
    @Category(RandomContext.class)
    public void testBigGcd() {
        for (int index = 0; index < REPEAT; index++) {
            BigInteger a = BigInteger.valueOf(fc.nextLong(1, Long.MAX_VALUE));
            BigInteger b = BigInteger.valueOf(fc.nextLong(1, Long.MAX_VALUE));
            BigInteger gcd = DiscreteMath.gcd(a, b);
            assertEquals(BigInteger.ZERO, a.remainder(gcd));
            assertEquals(BigInteger.ZERO, b.remainder(gcd));
        }
    }

    @Test
    @Category(RandomContext.class)
    public void testBigLcm() {
        for (int index = 0; index < REPEAT; index++) {
            BigInteger a = BigInteger.valueOf(fc.nextLong(1, Long.MAX_VALUE));
            BigInteger b = BigInteger.valueOf(fc.nextLong(1, Long.MAX_VALUE));
            BigInteger lcm = DiscreteMath.leastCommonMultiple(a, b);
            assertEquals(BigInteger.ZERO, lcm.remainder(a));
            assertEquals(BigInteger.ZERO, lcm.remainder(b));
        }
    }

    @Test
    public void caseModPower() {
        assertEquals(1, DiscreteMath.modPower(9, 0, 5));
        assertEquals(1, DiscreteMath.modPower(1, 1, 6));
        assertEquals(2, DiscreteMath.modPower(2, 1, 7));
        assertEquals(9, DiscreteMath.modPower(3, 2, 10));
    }

    @Test
    public void caseLongModPower() {
        assertEquals(1L, DiscreteMath.modPower(9L, 0L, 15L));
        assertEquals(1L, DiscreteMath.modPower(1L, 1L, 17L));
        assertEquals(2L, DiscreteMath.modPower(2L, 1L, 19L));
        assertEquals(9L, DiscreteMath.modPower(3L, 2L, 21L));
    }

    @Test
    public void casePower() {
        assertEquals(1.0, DiscreteMath.power(2020.0, 0), EPSILON);
        assertEquals(3.0, DiscreteMath.power(3.0, 1), EPSILON);
        assertEquals(Math.pow(77.0, 9), DiscreteMath.power(77.0, 9), EPSILON);
        assertEquals(Math.pow(-25.0, 12), DiscreteMath.power(-25.0, 12), EPSILON);
        assertEquals(Math.pow(Math.PI, -7), DiscreteMath.power(Math.PI, -7), EPSILON);
        assertEquals(Math.exp(7), DiscreteMath.power(Math.E, 7), EPSILON);
    }

    @Test
    public void caseFactorial() {
        assertEquals(1.0, DiscreteMath.factorial(0), EPSILON);
        assertEquals(1.0, DiscreteMath.factorial(1), EPSILON);
        assertEquals(2.0, DiscreteMath.factorial(2), EPSILON);
        assertEquals(362880.0, DiscreteMath.factorial(9), EPSILON);
        assertEquals(87178291200.0, DiscreteMath.factorial(14), EPSILON);
    }

    @Test
    public void caseBigFactorial() {
        assertEquals(new BigInteger("87178291200"), DiscreteMath.bigFactorial(14));
    }

    @Test
    public void caseDoubleFactorial() {
        assertEquals(1.0, DiscreteMath.doubleFactorial(0), EPSILON);
        assertEquals(2.0, DiscreteMath.doubleFactorial(2), EPSILON);
        assertEquals(384.0, DiscreteMath.doubleFactorial(8), EPSILON);
        assertEquals(645120.0, DiscreteMath.doubleFactorial(14), EPSILON);
        assertEquals(1.0, DiscreteMath.doubleFactorial(1), EPSILON);
        assertEquals(3.0, DiscreteMath.doubleFactorial(3), EPSILON);
        assertEquals(135135.0, DiscreteMath.doubleFactorial(13), EPSILON);
    }

    @Test
    public void caseArrangement() {
        assertEquals(1.0, DiscreteMath.arrangement(1, 1), EPSILON);
        assertEquals(720.0, DiscreteMath.arrangement(6, 6), EPSILON);
        assertEquals(40320.0, DiscreteMath.arrangement(8, 8), EPSILON);
        assertEquals(1.0, DiscreteMath.arrangement(722, 0), EPSILON);
        assertEquals(30.0, DiscreteMath.arrangement(30, 1), EPSILON);
        assertEquals(20.0 * 19.0 * 18.0, DiscreteMath.arrangement(20, 3), EPSILON);
    }

    @Test
    public void caseCombination() {
        assertEquals(1.0, DiscreteMath.combination(330, 0), EPSILON);
        assertEquals(1.0, DiscreteMath.combination(217, 217), EPSILON);
        assertEquals(45.0, DiscreteMath.combination(45, 1), EPSILON);
        assertEquals(81.0, DiscreteMath.combination(81, 80), EPSILON);
        assertEquals(15.0, DiscreteMath.combination(6, 2), EPSILON);
        assertEquals(120.0, DiscreteMath.combination(10, 3), EPSILON);
        assertEquals(3432.0, DiscreteMath.combination(14, 7), EPSILON);
    }

    @Test
    public void caseBigCombination1() {
        assertEquals(new BigInteger("601080390"), DiscreteMath.bigCombination(32, 16));
        assertEquals(new BigInteger("1832624140942590534"), DiscreteMath.bigCombination(64, 32));
    }

    @Test
    public void caseBigCombination2() {
        for (int n = 1; n < 80; n++) {
            for (int m = 0; m <= n; m++) {
                double expect = DiscreteMath.combination(n, m);
                double epsilon = n * Math.ulp(expect);
                assertEquals(expect, DiscreteMath.bigCombination(n, m).doubleValue(), epsilon);
            }
        }
    }

    @Test
    public void caseBigCombination3() {
        for (int n = 1; n < 80; n++) {
            for (int m = 0; m < n; m++) {
                assertEquals(
                        DiscreteMath.bigCombination(n + 1, m + 1),
                        DiscreteMath.bigCombination(n, m).add(DiscreteMath.bigCombination(n, m + 1))
                );
            }
        }
    }

    @Test
    public void caseIntCombination() {
        for (int n = 1; n < 32; n++) {
            for (int m = 0; m <= n; m++) {
                assertEquals(
                        DiscreteMath.intCombination(n, m),
                        DiscreteMath.bigCombination(n, m).intValueExact()
                );
            }
        }
    }

    @Test
    public void caseHalfPascalTriangle() {
        final int n = 40;
        final int[][] full = DiscreteMath.pascalTriangle(n);
        final int[][] half = DiscreteMath.halfPascalTriangle(n);
        for (int i = 0; i <= 40; i++) {
            int[] fullRow = full[i];
            int[] halfRow = half[i];
            int length = halfRow.length;
            assertTrue(Arrays.equals(fullRow, 0, length, halfRow, 0, length));
        }
    }

    @Test
    public void caseStirling1() {
        // n = 4
        assertEquals(0.0, DiscreteMath.stirling1(4, 0), EPSILON);
        assertEquals(6.0, DiscreteMath.stirling1(4, 1), EPSILON);
        assertEquals(11.0, DiscreteMath.stirling1(4, 2), EPSILON);
        assertEquals(6.0, DiscreteMath.stirling1(4, 3), EPSILON);
        assertEquals(1.0, DiscreteMath.stirling1(4, 4), EPSILON);
        // n = 7
        assertEquals(720.0, DiscreteMath.stirling1(7, 1), EPSILON);
        assertEquals(1764.0, DiscreteMath.stirling1(7, 2), EPSILON);
        assertEquals(1624.0, DiscreteMath.stirling1(7, 3), EPSILON);
        assertEquals(735.0, DiscreteMath.stirling1(7, 4), EPSILON);
        assertEquals(175.0, DiscreteMath.stirling1(7, 5), EPSILON);
        assertEquals(21.0, DiscreteMath.stirling1(7, 6), EPSILON);
    }

    @Test
    public void caseStirling2() {
        // n = 4
        assertEquals(0.0, DiscreteMath.stirling2(4, 0), EPSILON);
        assertEquals(1.0, DiscreteMath.stirling2(4, 1), EPSILON);
        assertEquals(7.0, DiscreteMath.stirling2(4, 2), EPSILON);
        assertEquals(6.0, DiscreteMath.stirling2(4, 3), EPSILON);
        assertEquals(1.0, DiscreteMath.stirling2(4, 4), EPSILON);
        // n = 9
        assertEquals(255.0, DiscreteMath.stirling2(9, 2), EPSILON);
        assertEquals(3025.0, DiscreteMath.stirling2(9, 3), EPSILON);
        assertEquals(7770.0, DiscreteMath.stirling2(9, 4), EPSILON);
        assertEquals(6951.0, DiscreteMath.stirling2(9, 5), EPSILON);
        assertEquals(2646.0, DiscreteMath.stirling2(9, 6), EPSILON);
        assertEquals(462.0, DiscreteMath.stirling2(9, 7), EPSILON);
        assertEquals(36.0, DiscreteMath.stirling2(9, 8), EPSILON);
    }

    @Test
    public void caseCatalan() {
        assertEquals(1.0, DiscreteMath.catalan(0), EPSILON);
        assertEquals(1.0, DiscreteMath.catalan(1), EPSILON);
        assertEquals(2.0, DiscreteMath.catalan(2), EPSILON);
        assertEquals(5.0, DiscreteMath.catalan(3), EPSILON);
        assertEquals(42.0, DiscreteMath.catalan(5), EPSILON);
        assertEquals(1430.0, DiscreteMath.catalan(8), EPSILON);
        assertEquals(477638700.0, DiscreteMath.catalan(18), EPSILON);
    }

    @Test
    public void caseBigCatalan() {
        assertEquals(new BigInteger("477638700"), DiscreteMath.bigCatalan(18));
        assertEquals(new BigInteger("6564120420"), DiscreteMath.bigCatalan(20));
        assertEquals(new BigInteger("1289904147324"), DiscreteMath.bigCatalan(24));
    }

    @Test
    public void caseBell() {
        assertEquals(1.0, DiscreteMath.bell(0), EPSILON);
        assertEquals(1.0, DiscreteMath.bell(1), EPSILON);
        assertEquals(2.0, DiscreteMath.bell(2), EPSILON);
        assertEquals(5.0, DiscreteMath.bell(3), EPSILON);
        assertEquals(877.0, DiscreteMath.bell(7), EPSILON);
    }
}
