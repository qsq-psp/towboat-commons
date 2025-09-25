package mujica.math.algebra.discrete;

import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/15")
public class IntegralCastTest {

    private static final int REPEAT = 1000;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzz(@NotNull CastToZero expect, @NotNull CastToZero actual) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int x = fc.nextInt();
            int y = fc.nextInt();
            Assert.assertEquals(
                    expect.mean(x, y),
                    actual.mean(x, y)
            );
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            long x = fc.nextLong();
            long y = fc.nextLong();
            Assert.assertEquals(
                    expect.mean(x, y),
                    actual.mean(x, y)
            );
        }
    }

    @Test
    public void fuzzCastToZero() {
        fuzz(new CastToZero(), new BaselineCastToZero());
    }
}
