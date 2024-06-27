package indi.qsq.json.io;

import indi.qsq.json.api.JsonNullConsumer;
import indi.qsq.util.random.FuzzyContext;
import indi.qsq.util.random.RandomContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created in infrastructure on 2022/1/1.
 * Recreated on 2022/6/12, named CharSequenceReaderTest.
 * Renamed in 2022/8/12
 */
public class JsonIOAroundTest {

    private static final RandomContext rc = new RandomContext();

    private void assertAround(@NotNull String in, @NotNull String out, int config) {
        final JsonStringWriter writer = new JsonStringWriter();
        {
            JsonCharSequenceReader reader = new JsonCharSequenceReader(in);
            reader.config(config);
            reader.read(writer);
            assertEquals(out, writer.get());
        }
        final ByteBuf byteBuf = Unpooled.copiedBuffer(in, StandardCharsets.UTF_8);
        try {
            {
                JsonByteBufReader reader = new JsonByteBufReader(byteBuf);
                reader.config(config);
                writer.reset();
                reader.read(writer);
                assertEquals(out, writer.get());
            }
            byteBuf.readerIndex(0); // the content can be read again
            {
                writer.reset();
                JsonAsyncByteBufReader reader = new JsonAsyncByteBufReader(writer);
                reader.config(config);
                reader.start();
                reader.testUpdate(rc, byteBuf);
                reader.finish();
                assertEquals(out, writer.get());
            }
        } finally {
            byteBuf.release();
        }
        try (InputStream is = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8))) {
            JsonInputStreamReader reader = new JsonInputStreamReader(is);
            reader.config(config);
            writer.reset();
            reader.read(writer);
            assertEquals(out, writer.get());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    private void assertAround(@NotNull String in, @NotNull String out) {
        assertAround(in, out, 0);
    }

    private void assertAround(@NotNull String io) {
        assertAround(io, io);
    }

    @Test
    public void caseEmpty() {
        assertAround("[]");
        assertAround("{}");
    }

    @Test
    public void caseArray() {
        assertAround("[0,true,false,null,\"\"]");
        assertAround("[-333355558888,777700001111,-9,2022]");
        assertAround("[\"''' '''\\t\\r\\n\\f\"]");
        assertAround("[\"//\u1f08\u03c0\u03bf\u03bb\u03bb\u03ce\u03bd\u03b9\u03bf\u03c2\\n\",\"\",\"03\"]");
        assertAround("[\"..\",-48,\"\u0395\u1f50\u03ba\u03bb\u03b5\u03af\u03b4\u03b7\u03c2\\n\"]");
        assertAround("[\"万神庙自文艺复兴时期以来就是伟人的公墓，这里埋葬的除了维克多，埃马努埃莱二世外，还包括了意大利著名的艺术家拉斐尔和阿尼巴雷" +
                "卡拉齐等人。万神庙今天还是意大利的一个教堂，这里定期举行弥撒以及婚礼庆典，但同时它又是世界各国游客们竞相参观的对象，以及建筑史上重" +
                "要的里程碑。\",\"1824年挪威人阿贝尔证明了五次代数方程通用的求根公式是不存在的。结合高斯关于分圆多项式的结论，接下来的问题自然是，" +
                "如何判定具体的代数方程是否有根式解。到了1830年，法国数学天才伽罗瓦彻底解决了五次多项式方程何时可以有根式解的问题。他的结果也一直没有能够发表。" +
                "1846年，在伽罗瓦死后14年，他的这一伟大成果终见天日。伽罗瓦首次提出了群的概念，并最终利用群论解决了这个世界难题。1870年，法国数学家" +
                "约当根据伽罗瓦的思想撰写了《论置换与代数方程》一书，人们才真正领略了伽罗瓦的伟大思想。伽罗瓦的思想后来衍生出了伽罗瓦理论，属于抽象代数的一个分支。\"]");
    }

    @Test
    public void caseObject() {
        assertAround("{\"gg\":\"0456\",\"[\":\"(\",\"}\":\"*\"}");
        assertAround("{\"#\":\"==++\",\"''''\":\"1123\",\"\":\"\",\"][\":\"ABC!\"}");
        assertAround("{\"\u03a0\u03bb\u03ac\u03c4\u03c9\u03bd柏拉图\":\"\u03a3\u03c9\u03ba\u03c1\u03ac\u03c4\u03b7\u03c2苏格拉底\"}");
        assertAround("{\"before\":\"如果你从事的是宇宙学研究，那么每周都可能收到一些人的信件，电子邮件或传真，描述他们关于宇宙的理论。没错，" +
                "这些人都是男性。如果你礼节性地回信说自己想要了解更多的话，就会犯下大错：无穷无尽的信息将接踵而至，把你淹没。\",\"content\":\"数学" +
                "语言适于表达物理法则，这种神奇是上天赐予我们的绝妙礼物。事实上我们并未真正理解这份礼物，希望在未来的研究中它仍然有效，而且继续扩展" +
                "以拓展人类认知，无论这是好是坏，也无论这带给我们的是欢乐还是困惑。\",\"after\":\"成年的鲭鸥不能识别自己所生的蛋，它会愉快地伏在" +
                "其他海鸥的蛋上，有些做试验的人甚至以粗糙的土制假蛋代替真蛋，它也分辨不出，照样坐在上面。在自然界中，对蛋的识别对于海鸥而言并不重要，" +
                "因为蛋不会滚到几码以外的邻居的鸟窝附近。不过，海鸥还是识别得出它所孵的小海鸥。和蛋不一样，小海鸥会外出溜达，弄不好会可能走到大海鸥的" +
                "窝附近，常常因此断送了性命。这种情况在第一章里已经述及。\"}");
    }

    @Test
    public void caseArrayNest() {
        assertAround("[[],null,35,[8,7,[2],null,[[]],\"\"]]");
        assertAround("[{},false,false,[\"\"],\"''\",{},[{}]]");
    }

    @Test
    public void caseObjectNest() {
        assertAround("{\"a\":{\"aa\":null,\"ab\":{\"aba\":null}},\"b\":null,\"c\":{\"ca\":null},\"d\":{\"da\":null,\"db\":null,\"dc\":{\"dca\":null}}}");
        assertAround("{\"horizontal-alignment\":{\"margin-left\":12,\"margin-right\":12},\"data-object\":{\"style\":{\"display\":\"block\",\"position\":\"absolute\"}},\"extra\":null,\"collection-head\":[\"foo\"]}");
    }

    @Test
    public void caseWhiteSpace() {
        assertAround("[ ]", "[]");
        assertAround("[\r\n]", "[]");
        assertAround("\t [\r\n]", "[]");
        assertAround("\t [] \n", "[]");
        assertAround("{ }", "{}");
        assertAround("    {}", "{}");
        assertAround("{\r\n  \t\t}\r\n\r\n", "{}");
        assertAround("[4, 5, 6, 7]", "[4,5,6,7]");
        assertAround("[[1, 2, 3],\r\n[4, 5, 6],\r\n[7, 8, 9]]", "[[1,2,3],[4,5,6],[7,8,9]]");
        assertAround(" [ 0\r,\n\t 3600,    null\t,true,  \n\nfalse\n,[],{}] ", "[0,3600,null,true,false,[],{}]");
        assertAround(
                "{ \"TR\":true ,\"&*\": 6,\"--\" :1.5,\"++\"  :-0.5 , \"<>\" :\"\\r \\n \\r \\n\",\"\\\"||\\\"\\u0002\\u0003\"    :  \"\" }",
                "{\"TR\":true,\"&*\":6,\"--\":1.5,\"++\":-0.5,\"<>\":\"\\r \\n \\r \\n\",\"\\\"||\\\"\\u0002\\u0003\":\"\"}"
        );
        assertAround(
                "{ \"empty-array\":[\r\n ],\"null\": null,\"bool\": false, \"road\" :{\"stroke-width\":2.5,  \"dash-array\":[\t4 ,6]},\"cross\":{ \"sole\":{\"SSS\":{ } } } }",
                "{\"empty-array\":[],\"null\":null,\"bool\":false,\"road\":{\"stroke-width\":2.5,\"dash-array\":[4,6]},\"cross\":{\"sole\":{\"SSS\":{}}}}"
        );
    }

    @Test
    public void caseSurrogatePair() {
        assertAround("\"\\ud83d\\ude31nonce\"");
        assertAround("\"user\\ud83e\\udd23\"");
        assertAround("[\"nomenclature\",\"occam razor\",\"\\ud83d\\ude00\"]");
        assertAround("[\"transposon\",\"triangulation algorithm\",\"watermelon\\ud83c\\udf49\"]");
        assertAround("{\"\\ud83d\\udc14\\ud83d\\uded2\":22}");
        assertAround("{\"\\ud83d\\udc14*\\ud83d\\uded2\":7}");
        assertAround("{\"--\\ud83d\\udc14\\ud83d\\uded2+\":60059}");
    }

    @Test
    public void caseComment() {
        assertAround("[/*--*/ /* * */\t]", "[]", SyncReader.FLAG_BLOCK_COMMENT);
        assertAround("[//lobby\n //loom\r\n\t]", "[]", SyncReader.FLAG_LINE_COMMENT);
        assertAround("{//mastiff dog\n //mastodon\r\n}", "{}", SyncReader.FLAG_LINE_COMMENT);
        assertAround("{\t\t/******//* mesosphere & meteorite */}", "{}", SyncReader.FLAG_BLOCK_COMMENT);
        assertAround("[{// moccasin \n /* mousse cake\n */}]", "[{}]", SyncReader.FLAG_LINE_COMMENT | SyncReader.FLAG_BLOCK_COMMENT);
        assertAround("[{/* breadcrumb navigation\\n */// ***? \n }]", "[{}]", SyncReader.FLAG_LINE_COMMENT | SyncReader.FLAG_BLOCK_COMMENT);
        assertAround(
                "[null, //  empty array\r\n[],// empty object\n\t{/*foo*/},\"255\"  /** UC **/  ,\"//CC\",\"/* */\",\"\\\\\"// 4 back slashes\r\n]",
                "[null,[],{},\"255\",\"//CC\",\"/* */\",\"\\\\\"]",
                SyncReader.FLAG_LINE_COMMENT | SyncReader.FLAG_BLOCK_COMMENT
        );
        assertAround(
                "//\n{\"cat\"//con\n:\"meow\",//chase\n\"dog\":\"bang\",\"bird\":/* ** // */45,\"pigeon\":96 //OP\n ,\"fish\":null,\"monkey\":[//8,\n\n9,0,3]/**/}",
                "{\"cat\":\"meow\",\"dog\":\"bang\",\"bird\":45,\"pigeon\":96,\"fish\":null,\"monkey\":[9,0,3]}",
                SyncReader.FLAG_LINE_COMMENT | SyncReader.FLAG_BLOCK_COMMENT
        );
    }

    @Test
    public void caseSingleQuote() {
        assertAround(
                "['55','ADC','\"\"','\\r\\t++']",
                "[\"55\",\"ADC\",\"\\\"\\\"\",\"\\r\\t++\"]",
                SyncReader.FLAG_SINGLE_QUOTE_STRING
        );
        assertAround(
                "{'outline':\"circle\\\"'\",\"shape\":'ellipse\"\\'','area':80}",
                "{\"outline\":\"circle\\\"'\",\"shape\":\"ellipse\\\"'\",\"area\":80}",
                SyncReader.FLAG_SINGLE_QUOTE_STRING
        );
    }

    @Test
    public void caseTrialingComma() {
        assertAround("[true,]", "[true]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[false,\r\n]", "[false]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[null ,]", "[null]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[0\t,  ]\r\n", "[0]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[4,5,]", "[4,5]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[-3,-7,\"!\",{},]", "[-3,-7,\"!\",{}]", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("{\"groups\":[\"Tommy\",\"Steve\",],}", "{\"groups\":[\"Tommy\",\"Steve\"]}", SyncReader.FLAG_TRAILING_COMMA);
        assertAround("[{\"count-over\":64,\"quick-scan\":null,},null,null,[],]", "[{\"count-over\":64,\"quick-scan\":null},null,null,[]]", SyncReader.FLAG_TRAILING_COMMA);
    }

    private void assertForm(String in, String out) {
        final JsonStringWriter writer = new JsonStringWriter();
        SyncReader reader = new UriFormDataReader(in);
        reader.read(writer);
        assertEquals(out, writer.get());
    }

    @Test
    public void caseForm1() {
        assertForm("ww=tt", "{\"ww\":\"tt\"}");
        assertForm("5070=2391", "{\"5070\":\"2391\"}");
        assertForm("pp=qq&ff=ss", "{\"pp\":\"qq\",\"ff\":\"ss\"}");
        assertForm("q123-w456-e789", "{\"q123-w456-e789\":true}");
        assertForm("42", "{\"42\":true}");
        assertForm("v&b&n", "{\"v\":true,\"b\":true,\"n\":true}");
        assertForm("btn=false", "{\"btn\":\"false\"}");
    }

    @Test
    public void caseForm2() {
        assertForm("%2bww=%3dtt", "{\"+ww\":\"=tt\"}");
        assertForm("5070%40=2391%40", "{\"5070@\":\"2391@\"}");
        assertForm("pp=qq%5c&ff=ss%5c", "{\"pp\":\"qq\\\\\",\"ff\":\"ss\\\\\"}");
        assertForm("q123%20w456%20e789", "{\"q123 w456 e789\":true}");
        assertForm("%5B42%5D", "{\"[42]\":true}");
        assertForm("%7b&%7c&%7d", "{\"{\":true,\"|\":true,\"}\":true}");
        assertForm("%22btn%22=false", "{\"\\\"btn\\\"\":\"false\"}");
    }

    private void assertException(String in, int config) {
        SyncReader reader;
        boolean success;
        success = false;
        try {
            reader = new JsonCharSequenceReader(in);
            reader.config(config);
            reader.read(JsonNullConsumer.INSTANCE);
        } catch (RuntimeException e) {
            success = true;
        }
        assertTrue(success);
        success = false;
        ByteBuf byteBuf = Unpooled.copiedBuffer(in, StandardCharsets.UTF_8);
        try {
            reader = new JsonByteBufReader(byteBuf);
            reader.config(config);
            reader.read(JsonNullConsumer.INSTANCE);
        } catch (RuntimeException e) {
            success = true;
        } finally {
            byteBuf.release();
        }
        assertTrue(success);
        success = false;
        try (InputStream is = new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8))) {
            reader = new JsonInputStreamReader(is);
            reader.config(config);
            reader.read(JsonNullConsumer.INSTANCE);
        } catch (Exception e) {
            success = true;
        }
        assertTrue(success);
    }

    private void assertException(String in) {
        assertException(in, 0);
    }

    @Test
    public void caseBadStructure() {
        assertException(":(");
        assertException("[fe]");
        assertException("[nu]");
        assertException("[[[");
        assertException("}}}}");
        assertException("[[][]]");
        assertException("{{}}");
        assertException("{[]}");
        assertException("{:}");
        assertException("[[]:[]]");
        assertException("{[]:[]}");
        assertException("{[],[],[],[]}");
    }

    @Test
    public void caseBadBoolean() {
        assertException("tr,ue");
        assertException("[tru]");
        assertException("[ture]");
        assertException("[tee,true]");
        assertException("[false,fse]");
        assertException("nu[]ll");
        assertException("[nul]");
        assertException("[nil]");
        assertException("[nonnull]");
        assertException("[nullptr]");
        assertException("{true:null}");
        assertException("{null:false}");
        assertException("{false:true}");
    }

    @Test
    public void caseBadNumber() {
        assertException("[1.2, 3.3.4]");
        assertException("[9.8, 5+6]");
        assertException("[4-5]");
        assertException("[2030, - 7]");
        assertException("[40098200, -7 00]");
        assertException("[1280x960, 0]");
        assertException("[8a, 22b, 7c]");
        assertException("[80_997_203, 9_445, 73, 1, 1]");
        assertException("[.]");
        assertException("[3,..]");
        assertException("{6: 5}");
        assertException("{24: 4008, 45: 92.50}");
        assertException("{\"log\":}");
        assertException("{\"fuel\":.}");
        assertException("{\"mass\":..}");
    }

    @Test
    public void caseBadString() {
        assertException("{\"true\", \"false\"}");
        assertException("[\"true\", 'false']");
        assertException("[\"pick\", `pack`]");
        assertException("[\"\"\"]");
        assertException("[\"\"\"\"\"\"]");
        assertException("{name: \"Emily\"}");
        assertException("{null: \"roll\"}");
        assertException("[\"cats\": \"meow\"]");
        assertException("[\"false\": \"bad\"]");
        assertException("[a\"glue\", 0, null, 9]");
        assertException("[\"glue\"a, 0, null, 9]");
        assertException("[a\r\n\"glue\", 0, null, 9]");
        assertException("[6000, true, true, \"look \\\"at\\\" me\":]");
        assertException("[6000, true, true, :\"look \\\"at\\\" me\"]");
        assertException("[6000, true, true, }\n\"look \\\"at\\\" me\"]");
        assertException("[6000, true, true, \"look \\\"at\\\" me\"}\n]");
        assertException("{空间\"建筑\":\"海洋馆\"}");
        assertException("{\"建筑\"兴趣:\"海洋馆\"}");
        assertException("{\"建筑\":数学\"海洋馆\"}");
        assertException("{\"建筑\":\"海洋馆\"游戏}");
    }

    @Test
    public void caseBadComment() {
        assertException("[//lobby\n //loom\r\n\t]", SyncReader.FLAG_BLOCK_COMMENT);
        assertException("{//mastiff dog\n //mastodon\r\n}", SyncReader.FLAG_BLOCK_COMMENT);
        assertException("[/*--*/ /* * */\t]", SyncReader.FLAG_LINE_COMMENT);
        assertException("{\t\t/******//* mesosphere & meteorite */}", SyncReader.FLAG_LINE_COMMENT);
        assertException("[{// moccasin \n /* mousse cake\n */}]", SyncReader.FLAG_LINE_COMMENT);
        assertException("[{/* breadcrumb navigation\\n */// ***? \n }]", SyncReader.FLAG_BLOCK_COMMENT);
    }

    @Test
    public void caseBadSingleQuote() {
        assertException("''");
        assertException("[\"勤奋\",'南宋',\"流水']", SyncReader.FLAG_SINGLE_QUOTE_STRING);
        assertException("[''经天纬地'']", SyncReader.FLAG_SINGLE_QUOTE_STRING);
        assertException("['thanks','''jet''']", SyncReader.FLAG_SINGLE_QUOTE_STRING);
    }

    @Test
    public void caseBadTrialingComma() {
        assertException("[,]");
        assertException("{,}");
        assertException("[0,]");
        assertException("{\"quick\":4,}");
        assertException("[,,]", SyncReader.FLAG_TRAILING_COMMA);
        assertException("{,,}", SyncReader.FLAG_TRAILING_COMMA);
        assertException("[null,,]", SyncReader.FLAG_TRAILING_COMMA);
        assertException("{\"fill\":\"45dcf9\",,}", SyncReader.FLAG_TRAILING_COMMA);
    }

    private final FuzzyContext fc = new FuzzyContext();

    private static final int ARRAY = 25;

    public void assertIntArray1(int[] array) {
        final String string = Arrays.toString(array);
        assertArrayEquals(array, JsonCharSequenceReader.readIntArray1(string));
        try (InputStream is = new ByteArrayInputStream(string.getBytes(StandardCharsets.US_ASCII))) {
            JsonInputStreamReader reader = new JsonInputStreamReader(is);
            assertArrayEquals(array, reader.readIntArray1());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @Category(FuzzyContext.class)
    public void testIntArray1() {
        for (int arrayIndex = 0; arrayIndex < ARRAY; arrayIndex++) {
            assertIntArray1(fc.fuzzyIntArray(fc.nextInt(ARRAY)));
        }
    }

    public void assertLongArray1(long[] array) {
        final String string = Arrays.toString(array);
        assertArrayEquals(array, JsonCharSequenceReader.readLongArray1(string));
        try (InputStream is = new ByteArrayInputStream(string.getBytes(StandardCharsets.US_ASCII))) {
            JsonInputStreamReader reader = new JsonInputStreamReader(is);
            assertArrayEquals(array, reader.readLongArray1());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @Category(FuzzyContext.class)
    public void testLongArray1() {
        for (int arrayIndex = 0; arrayIndex < ARRAY; arrayIndex++) {
            assertLongArray1(fc.fuzzyLongArray(fc.nextInt(ARRAY)));
        }
    }

    public void assertIntArray2(int[][] array) {
        final String string = Arrays.deepToString(array);
        assertArrayEquals(array, JsonCharSequenceReader.readIntArray2(string));
        try (InputStream is = new ByteArrayInputStream(string.getBytes(StandardCharsets.US_ASCII))) {
            JsonInputStreamReader reader = new JsonInputStreamReader(is);
            assertArrayEquals(array, reader.readIntArray2());
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Test
    @Category(FuzzyContext.class)
    public void testIntArray2() {
        for (int arrayIndex = 0; arrayIndex < ARRAY; arrayIndex++) {
            int length = fc.nextInt(ARRAY);
            int[][] array = new int[length][];
            for (int index = 0; index < length; index++) {
                array[index] = fc.fuzzyIntArray(fc.nextInt(ARRAY));
            }
            assertIntArray2(array);
        }
    }

    public void assertLongArray2(long[][] array) {
        assertArrayEquals(array, JsonCharSequenceReader.readLongArray2(Arrays.deepToString(array)));
    }

    @Test
    @Category(FuzzyContext.class)
    public void testLongArray2() {
        for (int arrayIndex = 0; arrayIndex < ARRAY; arrayIndex++) {
            int length = fc.nextInt(ARRAY);
            long[][] array = new long[length][];
            for (int index = 0; index < length; index++) {
                array[index] = fc.fuzzyLongArray(fc.nextInt(ARRAY));
            }
            assertLongArray2(array);
        }
    }

    @Test
    public void caseCoarseInt() {
        assertEquals(0, JsonCharSequenceReader.readCoarseInt("0", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseInt("1", 99));
        assertEquals(-1, JsonCharSequenceReader.readCoarseInt("-1", 99));
        assertEquals(2147483647, JsonCharSequenceReader.readCoarseInt("2147483647", 99));
        assertEquals(-2147483648, JsonCharSequenceReader.readCoarseInt("-2147483648", 99));
        assertEquals(0, JsonCharSequenceReader.readCoarseInt("-0", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseInt("cc1", 99));
        assertEquals(-1, JsonCharSequenceReader.readCoarseInt("-1xx", 99));
        assertEquals(33, JsonCharSequenceReader.readCoarseInt("33*78", 99));
        assertEquals(-450, JsonCharSequenceReader.readCoarseInt("-450+211", 99));
        assertEquals(2147483647, JsonCharSequenceReader.readCoarseInt("ii2147483647", 99));
        assertEquals(-2147483648, JsonCharSequenceReader.readCoarseInt("kk-2147483648", 99));
        assertEquals(99, JsonCharSequenceReader.readCoarseInt("4444455555%%", 99));
        assertEquals(99, JsonCharSequenceReader.readCoarseInt("-4444455555##", 99));
    }

    @Test
    public void caseCoarseLong() {
        assertEquals(0, JsonCharSequenceReader.readCoarseLong("0", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseLong("1", 99));
        assertEquals(-1, JsonCharSequenceReader.readCoarseLong("-1", 99));
        assertEquals(-1, JsonCharSequenceReader.readCoarseLong("--1", 99));
        assertEquals(-4, JsonCharSequenceReader.readCoarseLong("-(-4)", 99));
        assertEquals(5630, JsonCharSequenceReader.readCoarseLong("5630-2300", 99));
        assertEquals(33, JsonCharSequenceReader.readCoarseLong("(33+89)*(21-90)", 99));
    }

    @Test
    public void caseCoarseUnsignedInt() {
        assertEquals(0, JsonCharSequenceReader.readCoarseUnsignedInt("0", 99));
        assertEquals(0, JsonCharSequenceReader.readCoarseUnsignedInt("000", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseUnsignedInt("1", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseUnsignedInt("-1", 99));
        assertEquals(0, JsonCharSequenceReader.readCoarseUnsignedInt("0x114514", 99));
        assertEquals(89, JsonCharSequenceReader.readCoarseUnsignedInt("#fa89c5", 99));
        assertEquals(40, JsonCharSequenceReader.readCoarseUnsignedInt("margin = 40", 99));
        assertEquals(47, JsonCharSequenceReader.readCoarseUnsignedInt("image47.jpg", 99));
        assertEquals(8, JsonCharSequenceReader.readCoarseUnsignedInt("8.225", 99));
        assertEquals((int) 3333377777L, JsonCharSequenceReader.readCoarseUnsignedInt("<3333377777>", 99));
        assertEquals((int) 3333377777L, JsonCharSequenceReader.readCoarseUnsignedInt("--3333377777++", 99));
    }

    @Test
    public void caseCoarseUnsignedLong() {
        assertEquals(0, JsonCharSequenceReader.readCoarseUnsignedLong("0", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseUnsignedLong("1", 99));
        assertEquals(1, JsonCharSequenceReader.readCoarseUnsignedLong("-1", 99));
        assertEquals(4007, JsonCharSequenceReader.readCoarseUnsignedLong("--4007--", 99));
        assertEquals(652, JsonCharSequenceReader.readCoarseUnsignedLong("((652,908))", 99));
        assertEquals(204, JsonCharSequenceReader.readCoarseUnsignedLong("204.50", 99));
    }
}
