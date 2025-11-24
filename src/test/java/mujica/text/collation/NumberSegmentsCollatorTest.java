package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.text.Collator;

@CodeHistory(date = "2025/10/15")
@SuppressWarnings("SpellCheckingInspection")
public class NumberSegmentsCollatorTest {

    @Test
    public void caseToZero10() {
        Assert.assertEquals("0sdnqnaa0nr", NumberSegmentsCollationKey.toZero("8sdnqnaa7nr", 10));
        Assert.assertEquals("dbyjpcu0zsf", NumberSegmentsCollationKey.toZero("dbyjpcu0zsf", 10));
        Assert.assertEquals("0plxnncbt0", NumberSegmentsCollationKey.toZero("7plxnncbt72", 10));
        Assert.assertEquals("t0n0hqf", NumberSegmentsCollationKey.toZero("t7713n5hqf", 10));
    }

    @Test
    public void caseWeak10() {
        final NumberSegmentsCollator collator = new NumberSegmentsCollator();
        collator.setStrength(Collator.PRIMARY);
        Assert.assertEquals(-1, collator.normalizedCompare("a02", "b01"));
        Assert.assertEquals(-1, collator.normalizedCompare("88F", "7W"));
        Assert.assertEquals(-1, collator.normalizedCompare("9087ddx602", "1ddy8"));
        Assert.assertEquals(1, collator.normalizedCompare("a30", "A30"));
        Assert.assertEquals(1, collator.normalizedCompare("2C30", "87A0"));
        Assert.assertEquals(1, collator.normalizedCompare("556tvw", "430091tuw"));
        Assert.assertEquals(0, collator.normalizedCompare("ffcd22", "ffcd359"));
        Assert.assertEquals(0, collator.normalizedCompare("gi8rcc", "gi92rcc"));
        Assert.assertEquals(0, collator.normalizedCompare("02AW", "2007AW"));
        Assert.assertEquals(-1, collator.normalizedCompare("a579824578247592", "b574897"));
        Assert.assertEquals(-1, collator.normalizedCompare("81F", "06524700048983493W"));
        Assert.assertEquals(-1, collator.normalizedCompare("00000000439279ddx947398749", "4ddy2"));
        Assert.assertEquals(1, collator.normalizedCompare("a8497489798", "A79279878884738748379989439849387583744"));
        Assert.assertEquals(1, collator.normalizedCompare("0909002849347893438657C00", "27A462345726538726573525768"));
        Assert.assertEquals(1, collator.normalizedCompare("000tvw", "4802839274379874389tuw"));
        Assert.assertEquals(0, collator.normalizedCompare("ffcd32", "ffcd8008234676199377466"));
        Assert.assertEquals(0, collator.normalizedCompare("gi3rcc", "gi8329847rcc"));
        Assert.assertEquals(0, collator.normalizedCompare("27936287984797296738463AW", "2024AW"));
    }

    @Test
    public void caseNormal10() {
        final NumberSegmentsCollator collator = new NumberSegmentsCollator();
        collator.setStrength(Collator.SECONDARY);
        Assert.assertEquals(-1, collator.normalizedCompare("XC51", "XD49"));
        Assert.assertEquals(-1, collator.normalizedCompare("3002FRTTT", "3007FRXXX"));
        Assert.assertEquals(-1, collator.normalizedCompare("s4096i409871m", "s76j04m"));
        Assert.assertEquals(1, collator.normalizedCompare("ax", "AX"));
        Assert.assertEquals(1, collator.normalizedCompare("780QY64", "1099PY308710"));
        Assert.assertEquals(1, collator.normalizedCompare("109825648s7872z", "2s8y"));
        Assert.assertEquals(-1, collator.normalizedCompare("*5", "*14"));
        Assert.assertEquals(-1, collator.normalizedCompare("02^", "11^"));
        Assert.assertEquals(-1, collator.normalizedCompare("--45--", "--109--"));
        Assert.assertEquals(1, collator.normalizedCompare("##7##68", "##007##67"));
        Assert.assertEquals(1, collator.normalizedCompare("2(flip)", "0(flip)"));
        Assert.assertEquals(1, collator.normalizedCompare("[209875]", "[1]"));
        Assert.assertEquals(0, collator.normalizedCompare("abcdef5", "abcdef05"));
        Assert.assertEquals(0, collator.normalizedCompare("02r03r006r", "2r003r6r"));
        Assert.assertEquals(0, collator.normalizedCompare("w01", "w00000001"));
        Assert.assertEquals(-1, collator.normalizedCompare("*334793", "*18405842050928450928405843"));
        Assert.assertEquals(-1, collator.normalizedCompare("00024792384^", "1241234238042379472^"));
        Assert.assertEquals(-1, collator.normalizedCompare("--329389--", "--29349832748937249723974974342--"));
        Assert.assertEquals(1, collator.normalizedCompare("##4##397498372479237493274", "##004##397498372479237493271"));
        Assert.assertEquals(1, collator.normalizedCompare("23749723987432987494(flip)", "23749723987432987194(flip)"));
        Assert.assertEquals(1, collator.normalizedCompare("[38493274872397492739879287343]", "[274872397492739879287]"));
        Assert.assertEquals(0, collator.normalizedCompare("abcdef4793749279", "abcdef04793749279"));
        Assert.assertEquals(0, collator.normalizedCompare("0223749729472r03453453465653r005464353465r", "223749729472r3453453465653r0000005464353465r"));
        Assert.assertEquals(0, collator.normalizedCompare("w04792374923749", "w00000000000000004792374923749"));
    }
}
