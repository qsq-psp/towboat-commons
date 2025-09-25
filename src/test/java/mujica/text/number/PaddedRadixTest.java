package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/1")
public class PaddedRadixTest {

    @Test
    public void caseBinaryEmpty() {
        final IntegralToStringFunction instance = new PaddedRadix(2, 0, Integer.MAX_VALUE);
        Assert.assertEquals("", instance.stringify(0));
        Assert.assertEquals("101", instance.stringify(5));
        Assert.assertEquals("1010", instance.stringify(10));
        Assert.assertEquals("-100000", instance.stringify(-32));
    }

    @Test
    public void caseBinaryFree() {
        final IntegralToStringFunction instance = new PaddedRadix(2, 1, Integer.MAX_VALUE);
        Assert.assertEquals("0", instance.stringify(0));
        Assert.assertEquals("1", instance.stringify(1));
        Assert.assertEquals("10", instance.stringify(2));
        Assert.assertEquals("110", instance.stringify(6));
        Assert.assertEquals("10001", instance.stringify(17));
        Assert.assertEquals("-1000010", instance.stringify(-66));
    }

    @Test
    public void caseBinaryRange() {
        final IntegralToStringFunction instance = new PaddedRadix(2, 4, 8);
        Assert.assertEquals("0000", instance.stringify(0));
        Assert.assertEquals("0001", instance.stringify(1));
        Assert.assertEquals("0111", instance.stringify(7));
    }
}
