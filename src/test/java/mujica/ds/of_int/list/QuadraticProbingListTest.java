package mujica.ds.of_int.list;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

@CodeHistory(date = "2025/7/2")
public class QuadraticProbingListTest {

    private static final int SIZE = 600;

    private final RandomContext rc = new RandomContext();

    private void checkUnique(int modulo) {
        final QuadraticProbingList list = (new QuadraticProbingList(modulo)).setHash(rc.nextInt());
        final int size = Math.min(SIZE, list.intLength());
        final HashSet<Integer> set = new HashSet<>();
        try {
            for (int i = 0; i < size; i++) {
                Assert.assertTrue(set.add(list.getInt(i)));
            }
        } catch (AssertionError e) {
            System.out.println(list);
            System.out.println(set);
            throw e;
        }
    }

    @Test
    public void checkUnique() {
        checkUnique(22);
        checkUnique(107);
        checkUnique(118);
        checkUnique(2302);
        checkUnique(17359);
        checkUnique(30574);
        checkUnique(43943);
        checkUnique(46198);
    }

    private void checkCover(int modulo) {
        final QuadraticProbingList list = (new QuadraticProbingList(modulo)).setHash(rc.nextInt());
        final int size = Math.min(SIZE, list.intLength());
        final HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(i);
        }
        for (int i : list) {
            set.remove(i);
        }
        try {
            Assert.assertTrue(set.isEmpty());
        } catch (AssertionError e) {
            System.out.println(list);
            System.out.println(set);
            throw e;
        }
    }

    @Test
    public void checkCover() {
        checkCover(59);
        checkCover(62);
        checkCover(454);
        checkCover(463);
        checkCover(907);
        checkCover(911);
        checkCover(1286);
        checkCover(1723);
    }
}
