package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.entity.JsonArray;
import indi.qsq.json.io.JsonStringWriter;
import indi.qsq.util.ds.ByteArray;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.IntConsumer;

import static org.junit.Assert.*;

/**
 * Created on 2022/7/20.
 */
@SuppressWarnings("SimplifiableJUnitAssertion")
public class JsonParserTest {

    @AfterClass
    public static void printMap() {
        System.out.println("ClosurePreset.MAP");
        System.out.println(JsonStringWriter.stringify(ClosurePreset::queryAll));
        System.out.println("LoadedClass.MAP");
        System.out.println(JsonStringWriter.stringify(ReflectClass::queryAll));
    }

    private final JsonParser jsonParser = new JsonParser();

    @Test
    public void testPrimitive() {
        assertEquals(true, jsonParser.parse("true"));
        assertEquals(false, jsonParser.parse("false"));
        assertNull(jsonParser.parse("null"));
        assertEquals(7007L, jsonParser.parse("7007"));
        assertEquals("true", jsonParser.parse("\"true\""));
        assertEquals("false", jsonParser.parse("\"false\""));
        assertEquals("null", jsonParser.parse("\"null\""));
        assertEquals("\"true\"", jsonParser.parse("\"\\\"true\\\"\""));
        assertEquals("\"false\"", jsonParser.parse("\"\\\"false\\\"\""));
        assertEquals("\"null\"", jsonParser.parse("\"\\\"null\\\"\""));
    }

    @Test
    public void testArray1() {
        JsonArray obj;
        obj = (JsonArray) jsonParser.parse("[true,false]");
        assertEquals(2, obj.size());
        assertTrue(obj.getBooleanValue(0));
        obj = (JsonArray) jsonParser.parse("[\r\n  true, false, false, false, false  \r\n]");
        assertEquals(5, obj.size());
        assertTrue(obj.getBooleanValue(0));
        obj = (JsonArray) jsonParser.parse("[0,3,-1,82]");
        assertEquals(4, obj.size());
        assertEquals(82, obj.getLongValue(3));
        obj = (JsonArray) jsonParser.parse("[ 607, -5.530, 8.8, 75000]");
        assertEquals(4, obj.size());
        assertEquals(75000, obj.getLongValue(3));
        obj = (JsonArray) jsonParser.parse("[\"hippopotamus\",\"hypothesis\",\"hexane\"]");
        assertEquals(3, obj.size());
        assertEquals("hypothesis", obj.getStringValue(1));
        obj = (JsonArray) jsonParser.parse("[\"justification\",true,null,92,5.509]");
        assertEquals(5, obj.size());
        assertEquals("justification", obj.getStringValue(0));
        assertTrue(obj.getBooleanValue(1));
        assertNull(obj.get(2));
        assertEquals(92L, obj.getLongValue(3));
    }

    @Test
    public void testArray2() {
        JsonArray obj;
        obj = (JsonArray) jsonParser.parse("[[]]");
        assertEquals(1, obj.size());
        obj = (JsonArray) obj.get(0);
        assertEquals(0, obj.size());
        obj = (JsonArray) jsonParser.parse("[[true],[true],[false,false,false]]");
        assertEquals(3, obj.size());
        obj = (JsonArray) obj.get(2);
        assertEquals(3, obj.size());
        assertFalse(obj.getBooleanValue(0));
        assertFalse(obj.getBooleanValue(1));
        assertFalse(obj.getBooleanValue(2));
        obj = (JsonArray) jsonParser.parse("[\"abandon\", \"abnormal\", \"abacus\", [\"acknowledge\"]]");
        assertEquals(4, obj.size());
        assertEquals("abandon", obj.getStringValue(0));
        assertEquals("abnormal", obj.getStringValue(1));
        assertEquals("abacus", obj.getStringValue(2));
        obj = (JsonArray) obj.get(3);
        assertEquals(1, obj.size());
        assertEquals("acknowledge", obj.getStringValue(0));
    }

    @Test
    public void testArray3() {
        JsonArray obj;
        obj = (JsonArray) jsonParser.parse("[[],[[]]]");
        assertEquals(2, obj.size());
        obj = (JsonArray) obj.get(1);
        assertEquals(1, obj.size());
        obj = (JsonArray) obj.get(0);
        assertEquals(0, obj.size());
        obj = (JsonArray) jsonParser.parse("[4,[0,0,9],[-3,[4,-9200]],0,1044]");
        assertEquals(5, obj.size());
        assertEquals(1044L, obj.getLongValue(4));
        obj = (JsonArray) obj.get(2);
        assertEquals(2, obj.size());
        assertEquals(-3L, obj.getLongValue(0));
        obj = (JsonArray) obj.get(1);
        assertEquals(2, obj.size());
        assertEquals(-9200L, obj.getLongValue(1));
        obj = (JsonArray) jsonParser.parse("[\"html\", [\"body\", [\"main\", \"aside\", \"footer\"]]]");
        assertEquals(2, obj.size());
        assertEquals("html", obj.getStringValue(0));
        obj = (JsonArray) obj.get(1);
        assertEquals(2, obj.size());
        assertEquals("body", obj.getStringValue(0));
        obj = (JsonArray) obj.get(1);
        assertEquals(3, obj.size());
        assertEquals("main", obj.getStringValue(0));
        assertEquals("aside", obj.getStringValue(1));
        assertEquals("footer", obj.getStringValue(2));
    }

    @Test
    public void testListsHandle() {
        ListsHandle obj = new ListsHandle();
        jsonParser.parse("{\"doubles\": [], \"strings\": []}", obj);
        assertEquals(0, obj.doubles.size());
        assertEquals(0, obj.strings.size());
        obj = new ListsHandle();
        jsonParser.parse("{\"strings\": [\"books 666\"]}", obj);
        assertEquals(1, obj.strings.size());
        assertEquals("books 666", obj.strings.get(0));
        jsonParser.parse("{\"doubles\": [4.48]}", obj);
        assertEquals(1, obj.doubles.size());
        assertEquals(1.0, obj.doubles.get(0), 0.0);
        obj = new ListsHandle();
        jsonParser.parse("{\"strings\": [\".\", null, true, 73, null]}", obj);
        assertEquals(5, obj.strings.size());
        assertEquals(".", obj.strings.get(0));
        assertNull(obj.strings.get(1));
        assertEquals("true", obj.strings.get(2));
        assertEquals("73", obj.strings.get(3));
        jsonParser.parse("{\"doubles\": [0.2, 0.8, 1.4, 2.7, -3.3, -7.1]}", obj);
        assertEquals(6, obj.doubles.size());
        assertEquals(0.2, obj.doubles.get(0), 0.0);
        assertEquals(0.0, obj.doubles.get(5), 0.0);
    }

    @Test
    public void testRuler() {
        Ruler obj = new Ruler();
        jsonParser.parse("{\"unit\": \"cm\", \"length\": 22}", obj);
        assertEquals("cm", obj.unit);
        assertEquals(22, obj.length);
    }

    @Test
    public void testRulerArray() {
        RulerArray obj = new RulerArray();
        jsonParser.parse("{\"rulers\":[{\"unit\":\"m\",\"length\":5},{\"unit\":\"m\",\"length\":1},{\"unit\":\"dm\",\"length\":4},{\"unit\":\"cm\",\"length\":34}],\"price\":18.5}", obj);
        assertEquals(18.5, obj.price, 0.0);
        assertNotNull(obj.rulers);
        assertEquals(4, obj.rulers.length);
        assertEquals(new Ruler("m", 5), obj.rulers[0]);
        assertEquals(new Ruler("m", 1), obj.rulers[1]);
        assertEquals(new Ruler("dm", 4), obj.rulers[2]);
        assertEquals(new Ruler("cm", 34), obj.rulers[3]);
    }

    @Test
    public void testRulerSet() {
        RulerSet obj = new RulerSet();
        jsonParser.parse("{\"rulers\":[{\"unit\":\"m\",\"length\":6},{\"unit\":\"cm\",\"length\":45},{\"unit\":\"mm\",\"length\":227}],\"price\":40}", obj);
        assertEquals(40.0, obj.price, 0.0);
        assertEquals(3, obj.rulers.size());
        assertTrue(obj.rulers.contains(new Ruler("mm", 227)));
        assertTrue(obj.rulers.contains(new Ruler("cm", 45)));
        assertTrue(obj.rulers.contains(new Ruler("m", 6)));
        assertFalse(obj.rulers.contains(new Ruler("mm", 100)));
        assertFalse(obj.rulers.contains(new Ruler("cm", 100)));
        assertFalse(obj.rulers.contains(new Ruler("cm", 34)));
        assertFalse(obj.rulers.contains(new Ruler("dm", 45)));
        assertFalse(obj.rulers.contains(new Ruler("m", 5)));
        assertFalse(obj.rulers.contains(new Ruler("m", 4)));
    }

    @Test
    public void testMaterial() {
        Material obj = new Material();
        jsonParser.parse("{\"name\": \"Paper\", \"color\": \"0xf8f8f5\", \"density\": 0.83, \"thickness\": 0.12, \"hash\": 800, \"slot\": 4}", obj);
        assertEquals("Paper", obj.name);
        assertEquals(0xf8f8f5, obj.color);
        assertEquals(0.83, obj.density, 0.0);
        assertEquals(0, obj.slot);
        assertEquals(0, obj.hash);
    }

    @Test
    public void testMetal() {
        Metal obj = new Metal();
        jsonParser.parse("{\"name\": \"Silver\", \"shells\": [2, 8, 18, 18, 1], \"color\": \"0xfcfcfc\", \"specular\": 0.97, \"density\": 10.49, \"hash\": 9003, \"slot\": 5, \"conductivity\": 6.3e7}", obj);
        assertEquals("Silver", obj.name);
        assertEquals(0xfcfcfc, obj.color);
        assertEquals(10.49, obj.density, 0.0);
        assertEquals(0, obj.slot);
        assertEquals(0.97, obj.specular, 0.0);
        assertEquals(6.3e7, obj.conductivity, 0.0);
        assertEquals(5, obj.shells.size());
        assertEquals(8, obj.shells.get(1).intValue());
    }

    @Test
    public void testAirConditioner() {
        AirConditioner obj = new AirConditioner();
        jsonParser.parse("{\"brand\":\"GREE\",\"temperature\":25.00,\"on\":true}", obj);
        assertEquals("GREE", obj.brand);
        assertTrue(obj.on);
        assertEquals(25.0, obj.temperature, 0.0);
    }

    @Test
    public void testMobile() {
        Mobile obj = new Mobile();
        jsonParser.parse("{\"IMEI\":\"35 565001 578902 1\",\"ICCID\":\"898700b6102751040331\",\"MEID\":\"35565001578902\",\"SEID\":\"1800B4B40367F00571F4\"}", obj);
        assertEquals("35 565001 578902 1", obj.IMEI);
        assertEquals("898700b6102751040331", obj.ICCID);
        assertEquals("35565001578902", obj.MEID);
        assertEquals("1800B4B40367F00571F4", obj.SEID);
    }

    @Test
    public void testProcessor() {
        Processor obj = new Processor();
        jsonParser.parse("{\"arch\":\"ARMv7\",\"clock\":\"1.08G\",\"physical\":4,\"logic\":8,\"price\":\"720.00\"}", obj);
        assertEquals(4, obj.physicalNumber);
        assertEquals(8, obj.logicNumber);
        assertEquals(1.08 * 1000_000_000.0, obj.clockRate, 0.0);
        assertEquals("ARMv7", obj.architecture);
    }

    @Test
    public void testMobileProcessor() {
        MobileProcessor obj = new MobileProcessor();
        jsonParser.parse("{\"processor\":{\"arch\":\"ARMv7\",\"clock\":\"1.08G\",\"physical\":4,\"logic\":8,\"price\":\"720.00\"},\"mobile\":{\"IMEI\":\"35 565001 578902 1\",\"ICCID\":\"898700b6102751040331\",\"MEID\":\"35565001578902\",\"SEID\":\"1800B4B40367F00571F4\"},\"memory\":\"4G\"}", obj);
        assertEquals(4000000000L, obj.memory);
        assertNotNull(obj.mobile);
        assertEquals("898700b6102751040331", obj.mobile.ICCID);
        assertNotNull(obj.processor);
        assertEquals(8, obj.processor.logicNumber);
    }

    @Test
    public void testCharacterInfo() {
        CharacterInfo obj = new CharacterInfo();
        jsonParser.parse("{\"cp\":40997,\"cc\":1,\"type\":\"Co\",\"dir\":\"B\",\"escaped\":[\"C\",\"java\",\"regex\"]}", obj);
        assertEquals(40997, obj.codePoint);
        assertEquals(1, obj.charCount);
        assertEquals(Character.PRIVATE_USE, obj.type);
        assertEquals(Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR, obj.directionality);
        assertEquals(0x23, obj.escaped);
        jsonParser.parse("{\"cp\":304,\"cc\":1,\"escaped\":[\"javascript\",\"xml\",\"URI\"],\"type\":\"Lu\",\"dir\":\"PDF\"}", obj);
        assertEquals(304, obj.codePoint);
        assertEquals(1, obj.charCount);
        assertEquals(Character.UPPERCASE_LETTER, obj.type);
        assertEquals(Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT, obj.directionality);
        assertEquals(0xc4, obj.escaped);
    }

    @Test
    public void testIconRepository() {
        final Base64.Decoder decoder = Base64.getDecoder();
        IconRepository obj = new IconRepository();
        jsonParser.parse("{\"positive\":\"data:application/octet-stream;base64," + IconRepository.POSITIVE
                + "\",\"negative\":\"data:image/png;base64," + IconRepository.NEGATIVE
                + "\",\"search\":\"base16," + StringUtil.toHexString(decoder.decode(IconRepository.SEARCH))
                + "\",\"reset\":\"base16," + StringUtil.toHexString(decoder.decode(IconRepository.RESET))
                + "\"}", obj);
        assertNotNull(obj.positive);
        assertNotNull(obj.negative);
        assertNotNull(obj.search);
        assertNotNull(obj.reset);
        assertTrue(Arrays.equals(decoder.decode(IconRepository.POSITIVE), obj.positive));
        assertTrue(Arrays.equals(decoder.decode(IconRepository.NEGATIVE), ByteArray.Box.unbox(obj.negative)));
        assertTrue(Arrays.equals(decoder.decode(IconRepository.SEARCH), obj.search.array()));
        assertTrue(Arrays.equals(decoder.decode(IconRepository.RESET), obj.reset.array()));
    }

    @Test
    public void testConstructorBuilder() {
        ConstructorBuilder obj = new ConstructorBuilder();
        jsonParser.parse("{\"cs\":{},\"CPU\":{\"physical\":1},\"rulerList\":[{\"unit\":\"AU\"}],\"rulerSet\":[{\"length\":2},{\"length\":5}]}", obj);
        assertTrue(obj.cs instanceof StringBuilder);
        assertTrue(obj.cpu instanceof Processor);
        assertTrue(obj.rulerList instanceof LinkedList);
        assertEquals(2, obj.rulerSet.size());
    }

    @Test
    public void testStaticMethodBuilder() {
        StaticMethodBuilder obj = new StaticMethodBuilder();
        jsonParser.parse("{\"buffer\":{},\"runtime\":{},\"stringQueue\":[\"01\",\"02\"],\"threadSet\":[{},{}]}", obj);
        //assertNotNull(obj.buffer);
        //assertEquals(32, obj.buffer.capacity());
        assertNotNull(obj.runtime);
        assertTrue(obj.stringQueue instanceof ArrayDeque);
        assertEquals("01", obj.stringQueue.remove());
        assertEquals("02", obj.stringQueue.remove());
        assertNotNull(obj.threadSet);
        assertEquals(1, obj.threadSet.size());
    }

    @Test
    public void testTypedPrimitive() {
        assertNull(jsonParser.parse("null", null, Object.class));
        assertNull(jsonParser.parse("null", new Object(), Object.class));
        assertTrue((boolean) jsonParser.parse("true", null, Boolean.class));
        assertEquals(4800, jsonParser.parse("4800", null, Integer.class));
        assertEquals(14.5, jsonParser.parse("14.5", null, Double.class));
    }

    @Test
    public void testTypedArray() {
        int[] intArray = (int[]) jsonParser.parse("[3,19,0]", null, int[].class);
        assertEquals(3, intArray.length);
        assertEquals(3, intArray[0]);
        assertEquals(19, intArray[1]);
        assertEquals(0, intArray[2]);
        long[] longArray = (long[]) jsonParser.parse("[-300,4398046511104]", null, long[].class);
        assertEquals(2, longArray.length);
        assertEquals(-300L, longArray[0]);
        assertEquals(4398046511104L, longArray[1]);
        String[] stringArray = (String[]) jsonParser.parse("[\"Memory\",\"Application\"]", null, String[].class);
        assertEquals(2, stringArray.length);
        assertEquals("Memory", stringArray[0]);
        assertEquals("Application", stringArray[1]);
        Object[] objectArray = (Object[]) jsonParser.parse("[\"RTC\",6,62,null,8.25,false]", null, Object[].class);
        assertEquals(6, objectArray.length);
        assertEquals("RTC", objectArray[0]);
        assertEquals(6L, objectArray[1]);
        assertNull(objectArray[3]);
        assertEquals(8.25, objectArray[4]);
        assertFalse((boolean) objectArray[5]);
    }

    @Test
    public void testTypedJsonArray() {
        JsonArray obj;
        obj = (JsonArray) jsonParser.parse("[0,-3,9091]", null, JsonArray.class);
        assertEquals(3, obj.size());
        assertEquals(0L, obj.get(0));
        obj = (JsonArray) jsonParser.parse("[true,true,false]", null, JsonArray.class);
        assertEquals(3, obj.size());
        assertTrue((boolean) obj.get(0));
        obj = (JsonArray) jsonParser.parse("[true,null,false,null]", null, JsonArray.class);
        assertEquals(4, obj.size());
        assertNull(obj.get(1));
        obj = (JsonArray) jsonParser.parse("[\"true\",6800,false,\"\"]", null, JsonArray.class);
        assertEquals(4, obj.size());
        assertEquals("", obj.get(3));
    }

    @Test
    public void testTypedCollection() {
        ArrayList<?> obj;
        obj = new FloatArrayList();
        assertSame(obj, jsonParser.parse("[24.5,25.5,26.5]", obj, FloatArrayList.class));
        assertEquals(3, obj.size());
        assertEquals(24.5f, obj.get(0));
        obj = new AirConditionerArrayList();
        assertSame(obj, jsonParser.parse("[{\"brand\":\"AUX\",\"temperature\":28.0},{\"brand\":\"mi\",\"temperature\":29.0}]", obj, AirConditionerArrayList.class));
        assertEquals(2, obj.size());
        AirConditioner obj1 = (AirConditioner) obj.get(1);
        assertNotNull(obj1);
        assertEquals("mi", obj1.brand);
    }

    @Test
    public void testTypedProcessor() {
        Processor obj = (Processor) jsonParser.parse("{\"arch\":\"ARMv8\",\"clock\":\"2.54G\",\"physical\":4,\"logic\":8,\"price\":\"833.00\"}", null, Processor.class);
        assertEquals(4, obj.physicalNumber);
        assertEquals(8, obj.logicNumber);
        assertEquals(2.54 * 1000_000_000.0, obj.clockRate, 0.0);
        assertEquals("ARMv8", obj.architecture);
    }

    @Test
    public void testSingleValue1() {
        RulerArray obj = (RulerArray) jsonParser.parse("{\"rulers\":{\"unit\":\"in\",\"length\":33},\"price\":14.5}", null, RulerArray.class);
        assertNotNull(obj.rulers);
        assertEquals(1, obj.rulers.length);
        assertEquals("in", obj.rulers[0].unit);
        assertEquals(14.5, obj.price, 0.0);
    }

    @Test
    public void testSingleValue2() {
        Bucket obj = (Bucket) jsonParser.parse("{\"hash\":5043,\"name\":\"Miller\"}", null, Bucket.class);
        assertNotNull(obj.hash);
        assertNotNull(obj.name);
        assertEquals(1, obj.hash.length);
        assertEquals(1, obj.name.length);
        assertEquals(5043, obj.hash[0]);
        assertEquals("Miller", obj.name[0]);
    }

    public static class FloatArrayList extends ArrayList<Float> {};

    @ParseHint(ParseHint.ACCEPT_NULL)
    public static class ListsHandle implements JsonStructure {

        @DoubleValue(min = 0.0, max = 1.0)
        @ParseHint(ParseHint.APPLY_CLAMP)
        public final LinkedList<Double> doubles = new LinkedList<>();

        @ParseHint(ParseHint.ACCEPT_NULL | ParseHint.ACCEPT_BOOLEAN | ParseHint.ACCEPT_NUMBER | ParseHint.WRAP_SINGLE_VALUE)
        public final List<String> strings = new ArrayList<>();

        public ArrayList<Integer> integers;

        @Override
        public void toJson(JsonConsumer jc) {
            jc.openObject();
            jc.key("doubles");
            jc.openArray();
            for (Double value : doubles) {
                if (value != null) {
                    jc.numberValue(value);
                } else {
                    jc.nullValue();
                }
            }
            jc.closeArray();
            jc.key("strings");
            jc.openArray();
            for (String value : strings) {
                if (value != null) {
                    jc.stringValue(value);
                } else {
                    jc.nullValue();
                }
            }
            jc.closeArray();
            jc.key("integers");
            if (integers != null) {
                jc.openArray();
                for (Integer value : integers) {
                    if (value != null) {
                        jc.numberValue(value);
                    } else {
                        jc.nullValue();
                    }
                }
                jc.closeArray();
            } else {
                jc.nullValue();
            }
            jc.closeObject();
        }
    }

    public static class Ruler implements Serializable {

        private static final long serialVersionUID = 0xC3CC5E978E7AC147L;

        public String unit;

        public int length;

        public Ruler() {
            super();
        }

        public Ruler(String unit, int length) {
            super();
            this.unit = unit;
            this.length = length;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(unit) ^ length;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Ruler) {
                Ruler that = (Ruler) obj;
                return Objects.equals(this.unit, that.unit) && this.length == that.length;
            }
            return false;
        }
    }

    public static class RulerArray implements Serializable {

        private static final long serialVersionUID = 0x7F1E5A838DE125D0L;

        @ParseHint(ParseHint.WRAP_SINGLE_VALUE)
        public Ruler[] rulers;

        public double price;
    }

    public static class RulerSet implements Serializable {

        private static final long serialVersionUID = 0xC3CC5E978E7AC147L;

        public final HashSet<Ruler> rulers = new HashSet<>();

        public double price;
    }

    public static class Material {

        public String name;

        @ParseHint(ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_DEC_STRING | ParseHint.ACCEPT_HEX_STRING)
        public int color;

        public double density;

        public transient int slot;

        private int hash;
    }

    public static class Metal extends Material {

        public double specular;

        public double conductivity;

        @IntValue(min = 0)
        @ParseHint(ParseHint.APPLY_CLAMP)
        public final ArrayList<Integer> shells = new ArrayList<>();
    }

    public static class AirConditioner {

        public String brand;

        public boolean on;

        public double temperature;
    }

    public static class AirConditionerArrayList extends ArrayList<AirConditioner> {}

    public static class Mobile {

        public String IMEI;

        public String ICCID;

        public String MEID;

        public String SEID;
    }

    public static class Processor implements IntConsumer {

        @JsonName("physical")
        public int physicalNumber;

        @JsonName("logic")
        public int logicNumber;

        @JsonName("clock")
        @ParseHint(ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_DOT_STRING | ParseHint.ACCEPT_DEC_UNITS)
        public double clockRate;

        @JsonName("arch")
        public String architecture;

        @Override
        public void accept(int value) {
            // discard
        }
    }

    public static class MobileProcessor {

        public Mobile mobile;

        public Processor processor;

        @ParseHint(ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_DEC_STRING | ParseHint.ACCEPT_DEC_UNITS)
        public long memory;
    }

    /**
     * {"indi.qsq.json.reflect.JsonParserTest$CharacterInfo":{"serialize":0,"parse":0,"closure":23,"mappings":[
     *     {"name":"cp","type":{"type":"int","serialize":0,"parse":0,"min":0,"max":1114111},"write":"FieldReflectSetter","read":"FieldReflectGetter"},
     *     {"name":"cc","type":{"type":"int","serialize":0,"parse":0,"min":1,"max":2},"write":"FieldReflectSetter","read":"FieldReflectGetter"},
     *     {"name":"type","type":{"type":"int-enum","serialize":0,"parse":0,"map":{"size":60,"map":{"Cc":15,"Cf":16,"Cn":0,"Co":18,"Cs":19,"Pc":23,"Pd":20,"Pe":22,"Pf":30,"Pi":29,"Po":24,"Ll":2,"Lm":4,"Lo":5,"Ps":21,"Lt":3,"Lu":1,"Mc":8,"Me":7,"Mn":6,"Zl":13,"Nd":9,"Zp":14,"Zs":12,"Nl":10,"No":11,"Sc":26,"Sk":27,"Sm":25,"So":28}}},"write":"FieldReflectSetter","read":"FieldReflectGetter"},
     *     {"name":"dir","type":{"type":"int-enum","serialize":0,"parse":0,"map":{"size":48,"map":{"LRI":19,"B":10,"LRO":15,"RLE":16,"L":0,"BN":9,"RLI":20,"R":1,"PDF":18,"S":11,"RLO":17,"PDI":22,"WS":12,"FSI":21,"ON":13,"Undefined":-1,"EN":3,"NSM":8,"AL":2,"AN":6,"ES":4,"ET":5,"CS":7,"LRE":14}}},"write":"FieldReflectSetter","read":"FieldReflectGetter"},
     *     {"name":"escaped","type":{"type":"any","serialize":0,"parse":0},"write":"FieldReflectSetter","read":"FieldReflectGetter"},
     *     {"name":"codePoint","type":{"type":"int","serialize":0,"parse":0},"write":"MethodReflectSetter","read":"Getter"}
     * ]}}
     */
    public static class CharacterInfo {

        @JsonName("cp")
        @IntValue(min = 0, max = 0x10ffff)
        public int codePoint;

        @JsonName("cc")
        @IntValue(min = 1, max = 2)
        public int charCount;

        @IntEnumValue(
                integers = {
                        Character.UNASSIGNED, Character.UPPERCASE_LETTER, Character.LOWERCASE_LETTER, Character.TITLECASE_LETTER,
                        Character.MODIFIER_LETTER, Character.OTHER_LETTER, Character.NON_SPACING_MARK, Character.ENCLOSING_MARK,
                        Character.COMBINING_SPACING_MARK, Character.DECIMAL_DIGIT_NUMBER, Character.LETTER_NUMBER, Character.OTHER_NUMBER,
                        Character.SPACE_SEPARATOR, Character.LINE_SEPARATOR, Character.PARAGRAPH_SEPARATOR, Character.CONTROL,
                        Character.FORMAT, Character.PRIVATE_USE, Character.SURROGATE, Character.DASH_PUNCTUATION,
                        Character.START_PUNCTUATION, Character.END_PUNCTUATION, Character.CONNECTOR_PUNCTUATION, Character.OTHER_PUNCTUATION,
                        Character.MATH_SYMBOL, Character.CURRENCY_SYMBOL, Character.MODIFIER_SYMBOL, Character.OTHER_SYMBOL,
                        Character.INITIAL_QUOTE_PUNCTUATION, Character.FINAL_QUOTE_PUNCTUATION
                },
                strings = {
                        "Cn", "Lu", "Ll", "Lt",
                        "Lm", "Lo", "Mn", "Me",
                        "Mc", "Nd", "Nl", "No",
                        "Zs", "Zl", "Zp", "Cc",
                        "Cf", "Co", "Cs", "Pd",
                        "Ps", "Pe", "Pc", "Po",
                        "Sm", "Sc", "Sk", "So",
                        "Pi", "Pf"
                }
        )
        public int type;

        @JsonName("dir")
        @IntEnumValue(
                integers = {
                        Character.DIRECTIONALITY_LEFT_TO_RIGHT, Character.DIRECTIONALITY_RIGHT_TO_LEFT,
                        Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING, Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE, Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING, Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE,
                        Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT, Character.DIRECTIONALITY_LEFT_TO_RIGHT_ISOLATE,  Character.DIRECTIONALITY_RIGHT_TO_LEFT_ISOLATE, Character.DIRECTIONALITY_FIRST_STRONG_ISOLATE,
                        Character.DIRECTIONALITY_POP_DIRECTIONAL_ISOLATE, Character.DIRECTIONALITY_UNDEFINED,
                        Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC, Character.DIRECTIONALITY_EUROPEAN_NUMBER, Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR, Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR,
                        Character.DIRECTIONALITY_ARABIC_NUMBER, Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR, Character.DIRECTIONALITY_NONSPACING_MARK, Character.DIRECTIONALITY_BOUNDARY_NEUTRAL,
                        Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR, Character.DIRECTIONALITY_SEGMENT_SEPARATOR, Character.DIRECTIONALITY_WHITESPACE, Character.DIRECTIONALITY_OTHER_NEUTRALS
                },
                strings = {
                        "L", "R",
                        "LRE", "LRO", "RLE", "RLO",
                        "PDF", "LRI", "RLI", "FSI",
                        "PDI", "Undefined",
                        "AL", "EN", "ES", "ET",
                        "AN", "CS", "NSM", "BN",
                        "B", "S", "WS", "ON"
                }
        )
        public int directionality;

        @IntFlagValue(
                integers = {
                        0x02, 0x01, 0x08, 0x04,
                        0x10, 0x40, 0x20, 0x80, 0x400
                },
                strings = {
                        "C", "java", "Python", "javascript",
                        "json", "xml", "regex", "URI", "csv"
                }
        )
        public int escaped;

        public void setCodePoint(int codePoint) {
            this.codePoint = codePoint;
            this.charCount = Character.charCount(codePoint);
            this.type = Character.getType(codePoint);
            this.directionality = Character.getDirectionality(codePoint);
        }
    }

    @ParseHint(ParseHint.ACCEPT_STRING | ParseHint.ACCEPT_BASE_N_STRING)
    public static class IconRepository {

        static final String POSITIVE = "cEhZcwAAAEgAAABIAEbJaz4AAAbnSURBVGje7VltTFNnFD6nHwgjxM3FJcMxCNEhQ1DubQkpykeQOUww+EMTlgVm1FhGAKE4mQqMoPLRYuimE0gUQ2Q/YEE2WOZiNqCaSdvbFgdkweAGGQYSYAQMGxO4Zz8ut9taO8bXZJnPn7b3nvec53ne9973owDP8AxPBcqLyovKi0FB4ufT4oH/ViGWYRmWefVVSqEUSjl7FnuwB3vefhssYAELIsRDPMS3tPB7+b383qwsm8qmsqn6+v6zBgQHBwcHB7/wgrxOXievO3kSTWhC0/HjcBkuw+V16/6+9cwMNEADNNTUzA==";

        static final String NEGATIVE = "aFG07N5NBjKQwWoFBATU6xcSLsLqZfWyerEsEBDQwYOgAhWoBgbE+/Y8JVACJR9+SCVUQiXd3YLhBw4sl/+iR8AOww7DDsPrr0v6Jf2Sfp0O9ahHfXz8UglYLBaLxYJ2HizLsiz73HOQARmQkZ5OW2gLbTl9GtMxHdO9vJ6cpbUVPdETPTUazsAZOIPNtmIGCIRefhnqoA7qzp0DAxjAkJICVVAFVZJljyBHAxyxXb1dvV29aZPMR+Yj8zl7FjbCRtiYnOxU/xgcg2M8T5M0SZPXrkmNUqPUePq0ucHcYG4YHl60AfaeOAEn4ERPD2hBC1o/v+U=";

        static final String SEARCH = "PaWB0kBpYHGxk14ngSEUQiH79rkiiIVYiIX9/dYN1g3WDTExwssxMZGKqZiK799fmmyJhNnMbGY2HzokH5APyAfu3wcFKEBRUCAK+Gt8U5OwoAoKEkbQ++9P+037TfspFBAFURB1+LAT71qsxVpnXU4GCD2v0VAmZVLml18uRN2y1bLVsvWzzzAXczF32zZhPs/MFBcwC7VnHjGPmEcch+txPa6/ehXuwl0=";

        static final String RESET = "GxcLsRBbUODV7tXu1V5VNa4b143r1q2Tp8vT5emnTlEndVJndjamYRqmubu7yicY5e9vajY1m5p//HHBEbBS6Orq6urqGh+3nreet57PzuZT+BQ+JSgI9sAe2PPVV07CdaQj3eCg27DbsNvwa69ZDVaD1fDxx1MJUwlTCUlJcm+5t9y7txciIAIiTp1yEs4CC3/uqHm4Er7qBjjCvru7CTfh5pUrjvcxB3Mw55VXHmc+znycefgwm8PmsDnffktDNERDtbVwAS7AhU0=";

        public byte[] positive;

        public Byte[] negative;

        public ByteBuffer search;

        public ByteBuf reset;
    }

    public static class ConstructorBuilder {

        @CallBuilder(value = "java.lang.StringBuilder.<init>()", when = UseBuilder.ALWAYS)
        public CharSequence cs;

        @JsonName("CPU")
        @CallBuilder(value = "indi.qsq.json.reflect.JsonParserTest$Processor.new()", when = UseBuilder.ALWAYS)
        public IntConsumer cpu;

        @CallBuilder(value = "java.util.LinkedList.new()", when = UseBuilder.ALWAYS)
        public List<Ruler> rulerList;

        @CallBuilder(value = "indi.qsq.json.reflect.JsonParserTest$Ruler.<init>()[]", when = UseBuilder.ALWAYS)
        public HashSet<Serializable> rulerSet = new HashSet<>();
    }

    @SuppressWarnings("unused")
    public static ByteBuffer allocateByteBuffer(JsonConverter jv) {
        return ByteBuffer.allocate(32);
    }

    @SuppressWarnings("unused")
    public static Deque<String> buildStringDeque(JsonParser jp) {
        return new ArrayDeque<>(12);
    }

    public static class StaticMethodBuilder {

        //@CallBuilder(value = "indi.qsq.json.reflect.JsonParserTest.allocateByteBuffer(indi.qsq.json.reflect.JsonConverter)", when = UseBuilder.ALWAYS)
        //public ByteBuffer buffer;

        @CallBuilder(value = "java.lang.Runtime.getRuntime()", when = UseBuilder.ALWAYS)
        public Runtime runtime;

        @CallBuilder(value = "indi.qsq.json.reflect.JsonParserTest.buildStringDeque(indi.qsq.json.reflect.JsonParser)", when = UseBuilder.ALWAYS)
        public Queue<String> stringQueue;

        @CallBuilder(value = "java.lang.Thread.currentThread()[]", when = UseBuilder.ALWAYS)
        public HashSet<Thread> threadSet;
    }

    public static class Bucket {

        @ParseHint(ParseHint.WRAP_SINGLE_VALUE)
        public int[] hash;

        @ParseHint(ParseHint.WRAP_SINGLE_VALUE)
        public String[] name;
    }
}
