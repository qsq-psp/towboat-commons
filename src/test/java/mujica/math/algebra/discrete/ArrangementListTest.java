package mujica.math.algebra.discrete;

import mujica.ds.of_int.list.IntList;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.ListIterator;

@CodeHistory(date = "2025/3/15")
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ArrangementListTest {

    private static final int REPEAT = 500;

    private static final int MAX_N = 15;

    @NotNull
    private final RandomContext rc = new RandomContext();

    @Test
    public void caseSize0() {
        Assert.assertEquals(1, (new ArrangementList(0, 0)).size());
    }

    @Test
    public void caseSize1() {
        Assert.assertEquals(1, (new ArrangementList(1, 0)).size());
        Assert.assertEquals(1, (new ArrangementList(1, 1)).size());
    }

    @Test
    public void caseSize2() {
        Assert.assertEquals(1, (new ArrangementList(2, 0)).size());
        Assert.assertEquals(2, (new ArrangementList(2, 1)).size());
        Assert.assertEquals(2, (new ArrangementList(2, 2)).size());
    }

    @Test
    public void caseSize3() {
        Assert.assertEquals(1, (new ArrangementList(3, 0)).size());
        Assert.assertEquals(3, (new ArrangementList(3, 1)).size());
        Assert.assertEquals(6, (new ArrangementList(3, 2)).size());
        Assert.assertEquals(6, (new ArrangementList(3, 3)).size());
    }

    @Test
    public void caseSize7() {
        Assert.assertEquals(1, (new ArrangementList(7, 0)).size());
        Assert.assertEquals(7, (new ArrangementList(7, 1)).size());
        Assert.assertEquals(2520, (new ArrangementList(7, 5)).size());
        Assert.assertEquals(5040, (new ArrangementList(7, 6)).size());
    }

    @Test
    public void caseSize12() {
        Assert.assertEquals(132, (new ArrangementList(12, 2)).size());
        Assert.assertEquals(239500800, (new ArrangementList(12, 10)).size());
    }

    @Test
    public void caseSize15() {
        Assert.assertEquals(2730, (new ArrangementList(15, 3)).size());
        Assert.assertEquals(Integer.MAX_VALUE, (new ArrangementList(15, 12)).size()); // 217945728000 > Integer.MAX_VALUE
    }

    @Test
    public void caseGet3out4() {
        final ArrangementList list = new ArrangementList(4, 3);
        Assert.assertArrayEquals(new int[] {0, 1, 2}, list.get(0).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 3}, list.get(1).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 1}, list.get(2).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 3}, list.get(3).toArray());
        Assert.assertArrayEquals(new int[] {0, 3, 1}, list.get(4).toArray());
        Assert.assertArrayEquals(new int[] {0, 3, 2}, list.get(5).toArray());
    }

    @Test
    public void caseGet3out5() {
        final ArrangementList list = new ArrangementList(5, 3);
        Assert.assertArrayEquals(new int[] {0, 4, 3}, list.get(11).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 2}, list.get(12).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 3}, list.get(13).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 4}, list.get(14).toArray());
        Assert.assertArrayEquals(new int[] {1, 2, 0}, list.get(15).toArray());
    }

    @Test
    public void checkGet() {
        for (int n = 2; n < MAX_N; n++) {
            for (int m = 2; m <= n; m++) {
                ArrangementList list = new ArrangementList(n, m);
                try {
                    int size = list.size();
                    int repeatCount = Math.min(REPEAT, size);
                    for (int repeatIndex = 0; repeatIndex < repeatCount; repeatIndex++) {
                        int index0 = rc.nextInt(size);
                        int index1 = rc.nextInt(size);
                        int[] array0 = list.get(index0).toArray();
                        int[] array1 = list.get(index1).toArray();
                        Assert.assertEquals(
                                Integer.compare(index0, index1),
                                Arrays.compare(array0, array1) // when array size equals, the returned value is only -1, 0, 1
                        );
                    }
                } catch (AssertionError e) {
                    System.out.println(list);
                    throw e;
                }
            }
        }
    }

    @Test
    public void checkGetBig() {
        for (int n = 2; n < MAX_N; n++) {
            for (int m = 2; m <= n; m++) {
                ArrangementList list = new ArrangementList(n, m);
                try {
                    int size = list.size();
                    int repeatCount = Math.min(REPEAT, size);
                    for (int repeatIndex = 0; repeatIndex < repeatCount; repeatIndex++) {
                        int index = rc.nextInt(size);
                        Assert.assertEquals(list.get(index), list.get(BigInteger.valueOf(index)));
                    }
                } catch (AssertionError e) {
                    System.out.println(list);
                    throw e;
                }
            }
        }
    }

    @Test
    public void checkIterator() {
        for (int n = 2; n < MAX_N; n++) {
            for (int m = 2; m <= n; m++) {
                ArrangementList list = new ArrangementList(n, m);
                try {
                    int limit = Math.min(list.size(), REPEAT);
                    int index = 0;
                    for (IntList item : list) {
                        Assert.assertEquals(list.get(index++), item);
                        if (index >= limit) {
                            break;
                        }
                    }
                } catch (AssertionError e) {
                    System.out.println(list);
                    throw e;
                }
            }
        }
    }

    @Test
    public void checkReverseIterator() {
        for (int n = 2; n < MAX_N; n++) {
            for (int m = 2; m <= n; m++) {
                ArrangementList list = new ArrangementList(n, m);
                try {
                    int index = list.size();
                    int limit = Math.max(0, index - REPEAT);
                    for (ListIterator<IntList> iterator = list.listIterator(index); iterator.hasPrevious();) {
                        IntList item = iterator.previous();
                        Assert.assertEquals(list.get(--index), item);
                        if (index <= limit) {
                            break;
                        }
                    }
                } catch (AssertionError e) {
                    System.out.println(list);
                    throw e;
                }
            }
        }
    }

    /**
     * Blind means that hasNext() is not called before calling next() and hasPrevious() is not called before calling previous()
     */
    @Test
    public void checkBlindIterator() {
        for (int n = 2; n < MAX_N; n++) {
            for (int m = 2; m <= n; m++) {
                ArrangementList list = new ArrangementList(n, m);
                try {
                    int size = list.size();
                    int index = rc.nextInt(size);
                    ListIterator<IntList> iterator = list.listIterator(index);
                    int repeatCount = Math.min(REPEAT, size);
                    for (int repeatIndex = 0; repeatIndex < repeatCount; repeatIndex++) {
                        boolean moveNext;
                        if (index > 0) {
                            if (index + 1 < size) {
                                moveNext = rc.nextBoolean();
                            } else {
                                moveNext = false;
                            }
                        } else {
                            if (index + 1 < size) {
                                moveNext = true;
                            } else {
                                break;
                            }
                        }
                        try {
                            if (moveNext) {
                                Assert.assertEquals(list.get(index++), iterator.next());
                            } else {
                                Assert.assertEquals(list.get(--index), iterator.previous());
                            }
                            Assert.assertEquals(index, iterator.nextIndex());
                        } catch (AssertionError e) {
                            System.out.println("moveNext = " + moveNext + ", index = " + index);
                            throw e;
                        }
                    }
                } catch (AssertionError e) {
                    System.out.println(list);
                    throw e;
                }
            }
        }
    }
}
