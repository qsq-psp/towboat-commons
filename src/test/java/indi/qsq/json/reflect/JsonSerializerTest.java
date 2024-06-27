package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.entity.JsonArray;
import indi.qsq.json.entity.JsonObject;
import indi.qsq.json.io.JsonStringWriter;
import indi.qsq.util.text.IdentifierFormat;
import indi.qsq.util.text.IdentifierReformat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.AfterClass;
import org.junit.Test;

import java.awt.*;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.*;

import static org.junit.Assert.*;

/**
 * Created in infrastructure on 2022/1/1.
 * Created on 2022/7/13.
 */
public class JsonSerializerTest {

    @AfterClass
    public static void printMap() {
        System.out.println("ClosurePreset.MAP");
        System.out.println(JsonStringWriter.stringify(ClosurePreset::queryAll));
        System.out.println("LoadedClass.MAP");
        System.out.println(JsonStringWriter.stringify(ReflectClass::queryAll));
    }

    private final JsonSerializer js = new JsonSerializer();

    public void testExact(Object object, String string) {
        assertEquals(string, js.stringify(object));
    }

    public void testFormatExact(Object object, int mantissa, int indent, String string) {
        JsonStringWriter stringWriter = new JsonStringWriter();
        stringWriter.setMantissa(mantissa);
        stringWriter.setIndent(indent);
        js.serialize(object, stringWriter);
        assertEquals(string, stringWriter.get());
    }

    public void testSubstring(Object object, String string) {
        String actual = js.stringify(object);
        if (!actual.contains(string)) {
            fail("Substring");
        }
    }

    public void testForException(Object object) {
        boolean success = false;
        try {
            System.out.println(js.stringify(object));
        } catch (RuntimeException e) {
            success = true;
        }
        assertTrue(success);
    }

    @Test
    public void testPrimitive() {
        testExact(true, "true");
        testExact(false, "false");
        testExact(null, "null");
        testExact(0, "0");
        testExact(2022, "2022");
        testExact(-304, "-304");
        testExact(19.5, "19.5");
        testExact("goal", "\"goal\"");
        testExact("bookstore", "\"bookstore\"");
    }

    @Test
    public void testArray1() {
        testExact(new boolean[] {false, false, true, false}, "[false,false,true,false]");
        testExact(new int[] {3, 3, 4, 5}, "[3,3,4,5]");
        testExact(new Integer[] {88, 99}, "[88,99]");
        testExact(new Double[] {66.5, 77.5, null}, "[66.5,77.5,null]");
        testExact(new String[] {"Thread", "Throwable", "Third"}, "[\"Thread\",\"Throwable\",\"Third\"]");
        testExact(new Object[] {null, true, 59, "[]"}, "[null,true,59,\"[]\"]");
    }

    @Test
    public void testArray2() {
        testExact(new boolean[][] {{true}, {true}, {false, false, false}}, "[[true],[true],[false,false,false]]");
        testExact(new int[][] {{-4, -5, 0}, {6, 7}, {9000}, {}}, "[[-4,-5,0],[6,7],[9000],[]]");
        testExact(new String[][] {{}, {}, {}, {}}, "[[],[],[],[]]");
        testExact(new String[][] {{"gift", "gear"}, {"collect", "clear", "close"}}, "[[\"gift\",\"gear\"],[\"collect\",\"clear\",\"close\"]]");
        testExact(new String[][] {{"\r", "\n", "\r\n", "", "a"}}, "[[\"\\r\",\"\\n\",\"\\r\\n\",\"\",\"a\"]]");
        testExact(new Object[] {new boolean[] {true}}, "[[true]]");
        testExact(new Object[] {new double[] {0.25}}, "[[0.25]]");
        testExact(new Object[] {
                new int[] {4, 5, 28},
                new String[] {"true", "truth"},
                new Object[] {"null", 22, false},
                null
        }, "[[4,5,28],[\"true\",\"truth\"],[\"null\",22,false],null]");
    }

    @Test
    public void testArray3() {
        testExact(new int[0][][], "[]");
        testExact(new int[2][0][], "[[],[]]");
        testExact(new int[2][2][2], "[[[0,0],[0,0]],[[0,0],[0,0]]]");
        testExact(new int[][][] {
                {{2, 2}, {37, 38}},
                {{5, 5}}
        }, "[[[2,2],[37,38]],[[5,5]]]");
    }

    @Test
    public void testList() {
        testExact(List.of(), "[]");
        testExact(List.of(4, 4, 50), "[4,4,50]");
        testExact(List.of(false, false, true, true), "[false,false,true,true]");
        testExact(List.of("OK", "Cancel"), "[\"OK\",\"Cancel\"]");
        ArrayList<Object> list = new ArrayList<>();
        list.add(5000);
        list.add(new NamedCounter());
        list.add(new SimpleLink());
        list.add(null);
        testExact(list, "[5000,{\"name\":null,\"count\":0},{\"next\":null,\"value\":2233},null]");
    }

    @Test
    public void testSet() {
        testExact(Set.of(), "[]");
        testSubstring(Set.of(0.75, 1.75, -0.5), "0.75");
        testSubstring(Set.of(false, true), "false");
        testSubstring(Set.of("Mon", "Tue", "Wed"), "\"Wed\"");
        HashSet<Object> set = new HashSet<>();
        set.add(22);
        set.add(7);
        set.add(0);
        set.add(new int[] {43, 44, 45});
        testSubstring(set, "[43,44,45]");
    }

    @Test
    public void testMutableInt() {
        MutableInt obj = new MutableInt();
        obj.value = 45;
        testExact(obj, "{\"value\":45}");
    }

    @Test
    public void testMutableInteger() {
        MutableInteger obj = new MutableInteger();
        obj.value = 50089;
        testExact(obj, "{\"value\":50089}");
    }

    @Test
    public void testMutableString() {
        MutableString obj = new MutableString("organize");
        obj.text = "join in";
        testExact(obj, "{\"text\":\"join in\"}");
    }

    @Test
    public void testMutableDate() {
        MutableDate obj = new MutableDate();
        obj.date = new java.util.Date(1658124160808L);
        testExact(obj, "{\"date\":1658124160808}");
    }

    @Test
    public void testDoubleModel() {
        DoubleModel obj = new DoubleModel();
        testExact(obj, "{\"value\":null}");
        obj.value = 10.25;
        testExact(obj, "{\"value\":10.25}");
    }

    @Test
    public void testSimplerInt() {
        SimplerInt obj = new SimplerInt();
        testExact(obj, "{\"value\":0,\"offset\":null,\"length\":null}");
        obj.major = 2002;
        obj.minor = 4;
        obj.offset = 335;
        obj.length = 0;
        testExact(obj, "{\"value\":0,\"major\":2002,\"minor\":4,\"offset\":335,\"length\":0}");
    }

    @Test
    public void testSimplerDouble() {
        SimplerDouble obj = new SimplerDouble();
        testExact(obj, "{\"a\":null,\"b\":null,\"c\":null}");
        obj.a = Double.POSITIVE_INFINITY;
        obj.b = 12.5;
        obj.c = -7.25;
        obj.d = Double.NEGATIVE_INFINITY;
        testExact(obj, "{\"b\":12.5,\"c\":-7.25}");
        obj.a = 5.5;
        obj.b = Double.NaN;
        obj.c = Double.NaN;
        obj.d = 1.0 / 32;
        testExact(obj, "{\"a\":5.5,\"d\":0.03125}");
    }

    @Test
    public void testBadDouble() {
        SimplerDouble obj = new SimplerDouble();
        obj.a = Double.NaN;
        testForException(obj);
        obj.a = 12.5;
        obj.b = Double.POSITIVE_INFINITY;
        testForException(obj);
    }

    @Test
    public void testSimplerString() {
        SimplerString obj = new SimplerString();
        testExact(obj, "{\"guc\":null,\"psb\":null,\"replacement\":null}");
        obj.pattern = "a";
        obj.replacement = "";
        obj.generalUserComment = " ";
        obj.predefinedSwapBasis = "";
        testExact(obj, "{\"psb\":\"\",\"pattern\":\"a\",\"replacement\":null}");
        obj.pattern = "\r\n";
        obj.replacement = "x";
        obj.generalUserComment = "";
        obj.predefinedSwapBasis = "t";
        testExact(obj, "{\"psb\":\"t\",\"pattern\":\"\\r\\n\",\"replacement\":\"x\"}");
    }

    @Test
    public void testDate() {
        java.util.Date obj = new java.util.Date(1658224568874L);
        testExact(obj, "1658224568874");
        obj = new java.sql.Date(1658224708319L);
        testExact(obj, "1658224708319");
        obj = new java.sql.Timestamp(1658224770953L);
        testExact(obj, "1658224770953");
    }

    @Test
    public void testByteBuffer() {
        ByteBuffer obj = ByteBuffer.allocate(0x100);
        obj.put((byte) 0x20);
        obj.put((byte) 0x41);
        testSubstring(obj, "\"position\":2");
    }

    @Test
    public void testNettyBuffer() {
        ByteBuf obj = Unpooled.buffer();
        obj.writeByte(0x53);
        obj.writeByte(0x54);
        obj.writeByte(0x2a);
        obj.readByte();
        testSubstring(obj, "\"writerIndex\":3");
    }

    @Test
    public void testManagementBeans() {
        testSubstring(ManagementFactory.getMemoryMXBean(), "\"heapMemoryUsage\"");
        testSubstring(ManagementFactory.getThreadMXBean(), "\"threadCount\"");
        testSubstring(ManagementFactory.getCompilationMXBean(), "\"totalCompilationTime\"");
        testSubstring(ManagementFactory.getOperatingSystemMXBean(), "\"availableProcessors\"");
        /* other beans are not recommended */
    }

    @Test
    public void testNamedCounter() {
        NamedCounter obj = new NamedCounter();
        obj.accept(8.0);
        obj.accept(92.0);
        testExact(obj, "{\"name\":null,\"count\":2}");
    }

    @Test
    public void testNamedStatistics() {
        NamedStatistics obj = new NamedStatistics();
        obj.name = "hair";
        obj.reset();
        obj.accept(-4.0);
        obj.accept(22.0);
        obj.accept(25.0);
        testExact(obj, "{\"count\":3,\"min\":-4.0,\"max\":25.0,\"s1\":43.0,\"s2\":1125.0,\"name\":\"hair\"}");
    }

    @Test
    public void testPrintedPage() {
        PrintedPage obj = new PrintedPage();
        obj.colorModel = 8;
        obj.rotate = true;
        obj.flip = false;
        obj.paragraphs = new String[] {
                "投我以木瓜，报之以琼琚。",
                "投我以木桃，报之以琼瑶。",
                "投我以木李，报之以琼玖。"
        };
        testExact(obj, "{\"paragraphs\":[\"投我以木瓜，报之以琼琚。\",\"投我以木桃，报之以琼瑶。\",\"投我以木李，报之以琼玖。\"],\"colorModel\":8,\"flip\":false,\"rotate\":true}");
    }

    @Test
    public void testColorSeries() {
        ColorSeries obj = new ColorSeries();
        obj.rgb = new int[] {Color.MAGENTA.getRGB(), Color.YELLOW.getRGB(), Color.GRAY.getRGB()};
        obj.position = new double[] {0.0, 0.25, 1.0};
        obj.name = new String[] {"magenta", "yellow", "gray"};
        obj.config = 31;
        testExact(obj, "{\"config\":31,\"rgb\":[-65281,-256,-8355712],\"name\":[\"magenta\",\"yellow\",\"gray\"],\"position\":[0.0,0.25,1.0]}");
    }

    @Test
    public void testDocumentSource() {
        DocumentSource obj = new DocumentSource();
        obj.id = 1001;
        obj.usage = 3;
        obj.creation = 1659184761483L;
        obj.access = 1659184775365L;
        obj.comment = "https://www.yuque.com/";
        testExact(obj, "{\"id\":1001,\"usage\":\"group\",\"priority\":0.0,\"creation-time\":1659184761483,\"access-time\":1659184775365,\"comment\":\"https://www.yuque.com/\"}");
    }

    @Test
    public void testSensitiveNames() {
        SensitiveNames obj = new SensitiveNames();
        obj.clazz = 6;
        obj.as = 5;
        obj.var0 = 4;
        testExact(obj, "{\"class\":6,\"as\":5,\"0\":4}");
    }

    @Test
    public void testElectricVehicle() {
        ElectricVehicle obj = new ElectricVehicle();
        obj.horsePower = 235;
        obj.doors = 4;
        obj.engine = new Engine();
        obj.engine.milesGallon = 14;
        obj.engine.CO2Output = 4;
        obj.engine.percentElectric = 77;
        testExact(obj, "{\"horsePower\":235,\"doors\":4,\"engine\":{\"milesGallon\":14,\"CO2Output\":4,\"percentElectric\":77},\"make\":null,\"model\":null,\"year\":null}");
    }

    @Test
    public void testMimeField() {
        MineField obj = new MineField();
        obj.difficulty = 4;
        obj.grid = new int[] {0, 0, 1, 0, -1, 1, 0, 1};
        testExact(obj, "{\"difficulty\":\"expert\",\"grid\":[\"closed\",\"closed\",\"open\",\"closed\",\"mine\",\"open\",\"closed\",\"open\"]}");
    }

    @Test
    public void testRuntimeGuard() {
        RuntimeGuard obj = new RuntimeGuard();
        obj.filter = Modifier.PUBLIC;
        obj.check = 0xff;
        testExact(obj, "{\"filter\":[\"public\"],\"check\":[\"before\",\"running\",\"after\"]}");
    }

    @Test
    public void testMiscEnum() {
        MiscEnum obj = new MiscEnum();
        obj.policy = RetentionPolicy.SOURCE;
        obj.elementType = ElementType.CONSTRUCTOR;
        obj.threadState = Thread.State.TIMED_WAITING;
        testExact(obj, "{\"policy\":\"SOURCE\",\"elementType\":\"constructor\",\"threadState\":\"timed-waiting\"}");
    }

    @Test
    public void testJsonArray() {
        JsonArray obj = new JsonArray();
        obj.add("beginning");
        obj.add(56);
        obj.add(null);
        obj.add(new NamedCounter());
        testExact(obj, "[\"beginning\",56,null,{\"name\":null,\"count\":0}]");
    }

    @Test
    public void testJsonObject() {
        JsonObject obj = new JsonObject();
        testExact(obj, "{}");
        obj.put("category", 5);
        testExact(obj, "{\"category\":5}");
        obj.put("time", new java.util.Date(1658224073874L));
        obj.remove("category");
        testExact(obj, "{\"time\":1658224073874}");
        obj.put("time", new java.util.Date(1658224073033L));
        testExact(obj, "{\"time\":1658224073033}");
        obj.remove("time");
        obj.put("agent", "netty4");
        testExact(obj, "{\"agent\":\"netty4\"}");
        obj.put("log", null);
        obj.remove("agent");
        testExact(obj, "{\"log\":null}");
    }

    @Test
    public void testMantissa1() {
        testFormatExact(0.033102, 2, -1, "0.03");
        testFormatExact(-0.033102, 2, -1, "-0.03");
        testFormatExact(0.038852, 2, -1, "0.03");
        testFormatExact(-0.038852, 2, -1, "-0.03");
        testFormatExact(8055.98022, 2, -1, "8055.98");
        testFormatExact(-8055.98022, 2, -1, "-8055.98");
    }

    @Test
    public void testMantissa2() {
        DoubleModel obj = new DoubleModel();
        obj.value = 0.0;
        testFormatExact(obj, 4, -1, "{\"value\":0}");
        obj.value = 730.0;
        testFormatExact(obj, 4, -1, "{\"value\":730}");
        obj.value = 440.13365;
        testFormatExact(obj, 4, -1, "{\"value\":440.1336}");
        obj.value = -80.00912;
        testFormatExact(obj, 4, -1, "{\"value\":-80.0091}");
        obj.value = 8.05002;
        testFormatExact(obj, 4, -1, "{\"value\":8.05}");
        obj.value = 21.00006;
        testFormatExact(obj, 4, -1, "{\"value\":21}");
        obj.value = -0.32006;
        testFormatExact(obj, 4, -1, "{\"value\":-0.32}");
        obj.value = Double.NEGATIVE_INFINITY;
        testFormatExact(obj, 4, -1, "{\"value\":null}");
        obj.value = Double.NaN;
        testFormatExact(obj, 4, -1, "{\"value\":null}");
    }

    @Test
    public void testMantissa3() {
        NamedStatistics obj = new NamedStatistics();
        obj.name = "omega";
        obj.reset();
        obj.accept(Math.cbrt(2));
        obj.accept(Math.log(3));
        obj.accept(Math.exp(4));
        testFormatExact(obj, 3, -1, "{\"count\":3,\"min\":1.098,\"max\":54.598,\"s1\":56.956,\"s2\":2983.752,\"name\":\"omega\"}");
    }

    @Test
    public void testMantissa4() {
        ColorSeries obj = new ColorSeries();
        obj.rgb = new int[] {0x1750EB, 0x871094};
        obj.position = new double[] {Math.PI, Math.E, Math.sqrt(2)};
        obj.name = new String[] {"cyan", "brown", "golden"};
        obj.config = 31;
        testFormatExact(obj, 3, -1, "{\"config\":31,\"rgb\":[1528043,8851604],\"name\":[\"cyan\",\"brown\",\"golden\"],\"position\":[3.141,2.718,1.414]}");
    }

    @Test
    public void testSimpleLink() {
        SimpleLink obj = new SimpleLink();
        for (int i = 0; i < 4; i++) {
            SimpleLink obj0 = new SimpleLink();
            obj0.next = obj;
            obj = obj0;
        }
        testExact(obj, "{\"next\":{\"next\":{\"next\":{\"next\":{\"next\":null,\"value\":2233},\"value\":2233},\"value\":2233},\"value\":2233},\"value\":2233}");
    }

    @Test
    public void testAlternateLink() {
        AlternateLink0 obj = new AlternateLink0();
        for (int i = 0; i < 3; i++) {
            AlternateLink1 obj1 = new AlternateLink1();
            obj1.next0 = obj;
            AlternateLink0 obj0 = new AlternateLink0();
            obj0.next1 = obj1;
            obj = obj0;
        }
        testExact(obj, "{\"next1\":{\"next0\":{\"next1\":{\"next0\":{\"next1\":{\"next0\":{\"next1\":null}}}}}}}");
    }

    @Test
    public void testRandomObjectLink() {
        RandomObjectLink obj = new RandomObjectLink();
        testExact(obj, "{}");
        obj.setLink(true);
        testExact(obj, "{\"link\":true}");
        obj.setLink(92L);
        testExact(obj, "{\"link\":92}");
        obj.setLink(7.5);
        testExact(obj, "{\"link\":7.5}");
        obj.setLink("7.5");
        testExact(obj, "{\"link\":\"7.5\"}");
        obj.setLink(new SimpleLink());
        testExact(obj, "{\"link\":{\"next\":null,\"value\":2233}}");
    }

    @Test
    public void testRandomObjectTree() {
        RandomObjectTree obj = new RandomObjectTree();
        testExact(obj, "{\"left\":null,\"right\":null}");
        obj.left = false;
        obj.right = true;
        obj.setValue(-5);
        testExact(obj, "{\"left\":false,\"right\":true,\"value\":-5}");
        obj.left = new SimpleLink();
        obj.right = new RandomObjectTree();
        obj.setValue(-6);
        testExact(obj, "{\"left\":{\"next\":null,\"value\":2233},\"right\":{\"left\":null,\"right\":null},\"value\":-6}");
        obj.left = new AlternateLink0();
        obj.right = new AlternateLink1();
        obj.setValue(-7);
        testExact(obj, "{\"left\":{\"next1\":null},\"right\":{\"next0\":null},\"value\":-7}");
    }

    @Test
    public void testSimpleCyclic() {
        SimpleLink obj = new SimpleLink();
        obj.next = obj;
        testForException(obj);
        obj.next = new SimpleLink();
        obj.next.next = obj;
        testForException(obj);
    }

    @Test
    public void testTreeCyclic() {
        RandomObjectTree obj = new RandomObjectTree();
        RandomObjectLink obj1 = new RandomObjectLink();
        RandomObjectLink obj2 = new RandomObjectLink();
        obj.left = new RandomObjectLink();
        obj.right = obj1;
        obj.setValue(8);
        obj1.link = obj2;
        obj2.link = obj;
        testForException(obj);
    }

    @Test
    public void testNonCyclic() {
        RandomObjectTree obj = new RandomObjectTree();
        RandomObjectTree obj1 = new RandomObjectTree();
        SimpleLink obj2 = new SimpleLink();
        obj1.left = obj1.right = obj2;
        obj.left = obj.right = obj1;
        testExact(obj, "{\"left\":{\"left\":{\"next\":null,\"value\":2233},\"right\":{\"next\":null,\"value\":2233}},\"right\":{\"left\":{\"next\":null,\"value\":2233},\"right\":{\"next\":null,\"value\":2233}}}");
    }

    @Test
    public void testEliminateCyclic() {
        BackLink obj = new BackLink();
        obj.pointer = obj;
        testExact(obj, "{\"pointer\":null}");
        BackLink obj1 = new BackLink();
        obj.pointer = obj1;
        obj1.pointer = obj;
        testExact(obj, "{\"pointer\":{\"pointer\":null}}");
        obj1.pointer = obj1;
        testExact(obj, "{\"pointer\":{\"pointer\":null}}");
    }

    @Test
    public void testJsonStructure() {
        testExact(new AlreadyStructure(), "{\"structure\":true}");
        testExact(new StructureHolder(), "{\"hold\":{\"structure\":true}}");
        RandomObjectLink obj = new RandomObjectLink();
        obj.setLink(new AlreadyStructure());
        testExact(obj, "{\"link\":{\"structure\":true}}");
    }

    public static class MutableInt implements IntPredicate, IntSupplier, IntConsumer, IntUnaryOperator, Cloneable, Serializable {

        private static final long serialVersionUID = 0xA4A6AC1958EE64B1L;

        @DatabaseColumn(type = DatabaseColumnType.INT, name = "value")
        public int value;

        @Override
        public void accept(int value) {
            this.value = value;
        }

        @Override
        public boolean test(int value) {
            return this.value == value;
        }

        @Override
        public int getAsInt() {
            return value;
        }

        @Override
        public int applyAsInt(int newValue) {
            int oldValue = value;
            value = newValue;
            return oldValue;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public MutableInt clone() {
            MutableInt that = new MutableInt();
            that.value = this.value;
            return that;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MutableInt && ((MutableInt) obj).value == this.value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    public static class MutableInteger {

        public Integer value;
    }

    public static class MutableString implements Comparable<MutableString> {

        public static final Object KEY = new Object();

        public String text;

        public transient String collate;

        public transient int codec;

        public MutableString() {
            super();
        }

        public MutableString(Object object) {
            super();
            if (object != null) {
                text = object.toString();
            }
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public int compareTo(MutableString that) {
            if (this.text != null) {
                if (that.text != null) {
                    return this.text.compareTo(that.text);
                } else {
                    return 1;
                }
            } else {
                if (that.text != null) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }

        @Override
        public int hashCode() {
            return text != null ? text.hashCode() : 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MutableString && compareTo((MutableString) obj) == 0;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static class MutableDate implements Serializable {

        private static final long serialVersionUID = 0xD558462A65DBE90EL;

        public java.util.Date date;

        private int modCount;
    }

    public static class DoubleModel extends Number {

        @SerializeToNull(SerializeFrom.INFINITE | SerializeFrom.NAN)
        public double value = Double.NaN;

        @Override
        public int intValue() {
            return (int) value;
        }

        @Override
        public long longValue() {
            return (long) value;
        }

        @Override
        public float floatValue() {
            return (float) value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @Override
        public DoubleModel clone() {
            DoubleModel that = new DoubleModel();
            that.value = this.value;
            return that;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DoubleModel && Double.doubleToLongBits(this.value) == Double.doubleToLongBits(((DoubleModel) obj).value);
        }

        @Override
        public String toString() {
            return String.format("%.4f", value);
        }
    }

    /**
     * Created on 2022/7/24.
     */
    @FieldOrder({"value", "major", "minor", "offset", "length"})
    public static class SimplerInt extends MutableInt {

        public static final int DEFAULT_MAJOR = 12;

        public static final int DEFAULT_LENGTH = 256;

        @SerializeToUndefined(value = SerializeFrom.SPECIFIC, specific = "indi.qsq.json.reflect.JsonSerializerTest$SimplerInt.DEFAULT_MAJOR")
        public int major = DEFAULT_MAJOR;

        @SerializeToUndefined(SerializeFrom.ZERO_INTEGRAL)
        public int minor;

        @SerializeToNull(SerializeFrom.ZERO_INTEGRAL)
        public int offset;

        @SerializeToNull(value = SerializeFrom.SPECIFIC, specific = "indi.qsq.json.reflect.JsonSerializerTest$SimplerInt.DEFAULT_LENGTH")
        public int length = DEFAULT_LENGTH;
    }

    @FieldOrder({"a", "b", "c", "d"})
    @SerializeToNull(SerializeFrom.ZERO_DECIMAL)
    public static class SimplerDouble {

        @SerializeToUndefined(SerializeFrom.INFINITE)
        public double a;

        @SerializeToUndefined(SerializeFrom.NAN)
        public double b;

        @SerializeToUndefined(SerializeFrom.INFINITE | SerializeFrom.NAN)
        public double c;

        @SerializeToUndefined(SerializeFrom.ZERO_DECIMAL | SerializeFrom.INFINITE | SerializeFrom.NAN)
        public double d;
    }

    @FieldOrder({"guc", "psb", "pattern", "replacement", "complexity"})
    @SerializeToUndefined(SerializeFrom.NULL)
    public static class SimplerString implements Consumer<String> {

        public String pattern;

        @SerializeToUndefined(0)
        @SerializeToNull(SerializeFrom.EMPTY_STRING)
        public String replacement;

        @JsonName("guc")
        @SerializeToUndefined(SerializeFrom.BLANK_STRING)
        public String generalUserComment;

        @JsonName("psb")
        @SerializeToUndefined(0)
        public String predefinedSwapBasis;

        public Integer complexity;

        @Override
        public void accept(String string) {
            // pass
        }
    }

    /**
     * Created on 2022/7/15.
     */
    @FieldOrder({"name", "count"})
    public static class NamedCounter implements DoubleConsumer {

        public String name;

        public int count;

        public void reset() {
            count = 0;
        }

        @Override
        public void accept(double value) {
            count++;
        }
    }

    /**
     * Created on 2022/7/15.
     */
    @FieldOrder({"count", "min", "max", "s1", "s2", "name"})
    public static class NamedStatistics extends NamedCounter {

        public double min;

        public double max;

        public double s1;

        public double s2;

        public NamedStatistics() {
            super();
            reset();
        }

        @Override
        public void reset() {
            count = 0;
            min = Double.POSITIVE_INFINITY;
            max = Double.NEGATIVE_INFINITY;
            s1 = 0.0;
            s2 = 0.0;
        }

        @Override
        public void accept(double value) {
            count++;
            min = Math.min(min, value);
            max = Math.max(max, value);
            s1 += value;
            s2 += value * value;
        }
    }

    /**
     * Created on 2022/7/15.
     */
    @FieldOrder({"paragraphs", "colorModel", "flip", "rotate"})
    public static class PrintedPage {

        public int colorModel;

        public boolean rotate;

        public boolean flip;

        public String[] paragraphs;
    }

    /**
     * Created on 2022/7/15.
     */
    @FieldOrder({"config", "rgb", "name", "position"})
    public static class ColorSeries {

        public int[] rgb;

        public double[] position;

        public String[] name;

        public int config;
    }

    /**
     * Created on 2022/7/14.
     */
    @FieldOrder({"id", "usage", "priority", "creation-time", "access-time", "comment"})
    public static class DocumentSource {

        public int id;

        @IntEnumValue(
                integers = {1, 2, 3, 4},
                strings = {"offline", "private", "group", "public"}
        )
        public int usage;

        public float priority;

        @JsonName("creation-time")
        public long creation;

        @JsonName("access-time")
        public long access;

        public String comment;
    }

    @FieldOrder({"class", "as", "0"})
    public static class SensitiveNames {

        private int clazz;

        private int as;

        @JsonName("0")
        public int var0;

        @JsonName("class")
        public int getClazz() {
            return clazz;
        }

        public void setClass(int clazz) {
            this.clazz = clazz;
        }

        @JsonName("as")
        public int getAs() {
            return as;
        }

        @JsonName("as")
        public void setAs(int as) {
            this.as = as;
        }
    }

    /**
     * Created on 2022/8/17.
     *
     * From Jakarta commons cookbook
     */
    @FieldOrder({"milesGallon", "CO2Output", "percentElectric"})
    public static class Engine {

        public Integer milesGallon;

        public Integer CO2Output;

        public Integer percentElectric;
    }

    /**
     * Created on 2022/8/17.
     *
     * From Jakarta commons cookbook
     */
    @FieldOrder({"horsePower", "doors", "engine", "make", "model", "year"})
    public static class ElectricVehicle {

        public Integer horsePower;

        public Integer doors;

        public Engine engine;

        public String make;

        public String model;

        public Integer year;
    }

    /**
     * Created on 2022/8/12.
     */
    @FieldOrder({"difficulty", "grid"})
    public static class MineField {

        @IntEnumValue(
                integers = {1, 2, 3, 4},
                strings = {"simple", "medium", "hard", "expert"}
        )
        public int difficulty;

        @IntEnumValue(
                integers = {-1, 0, 1},
                strings = {"mine", "closed", "open"}
        )
        public int[] grid;
    }

    public static class RuntimeGuard {

        @IntFlagValue(
                integers = {Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE},
                strings = {"public", "protected", "private"}
        )
        public int filter;

        @IntFlagValue(
                integers = {0x01, 0x02, 0x04},
                strings = {"before", "running", "after"}
        )
        public int check;
    }

    /**
     * Created on 2022/8/17.
     */
    @FieldOrder({"policy", "elementType", "threadState"})
    public static class MiscEnum {

        public RetentionPolicy policy;

        @IdentifierReformat(join = IdentifierFormat.DASH)
        public ElementType elementType;

        @IdentifierReformat(join = IdentifierFormat.DASH)
        public Thread.State threadState;
    }

    public static class SimpleLink {

        public SimpleLink next;

        public int getValue() {
            return 2233;
        }
    }

    public static class AlternateLink0 {

        public AlternateLink1 next1;
    }

    public static class AlternateLink1 {

        public AlternateLink0 next0;
    }

    public static class RandomObjectLink {

        private Object link;

        public void setLink(Object link) {
            this.link = link;
        }

        @SerializeToUndefined(SerializeFrom.NULL)
        public Object getLink() {
            return link;
        }
    }

    public static class BackLink {

        @SerializeToNull(SerializeFrom.CYCLIC_OBJECT)
        public Object pointer;
    }

    public static class RandomObjectTree {

        public Object left;

        public Object right;

        private int value;

        @SerializeToUndefined(SerializeFrom.ZERO_INTEGRAL)
        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class AlreadyStructure implements JsonStructure, Serializable {

        private static final long serialVersionUID = 0x87B6AC6108B274AEL;

        @Override
        public void toJson(JsonConsumer jc) {
            jc.openObject();
            jc.key("structure");
            jc.booleanValue(true);
            jc.closeObject();
        }
    }

    public static class StructureHolder implements Serializable {

        private static final long serialVersionUID = 0x3D6B9CEFF6931131L;

        public AlreadyStructure getHold() {
            return new AlreadyStructure();
        }
    }
}
