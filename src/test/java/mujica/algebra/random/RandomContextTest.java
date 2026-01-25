package mujica.algebra.random;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2026/1/21.
 */
public class RandomContextTest {

    @NotNull
    private static List<RandomContext> randomContexts() {
        final ArrayList<RandomContext> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RandomContext rc = new RandomContext();
            for (int j = 0; j < 10; j++) {
                list.add(rc);
            }
        }
        return list;
    }

    @Test
    public void checkShuffledCardinal() {
        for (RandomContext rc : randomContexts()) {
            int[] array = rc.shuffledCardinal(rc.nextInt(20, 80));
            Arrays.sort(array);
            int length = array.length;
            for (int index = 0; index < length; index++) {
                Assert.assertEquals(index, array[index]);
            }
        }
    }

    @Test
    public void checkDistribute() {
        for (RandomContext rc : randomContexts()) {
            int expectedSum = rc.nextInt(20, 80);
            int[] array = rc.distributed(rc.nextInt(1, 9), expectedSum);
            int actualSum = 0;
            for (int value : array) {
                actualSum += value;
            }
            Assert.assertEquals(expectedSum, actualSum);
        }
    }

    @Test
    public void checkRedistribute() {
        for (RandomContext rc : randomContexts()) {
            int[] array = rc.nextIntArray(1, 9, rc.nextInt(1, 9));
            int expectedSum = 0;
            for (int value : array) {
                expectedSum += value;
            }
            rc.redistribute(array);
            int actualSum = 0;
            for (int value : array) {
                actualSum += value;
            }
            Assert.assertEquals(expectedSum, actualSum);
        }
    }

    @Test
    public void checkNextFloat() {
        for (RandomContext rc : randomContexts()) {
            float v = rc.nextFloat();
            Assert.assertTrue(0.0f <= v && v < 1.0f);
        }
    }

    @Test
    public void checkNextDouble() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextDouble();
            Assert.assertTrue(0.0 <= v && v < 1.0);
        }
    }

    @Test
    public void checkNextSlope() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextSlope();
            Assert.assertTrue(0.0 <= v && v < 1.0);
        }
    }

    @Test
    public void checkNextUnitHill() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextUnitHill();
            Assert.assertTrue(0.0 <= v && v < 1.0);
        }
    }

    @Test
    public void checkNextBipolarHill() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextBipolarHill();
            Assert.assertTrue(-1.0 < v && v < 1.0);
        }
    }

    @Test
    public void checkNextQuadraticSlope() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextQuadraticSlope();
            Assert.assertTrue(0.0 <= v && v < 1.0);
        }
    }

    @Test
    public void checkNextQuadraticHill() {
        for (RandomContext rc : randomContexts()) {
            double v = rc.nextQuadraticHill();
            Assert.assertTrue(0.0 <= v && v < 1.0);
        }
    }
}
