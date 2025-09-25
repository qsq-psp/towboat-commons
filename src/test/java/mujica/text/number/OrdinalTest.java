package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/1")
public class OrdinalTest {

    @Test
    public void caseInt() {
        final IntegralToStringFunction instance = Ordinal.INSTANCE;
        Assert.assertEquals("0th", instance.stringify(0));
        Assert.assertEquals("1st", instance.stringify(1));
        Assert.assertEquals("2nd", instance.stringify(2));
        Assert.assertEquals("3rd", instance.stringify(3));
        Assert.assertEquals("4th", instance.stringify(4));
        Assert.assertEquals("11th", instance.stringify(11));
        Assert.assertEquals("12th", instance.stringify(12));
        Assert.assertEquals("13th", instance.stringify(13));
        Assert.assertEquals("21st", instance.stringify(21));
        Assert.assertEquals("22nd", instance.stringify(22));
        Assert.assertEquals("23rd", instance.stringify(23));
        Assert.assertEquals("4000th", instance.stringify(4000));
        Assert.assertEquals("4001st", instance.stringify(4001));
        Assert.assertEquals("4002nd", instance.stringify(4002));
        Assert.assertEquals("4003rd", instance.stringify(4003));
        Assert.assertEquals("4004th", instance.stringify(4004));
        Assert.assertEquals("4010th", instance.stringify(4010));
        Assert.assertEquals("4011th", instance.stringify(4011));
        Assert.assertEquals("4012th", instance.stringify(4012));
        Assert.assertEquals("4013th", instance.stringify(4013));
        Assert.assertEquals("2147483647th", instance.stringify(Integer.MAX_VALUE));
    }

    @Test
    public void caseLong() {
        final IntegralToStringFunction instance = Ordinal.INSTANCE;
        Assert.assertEquals("1234567890000th", instance.stringify(1234567890000L));
        Assert.assertEquals("1234567890001st", instance.stringify(1234567890001L));
        Assert.assertEquals("1234567890002nd", instance.stringify(1234567890002L));
        Assert.assertEquals("1234567890003rd", instance.stringify(1234567890003L));
        Assert.assertEquals("1234567890004th", instance.stringify(1234567890004L));
        Assert.assertEquals("1234567890010th", instance.stringify(1234567890010L));
        Assert.assertEquals("1234567890011th", instance.stringify(1234567890011L));
        Assert.assertEquals("1234567890012th", instance.stringify(1234567890012L));
        Assert.assertEquals("1234567890013th", instance.stringify(1234567890013L));
        Assert.assertEquals("1234567890020th", instance.stringify(1234567890020L));
        Assert.assertEquals("1234567890021st", instance.stringify(1234567890021L));
        Assert.assertEquals("1234567890022nd", instance.stringify(1234567890022L));
        Assert.assertEquals("1234567890023rd", instance.stringify(1234567890023L));
        Assert.assertEquals("1234567890024th", instance.stringify(1234567890024L));
        Assert.assertEquals("9223372036854775807th", instance.stringify(Long.MAX_VALUE));
    }
}
