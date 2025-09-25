package mujica.text.number;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Created on 2025/3/5.
 */
public class DataSizeStyleTest {

    private void caseInt(@NotNull DataSizeStyle style, int input, @NotNull String output) {
        Assert.assertFalse(style.isEmpty());
        Assert.assertEquals(output, style.stringify(input));
        Assert.assertEquals(output, style.stringify((long) input));
        Assert.assertEquals(output, style.stringify(BigInteger.valueOf(input)));
    }

    private void caseLong(@NotNull DataSizeStyle style, long input, @NotNull String output) {
        Assert.assertFalse(style.isEmpty());
        Assert.assertEquals(output, style.stringify(input));
        Assert.assertEquals(output, style.stringify(BigInteger.valueOf(input)));
    }

    @Test
    public void caseDec() {
        final DataSizeStyle style = DataSizeStyle.createDec();
        caseInt(style, 0, "0");
        caseInt(style, 1, "1");
        caseInt(style, 9, "9");
        caseInt(style, 10, "10");
        caseInt(style, 20, "20");
        caseInt(style, 99, "99");
        caseInt(style, 10000, "10000");
        caseInt(style, 90000, "90000");
        caseInt(style, Integer.MAX_VALUE - 1, "2147483646");
        caseInt(style, Integer.MAX_VALUE, "2147483647");
        caseLong(style, Integer.MAX_VALUE + 1L, "2147483648");
        caseLong(style, 9876543210L, "9876543210");
        caseLong(style, Long.MAX_VALUE - 1L, "9223372036854775806");
        caseLong(style, Long.MAX_VALUE, "9223372036854775807");
    }

    @Test
    public void caseHexLower() {
        final DataSizeStyle style = DataSizeStyle.createHex(false, false);
        caseInt(style, 0, "0x0");
        caseInt(style, 1, "0x1");
        caseInt(style, 2, "0x2");
        caseInt(style, 15, "0xf");
        caseInt(style, 16, "0x10");
        caseInt(style, 43690, "0xaaaa");
        caseInt(style, 48879, "0xbeef");
    }

    @Test
    public void caseHexUpper() {
        final DataSizeStyle style = DataSizeStyle.createHex(true, false);
        caseInt(style, 0, "0x0");
        caseInt(style, 1, "0x1");
        caseInt(style, 4, "0x4");
        caseInt(style, 7, "0x7");
        caseInt(style, 10, "0xA");
        caseInt(style, 148, "0x94");
        caseInt(style, 188, "0xBC");
        caseInt(style, 329, "0x149");
        caseInt(style, 65533, "0xFFFD");
        caseInt(style, 65786, "0x100FA");
    }

    @Test
    public void caseHexPadding() {
        final DataSizeStyle style = DataSizeStyle.createHex(false, true);
        Assert.assertEquals("0x00000000", style.stringify(0));
        Assert.assertEquals("0x00000001", style.stringify(1));
        Assert.assertEquals("0x01010101", style.stringify(16843009));
        Assert.assertEquals("0x5a5a5a5a", style.stringify(1515870810));
        Assert.assertEquals("0x7f7f7f7f", style.stringify(2139062143));
        Assert.assertEquals("0x7fffffff", style.stringify(Integer.MAX_VALUE));
        Assert.assertEquals("0x0000000000000000", style.stringify(0L));
        Assert.assertEquals("0x0000000000000001", style.stringify(1L));
        Assert.assertEquals("0x0000000001010101", style.stringify(16843009L));
        Assert.assertEquals("0x000000005a5a5a5a", style.stringify(1515870810L));
        Assert.assertEquals("0x000000007f7f7f7f", style.stringify(2139062143L));
        Assert.assertEquals("0x7fffffffffffffff", style.stringify(Long.MAX_VALUE));
    }
}
