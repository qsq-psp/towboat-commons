package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/3/4")
public class SimplifiedChineseTest {

    @Test
    public void caseSmall() {
        Assert.assertEquals("零", SimplifiedChinese.SIMPLIFIED.stringify(0));
        Assert.assertEquals("一", SimplifiedChinese.SIMPLIFIED.stringify(1));
        Assert.assertEquals("九", SimplifiedChinese.SIMPLIFIED.stringify(9));
        Assert.assertEquals("十", SimplifiedChinese.SIMPLIFIED.stringify(10));
        Assert.assertEquals("十四", SimplifiedChinese.SIMPLIFIED.stringify(14));
        Assert.assertEquals("二十", SimplifiedChinese.SIMPLIFIED.stringify(20));
        Assert.assertEquals("二十八", SimplifiedChinese.SIMPLIFIED.stringify(28));
        Assert.assertEquals("八十九", SimplifiedChinese.SIMPLIFIED.stringify(89));
        Assert.assertEquals("九十九", SimplifiedChinese.SIMPLIFIED.stringify(99));
        Assert.assertEquals("一百", SimplifiedChinese.SIMPLIFIED.stringify(100));
        Assert.assertEquals("一百零一", SimplifiedChinese.SIMPLIFIED.stringify(101));
        Assert.assertEquals("一百一十", SimplifiedChinese.SIMPLIFIED.stringify(110));
        Assert.assertEquals("一百一十七", SimplifiedChinese.SIMPLIFIED.stringify(117));
        Assert.assertEquals("两百", SimplifiedChinese.SIMPLIFIED.stringify(200));
        Assert.assertEquals("三百零六", SimplifiedChinese.SIMPLIFIED.stringify(306));
        Assert.assertEquals("四百四十", SimplifiedChinese.SIMPLIFIED.stringify(440));
        Assert.assertEquals("九百零九", SimplifiedChinese.SIMPLIFIED.stringify(909));
        Assert.assertEquals("九百九十九", SimplifiedChinese.SIMPLIFIED.stringify(999));
        Assert.assertEquals("一千", SimplifiedChinese.SIMPLIFIED.stringify(1000));
        Assert.assertEquals("一千零三", SimplifiedChinese.SIMPLIFIED.stringify(1003));
        Assert.assertEquals("一千零四十", SimplifiedChinese.SIMPLIFIED.stringify(1040));
        Assert.assertEquals("一千五百", SimplifiedChinese.SIMPLIFIED.stringify(1500));
        Assert.assertEquals("一千九百九十九", SimplifiedChinese.SIMPLIFIED.stringify(1999));
        Assert.assertEquals("两千", SimplifiedChinese.SIMPLIFIED.stringify(2000));
        Assert.assertEquals("两千零一", SimplifiedChinese.SIMPLIFIED.stringify(2001));
        Assert.assertEquals("两千零二十", SimplifiedChinese.SIMPLIFIED.stringify(2020));
        Assert.assertEquals("两千八百", SimplifiedChinese.SIMPLIFIED.stringify(2800));
        Assert.assertEquals("两千九百零一", SimplifiedChinese.SIMPLIFIED.stringify(2901));
    }

    @Test
    public void caseMedium() {
        Assert.assertEquals("一万", SimplifiedChinese.SIMPLIFIED.stringify(10000));
        Assert.assertEquals("一万零六", SimplifiedChinese.SIMPLIFIED.stringify(10006));
        Assert.assertEquals("一万零三十", SimplifiedChinese.SIMPLIFIED.stringify(10030));
        Assert.assertEquals("一万零四十四", SimplifiedChinese.SIMPLIFIED.stringify(10044));
        Assert.assertEquals("一万零八百零八", SimplifiedChinese.SIMPLIFIED.stringify(10808));
        Assert.assertEquals("一万零九百", SimplifiedChinese.SIMPLIFIED.stringify(10900));
        Assert.assertEquals("一万一千", SimplifiedChinese.SIMPLIFIED.stringify(11000));
        Assert.assertEquals("一万两千零二", SimplifiedChinese.SIMPLIFIED.stringify(12002));
        Assert.assertEquals("两万零三百", SimplifiedChinese.SIMPLIFIED.stringify(20300));
        Assert.assertEquals("两万四千", SimplifiedChinese.SIMPLIFIED.stringify(24000));
        Assert.assertEquals("五万零两百二十二", SimplifiedChinese.SIMPLIFIED.stringify(50222));
        Assert.assertEquals("九万八千七百六十五", SimplifiedChinese.SIMPLIFIED.stringify(98765));
        Assert.assertEquals("十万", SimplifiedChinese.SIMPLIFIED.stringify(100000));
        Assert.assertEquals("十万零两千", SimplifiedChinese.SIMPLIFIED.stringify(102000));
        Assert.assertEquals("十二万", SimplifiedChinese.SIMPLIFIED.stringify(120000));
        Assert.assertEquals("十三万三千", SimplifiedChinese.SIMPLIFIED.stringify(133000));
        Assert.assertEquals("十四万四千四百", SimplifiedChinese.SIMPLIFIED.stringify(144400));
        Assert.assertEquals("十八万八千零九十二", SimplifiedChinese.SIMPLIFIED.stringify(188092));
        Assert.assertEquals("二十万零八千零二十一", SimplifiedChinese.SIMPLIFIED.stringify(208021));
        Assert.assertEquals("三十二万", SimplifiedChinese.SIMPLIFIED.stringify(320000));
    }

    @Test
    public void caseLarge() {
        Assert.assertEquals("一亿", SimplifiedChinese.SIMPLIFIED.stringify(100000000L));
        Assert.assertEquals("两亿三千万", SimplifiedChinese.SIMPLIFIED.stringify(230000000L));
        Assert.assertEquals("五亿四千八百万", SimplifiedChinese.SIMPLIFIED.stringify(548000000L));
        Assert.assertEquals("六亿零七百万", SimplifiedChinese.SIMPLIFIED.stringify(607000000L));
        Assert.assertEquals("六亿零九百九十万", SimplifiedChinese.SIMPLIFIED.stringify(609900000L));
        Assert.assertEquals("八亿两千零四万四千", SimplifiedChinese.SIMPLIFIED.stringify(820044000L));
        Assert.assertEquals("九亿零二十", SimplifiedChinese.SIMPLIFIED.stringify(900000020L));
        Assert.assertEquals("九亿零三百三十三", SimplifiedChinese.SIMPLIFIED.stringify(900000333L));
        Assert.assertEquals("十亿", SimplifiedChinese.SIMPLIFIED.stringify(1000000000L));
        Assert.assertEquals("三十九亿", SimplifiedChinese.SIMPLIFIED.stringify(3900000000L));
        Assert.assertEquals("五百亿", SimplifiedChinese.SIMPLIFIED.stringify(50000000000L));
        Assert.assertEquals("七百零七亿", SimplifiedChinese.SIMPLIFIED.stringify(70700000000L));
        Assert.assertEquals("八千亿", SimplifiedChinese.SIMPLIFIED.stringify(800000000000L));
        Assert.assertEquals("八千八百二十亿", SimplifiedChinese.SIMPLIFIED.stringify(882000000000L));
        Assert.assertEquals("八千九百零两亿", SimplifiedChinese.SIMPLIFIED.stringify(890200000000L)); // ?
    }

    @Test
    public void caseNegative() {
        Assert.assertEquals("负一", SimplifiedChinese.SIMPLIFIED.stringify(-1));
        Assert.assertEquals("负九十九", SimplifiedChinese.SIMPLIFIED.stringify(-99));
        Assert.assertEquals("负九百九十二", SimplifiedChinese.SIMPLIFIED.stringify(-992));
        Assert.assertEquals("负七千零三", SimplifiedChinese.SIMPLIFIED.stringify(-7003));
        Assert.assertEquals("负一万零三百", SimplifiedChinese.SIMPLIFIED.stringify(-10300));
        Assert.assertEquals("负五万零九", SimplifiedChinese.SIMPLIFIED.stringify(-50009));
        Assert.assertEquals("负八万八千零二", SimplifiedChinese.SIMPLIFIED.stringify(-88002));
    }

    @Test
    public void casePercent() {
        Assert.assertEquals("百分之零", SimplifiedChinese.SIMPLIFIED.stringify("0%", 10));
        Assert.assertEquals("百分之八", SimplifiedChinese.SIMPLIFIED.stringify("8%", 10));
        Assert.assertEquals("百分之三十", SimplifiedChinese.SIMPLIFIED.stringify("30%", 10));
        Assert.assertEquals("百分之三十三", SimplifiedChinese.SIMPLIFIED.stringify("33%", 10));
        Assert.assertEquals("百分之五十", SimplifiedChinese.SIMPLIFIED.stringify("50%", 10));
        Assert.assertEquals("百分之九十一", SimplifiedChinese.SIMPLIFIED.stringify("91%", 10));
        Assert.assertEquals("百分之九十九", SimplifiedChinese.SIMPLIFIED.stringify("99%", 10));
        Assert.assertEquals("百分之一百", SimplifiedChinese.SIMPLIFIED.stringify("100%", 10));
        Assert.assertEquals("百分之一百零四", SimplifiedChinese.SIMPLIFIED.stringify("104%", 10));
    }

    @Test
    public void caseDot() {
        Assert.assertEquals("一点零", SimplifiedChinese.SIMPLIFIED.stringify("1.0", 10));
        Assert.assertEquals("一点七", SimplifiedChinese.SIMPLIFIED.stringify("1.7", 10));
        Assert.assertEquals("二十三点零八", SimplifiedChinese.SIMPLIFIED.stringify("23.08", 10));
        Assert.assertEquals("七十八点二零零", SimplifiedChinese.SIMPLIFIED.stringify("78.200", 10));
        Assert.assertEquals("一千零三点零二二", SimplifiedChinese.SIMPLIFIED.stringify("1003.022", 10));
        Assert.assertEquals("负四点六", SimplifiedChinese.SIMPLIFIED.stringify("-4.6", 10));
        Assert.assertEquals("负九点五五", SimplifiedChinese.SIMPLIFIED.stringify("-9.55", 10));
        Assert.assertEquals("负四十九点二二七", SimplifiedChinese.SIMPLIFIED.stringify("-49.227", 10));
        Assert.assertEquals("负两百八十点零零三", SimplifiedChinese.SIMPLIFIED.stringify("-280.003", 10));
    }

    @Test
    public void casePercentDot() {
        Assert.assertEquals("百分之零点零三", SimplifiedChinese.SIMPLIFIED.stringify("0.03%", 10));
        Assert.assertEquals("百分之零点五零", SimplifiedChinese.SIMPLIFIED.stringify("0.50%", 10));
        Assert.assertEquals("百分之一点八", SimplifiedChinese.SIMPLIFIED.stringify("1.8%", 10));
        Assert.assertEquals("百分之十一点九五", SimplifiedChinese.SIMPLIFIED.stringify("11.95%", 10));
        Assert.assertEquals("百分之二十点五九一", SimplifiedChinese.SIMPLIFIED.stringify("20.591%", 10));
        Assert.assertEquals("百分之五十点零", SimplifiedChinese.SIMPLIFIED.stringify("50.0%", 10));
        Assert.assertEquals("百分之七十五点零", SimplifiedChinese.SIMPLIFIED.stringify("75.0%", 10));
        Assert.assertEquals("百分之九十九点二四", SimplifiedChinese.SIMPLIFIED.stringify("99.24%", 10));
    }

    @Test
    public void caseBinary() {
        Assert.assertEquals("十八", SimplifiedChinese.SIMPLIFIED.stringify("10010", 2));
        Assert.assertEquals("两千零十八", SimplifiedChinese.SIMPLIFIED.stringify("11111100010", 2));
        Assert.assertEquals("七亿四万两千零十八", SimplifiedChinese.SIMPLIFIED.stringify("101001101110011100101100100010", 2));
        Assert.assertEquals("负五", SimplifiedChinese.SIMPLIFIED.stringify("-101", 2));
        Assert.assertEquals("负九十五", SimplifiedChinese.SIMPLIFIED.stringify("-1011111", 2));
        Assert.assertEquals("负三千零九十五", SimplifiedChinese.SIMPLIFIED.stringify("-110000010111", 2));
        Assert.assertEquals("负两百万三千零九十五", SimplifiedChinese.SIMPLIFIED.stringify("-111101001000010010111", 2));
    }
}
