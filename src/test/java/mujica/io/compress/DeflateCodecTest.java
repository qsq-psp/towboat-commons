package mujica.io.compress;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@CodeHistory(date = "2025/10/6")
@SuppressWarnings("SpellCheckingInspection")
public class DeflateCodecTest {

    @NotNull
    private String canonicalConstruct(@NotNull int[] bitLengths) {
        return Arrays.toString(HuffmanCode.canonicalConstruct(bitLengths));
    }

    @Test
    public void caseCanonicalConstruct() {
        Assert.assertEquals("[10, 0, 110, 111]", canonicalConstruct(new int[] {2, 1, 3, 3}));
        Assert.assertEquals("[110, 111, 10, 0]", canonicalConstruct(new int[] {3, 3, 2, 1}));
        Assert.assertEquals("[010, 011, 100, 101, 110, 00, 1110, 1111]", canonicalConstruct(new int[] {3, 3, 3, 3, 3, 2, 4, 4}));
    }

    private void validate(@NotNull int[] bitLengths) {
        HuffmanCode.checkHealth(HuffmanCode.canonicalConstruct(bitLengths));
    }

    @Test
    public void caseValidate() {
        validate(new int[] {2, 1, 3, 3});
        validate(new int[] {3, 3, 2, 1});
        validate(new int[] {3, 3, 3, 3, 3, 2, 4, 4});
        HuffmanCode.checkHealth(HuffmanCode.fixedLiteralLengthAlphabet());
        HuffmanCode.checkHealth(HuffmanCode.fixedDistanceAlphabet());
    }

    private void caseInflaterInputStream(@NotNull byte[] expected) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int level = Deflater.NO_COMPRESSION; level <= Deflater.BEST_COMPRESSION; level++) {
            bos.reset();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bos, new Deflater(level, true))) {
                dos.write(expected);
                dos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            try (TowboatInflaterInputStream dis = new TowboatInflaterInputStream(new ByteArrayInputStream(compressed))) {
                actual = dis.readNBytes(expected.length);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, dis.read());
            } catch (Throwable e) {
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(actual)));
                throw e;
            }
        }
    }

    private void caseInflaterInputStreamBinary(@NotNull String base64Data) throws IOException {
        caseInflaterInputStream(Base64.getDecoder().decode(base64Data));
    }

    private void caseInflaterInputStreamUTF8(@NotNull String string) throws IOException {
        caseInflaterInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void caseInflaterInputStream1() throws IOException {
        caseInflaterInputStreamBinary("iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAYAAACtWK6eAAAWTklEQVR4nO2d0XXbxtLHh4nfya+CMO9xwluB4QqsVEC6AkEVCKqAdAWiK4huBYYqMJ3k3VQH9LtjfL8RgXttXwkisbtYAJzfOXN2V+dYAGbmv7uDpamRGIbxKCYQw6jBBGIYNZhADKMGE4hh1GACMYwaTCCGUYMJxDBqMIEYRg0mEMOowQRiGDWYQAyjBhOIYdRgAjGMGkwghlGDCcQwajCBdIQZ/PPPP2O68uOPP37aAF0jMiaQiKCJCaK4pLsoimJC+x9Go9GOZo1YrtCK9o0ImEAi8euvv54jigybMHwUFQqW/fnnn28YGi1jAmmZX375JSHhr4uimMoR8G+2/JvXf//9dy5Ga5hAWoLt1Ozz589Luom4kT979uyCbdeGvhEYE0hg0IXWGUtm/4V4hBVF6xMVyo6hEQgTSCBUGKwY5yRyijgm/Mg7/O4dv3vFivLGhBIGE0gAKMDnxb4An0oLIJQtpoX8W4aGR0aY4QktwEVEX9smEodcRK6skPeHCcQDbKemZZ1xxjA6rCY3ZX2yFcMJE4gDCOO+zqCbSTfJrD5xwwTSEOqMc1YMrTMmDDsLq4kdNDpgAjkSrTNIuOuipQLcF9zzlnt+bfXJcZhADoTt1Izt1JJuIv0mZ9t1wbZrQ994AhPIE6CLIAd9sWFFsYPGAzCBPIIKgxUj6EFfbHi2Hc9mB401mEAegAJ8XuwL8KmcAAhli2kh/5ah8RUjzCjRAlxEtM6YYadILnbQ+A0mEGA71aWDvjvsJywarCZ20Fgywk4WhKEF+GVRFCnDTsDsPWKLl3JPusUb86OYZKden5ysQEjCcxJQk3DCsDOoQGjuxctLgpTuJRYNVpOTPmi8D8Yp8fz58zOaJcKYSgepBFKBUHT7t+J+XzGMBiLZisjFX3/9dUN7MnwTjCFDos2YkZd0E+kw3wukonyBkInICywmOduuC7ZdG/qD58FgDAl0oTPwZdGTg77HBFLBCrjgWTKJX8jrQaN+48pWBkxtMPoMwpiwYvTuoO8pgSjls6Xls435URS4/o7rD/qg8clg9BEK8HnBLItNpWccIpAKFQqr44rnnDOMBkLZYlrIv2U4KEbYYCj36UtshvWSYwRSgVBmrCgrui+wmGywC54hl4FwdDC6CPmhdYa+mTpj2GtIrsYxKSeItcSvTwZz0Ng4GF0AYegW47Lo0EGfKy4CqWCLmeIT3WKOGUYDoawQihbyO4a9xDkYsSAJzkkATYIJw8HgQyCKTh5su1K6l1g0EMkO0/qklweNXoLRJrzmPKNZIoypDBBfAqlAKLr9XOGvVwyjgUi2Qg=="); // package-variant.png
        caseInflaterInputStreamBinary("ITxhcmNoPgovICAgICAgICAgICAgICAgLTEgICAgICAgICAgICAgICAgICAgICAgMCAgICAgICAxNjAgICAgICAgYAoAAAAFAAACHAAABG4AAAWuAAAHHAAABxxfX0lNUE9SVF9ERVNDUklQVE9SX2JsZW5kZXJfY3B1X2NoZWNrAF9fTlVMTF9JTVBPUlRfREVTQ1JJUFRPUgB/YmxlbmRlcl9jcHVfY2hlY2tfTlVMTF9USFVOS19EQVRBAF9faW1wX2NwdV9jaGVja193aW4zMgBjcHVfY2hlY2tfd2luMzIALyAgICAgICAgICAgICAgIC0xICAgICAgICAgICAgICAgICAgICAgIDAgICAgICAgMTcwICAgICAgIGAKBAAAABwCAABuBAAArgUAABwHAAAFAAAAAQACAAQABAADAF9fSU1QT1JUX0RFU0NSSVBUT1JfYmxlbmRlcl9jcHVfY2hlY2sAX19OVUxMX0lNUE9SVF9ERVNDUklQVE9SAF9faW1wX2NwdV9jaGVja193aW4zMgBjcHVfY2hlY2tfd2luMzIAf2JsZW5kZXJfY3B1X2NoZWNrX05VTExfVEhVTktfREFUQQAvLyAgICAgICAgICAgICAgLTEgICAgICAgICAgICAgICAgICAgICAgMCAgICAgICAyMiAgICAgICAgYApibGVuZGVyX2NwdV9jaGVjay5kbGwALzAgICAgICAgICAgICAgIC0xICAgICAgICAgICAgICAgICAgICAgIDAgICAgICAgNTMzICAgICAgIGAKZIYDAFzHMdMfAQAACAAAAAAAAAAuZGVidWckUwAAAAAAAAAASwAAAIwAAAAAAAAAAAAAAAAAAABAABBCLmlkYXRhJDIAAAAAAAAAABQAAADXAAAA6wAAAAAAAAADAAAAQAAwwC5pZGF0YSQ2AAAAAAAAAAAWAAAACQEAAOsAAAAAAAAAAAAAAEAAIMACAAAAHAAJAAAAAAAVYmxlbmRlcl9jcHVfY2hlY2suZGxsJwATEAcAAADQAAAAAAAAAA4AHQDEdRJNaWNyb3NvZnQgKFIpIExJTksAAAAAAAAAAAAAAAAAAAAAAAAAAAwAAAADAAAAAwAAAAAABAAAAAMAEAAAAAUAAAADAGJsZW5kZXJfY3B1X2NoZWNrLmRsbABAY29tcC5pZMR1AQH//wAAAwAAAAAABAAAAAAAAAACAAAAAgAuaWRhdGEkMkAAAMACAAAAaAAuaWRhdGEkNgAAAAADAAAAAwAuaWRhdGEkNEAAAMAAAAAAaAAuaWRhdGEkNUAAAMAAAAAAaAAAAAAAKgAAAAAAAAAAAAAAAgAAAAAAQwAAAAAAAA=="); // blender_cpu_check.lib
    }

    @Test
    public void caseInflaterInputStream3() throws IOException {
        caseInflaterInputStreamUTF8("[\"aliceblue\",\"antiquewhite\",\"aqua\",\"aquamarine\",\"azure\",\"beige\",\"bisque\",\"black\",\"blanchedalmond\",\"blue\",\"blueviolet\",\"brown\",\"burlywood\",\"cadetblue\",\"chartreuse\",\"chocolate\",\"coral\",\"cornflowerblue\",\"cornsilk\",\"crimson\",\"cyan\",\"darkblue\",\"darkcyan\",\"darkgoldenrod\",\"darkgray\",\"darkgreen\",\"darkgrey\",\"darkkhaki\",\"darkmagenta\",\"darkolivegreen\",\"darkorange\",\"darkorchid\",\"darkred\",\"darksalmon\",\"darkseagreen\",\"darkslateblue\",\"darkslategray\",\"darkslategrey\",\"darkturquoise\",\"darkviolet\",\"deeppink\",\"deepskyblue\",\"dimgray\",\"dimgrey\",\"dodgerblue\",\"firebrick\",\"floralwhite\",\"forestgreen\",\"fuchsia\",\"gainsboro\",\"ghostwhite\",\"gold\",\"goldenrod\",\"gray\",\"green\",\"greenyellow\",\"grey\",\"honeydew\",\"hotpink\",\"indianred\",\"indigo\",\"ivory\",\"khaki\",\"lavender\",\"lavenderblush\",\"lawngreen\",\"lemonchiffon\",\"lightblue\",\"lightcoral\",\"lightcyan\",\"lightgoldenrodyellow\",\"lightgray\",\"lightgreen\",\"lightgrey\",\"lightpink\",\"lightsalmon\",\"lightseagreen\",\"lightskyblue\",\"lightslategray\",\"lightslategrey\",\"lightsteelblue\",\"lightyellow\",\"lime\",\"limegreen\",\"linen\",\"magenta\",\"maroon\",\"mediumaquamarine\",\"mediumblue\",\"mediumorchid\",\"mediumpurple\",\"mediumseagreen\",\"mediumslateblue\",\"mediumspringgreen\",\"mediumturquoise\",\"mediumvioletred\",\"midnightblue\",\"mintcream\",\"mistyrose\",\"moccasin\",\"navajowhite\",\"navy\",\"oldlace\",\"olive\",\"olivedrab\",\"orange\",\"orangered\",\"orchid\",\"palegoldenrod\",\"palegreen\",\"paleturquoise\",\"palevioletred\",\"papayawhip\",\"peachpuff\",\"peru\",\"pink\",\"plum\",\"powderblue\",\"purple\",\"rebeccapurple\",\"red\",\"rosybrown\",\"royalblue\",\"saddlebrown\",\"salmon\",\"sandybrown\",\"seagreen\",\"seashell\",\"sienna\",\"silver\",\"skyblue\",\"slateblue\",\"slategray\",\"slategrey\",\"snow\",\"springgreen\",\"steelblue\",\"tan\",\"teal\",\"thistle\",\"tomato\",\"turquoise\",\"violet\",\"wheat\",\"white\",\"whitesmoke\",\"yellow\",\"yellowgreen\"]");
        caseInflaterInputStreamUTF8("#Inkscape page sizes\r\n#NAME, WIDTH, HEIGHT, UNIT\r\nA4, 210, 297, mm\r\nUS Letter, 8.5, 11, in\r\nUS Legal, 8.5, 14, in\r\nUS Executive, 7.25, 10.5, in\r\nA0, 841, 1189, mm\r\nA1, 594, 841, mm\r\nA2, 420, 594, mm\r\nA3, 297, 420, mm\r\nA5, 148, 210, mm\r\nA6, 105, 148, mm\r\nA7, 74, 105, mm\r\nA8, 52, 74, mm\r\nA9, 37, 52, mm\r\nA10, 26, 37, mm\r\nB0, 1000, 1414, mm\r\nB1, 707, 1000, mm\r\nB2, 500, 707, mm\r\nB3, 353, 500, mm\r\nB4, 250, 353, mm\r\nB5, 176, 250, mm\r\nB6, 125, 176, mm\r\nB7, 88, 125, mm\r\nB8, 62, 88, mm\r\nB9, 44, 62, mm\r\nB10, 31, 44, mm\r\nC0, 917, 1297, mm\r\nC1, 648, 917, mm\r\nC2, 458, 648, mm\r\nC3, 324, 458, mm\r\nC4, 229, 324, mm\r\nC5, 162, 229, mm\r\nC6, 114, 162, mm\r\nC7, 81, 114, mm\r\nC8, 57, 81, mm\r\nC9, 40, 57, mm\r\nC10, 28, 40, mm\r\nD1, 545, 771, mm\r\nD2, 385, 545, mm\r\nD3, 272, 385, mm\r\nD4, 192, 272, mm\r\nD5, 136, 192, mm\r\nD6, 96, 136, mm\r\nD7, 68, 96, mm\r\nE3, 400, 560, mm\r\nE4, 280, 400, mm\r\nE5, 200, 280, mm\r\nE6, 140, 200, mm\r\nCSE, 462, 649, pt\r\nUS #10 Envelope, 9.5, 4.125, in\r\nDL Envelope, 220, 110, mm\r\nLedger/Tabloid, 11, 17, in\r\nBanner 468x60, 468, 60, px\r\nIcon 16x16, 16, 16, px\r\nIcon 32x32, 32, 32, px\r\nIcon 48x48, 48, 48, px\r\nID Card (ISO 7810), 85.60, 53.98, mm\r\nBusiness Card (US), 3.5, 2, in\r\nBusiness Card (Europe), 85, 55, mm\r\nBusiness Card (Aus/NZ), 90, 55, mm\r\nArch A, 9, 12, in\r\nArch B, 12, 18, in\r\nArch C, 18, 24, in\r\nArch D, 24, 36, in\r\nArch E, 36, 48, in\r\nArch E1, 30, 42, in\r\nVideo SD / PAL, 768, 576, px\r\nVideo SD-Widescreen / PAL, 1024, 576, px\r\nVideo SD / NTSC, 544, 480, px\r\nVideo SD-Widescreen / NTSC, 872, 486, px\r\nVideo HD 720p, 1280, 720, px\r\nVideo HD 1080p, 1920, 1080, px\r\nVideo DCI 2k (Full Frame), 2048, 1080, px\r\nVideo UHD 4k, 3840, 2160, px\r\nVideo DCI 4k (Full Frame), 4096, 2160, px\r\nVideo UHD 8k, 7680, 4320, px\r\n");
        caseInflaterInputStreamUTF8("PassengerId,Survived,Pclass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked\r\n1,0,3,\"Braund, Mr. Owen Harris\",male,22,1,0,A/5 21171,7.25,,S\r\n2,1,1,\"Cumings, Mrs. John Bradley (Florence Briggs Thayer)\",female,38,1,0,PC 17599,71.2833,C85,C\r\n3,1,3,\"Heikkinen, Miss Laina\",female,26,0,0,STON/O2. 3101282,7.925,,S\r\n4,1,1,\"Futrelle, Mrs. Jacques Heath (Lily May Peel)\",female,35,1,0,113803,53.1,C123,S\r\n5,0,3,\"Allen, Mr. William Henry\",male,35,0,0,373450,8.05,,S\r\n6,0,3,\"Moran, Mr. James\",male,,0,0,330877,8.4583,,Q\r\n7,0,1,\"McCarthy, Mr. Timothy J\",male,54,0,0,17463,51.8625,E46,S\r\n8,0,3,\"Palsson, Master Gosta Leonard\",male,2,3,1,349909,21.075,,S\r\n9,1,3,\"Johnson, Mrs. Oscar W (Elisabeth Vilhelmina Berg)\",female,27,0,2,347742,11.1333,,S\r\n10,1,2,\"Nasser, Mrs. Nicholas (Adele Achem)\",female,14,1,0,237736,30.0708,,C\r\n11,1,3,\"Sandstrom, Miss Marguerite Rut\",female,4,1,1,PP 9549,16.7,G6,S\r\n12,1,1,\"Bonnell, Miss Elizabeth\",female,58,0,0,113783,26.55,C103,S\r\n13,0,3,\"Saundercock, Mr. William Henry\",male,20,0,0,A/5. 2151,8.05,,S\r\n14,0,3,\"Andersson, Mr. Anders Johan\",male,39,1,5,347082,31.275,,S\r\n15,0,3,\"Vestrom, Miss Hulda Amanda Adolfina\",female,14,0,0,350406,7.8542,,S\r\n16,1,2,\"Hewlett, Mrs. (Mary D Kingcome) \",female,55,0,0,248706,16,,S\r\n17,0,3,\"Rice, Master Eugene\",male,2,4,1,382652,29.125,,Q\r\n18,1,2,\"Williams, Mr. Charles Eugene\",male,,0,0,244373,13,,S\r\n19,0,3,\"Vander Planke, Mrs. Julius (Emelia Maria Vandemoortele)\",female,31,1,0,345763,18,,S\r\n20,1,3,\"Masselmani, Mrs. Fatima\",female,,0,0,2649,7.225,,C\r\n21,0,2,\"Fynney, Mr. Joseph J\",male,35,0,0,239865,26,,S\r\n22,1,2,\"Beesley, Mr. Lawrence\",male,34,0,0,248698,13,D56,S\r\n23,1,3,\"McGowan, Miss Anna \"\"Annie\"\"\",female,15,0,0,330923,8.0292,,Q\r\n24,1,1,\"Sloper, Mr. William Thompson\",male,28,0,0,113788,35.5,A6,S\r\n25,0,3,\"Palsson, Miss Torborg Danira\",female,8,3,1,349909,21.075,,S\r\n26,1,3,\"Asplund, Mrs. Carl Oscar (Selma Augusta Emilia Johansson)\",female,38,1,5,347077,31.3875,,S");
        caseInflaterInputStreamUTF8("<!ATTLIST web-app id ID #IMPLIED><!ATTLIST icon id ID #IMPLIED><!ATTLIST small-icon id ID #IMPLIED><!ATTLIST large-icon id ID #IMPLIED><!ATTLIST display-name id ID #IMPLIED><!ATTLIST description id ID #IMPLIED><!ATTLIST distributable id ID #IMPLIED><!ATTLIST context-param id ID #IMPLIED><!ATTLIST param-name id ID #IMPLIED><!ATTLIST param-value id ID #IMPLIED><!ATTLIST servlet id ID #IMPLIED><!ATTLIST servlet-name id ID #IMPLIED><!ATTLIST servlet-class id ID #IMPLIED><!ATTLIST jsp-file id ID #IMPLIED><!ATTLIST init-param id ID #IMPLIED><!ATTLIST load-on-startup id ID #IMPLIED><!ATTLIST servlet-mapping id ID #IMPLIED><!ATTLIST url-pattern id ID #IMPLIED><!ATTLIST session-config id ID #IMPLIED><!ATTLIST session-timeout id ID #IMPLIED><!ATTLIST mime-mapping id ID #IMPLIED><!ATTLIST extension id ID #IMPLIED><!ATTLIST mime-type id ID #IMPLIED><!ATTLIST welcome-file-list id ID #IMPLIED><!ATTLIST welcome-file id ID #IMPLIED><!ATTLIST taglib id ID #IMPLIED><!ATTLIST taglib-uri id ID #IMPLIED><!ATTLIST taglib-location id ID #IMPLIED><!ATTLIST error-page id ID #IMPLIED><!ATTLIST error-code id ID #IMPLIED><!ATTLIST exception-type id ID #IMPLIED><!ATTLIST location id ID #IMPLIED><!ATTLIST resource-ref id ID #IMPLIED><!ATTLIST res-ref-name id ID #IMPLIED><!ATTLIST res-type id ID #IMPLIED><!ATTLIST res-auth id ID #IMPLIED><!ATTLIST security-constraint id ID #IMPLIED><!ATTLIST web-resource-collection id ID #IMPLIED><!ATTLIST web-resource-name id ID #IMPLIED><!ATTLIST http-method id ID #IMPLIED><!ATTLIST user-data-constraint id ID #IMPLIED><!ATTLIST transport-guarantee id ID #IMPLIED><!ATTLIST auth-constraint id ID #IMPLIED><!ATTLIST role-name id ID #IMPLIED><!ATTLIST login-config id ID #IMPLIED><!ATTLIST realm-name id ID #IMPLIED><!ATTLIST form-login-config id ID #IMPLIED><!ATTLIST form-login-page id ID #IMPLIED><!ATTLIST form-error-page id ID #IMPLIED>");
        caseInflaterInputStreamUTF8("BEGIN; CREATE TABLE logging_event ( timestmp BIGINT NOT NULL, formatted_message TEXT NOT NULL, logger_name VARCHAR(254) NOT NULL, level_string VARCHAR(254) NOT NULL, thread_name VARCHAR(254), reference_flag SMALLINT, arg0 VARCHAR(254), arg1 VARCHAR(254), arg2 VARCHAR(254), arg3 VARCHAR(254), caller_filename VARCHAR(254) NOT NULL, caller_class VARCHAR(254) NOT NULL, caller_method VARCHAR(254) NOT NULL, caller_line CHAR(4) NOT NULL, event_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ); COMMIT; BEGIN; CREATE TABLE logging_event_property ( event_id BIGINT NOT NULL, mapped_key VARCHAR(254) NOT NULL, mapped_value TEXT, PRIMARY KEY(event_id, mapped_key), FOREIGN KEY (event_id) REFERENCES logging_event(event_id) ); COMMIT; BEGIN; CREATE TABLE logging_event_exception ( event_id BIGINT NOT NULL, i SMALLINT NOT NULL, trace_line VARCHAR(254) NOT NULL, PRIMARY KEY(event_id, i), FOREIGN KEY (event_id) REFERENCES logging_event(event_id) ); COMMIT;");
        caseInflaterInputStreamUTF8("{\"schema\":6,\"addons\":[{\"id\":\"{5caff8cc-3d2e-4110-a88a-003cc85b3858}\",\"icons\":{\"32\":\"https://addons.mozilla.org/user-media/addon_icons/823/823463-32.png?modified=mcrushed\",\"64\":\"https://addons.mozilla.org/user-media/addon_icons/823/823463-64.png?modified=mcrushed\",\"128\":\"https://addons.mozilla.org/user-media/addon_icons/823/823463-128.png?modified=mcrushed\"},\"name\":\"Vue.js devtools\",\"version\":\"6.6.3\",\"sourceURI\":\"https://addons.mozilla.org/firefox/downloads/file/4297952/vue_js_devtools-6.6.3.xpi\",\"homepageURL\":\"https://devtools.vuejs.org\",\"supportURL\":\"https://github.com/vuejs/vue-devtools/issues\",\"amoListingURL\":\"https://addons.mozilla.org/zh-CN/firefox/addon/vue-js-devtools/\",\"description\":\"DevTools extension for debugging Vue.js applications.\",\"fullDescription\":\"DevTools for debugging Vue.js applications.\",\"weeklyDownloads\":1318,\"type\":\"extension\",\"creator\":{\"name\":\"Evan You\",\"url\":\"https://addons.mozilla.org/zh-CN/firefox/user/13100848/\"},\"developers\":[{\"name\":\"Akryum\",\"url\":\"https://addons.mozilla.org/zh-CN/firefox/user/13723452/\"}],\"screenshots\":[{\"url\":\"https://addons.mozilla.org/user-media/previews/full/266/266310.png?modified=1644446571\",\"width\":1270,\"height\":476,\"thumbnailURL\":\"https://addons.mozilla.org/user-media/previews/thumbs/266/266310.jpg?modified=1644446571\",\"thumbnailWidth\":533,\"thumbnailHeight\":200},{\"url\":\"https://addons.mozilla.org/user-media/previews/full/266/266311.png?modified=1644446571\",\"width\":1270,\"height\":476,\"thumbnailURL\":\"https://addons.mozilla.org/user-media/previews/thumbs/266/266311.jpg?modified=1644446571\",\"thumbnailWidth\":533,\"thumbnailHeight\":200},{\"url\":\"https://addons.mozilla.org/user-media/previews/full/266/266312.png?modified=1644446571\",\"width\":1270,\"height\":476,\"thumbnailURL\":\"https://addons.mozilla.org/user-media/previews/thumbs/266/266312.jpg?modified=1644446571\",\"thumbnailWidth\":533,\"thumbnailHeight\":200}],\"contributionURL\":\"https://github.com/sponsors/webfansplz?utm_content=product-page-contribute&utm_medium=referral&utm_source=addons.mozilla.org\",\"averageRating\":4.718,\"reviewCount\":139,\"reviewURL\":\"https://addons.mozilla.org/zh-CN/firefox/addon/vue-js-devtools/reviews/\",\"updateDate\":1717754644000}]}");
        caseInflaterInputStreamUTF8("NewTabPage, accessibility, account_tracker_service_last_update, alternate_error_pages, announcement_notification_service_first_run_time, apps, autocomplete, autofill, bookmark, bookmark_bar, bookmark_editor, bookmarks, browser, cached_fonts, chrome, commerce_daily_metrics_last_update_time, countryid_at_install, data_sharing, default_apps_install_state, default_search_provider, default_search_provider_data, devtools, domain_diversity, download, download_bubble, dual_layer_user_pref_store, enterprise_profile_guid, extensions, feedv2, gaia_cookie, gcm, google, history_clusters, https_upgrade_navigations, import_dialog_autofill_form_data, import_dialog_history, import_dialog_saved_passwords, in_product_help, intl, invalidation, language_model_counters, login_detection, media, media_router, migrated_user_scripts_toggle, net, ntp, omnibox, optimization_guide, partition, password_manager, passwords_pref_with_new_label_used, prefetch, printing, privacy_guide, privacy_sandbox, profile, protection, reading_list, safebrowsing, safety_hub, saved_tab_groups, savefile, search, segmentation_platform, selectfile, sessions, settings, shopping_list_bookmark_last_update_time, should_read_incoming_syncing_theme_prefs, side_panel, signin, site_search_settings, spellcheck, supervised_user, sync, syncing_theme_prefs_migrated_to_non_syncing, tab_group_saves_ui_update_migrated, tab_groups, tab_search, toolbar, total_passwords_available_for_account, total_passwords_available_for_profile, tracking_protection, translate, translate_accepted_count, translate_allowlists, translate_blocked_languages, translate_denied_count_for_language, translate_ignored_count_for_language, translate_recent_target, translate_site_blacklist, translate_site_blacklist_with_time, translate_site_blocklist_with_time, updateclientdata, updateclientlastupdatecheckerror, updateclientlastupdatecheckerrorcategory, updateclientlastupdatecheckerrorextracode1, web_app_install_metrics, web_apps, webkit, zerosuggest");
    }

    @Test
    public void caseInflaterInputStream4() throws IOException {
        caseInflaterInputStreamUTF8("在宇宙诞生的最初三分钟，光与物质尚未分离。原初的等离子汤中，夸克与胶子如沸腾的熔岩般翻滚，光子被囚禁于这混沌的牢笼——直到大爆炸后约38万年，随着宇宙在膨胀中逐渐冷却，电子与质子结合为中性原子，光子终于挣脱束缚，化作宇宙微波背景辐射中永恒的光之涟漪。但在这史诗般的“黎明”诞生之前，另一种更古老的物质早已悄然编织宇宙的脉络。它们是暗物质——没有电荷、不与电磁波对话的幽灵，在物质与辐射解耦前的极早期宇宙中，便已挣脱热力学枷锁。这些冰冷的粒子以绝对零度般的沉寂，渗入时空的褶皱，用引力在虚空中刻下第一道伤痕。");
        caseInflaterInputStreamUTF8("马尔可夫凭借自己的渊博知识，注重发展学生的数学思维。他经常有意略去教科书中的传统题材，加入一些自己的最新研究成果。因此理解能力一般水平的学生抱怨难懂，但优秀学生从中受益匪浅。据马尔可夫的学生回忆，高年级的学生成功通过马尔可夫的课程后，还再来听他讲第二遍，以求更佳效果。另一学生认为，马尔可夫的讲课内容几乎与教学内容相去甚远，我们几乎忘记了二次方程的求解，但感到已成为马尔可夫的孩子，他完全控制了我们的思维和意志。");
        caseInflaterInputStreamUTF8("冰川是冰冻圈的三大组分之一，而冰冻圈是地球表层和气候系统的重要组成部分，对全球气候变化起到至关重要的作用。冰川通过冰雪反射、融化以及水循环影响全球气候，这也是冰川之于人类最重要的作用之一。所以尽管冰川地区降水量极低，冰川对周围环境的水文作用却非常重要。比如，青藏高原的积雪异常对东亚大气环流、印度降水以及长江中下游梅雨季节都有着相当的影响。我们看看亚欧大陆与青藏高原同纬度的地区，非洲是撒哈拉沙漠，中东是阿拉伯沙漠，而到中国和印度，则是郁郁葱葱的平原和丘陵，这离不开青藏高原的冰川雪水。在青藏高原三江源地区，长江、黄河、澜沧江三江发源，汩汩流淌分别形成了中国人民和印度人民的母亲河，哺育了两大文明、万千生灵。");
        caseInflaterInputStreamUTF8("豪俊昔未遇，白日无光辉。隆中卧龙客，长啸视群儿。九州英雄争着鞭，黄星午夜照中原。君看慷慨有心者，乃是山东高帝孙。老瞒赤壁抱马走，紫髯江左空回首。世上男儿能几人，眼看袁吕真何有。永安受诏堪垂涕，手挈庸儿是天意。渭上空张复汉旗，蜀民已哭归师至。堂堂八阵竟何为，长安不见汉官仪。邓艾老翁夸至计，谯周鼠子辨兴衰。梁父吟，君听取，击节高节为君舞。躬耕贫贱志功名，功名入手亡中路。逢时儿女各称雄，运去英雄非历数。梁父吟，悲复悲。古今人事半如此，所以达士观如遗。庞公可是无心者，何事鹿门招不归。");
    }

    private static final int REPEAT = 45;

    private static final int SIZE = 92000;

    private final FuzzyContext fc = new FuzzyContext();

    @Test
    public void fuzzInflaterInputStream() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            byte[] expected = fc.nextByteArray(size);
            bos.reset();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bos, new Deflater(Deflater.DEFAULT_COMPRESSION, true))) {
                dos.write(expected);
                dos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            try (TowboatInflaterInputStream dis = new TowboatInflaterInputStream(new ByteArrayInputStream(compressed))) {
                actual = dis.readNBytes(size);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, dis.read());
            } catch (Throwable e) {
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(expected)));
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(actual)));
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(compressed)));
                throw e;
            }
        }
    }

    @Test
    public void fuzzZlibInputStream() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            byte[] expected = fc.nextByteArray(size);
            bos.reset();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bos)) {
                dos.write(expected);
                dos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            try (TowboatZlibInputStream dis = new TowboatZlibInputStream(new ByteArrayInputStream(compressed), CompatibleIntMap::new, CyclicLookBackMemory::new)) {
                actual = dis.readNBytes(size);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, dis.read());
            } catch (Throwable e) {
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(expected)));
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(actual)));
                System.out.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(compressed)));
                throw e;
            }
        }
    }
}
