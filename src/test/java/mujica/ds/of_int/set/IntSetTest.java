package mujica.ds.of_int.set;

import mujica.algebra.random.FuzzyContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2026/1/20.
 */
public class IntSetTest {

    private static final int LENGTH = 98;

    private static final FuzzyContext fc = new FuzzyContext();

    private void fuzzAdd(@NotNull IntSet set) {
        final int[] array = fc.shuffledCardinal(fc.nextInt(LENGTH) + 1);
        final int length = array.length;
        for (int index = 0; index < length; index++) {
            Assert.assertEquals(index, set.intLength());
            Assert.assertTrue(set.add(array[index]));
        }
        Assert.assertEquals(length, set.intLength());
    }

    @Test
    public void fuzzAdd() {
        fuzzAdd(new LinearClosedHashIntSet(null));
        fuzzAdd(new QuadraticClosedHashIntSet(null));
        fuzzAdd(new JdkNavigableIntSet());
    }

    private void fuzzRemove(@NotNull IntSet set) {
        final int length = fc.nextInt(LENGTH) + 1;
        Assert.assertTrue(set.isEmpty());
        for (int value : fc.shuffledCardinal(length)) {
            Assert.assertTrue(set.add(value));
        }
        Assert.assertFalse(set.isEmpty());
        for (int value : fc.shuffledCardinal(length)) {
            Assert.assertTrue(set.remove(value));
        }
        Assert.assertTrue(set.isEmpty());
    }

    @Test
    public void fuzzRemove() {
        fuzzRemove(new LinearClosedHashIntSet(null));
        fuzzRemove(new QuadraticClosedHashIntSet(null));
        fuzzRemove(new JdkNavigableIntSet());
    }

    private void fuzzAddDuplicate(@NotNull IntSet set) {
        final int length = fc.nextInt(LENGTH) + 1;
        for (int value : fc.shuffledCardinal(length)) {
            Assert.assertTrue(set.add(value));
        }
        for (int value : fc.shuffledCardinal(length)) {
            Assert.assertFalse(set.add(value));
        }
    }

    @Test
    public void fuzzAddDuplicate() {
        fuzzAddDuplicate(new LinearClosedHashIntSet(null));
        fuzzAddDuplicate(new QuadraticClosedHashIntSet(null));
        fuzzAddDuplicate(new JdkNavigableIntSet());
    }
}
