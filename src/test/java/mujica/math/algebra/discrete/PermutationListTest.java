package mujica.math.algebra.discrete;

import mujica.ds.of_int.list.IntList;
import mujica.ds.of_int.list.PublicIntList;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.ListIterator;

@CodeHistory(date = "2025/3/13")
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class PermutationListTest {

    private static final int REPEAT = 500;

    private static final int MAX_N = 100;

    @NotNull
    private final RandomContext rc = new RandomContext();

    @Test
    public void caseSize() {
        Assert.assertEquals(1, (new PermutationList(0)).size());
        Assert.assertEquals(1, (new PermutationList(1)).size());
        Assert.assertEquals(2, (new PermutationList(2)).size());
        Assert.assertEquals(6, (new PermutationList(3)).size());
        Assert.assertEquals(120, (new PermutationList(5)).size());
        Assert.assertEquals(479001600, (new PermutationList(12)).size());
        Assert.assertEquals(Integer.MAX_VALUE, (new PermutationList(13)).size());
    }

    @Test
    public void caseGet3() {
        final PermutationList list = new PermutationList(3);
        Assert.assertArrayEquals(new int[] {0, 1, 2}, list.get(0).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 1}, list.get(1).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 2}, list.get(2).toArray());
        Assert.assertArrayEquals(new int[] {2, 1, 0}, list.get(5).toArray());
    }

    @Test
    public void caseGet4() {
        final PermutationList list = new PermutationList(4);
        Assert.assertArrayEquals(new int[] {0, 1, 2, 3}, list.get(0).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 3, 2}, list.get(1).toArray());
        Assert.assertArrayEquals(new int[] {0, 3, 2, 1}, list.get(5).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 2, 3}, list.get(6).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 3, 2}, list.get(7).toArray());
        Assert.assertArrayEquals(new int[] {3, 2, 0, 1}, list.get(22).toArray());
        Assert.assertArrayEquals(new int[] {3, 2, 1, 0}, list.get(23).toArray());
    }

    @Test
    public void caseGet5() {
        final PermutationList list = new PermutationList(5);
        Assert.assertArrayEquals(new int[] {0, 1, 2, 3, 4}, list.get(0).toArray());
        Assert.assertArrayEquals(new int[] {0, 4, 3, 2, 1}, list.get(23).toArray());
        Assert.assertArrayEquals(new int[] {1, 0, 2, 3, 4}, list.get(24).toArray());
        Assert.assertArrayEquals(new int[] {2, 0, 1, 3, 4}, list.get(48).toArray());
        Assert.assertArrayEquals(new int[] {3, 0, 1, 2, 4}, list.get(72).toArray());
        Assert.assertArrayEquals(new int[] {4, 0, 1, 2, 3}, list.get(96).toArray());
        Assert.assertArrayEquals(new int[] {4, 3, 2, 1, 0}, list.get(119).toArray());
    }

    private void assertCardinal(@NotNull int[] array) {
        Arrays.sort(array);
        final int size = array.length;
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(index, array[index]);
        }
    }

    @Test
    public void checkGet() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
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
                    assertCardinal(array0);
                    assertCardinal(array1);
                }
            } catch (AssertionError e) {
                System.out.println(list);
                throw e;
            }
        }
    }

    @Test
    public void checkGetBig() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
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

    @Test
    public void caseIndexOf3() {
        final PermutationList list = new PermutationList(3);
        Assert.assertEquals(-1, list.indexOf(new PublicIntList(new int[] {0, 1})));
        Assert.assertEquals(-1, list.indexOf(new PublicIntList(new int[] {0, 1, 2, 3})));
        Assert.assertEquals(-1, list.indexOf(new PublicIntList(new int[] {0, 1, 1})));
        Assert.assertEquals(-1, list.indexOf(new PublicIntList(new int[] {-1, 0, 1})));
        Assert.assertEquals(0, list.indexOf(new PublicIntList(new int[] {0, 1, 2})));
        Assert.assertEquals(1, list.indexOf(new PublicIntList(new int[] {0, 2, 1})));
        Assert.assertEquals(2, list.indexOf(new PublicIntList(new int[] {1, 0, 2})));
        Assert.assertEquals(3, list.indexOf(new PublicIntList(new int[] {1, 2, 0})));
        Assert.assertEquals(4, list.indexOf(new PublicIntList(new int[] {2, 0, 1})));
        Assert.assertEquals(5, list.indexOf(new PublicIntList(new int[] {2, 1, 0})));
    }

    @Test
    public void checkIndexOf() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
            try {
                int size = list.size();
                int repeatCount = Math.min(REPEAT, size);
                for (int repeatIndex = 0; repeatIndex < repeatCount; repeatIndex++) {
                    int index = rc.nextInt(size);
                    Assert.assertEquals(index, list.indexOf(list.get(index)));
                }
            } catch (AssertionError e) {
                System.out.println(list);
                throw e;
            }
        }
    }

    @Test
    public void caseBigIndexOf4() {
        final PermutationList list = new PermutationList(4);
        Assert.assertNull(list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2})));
        Assert.assertNull(list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2, 3, 4})));
        Assert.assertNull(list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2, 2})));
        Assert.assertNull(list.bigIndexOf(new PublicIntList(new int[] {-1, 0, 1, 2})));
        Assert.assertEquals(BigInteger.valueOf(0), list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2, 3})));
        Assert.assertEquals(BigInteger.valueOf(1), list.bigIndexOf(new PublicIntList(new int[] {0, 1, 3, 2})));
        Assert.assertEquals(BigInteger.valueOf(4), list.bigIndexOf(new PublicIntList(new int[] {0, 3, 1, 2})));
        Assert.assertEquals(BigInteger.valueOf(5), list.bigIndexOf(new PublicIntList(new int[] {0, 3, 2, 1})));
        Assert.assertEquals(BigInteger.valueOf(6), list.bigIndexOf(new PublicIntList(new int[] {1, 0, 2, 3})));
    }

    @Test
    public void caseBigIndexOf5() {
        final PermutationList list = new PermutationList(5);
        Assert.assertEquals(BigInteger.valueOf(1), list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2, 4, 3})));
        Assert.assertEquals(BigInteger.valueOf(2), list.bigIndexOf(new PublicIntList(new int[] {0, 1, 3, 2, 4})));
        Assert.assertEquals(BigInteger.valueOf(6), list.bigIndexOf(new PublicIntList(new int[] {0, 2, 1, 3, 4})));
        Assert.assertEquals(BigInteger.valueOf(24), list.bigIndexOf(new PublicIntList(new int[] {1, 0, 2, 3, 4})));
    }

    @Test
    public void checkIterator() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
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

    @Test
    public void checkReverseIterator() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
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

    /**
     * Blind means that hasNext() is not called before calling next() and hasPrevious() is not called before calling previous()
     */
    @Test
    public void checkBlindIterator() {
        for (int n = 2; n <= MAX_N; n++) {
            PermutationList list = new PermutationList(n);
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

    @Test
    public void caseInverse4() {
        final PermutationList list = new PermutationList(4);
        BigInteger bigIndex = list.bigIndexOf(new PublicIntList(new int[] {0, 1, 2, 3}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {0, 2, 1, 3}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {0, 1, 3, 2}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {3, 1, 2, 0}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        // case 5
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {1, 2, 0, 3}));
        Assert.assertNotNull(bigIndex);
        bigIndex = list.inverse(bigIndex);
        Assert.assertArrayEquals(new int[] {2, 0, 1, 3}, list.get(bigIndex).toArray());
        // case 6
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {1, 2, 3, 0}));
        Assert.assertNotNull(bigIndex);
        bigIndex = list.inverse(bigIndex);
        Assert.assertArrayEquals(new int[] {3, 0, 1, 2}, list.get(bigIndex).toArray());
    }

    @Test
    public void caseInverse5() {
        final PermutationList list = new PermutationList(5);
        BigInteger bigIndex = list.bigIndexOf(new PublicIntList(new int[] {1, 0, 2, 3, 4}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {3, 2, 1, 0, 4}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {4, 2, 1, 3, 0}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {3, 1, 4, 0, 2}));
        Assert.assertNotNull(bigIndex);
        Assert.assertEquals(bigIndex, list.inverse(bigIndex));
        // case 5
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {0, 2, 3, 1, 4}));
        Assert.assertNotNull(bigIndex);
        bigIndex = list.inverse(bigIndex);
        Assert.assertArrayEquals(new int[] {0, 3, 1, 2, 4}, list.get(bigIndex).toArray());
        // case 6
        bigIndex = list.bigIndexOf(new PublicIntList(new int[] {2, 3, 4, 0, 1}));
        Assert.assertNotNull(bigIndex);
        bigIndex = list.inverse(bigIndex);
        Assert.assertArrayEquals(new int[] {3, 4, 0, 1, 2}, list.get(bigIndex).toArray());
    }
}
