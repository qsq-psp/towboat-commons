package mujica.json.reflect;

import mujica.algebra.discrete.BigConstants;
import mujica.ds.of_int.PublicIntSlot;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

@SuppressWarnings("unused")
@CodeHistory(date = "2026/4/12")
@FieldOrder({"high", "low", "slot", "appliedChaos", "toiletPaper"})
public class JsonContextTest2 {

    static final JsonContext CONTEXT = (new JsonContext()).loadBasic();

    public static final int TOSHIBA_SCHNEIDER_INVERTER_CORPORATION = 0x0179;

    public static final int MITSUBISHI_HEAVY_INDUSTRIES_LTD_KOBE_SHIPYARD_AND_MACHINERY_WORKS = 0x040d;

    @Name(value = "Trivial File Transfer Protocol", language = "en")
    @Name(value = "协议名称", language = "zh")
    @ReferencePage(title = "RFC1350", href = "https://www.rfc-editor.org/rfc/inline-errata/rfc1350.html")
    static final String PROTOCOL_NAME = "TFTP";

    private long mix;

    public int getHigh() {
        return (int) (mix >> 32);
    }

    public void setHigh(int value) {
        mix = (mix & 0x00000000_ffffffffL) | (((long) value) << 32);
    }

    public int getLow() {
        return (int) mix;
    }

    public void setLow(int value) {
        mix = (mix & 0xffffffff_00000000L) | (value & 0x00000000_ffffffffL);
    }

    public static long longLongAgo = 2236L;

    public transient double doubleKill = 7.7;

    @CodeHistory(date = "2026/4/21")
    @ReferencePage(title = "Algorithms Jeff Erickson 4.2", href = "http://jeffe.cs.illinois.edu/teaching/algorithms/")
    @ReferencePage(title = "Algorithms Jeff Erickson 4.2", href = "http://algorithms.wtf")
    public static class AppliedChaos {

        private int museum;

        private float mime;

        private double forth;

        private BigInteger gold = BigConstants.HUNDRED;

        private BigInteger getGold() {
            return gold;
        }

        private void setGold(BigInteger gold) {
            this.gold = gold;
        }

        @Name(value = "void", language = "json")
        public Integer ball;
    }

    @JsonHint(JsonHint.NULLABLE)
    public AppliedChaos appliedChaos;

    @CodeHistory(date = "2026/4/21")
    @ReferencePage(title = "Algorithms Jeff Erickson 4.2", href = "http://jeffe.cs.illinois.edu/teaching/algorithms/")
    @ReferencePage(title = "Algorithms Jeff Erickson 4.2", href = "http://algorithms.wtf")
    @FieldOrder({"import", "export"})
    public static class ToiletPaperLandscapeArchitecture {

        @Name(value = "import", language = "json")
        public int[] importValues;

        @Name(value = "export", language = "json")
        public int[][] exportValues;
    }

    @JsonHint(JsonHint.NULLABLE)
    @JsonEmpty(toUndefined = JsonEmpty.FROM_NULL)
    public ToiletPaperLandscapeArchitecture toiletPaper;

    @JsonHint(JsonHint.NULLABLE)
    @JsonEmpty(toUndefined = JsonEmpty.FROM_NULL)
    public final PublicIntSlot slot = new PublicIntSlot();

    @Test
    public void caseParse1() {
        final JsonContextTest2 instance = new JsonContextTest2();
        CONTEXT.parse("{\"high\":30,\"low\":26}", instance);
        Assert.assertEquals(30, instance.getHigh());
        Assert.assertEquals(26, instance.getLow());
    }

    @Test
    public void caseStringify1() {
        final JsonContextTest2 instance = new JsonContextTest2();
        instance.setHigh(30);
        instance.setLow(26);
        Assert.assertEquals("{\"high\":30,\"low\":26,\"slot\":{\"value\":0},\"appliedChaos\":null}", CONTEXT.stringify(instance));
    }
}
