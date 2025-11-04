package mujica.math.algebra.prime;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2025/10/4.
 */
public class PrimePredicateTest {

    private void checkIsPrime(@NotNull PrimePredicate expected, @NotNull PrimePredicate actual) {
        int num = 0;
        try {
            while (num < 10000) {
                Assert.assertEquals(expected.isPrime(num), actual.isPrime(num));
                num++;
            }
        } catch (AssertionError e) {
            System.err.println("num = " + num);
            throw e;
        }
    }

    @Test
    public void checkIsPrime() {
        checkIsPrime(BaselinePrimePredicate.INSTANCE, PrimePredicate.INSTANCE);
    }
}
