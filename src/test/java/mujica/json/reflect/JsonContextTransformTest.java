package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2026/4/11")
public class JsonContextTransformTest {

    static final JsonContext CONTEXT = new JsonContext();

    @Test
    public void case1() {
        Assert.assertEquals("null", CONTEXT.stringify(null));
        Assert.assertEquals("false", CONTEXT.stringify(false));
        Assert.assertEquals("true", CONTEXT.stringify(true));
        Assert.assertEquals("7500", CONTEXT.stringify(7500));
        Assert.assertEquals("-81", CONTEXT.stringify(-81));
    }

    @Test
    public void case2() {
        Assert.assertEquals("[true,false]", CONTEXT.stringify(new boolean[] {true, false}));
        Assert.assertEquals("[true,false,false]", CONTEXT.stringify(new Boolean[] {true, false, false}));
        Assert.assertEquals("[9,0]", CONTEXT.stringify(new int[] {9, 0}));
        Assert.assertEquals("[9,0,9]", CONTEXT.stringify(new Integer[] {9, 0, 9}));
    }
}
