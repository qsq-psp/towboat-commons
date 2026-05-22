package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.math.RoundingMode;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2026/4/11")
public class JsonContextTransformTest {

    static final JsonContext CONTEXT = (new JsonContext()).loadBasic();

    @Test
    public void caseSimple() {
        Assert.assertEquals("null", CONTEXT.stringify(null));
        Assert.assertEquals("false", CONTEXT.stringify(false));
        Assert.assertEquals("true", CONTEXT.stringify(true));
        Assert.assertEquals("7500", CONTEXT.stringify(7500));
        Assert.assertEquals("-81", CONTEXT.stringify(-81));
        Assert.assertEquals("\"java:comp, java:module, java:app, java:global\"", CONTEXT.stringify("java:comp, java:module, java:app, java:global"));
    }

    @Test
    public void caseEnum() {
        Assert.assertEquals("\"BLOCKED\"", CONTEXT.stringify(Thread.State.BLOCKED));
        Assert.assertEquals("\"SOURCE\"", CONTEXT.stringify(RetentionPolicy.SOURCE));
        Assert.assertEquals("\"CONSTRUCTOR\"", CONTEXT.stringify(ElementType.CONSTRUCTOR));
        Assert.assertEquals("\"HOURS\"", CONTEXT.stringify(TimeUnit.HOURS));
        Assert.assertEquals("\"JANUARY\"", CONTEXT.stringify(Month.JANUARY));
        Assert.assertEquals("\"HALF_DAYS\"", CONTEXT.stringify(ChronoUnit.HALF_DAYS));
        Assert.assertEquals("\"MINUTE_OF_HOUR\"", CONTEXT.stringify(ChronoField.MINUTE_OF_HOUR));
        Assert.assertEquals("\"CEILING\"", CONTEXT.stringify(RoundingMode.CEILING));
    }

    @Test
    public void case2() {
        Assert.assertEquals("[true,false]", CONTEXT.stringify(new boolean[] {true, false}));
        Assert.assertEquals("[true,false,false]", CONTEXT.stringify(new Boolean[] {true, false, false}));
        Assert.assertEquals("[9,0]", CONTEXT.stringify(new int[] {9, 0}));
        Assert.assertEquals("[9,0,9]", CONTEXT.stringify(new Integer[] {9, 0, 9}));
    }
}
