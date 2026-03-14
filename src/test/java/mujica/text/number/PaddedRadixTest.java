package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/1")
public class PaddedRadixTest {

    @Test
    public void caseBinaryEmpty() {
        final PaddedRadix instance = new PaddedRadix(2, 0, Integer.MAX_VALUE);
        Assert.assertEquals("", instance.stringifyInteger(0));
        Assert.assertEquals("101", instance.stringifyInteger(5));
        Assert.assertEquals("1010", instance.stringifyInteger(10));
        Assert.assertEquals("-100000", instance.stringifyInteger(-32));
    }

    @Test
    public void caseBinaryFree() {
        final PaddedRadix instance = new PaddedRadix(2, 1, Integer.MAX_VALUE);
        Assert.assertEquals("0", instance.stringifyInteger(0));
        Assert.assertEquals("1", instance.stringifyInteger(1));
        Assert.assertEquals("10", instance.stringifyInteger(2));
        Assert.assertEquals("110", instance.stringifyInteger(6));
        Assert.assertEquals("10001", instance.stringifyInteger(17));
        Assert.assertEquals("-1000010", instance.stringifyInteger(-66));
    }

    @Test
    public void caseBinaryRange() {
        final PaddedRadix instance = new PaddedRadix(2, 4, 8);
        Assert.assertEquals("0000", instance.stringifyInteger(0));
        Assert.assertEquals("0001", instance.stringifyInteger(1));
        Assert.assertEquals("0111", instance.stringifyInteger(7));
    }
}
