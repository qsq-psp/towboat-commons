package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2025/10/25.
 */
@CodeHistory(date = "2025/10/25")
public class FuzzyContextTest {

    private static final int REPEAT = 100;

    private final RandomContext rc = new RandomContext();

    @Test
    public void checkNotZero() {
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            try {
                FuzzyContext fc = new FuzzyContext(rc.source, rc.nextInt() & ~FuzzyContext.FP_ZERO);
                repeatIndex++;
                Assert.assertNotEquals(0.0, fc.nextFloat());
                Assert.assertNotEquals(0.0, fc.nextDouble());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    @Test
    public void checkNotInfinity() {
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            try {
                FuzzyContext fc = new FuzzyContext(rc.source, rc.nextInt() & ~FuzzyContext.FP_INFINITY);
                repeatIndex++;
                Assert.assertFalse(Float.isInfinite(fc.nextFloat()));
                Assert.assertFalse(Double.isInfinite(fc.nextDouble()));
            } catch (IllegalArgumentException ignore) {}
        }
    }

    @Test
    public void checkNotNaN() {
        int repeatIndex = 0;
        while (repeatIndex < REPEAT) {
            try {
                FuzzyContext fc = new FuzzyContext(rc.source, rc.nextInt() & ~FuzzyContext.FP_NAN);
                repeatIndex++;
                Assert.assertFalse(Float.isNaN(fc.nextFloat()));
                Assert.assertFalse(Double.isNaN(fc.nextDouble()));
            } catch (IllegalArgumentException ignore) {}
        }
    }

    @Test
    public void checkFinite() {
        final FuzzyContext fc = new FuzzyContext(rc.source, FuzzyContext.FP_NORMAL | FuzzyContext.FP_SUBNORMAL | FuzzyContext.FP_ZERO);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Assert.assertTrue(Float.isFinite(fc.nextFloat()));
            Assert.assertTrue(Double.isFinite(fc.nextDouble()));
        }
    }

    @Test
    public void checkNaN() {
        final FuzzyContext fc = new FuzzyContext(rc.source, FuzzyContext.FP_NAN);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Assert.assertTrue(Float.isNaN(fc.nextFloat()));
            Assert.assertTrue(Double.isNaN(fc.nextDouble()));
        }
    }
}
