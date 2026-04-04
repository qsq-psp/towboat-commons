package mujica.json.io;

import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2022/1/1", project = "infrastructure")
@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "CharSequenceReaderTest")
@CodeHistory(date = "2022/8/12", project = "Ultramarine", name = "JsonIOAroundTest")
@CodeHistory(date = "2026/3/21")
@SuppressWarnings("SpellCheckingInspection")
public class JsonAroundTest {

    private void aroundString(@NotNull String in, @NotNull String out, int flags) {
        final JsonCharSequenceReader reader = new JsonCharSequenceReader(in);
        reader.setFlags(flags);
        final JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
        reader.read(writer);
        Assert.assertEquals(out, writer.getString());
    }

    private void aroundFromByteBuf1(@NotNull String in, @NotNull String out, int flags) {
        final JsonByteBufReader reader = new JsonByteBufReader(Unpooled.wrappedBuffer(in.getBytes(StandardCharsets.UTF_8)));
        try {
            reader.setFlags(flags);
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            reader.read(writer);
            Assert.assertEquals(out, writer.getString());
        } finally {
            reader.release();
        }
    }

    private void aroundFromByteBuf2(@NotNull String in, @NotNull String out, int flags) {
        final JsonRewriteByteBufReader reader = new JsonRewriteByteBufReader(Unpooled.wrappedBuffer(in.getBytes(StandardCharsets.UTF_8)));
        try {
            reader.setFlags(flags);
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            reader.read(writer);
            Assert.assertEquals(out, writer.getString());
        } finally {
            reader.release();
        }
    }

    private void aroundFromByteStream(@NotNull String in, @NotNull String out, int flags) throws IOException {
        final JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            is.read(writer);
        }
        Assert.assertEquals(out, writer.getString());
    }

    private void aroundToCharStream(@NotNull String in, @NotNull String out, int flags) throws IOException {
        final JsonCharSequenceReader reader = new JsonCharSequenceReader(in);
        reader.setFlags(flags);
        final CharArrayWriter innerWriter = new CharArrayWriter();
        try (JsonCharStreamWriter outerWriter = new JsonCharStreamWriter(innerWriter)) {
            reader.read(outerWriter);
            outerWriter.flush();
        }
        Assert.assertEquals(out, innerWriter.toString());
    }

    private void aroundToByteStream(@NotNull String in, @NotNull String out, int flags) throws IOException {
        final JsonCharSequenceReader reader = new JsonCharSequenceReader(in);
        reader.setFlags(flags);
        final ByteArrayOutputStream innerWriter = new ByteArrayOutputStream();
        try (JsonByteStreamWriter outerWriter = new JsonByteStreamWriter(innerWriter)) {
            reader.read(outerWriter);
            outerWriter.flush();
        }
        Assert.assertEquals(out, innerWriter.toString(StandardCharsets.UTF_8));
    }

    private void around(@NotNull String in, @NotNull String out, int flags) throws IOException {
        aroundString(in, out, flags);
        aroundFromByteBuf1(in, out, flags);
        aroundFromByteBuf2(in, out, flags);
        aroundFromByteStream(in, out, flags);
        aroundToCharStream(in, out, flags);
        aroundToByteStream(in, out, flags);
    }

    private void around(@NotNull String in, @NotNull String out) throws IOException {
        around(in, out, 0);
    }

    private void around(@NotNull String io) throws IOException {
        around(io, io);
    }

    @Test
    public void casePrimitive() throws IOException {
        around("null");
        around("true");
        around("false");
        around("4999");
        around("-56");
        around("2.25");
        around("0.12500", "0.125");
        around("1e3", "1000.0");
        around("\"\"");
        around("\"fjwoijriowejfmoi\\t\"");
        around("\"\\r\\n\\\\\"");
        around("\"\\\\goat\\\\bison\"");
    }

    @Test
    public void caseArray() throws IOException {
        around("[]");
        around("[0,true,false,null,\"\"]");
        around("[-333355558888,777700001111,-9,2022]");
        around("[2147483647,-2147483648,214748364,-214748364]");
        around("[9223372036854775807,-9223372036854775808,922337203685477580,-922337203685477580]");
        around("[\"''' '''\\t\\r\\n\\f\"]");
        around("[\"//\u1f08\u03c0\u03bf\u03bb\u03bb\u03ce\u03bd\u03b9\u03bf\u03c2\\n\",\"\",\"03\"]");
        around("[\"..\",-48,\"\u0395\u1f50\u03ba\u03bb\u03b5\u03af\u03b4\u03b7\u03c2\\n\"]");
        around("[\"万神庙自文艺复兴时期以来就是伟人的公墓，这里埋葬的除了维克多，埃马努埃莱二世外，还包括了意大利著名的艺术家拉斐尔和阿尼巴雷" +
                "卡拉齐等人。万神庙今天还是意大利的一个教堂，这里定期举行弥撒以及婚礼庆典，但同时它又是世界各国游客们竞相参观的对象，以及建筑史上重" +
                "要的里程碑。\",\"1824年挪威人阿贝尔证明了五次代数方程通用的求根公式是不存在的。结合高斯关于分圆多项式的结论，接下来的问题自然是，" +
                "如何判定具体的代数方程是否有根式解。到了1830年，法国数学天才伽罗瓦彻底解决了五次多项式方程何时可以有根式解的问题。他的结果也一直没有能够发表。" +
                "1846年，在伽罗瓦死后14年，他的这一伟大成果终见天日。伽罗瓦首次提出了群的概念，并最终利用群论解决了这个世界难题。1870年，法国数学家" +
                "约当根据伽罗瓦的思想撰写了《论置换与代数方程》一书，人们才真正领略了伽罗瓦的伟大思想。伽罗瓦的思想后来衍生出了伽罗瓦理论，属于抽象代数的一个分支。\"]");
    }

    @Test
    public void caseObject() throws IOException {
        around("{}");
        around("{\"gg\":\"0456\",\"[\":\"(\",\"}\":\"*\"}");
        around("{\"#\":\"==++\",\"''''\":\"1123\",\"\":\"\",\"][\":\"ABC!\"}");
        around("{\"\u03a0\u03bb\u03ac\u03c4\u03c9\u03bd柏拉图\":\"\u03a3\u03c9\u03ba\u03c1\u03ac\u03c4\u03b7\u03c2苏格拉底\"}");
        around("{\"before\":\"如果你从事的是宇宙学研究，那么每周都可能收到一些人的信件，电子邮件或传真，描述他们关于宇宙的理论。没错，" +
                "这些人都是男性。如果你礼节性地回信说自己想要了解更多的话，就会犯下大错：无穷无尽的信息将接踵而至，把你淹没。\",\"content\":\"数学" +
                "语言适于表达物理法则，这种神奇是上天赐予我们的绝妙礼物。事实上我们并未真正理解这份礼物，希望在未来的研究中它仍然有效，而且继续扩展" +
                "以拓展人类认知，无论这是好是坏，也无论这带给我们的是欢乐还是困惑。\",\"after\":\"成年的鲭鸥不能识别自己所生的蛋，它会愉快地伏在" +
                "其他海鸥的蛋上，有些做试验的人甚至以粗糙的土制假蛋代替真蛋，它也分辨不出，照样坐在上面。在自然界中，对蛋的识别对于海鸥而言并不重要，" +
                "因为蛋不会滚到几码以外的邻居的鸟窝附近。不过，海鸥还是识别得出它所孵的小海鸥。和蛋不一样，小海鸥会外出溜达，弄不好会可能走到大海鸥的" +
                "窝附近，常常因此断送了性命。这种情况在第一章里已经述及。\"}");
    }

    @Test
    public void caseArrayNest() throws IOException {
        around("[[[]]]");
        around("[[[[[]]],[],[[]]]]");
        around("[[],null,35,[8,7,[2],null,[[]],\"\"]]");
        around("[{},false,false,[\"\"],\"''\",{},[{}]]");
        around("[null,[[\"foihwehfiojfoijf\"]]]");
    }

    @Test
    public void caseObjectNest() throws IOException {
        around("{\"a\":{},\"b\":{\"ba\":{}}}");
        around("{\"a\":{\"aa\":null,\"ab\":{\"aba\":null}},\"b\":null,\"c\":{\"ca\":null},\"d\":{\"da\":null,\"db\":null,\"dc\":{\"dca\":null}}}");
        around("{\"horizontal-alignment\":{\"margin-left\":12,\"margin-right\":12},\"data-object\":{\"style\":{\"display\":\"block\",\"position\":\"absolute\"}},\"extra\":null,\"collection-head\":[\"foo\"]}");
    }

    @Test
    public void caseWhiteSpace() throws IOException {
        around("[ ]", "[]");
        around("[\r\n]", "[]");
        around("\t [\r\n]", "[]");
        around("\t [] \n", "[]");
        around("{ }", "{}");
        around("    {}", "{}");
        around("{\r\n  \t\t}\r\n\r\n", "{}");
        around("[4, 5, 6, 7]", "[4,5,6,7]");
        around("[[1, 2, 3],\r\n[4, 5, 6],\r\n[7, 8, 9]]", "[[1,2,3],[4,5,6],[7,8,9]]");
        around(" [ 0\r,\n\t 3600,    null\t,true,  \n\nfalse\n,[],{}] ", "[0,3600,null,true,false,[],{}]");
        around(
                "{ \"TR\":true ,\"&*\": 6,\"--\" :1.5,\"++\"  :-0.5 , \"<>\" :\"\\r \\n \\r \\n\",\"\\\"||\\\"\"    :  \"\" }",
                "{\"TR\":true,\"&*\":6,\"--\":1.5,\"++\":-0.5,\"<>\":\"\\r \\n \\r \\n\",\"\\\"||\\\"\":\"\"}"
        );
        around(
                "{ \"empty-array\":[\r\n ],\"null\": null,\"bool\": false, \"road\" :{\"stroke-width\":2.5,  \"dash-array\":[\t4 ,6]},\"cross\":{ \"sole\":{\"SSS\":{ } } } }",
                "{\"empty-array\":[],\"null\":null,\"bool\":false,\"road\":{\"stroke-width\":2.5,\"dash-array\":[4,6]},\"cross\":{\"sole\":{\"SSS\":{}}}}"
        );
    }

    @Test
    public void caseSurrogatePair() throws IOException {
        around("\"\ud83d\ude31nonce\"");
        around("\"user\ud83e\udd23\"");
        around("[\"nomenclature\",\"occam razor\",\"\ud83d\ude00\"]");
        around("[\"transposon\",\"triangulation algorithm\",\"watermelon\ud83c\udf49\"]");
        around("{\"\ud83d\udc14\ud83d\uded2\":22}");
        around("{\"\ud83d\udc14*\ud83d\uded2\":7}");
        around("{\"--\ud83d\udc14\ud83d\uded2+\":60059}");
    }

    @Test
    public void caseLineComment() throws IOException {
        around("//xx\r\n64//yy\r\n", "64", JsonSyncReader.FLAG_LINE_COMMENT);
        around("//---\ntrue//---\n", "true", JsonSyncReader.FLAG_LINE_COMMENT);
        around("//....\nnull//....\n", "null", JsonSyncReader.FLAG_LINE_COMMENT);
        around("[//lobby\n //loom\r\n\t]", "[]", JsonSyncReader.FLAG_LINE_COMMENT);
        around("{//mastiff dog\n //mastodon\r\n}", "{}", JsonSyncReader.FLAG_LINE_COMMENT);
    }

    @Test
    public void caseBlockComment() throws IOException {
        around("/**/\t[\t]\t/**/", "[]", JsonSyncReader.FLAG_BLOCK_COMMENT);
        around("/*..*/ /*..*/ [ \"block\" ]/*..*/", "[\"block\"]", JsonSyncReader.FLAG_BLOCK_COMMENT);
        around("[/*--*/ /* * */\t4500]", "[4500]", JsonSyncReader.FLAG_BLOCK_COMMENT);
        around("{\t/*****//** zone & allocate **/}", "{}", JsonSyncReader.FLAG_BLOCK_COMMENT);
        around("{\"hyphens\"\t\t/******/:/* mesosphere & meteorite */\"manual\"}", "{\"hyphens\":\"manual\"}", JsonSyncReader.FLAG_BLOCK_COMMENT);
    }

    @Test
    public void caseMixedComment() throws IOException {
        around("[{// moccasin \n /* mousse cake\n */}]", "[{}]", JsonSyncReader.FLAG_LINE_COMMENT | JsonSyncReader.FLAG_BLOCK_COMMENT);
        around("[{/* breadcrumb navigation\\n */// ***? \n }]", "[{}]", JsonSyncReader.FLAG_LINE_COMMENT | JsonSyncReader.FLAG_BLOCK_COMMENT);
        around(
                "[null, //  empty array\r\n[],// empty object\n\t{/*foo*/},\"255\"  /** UC **/  ,\"//CC\",\"/* */\",\"\\\\\"// 4 back slashes\r\n]",
                "[null,[],{},\"255\",\"//CC\",\"/* */\",\"\\\\\"]",
                JsonSyncReader.FLAG_LINE_COMMENT | JsonSyncReader.FLAG_BLOCK_COMMENT
        );
        around(
                "//\n{\"cat\"//con\n:\"meow\",//chase\n\"dog\":\"bang\",\"bird\":/* ** // */45,\"pigeon\":96 //OP\n ,\"fish\":null,\"monkey\":[//8,\n\n9,0,3]/**/}",
                "{\"cat\":\"meow\",\"dog\":\"bang\",\"bird\":45,\"pigeon\":96,\"fish\":null,\"monkey\":[9,0,3]}",
                JsonSyncReader.FLAG_LINE_COMMENT | JsonSyncReader.FLAG_BLOCK_COMMENT
        );
    }

    @Test
    public void caseApostropheQuoteString() throws IOException {
        around(
                "'Vec::new'",
                "\"Vec::new\"",
                JsonSyncReader.FLAG_APOSTROPHE_QUOTE_STRING
        );
        around(
                "['55','ADC','\"\"','\\r\\t++']",
                "[\"55\",\"ADC\",\"\\\"\\\"\",\"\\r\\t++\"]",
                JsonSyncReader.FLAG_APOSTROPHE_QUOTE_STRING
        );
        around(
                "{'outline':\"circle\\\"'\",\"shape\":'ellipse\"\\'','area':80}",
                "{\"outline\":\"circle\\\"'\",\"shape\":\"ellipse\\\"'\",\"area\":80}",
                JsonSyncReader.FLAG_APOSTROPHE_QUOTE_STRING
        );
    }

    @Test
    public void caseGraveAccentQuoteString() throws IOException {
        around(
                "`grid-column-start`",
                "\"grid-column-start\"",
                JsonSyncReader.FLAG_GRAVE_ACCENT_QUOTE_STRING
        );
        around(
                "[3, 0, -20, true, null, `%`, `\t\t`]\r\n",
                "[3,0,-20,true,null,\"%\",\"\\t\\t\"]",
                JsonSyncReader.FLAG_GRAVE_ACCENT_QUOTE_STRING
        );
        around(
                "{`flex-direction`: `column`, \"flex-wrap\": \"wrap\"}",
                "{\"flex-direction\":\"column\",\"flex-wrap\":\"wrap\"}",
                JsonSyncReader.FLAG_GRAVE_ACCENT_QUOTE_STRING
        );
    }

    @Test
    public void caseLeadingComma() throws IOException {
        around("\t[,true]\t", "[true]", JsonSyncReader.FLAG_LEADING_COMMA);
    }

    @Test
    public void caseTrailingComma() throws IOException {
        around("[true,]", "[true]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[false,\r\n]", "[false]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[null ,]", "[null]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[0\t,  ]\r\n", "[0]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[4,5,]", "[4,5]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[-3,-7,\"!\",{},]", "[-3,-7,\"!\",{}]", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("{\"groups\":[\"Tommy\",\"Steve\",],}", "{\"groups\":[\"Tommy\",\"Steve\"]}", JsonSyncReader.FLAG_TRAILING_COMMA);
        around("[{\"count-over\":64,\"quick-scan\":null,},null,null,[],]", "[{\"count-over\":64,\"quick-scan\":null},null,null,[]]", JsonSyncReader.FLAG_TRAILING_COMMA);
    }

    // JsonSkipAroundTest

    private void skipAroundFromByteBuf1(@NotNull String in, @NotNull String out, int flags) {
        final JsonByteBufReader reader = new JsonByteBufReader(Unpooled.wrappedBuffer(in.getBytes(StandardCharsets.UTF_8)));
        try {
            reader.setFlags(flags);
            reader.skip();
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            reader.read(writer);
            Assert.assertEquals(out, writer.getString());
        } finally {
            reader.release();
        }
    }

    private void skipAroundFromByteBuf2(@NotNull String in, @NotNull String out, int flags) {
        final JsonRewriteByteBufReader reader = new JsonRewriteByteBufReader(Unpooled.wrappedBuffer(in.getBytes(StandardCharsets.UTF_8)));
        try {
            reader.setFlags(flags);
            reader.skip();
            JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
            reader.read(writer);
            Assert.assertEquals(out, writer.getString());
        } finally {
            reader.release();
        }
    }

    private void skipAroundFromByteStream(@NotNull String in, @NotNull String out, int flags) throws IOException {
        final JsonStringBuilderWriter writer = new JsonStringBuilderWriter();
        try (JsonObjectInputStream is = new JsonObjectInputStream(new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8)))) {
            is.setFlags(flags);
            is.skip();
            is.read(writer);
        }
        Assert.assertEquals(out, writer.getString());
    }

    private void skipAround(@NotNull String in, @NotNull String out, int flags) throws IOException {
        skipAroundFromByteBuf1(in, out, flags);
        skipAroundFromByteBuf2(in, out, flags);
        skipAroundFromByteStream(in, out, flags);
    }

    private void skipAround(@NotNull String in, @NotNull String out) throws IOException {
        skipAround(in, out, 0);
    }

    @Test
    public void caseSkipPrimitive() throws IOException {
        skipAround("null null ", "null");
        skipAround("-323 null", "null");
        skipAround("2.10 null ", "null");
        skipAround("\"OK\"null", "null");
        skipAround("true\tnull", "null");
        skipAround("false\nnull\n", "null");
        skipAround("null\"\"", "\"\"");
        skipAround("5.44\"!\"", "\"!\"");
        skipAround("true\t\t\"video\"", "\"video\"");
        skipAround("\"webp\"\t\t\"avif\"", "\"avif\"");
        skipAround("42 43", "43");
        skipAround("-20 0", "0");
        skipAround("1e20 -5000", "-5000");
    }

    @Test
    public void caseSkipArray() throws IOException {
        skipAround("[][]", "[]");
        skipAround("[true,9,10][]", "[]");
        skipAround("[\"ridge\"][\"group\"]", "[\"group\"]");
        skipAround("[\"wound\\r\\n\"] [\"pound\\r\\n\"]", "[\"pound\\r\\n\"]");
    }

    @Test
    public void caseSkipObject() throws IOException {
        skipAround("{}{}", "{}");
        skipAround("{\"x\":4} {}", "{}");
    }
}
