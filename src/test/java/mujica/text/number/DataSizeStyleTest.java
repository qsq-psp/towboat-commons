package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Created on 2025/3/5.
 */
@CodeHistory(date = "2025/3/5")
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

    @Test
    public void case1000M0() {
        final DataSizeStyle style = DataSizeStyle.create1000(0);
        Assert.assertEquals("0 byte(s)", style.stringify(0));
        Assert.assertEquals("1 byte(s)", style.stringify(1));
        Assert.assertEquals("999 byte(s)", style.stringify(999));
        Assert.assertEquals("1KB", style.stringify(1000));
        Assert.assertEquals("1KB", style.stringify(1001));
        Assert.assertEquals("1KB", style.stringify(1499));
        Assert.assertEquals("1KB", style.stringify(1999));
        Assert.assertEquals("2KB", style.stringify(2000));
        Assert.assertEquals("999KB", style.stringify(999_000));
        Assert.assertEquals("1MB", style.stringify(1_000_000));
        Assert.assertEquals("1MB", style.stringify(1_000_999));
        Assert.assertEquals("1MB", style.stringify(1_999_999));
        Assert.assertEquals("307MB", style.stringify(307_000_000));
        Assert.assertEquals("214GB", style.stringify(214_000_000_000L));
        Assert.assertEquals("79TB", style.stringify(79_000_000_000_000L));
        Assert.assertEquals("6PB", style.stringify(6_050_040_030_020_010L));
    }

    @Test
    public void case1000M1() {
        final DataSizeStyle style = DataSizeStyle.create1000(1);
        Assert.assertEquals("0 byte(s)", style.stringify(0));
        Assert.assertEquals("1 byte(s)", style.stringify(1));
        Assert.assertEquals("999 byte(s)", style.stringify(999));
        Assert.assertEquals("1.0KB", style.stringify(1000));
        Assert.assertEquals("1.0KB", style.stringify(1007));
        Assert.assertEquals("1.3KB", style.stringify(1399));
        Assert.assertEquals("1.4KB", style.stringify(1400));
        Assert.assertEquals("1.7KB", style.stringify(1799));
        Assert.assertEquals("2.0KB", style.stringify(2000));
        Assert.assertEquals("999.0KB", style.stringify(999_000));
        Assert.assertEquals("1.0MB", style.stringify(1_000_000));
        Assert.assertEquals("1.0MB", style.stringify(1_000_500));
        Assert.assertEquals("1.0MB", style.stringify(1_002_750));
        Assert.assertEquals("1.1MB", style.stringify(1_122_800));
        Assert.assertEquals("282.0MB", style.stringify(282_000_288));
        Assert.assertEquals("339.3GB", style.stringify(339_331_337_332L));
        Assert.assertEquals("79.1TB", style.stringify(79_112_233_445_566L));
    }

    @Test
    public void case1000M2() {
        final DataSizeStyle style = DataSizeStyle.create1000(2);
        Assert.assertEquals("0 byte(s)", style.stringify(0));
        Assert.assertEquals("4 byte(s)", style.stringify(4));
        Assert.assertEquals("5 byte(s)", style.stringify(5));
        Assert.assertEquals("18 byte(s)", style.stringify(18));
        Assert.assertEquals("81 byte(s)", style.stringify(81));
        Assert.assertEquals("202 byte(s)", style.stringify(202));
        Assert.assertEquals("889 byte(s)", style.stringify(889));
        Assert.assertEquals("1.00KB", style.stringify(1000));
        Assert.assertEquals("1.00KB", style.stringify(1005));
        Assert.assertEquals("1.01KB", style.stringify(1019));
        Assert.assertEquals("1.02KB", style.stringify(1024));
        Assert.assertEquals("1.02KB", style.stringify(1025));
        Assert.assertEquals("1.10KB", style.stringify(1100));
        Assert.assertEquals("1.10KB", style.stringify(1109));
        Assert.assertEquals("1.14KB", style.stringify(1142));
        Assert.assertEquals("3.14KB", style.stringify(3143));
        Assert.assertEquals("5.66MB", style.stringify(5_662_710));
        Assert.assertEquals("33.80MB", style.stringify(33_801_920));
        Assert.assertEquals("39.99MB", style.stringify(39_999_920));
        Assert.assertEquals("1.05GB", style.stringify(1_057_170_391));
        Assert.assertEquals("2.14GB", style.stringify(2_147_483_646));
        Assert.assertEquals("2.14GB", style.stringify(2_147_483_647));
        Assert.assertEquals("5.04PB", style.stringify(5_040_030_020_010_000L));
    }

    @Test
    public void case1000M3() {
        final DataSizeStyle style = DataSizeStyle.create1000(3);
        Assert.assertEquals("45 byte(s)", style.stringify(45));
        Assert.assertEquals("54 byte(s)", style.stringify(54));
        Assert.assertEquals("509 byte(s)", style.stringify(509));
        Assert.assertEquals("905 byte(s)", style.stringify(905));
        Assert.assertEquals("1.000KB", style.stringify(1000));
        Assert.assertEquals("1.001KB", style.stringify(1001));
        Assert.assertEquals("1.499KB", style.stringify(1499));
        Assert.assertEquals("1.999KB", style.stringify(1999));
        Assert.assertEquals("2.000KB", style.stringify(2000));
        Assert.assertEquals("1.000MB", style.stringify(1_000_000));
        Assert.assertEquals("1.000MB", style.stringify(1_000_500));
        Assert.assertEquals("1.002MB", style.stringify(1_002_750));
        Assert.assertEquals("1.122MB", style.stringify(1_122_881));
        Assert.assertEquals("1.002TB", style.stringify(1_002_231_415_512L));
        Assert.assertEquals("6.193TB", style.stringify(6_193_449_215_608L));
        Assert.assertEquals("79.112TB", style.stringify(79_112_233_445_566L));
        Assert.assertEquals("9.000EB", style.stringify(9_000_000_000_000_000_000L));
        Assert.assertEquals("9.200EB", style.stringify(9_200_000_000_000_000_000L));
        Assert.assertEquals("9.220EB", style.stringify(9_220_000_000_000_000_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_000_000_000_000_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_372_000_000_000_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_372_036_000_000_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_372_036_854_000_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_372_036_854_775_000L));
        Assert.assertEquals("9.223EB", style.stringify(9_223_372_036_854_775_807L));
    }

    @Test
    public void case1024M0() {
        final DataSizeStyle style = DataSizeStyle.create1024(0);
        Assert.assertEquals("0 byte(s)", style.stringify(0));
        Assert.assertEquals("1 byte(s)", style.stringify(1));
        Assert.assertEquals("8 byte(s)", style.stringify(8));
        Assert.assertEquals("9 byte(s)", style.stringify(9));
        Assert.assertEquals("1000 byte(s)", style.stringify(1000));
        Assert.assertEquals("1023 byte(s)", style.stringify(1023));
        Assert.assertEquals("1KiB", style.stringify(1024));
        Assert.assertEquals("1KiB", style.stringify(1025));
        Assert.assertEquals("1KiB", style.stringify(2047));
        Assert.assertEquals("2KiB", style.stringify(2048));
    }
}
