package mujica.math.algebra.discrete;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@CodeHistory(date = "2025/5/24")
public class CombinationListTest {

    private static final int REPEAT = 500;

    private static final int MAX_N = 20;

    @NotNull
    private final RandomContext rc = new RandomContext();

    @Test
    public void caseSize0() {
        Assert.assertEquals(1, (new CombinationList(0, 0)).size());
    }

    @Test
    public void caseSize1() {
        Assert.assertEquals(1, (new CombinationList(1, 0)).size());
        Assert.assertEquals(1, (new CombinationList(1, 1)).size());
    }

    @Test
    public void caseSize2() {
        Assert.assertEquals(1, (new CombinationList(2, 0)).size());
        Assert.assertEquals(2, (new CombinationList(2, 1)).size());
    }

    @Test
    public void caseSize3() {
        Assert.assertEquals(1, (new CombinationList(3, 0)).size());
        Assert.assertEquals(3, (new CombinationList(3, 1)).size());
    }

    @Test
    public void caseSize6() {
        Assert.assertEquals(6, (new CombinationList(6, 1)).size());
        Assert.assertEquals(15, (new CombinationList(6, 2)).size());
        Assert.assertEquals(20, (new CombinationList(6, 3)).size());
    }

    @Test
    public void caseSize9() {
        Assert.assertEquals(36, (new CombinationList(9, 2)).size());
        Assert.assertEquals(84, (new CombinationList(9, 3)).size());
        Assert.assertEquals(126, (new CombinationList(9, 4)).size());
    }

    @Test
    public void caseSize11() {
        Assert.assertEquals(55, (new CombinationList(11, 2)).size());
        Assert.assertEquals(165, (new CombinationList(11, 3)).size());
        Assert.assertEquals(330, (new CombinationList(11, 4)).size());
        Assert.assertEquals(462, (new CombinationList(11, 5)).size());
    }

    @Test
    public void caseGet3Out5() {
        final CombinationList list = new CombinationList(5, 3);
        Assert.assertArrayEquals(new int[] {0, 1, 2}, list.get(0).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 3}, list.get(1).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 4}, list.get(2).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 3}, list.get(3).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 4}, list.get(4).toArray());
        Assert.assertArrayEquals(new int[] {0, 3, 4}, list.get(5).toArray());
        Assert.assertArrayEquals(new int[] {1, 2, 3}, list.get(6).toArray());
    }

    @Test
    public void caseGet2Out6() {
        final CombinationList list = new CombinationList(6, 2);
        Assert.assertArrayEquals(new int[] {0, 5}, list.get(4).toArray());
        Assert.assertArrayEquals(new int[] {1, 2}, list.get(5).toArray());
        Assert.assertArrayEquals(new int[] {1, 3}, list.get(6).toArray());
        Assert.assertArrayEquals(new int[] {1, 4}, list.get(7).toArray());
        Assert.assertArrayEquals(new int[] {1, 5}, list.get(8).toArray());
        Assert.assertArrayEquals(new int[] {2, 3}, list.get(9).toArray());
    }

    @Test
    public void caseGet4Out7() {
        final CombinationList list = new CombinationList(7, 4);
        Assert.assertArrayEquals(new int[] {0, 1, 3, 4}, list.get(4).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 4, 5}, list.get(4 + 3).toArray());
        Assert.assertArrayEquals(new int[] {0, 1, 5, 6}, list.get(4 + 3 + 2).toArray());
        Assert.assertArrayEquals(new int[] {0, 2, 3, 4}, list.get(10).toArray()); // 10 = 4 + 3 + 2 + 1
        Assert.assertArrayEquals(new int[] {0, 3, 4, 5}, list.get(10 + 6).toArray());
        Assert.assertArrayEquals(new int[] {0, 4, 5, 6}, list.get(10 + 6 + 3).toArray());
        Assert.assertArrayEquals(new int[] {1, 2, 3, 4}, list.get(20).toArray()); // 20 = 10 + 6 + 3 + 1
        Assert.assertArrayEquals(new int[] {2, 3, 4, 5}, list.get(20 + 10).toArray());
        Assert.assertArrayEquals(new int[] {3, 4, 5, 6}, list.get(20 + 10 + 4).toArray());
    }
}
