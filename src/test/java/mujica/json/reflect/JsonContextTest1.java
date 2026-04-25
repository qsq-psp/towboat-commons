package mujica.json.reflect;

import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FieldOrder;
import mujica.reflect.modifier.Name;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unused")
@CodeHistory(date = "2026/4/10")
@FieldOrder({"aQuickBrownFoxJumpsOverTheLazyDog", "enabled", "lineNumber", "errorCount", "core"})
public class JsonContextTest1 {

    static final JsonContext CONTEXT = (new JsonContext()).loadBasic();

    public static final int VACUUM_PRESSURE_GAUGE = 5;

    public boolean enabled;

    public transient boolean done;

    public int lineNumber;

    public int errorCount;

    @Name(value = "狐狸狗", language = "zh")
    public int aQuickBrownFoxJumpsOverTheLazyDog;

    @Name(value = "水母包", language = "zh")
    public transient Object lazyBadgersMoveUniqueWaxyJellyfishPackets;

    @CodeHistory(date = "2026/4/10")
    @FieldOrder({"styleCode", "developing"})
    public static class Inside1 {

        public boolean developing;

        public int styleCode;

        @Name(value = "简化版版本号", language = "zh")
        public transient int version;
    }

    @Name(value = "核心", language = "zh")
    @JsonHint(JsonHint.NULLABLE)
    public Inside1 core;

    @Test
    public void caseParse1() {
        final JsonContextTest1 instance = new JsonContextTest1();
        CONTEXT.parse("{\"errorCount\":3,\"lineNumber\":20}", instance);
        Assert.assertFalse(instance.enabled);
        Assert.assertFalse(instance.done);
        Assert.assertEquals(20, instance.lineNumber);
        Assert.assertEquals(3, instance.errorCount);
        Assert.assertEquals(0, instance.aQuickBrownFoxJumpsOverTheLazyDog);
        Assert.assertNull(instance.core);
    }

    @Test
    public void caseParse2() {
        final JsonContextTest1 instance = new JsonContextTest1();
        CONTEXT.parse("{\"aQuickBrownFoxJumpsOverTheLazyDog\":0,\"enabled\":false,\"lineNumber\":20,\"errorCount\":3,\"core\":null}", instance);
        Assert.assertFalse(instance.enabled);
        Assert.assertFalse(instance.done);
        Assert.assertEquals(20, instance.lineNumber);
        Assert.assertEquals(3, instance.errorCount);
        Assert.assertEquals(0, instance.aQuickBrownFoxJumpsOverTheLazyDog);
        Assert.assertNull(instance.core);
    }

    @Test
    public void caseStringify2() {
        final JsonContextTest1 instance = new JsonContextTest1();
        instance.lineNumber = 20;
        instance.errorCount = 3;
        Assert.assertEquals("{\"aQuickBrownFoxJumpsOverTheLazyDog\":0,\"enabled\":false,\"lineNumber\":20,\"errorCount\":3,\"core\":null}", CONTEXT.stringify(instance));
    }

    @Test
    public void caseParse3() {
        final JsonContextTest1 instance = new JsonContextTest1();
        CONTEXT.parse("{\"core\":{\"styleCode\":55,\"developing\":false},\"enabled\":false}", instance);
        Assert.assertNotNull(instance.core);
        Assert.assertFalse(instance.core.developing);
        Assert.assertEquals(55, instance.core.styleCode);
        Assert.assertEquals(0, instance.core.version);
    }

    @Test
    public void caseParse4() {
        final JsonContextTest1 instance = new JsonContextTest1();
        CONTEXT.parse("{\"aQuickBrownFoxJumpsOverTheLazyDog\":0,\"enabled\":false,\"lineNumber\":21,\"errorCount\":0,\"core\":{\"styleCode\":55,\"developing\":false}}", instance);
        Assert.assertNotNull(instance.core);
        Assert.assertFalse(instance.core.developing);
        Assert.assertEquals(55, instance.core.styleCode);
        Assert.assertEquals(0, instance.core.version);
    }

    @Test
    public void caseStringify4() {
        final JsonContextTest1 instance = new JsonContextTest1();
        instance.done = true;
        instance.lineNumber = 21;
        final Inside1 inside1 = new Inside1();
        inside1.styleCode = 55;
        instance.core = inside1;
        Assert.assertEquals("{\"aQuickBrownFoxJumpsOverTheLazyDog\":0,\"enabled\":false,\"lineNumber\":21,\"errorCount\":0,\"core\":{\"styleCode\":55,\"developing\":false}}", CONTEXT.stringify(instance));
    }

    @AfterClass
    public static void after() {
        CONTEXT.hashCode();
    }
}
