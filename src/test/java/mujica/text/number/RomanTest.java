package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

@CodeHistory(date = "2025/3/1")
@SuppressWarnings("SpellCheckingInspection")
public class RomanTest {

    @NotNull
    private String caseStringify(int value) {
        final String result = Roman.UPPER.stringify(value);
        Assert.assertEquals(result, Roman.LOWER.stringify(value).toUpperCase());
        Assert.assertEquals(result, Roman.UPPER.stringify((long) value));
        Assert.assertEquals(result, Roman.LOWER.stringify((long) value).toUpperCase());
        return result;
    }

    private void caseStringify(@NotNull Set<String> expected, int value) {
        Assert.assertTrue(expected.contains(Roman.UPPER.stringify(value)));
        Assert.assertTrue(expected.contains(Roman.LOWER.stringify(value).toUpperCase()));
        Assert.assertTrue(expected.contains(Roman.UPPER.stringify((long) value)));
        Assert.assertTrue(expected.contains(Roman.LOWER.stringify((long) value).toUpperCase()));
    }
    
    @Test
    public void case1() {
        Assert.assertEquals("I", caseStringify(1));
        Assert.assertEquals("II", caseStringify(2));
        Assert.assertEquals("III", caseStringify(3));
        Assert.assertEquals("IV", caseStringify(4));
        Assert.assertEquals("V", caseStringify(5));
        Assert.assertEquals("VI", caseStringify(6));
        Assert.assertEquals("VII", caseStringify(7));
        Assert.assertEquals("VIII", caseStringify(8));
        Assert.assertEquals("IX", caseStringify(9));
    }
    
    @Test
    public void case2() {
        Assert.assertEquals("X", caseStringify(10));
        Assert.assertEquals("XI", caseStringify(11));
        Assert.assertEquals("XII", caseStringify(12));
        Assert.assertEquals("XIII", caseStringify(13));
        Assert.assertEquals("XIV", caseStringify(14));
        Assert.assertEquals("XV", caseStringify(15));
        Assert.assertEquals("XVI", caseStringify(16));
        Assert.assertEquals("XVII", caseStringify(17));
        Assert.assertEquals("XVIII", caseStringify(18));
        Assert.assertEquals("XIX", caseStringify(19));
        Assert.assertEquals("XX", caseStringify(20));
    }

    @Test
    public void case3() {
        Assert.assertEquals("XXI", caseStringify(21));
        Assert.assertEquals("XXII", caseStringify(22));
        Assert.assertEquals("XXIX", caseStringify(29));
        Assert.assertEquals("XXX", caseStringify(30));
        Assert.assertEquals("XXXIV", caseStringify(34));
        Assert.assertEquals("XXXV", caseStringify(35));
        Assert.assertEquals("XXXIX", caseStringify(39));
        Assert.assertEquals("XL", caseStringify(40));
        Assert.assertEquals("XLV", caseStringify(45));
        caseStringify(Set.of("XLIX", "IL"), 49);
        Assert.assertEquals("L", caseStringify(50));
        Assert.assertEquals("LI", caseStringify(51));
        Assert.assertEquals("LV", caseStringify(55));
        Assert.assertEquals("LX", caseStringify(60));
        Assert.assertEquals("LXV", caseStringify(65));
        Assert.assertEquals("LXXX", caseStringify(80));
        Assert.assertEquals("XC", caseStringify(90));
        Assert.assertEquals("XCIII", caseStringify(93));
        Assert.assertEquals("XCV", caseStringify(95));
        Assert.assertEquals("XCVIII", caseStringify(98));
        caseStringify(Set.of("XCIX", "IC"), 99);
    }

    @Test
    public void case4() {
        Assert.assertEquals("C", caseStringify(100));
        Assert.assertEquals("CC", caseStringify(200));
        Assert.assertEquals("CCC", caseStringify(300));
        Assert.assertEquals("CD", caseStringify(400));
        caseStringify(Set.of("CDXC", "XD"), 490);
        Assert.assertEquals("CDXCV", caseStringify(495));
        Assert.assertEquals("CDXCIX", caseStringify(499));
        Assert.assertEquals("D", caseStringify(500));
        Assert.assertEquals("DC", caseStringify(600));
        Assert.assertEquals("DCC", caseStringify(700));
        Assert.assertEquals("DCCC", caseStringify(800));
        Assert.assertEquals("CM", caseStringify(900));
        Assert.assertEquals("CMXCIX", caseStringify(999));
    }
    
    @Test
    public void case5() {
        Assert.assertEquals("M", caseStringify(1000));
        Assert.assertEquals("MC", caseStringify(1100));
        Assert.assertEquals("MCD", caseStringify(1400));
        Assert.assertEquals("MD", caseStringify(1500));
        Assert.assertEquals("MDC", caseStringify(1600));
        Assert.assertEquals("MDCLXVI", caseStringify(1666));
        Assert.assertEquals("MDCCCLXXXVIII", caseStringify(1888));
        caseStringify(Set.of("MDCCCXCIX", "MDCCCIC"), 1899);
        Assert.assertEquals("MCM", caseStringify(1900));
        Assert.assertEquals("MCMLXXVI", caseStringify(1976));
        Assert.assertEquals("MCMLXXXIV", caseStringify(1984));
        caseStringify(Set.of("MCMXC", "MXM"), 1990);
        Assert.assertEquals("MM", caseStringify(2000));
        caseStringify(Set.of("MMMCMXCIX", "MMMIM"), 3999);
    }
}
