package mujica.math.algebra.discrete;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2022/7/18", project = "Ultramarine", name = "CounterTest")
@CodeHistory(date = "2023/3/25", project = "Ultramarine", name = "BitCount32Test")
@CodeHistory(date = "2025/3/17")
public class Bit32CombinationListTest {

    private static final int REPEAT = 500;

    private final RandomContext rc = new RandomContext();
    
    @Test
    public void caseNext() {
        Assert.assertEquals(0, Bit32CombinationList.next(0));
        Assert.assertEquals(Integer.MIN_VALUE, Bit32CombinationList.next(Integer.MIN_VALUE));
        Assert.assertEquals(0b10, Bit32CombinationList.next(0b01));
        Assert.assertEquals(0b100, Bit32CombinationList.next(0b010));
        Assert.assertEquals(0b101, Bit32CombinationList.next(0b011));
        Assert.assertEquals(0b110, Bit32CombinationList.next(0b101));
        Assert.assertEquals(0b1001, Bit32CombinationList.next(0b0110));
    }

    @Test
    public void casePrevious() {
        Assert.assertEquals(0, Bit32CombinationList.previous(0));
        Assert.assertEquals(1, Bit32CombinationList.previous(1));
        Assert.assertEquals(0xffff, Bit32CombinationList.previous(0xffff));
        Assert.assertEquals(0b010, Bit32CombinationList.previous(0b100));
        Assert.assertEquals(0b101, Bit32CombinationList.previous(0b110));
        Assert.assertEquals(0b0100, Bit32CombinationList.previous(0b1000));
        Assert.assertEquals(0b1010, Bit32CombinationList.previous(0b1100));
        Assert.assertEquals(0b1001, Bit32CombinationList.previous(0b1010));
    }
    
    @Test
    public void checkNextMonotonic() {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int before = 0x3fffffff & rc.nextInt();
            int after = Bit32CombinationList.next(before);
            Assert.assertTrue(before <= after);
        }
    }
    
    @Test
    public void checkPreviousMonotonic() {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int before = 0x3fffffff & rc.nextInt();
            int after = Bit32CombinationList.previous(before);
            Assert.assertTrue(before >= after);
        }
    }
}
