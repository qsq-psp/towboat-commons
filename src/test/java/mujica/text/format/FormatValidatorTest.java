package mujica.text.format;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.junit.Assert;
import org.junit.Test;

@CodeHistory(date = "2025/9/15")
@SuppressWarnings("SpellCheckingInspection")
public class FormatValidatorTest {

    @Test
    public void testCharsetName1() {
        final FormatValidator validator = CharsetNameValidator.INSTANCE;
        Assert.assertTrue(validator.test("ASCII"));
        Assert.assertTrue(validator.test("ISO646-US"));
        Assert.assertTrue(validator.test("ISO-LATIN-1"));
        Assert.assertTrue(validator.test("UTF-8"));
        Assert.assertTrue(validator.test("utf-8"));
    }

    @Test
    public void testCharsetName2() {
        final FormatValidator validator = CharsetNameValidator.INSTANCE;
        Assert.assertFalse(validator.test("American Standard Code for Information Interchange"));
        Assert.assertFalse(validator.test("UTF-16 BE"));
        Assert.assertFalse(validator.test("UTF-16 LE"));
    }

    @Test
    public void testDomainName1() {
        final FormatValidator validator = DomainNameValidator.INSTANCE;
        Assert.assertTrue(validator.test("localhost"));
        Assert.assertTrue(validator.test("www.rfc-editor.org"));
        Assert.assertTrue(validator.test("www.apache.org"));
        Assert.assertTrue(validator.test("www.w3school.com.cn"));
        Assert.assertTrue(validator.test("www.xz7.com"));
        Assert.assertTrue(validator.test("shields.io"));
        Assert.assertTrue(validator.test("b23.tv"));
        Assert.assertTrue(validator.test("junit.org"));
        Assert.assertTrue(validator.test("vscodeexperiments.azureedge.net"));
        Assert.assertTrue(validator.test("inkscape-manuals.readthedocs.io"));
        Assert.assertTrue(validator.test("cc-api-data.adobe.io"));
    }

    @Test
    public void testDomainName2() {
        final FormatValidator validator = DomainNameValidator.INSTANCE;
        Assert.assertTrue(validator.test("[127.0.0.1]"));
        Assert.assertTrue(validator.test("[192.168.1.2]"));
        Assert.assertTrue(validator.test("[43.129.115.16]"));
        Assert.assertTrue(validator.test("[14.22.9.100]"));
        // Assert.assertTrue(validator.test("[240E:904:3401:1001::E]"));
        // Assert.assertTrue(validator.test("[2409:8C4C:C00:1311:3B::13]"));
        Assert.assertTrue(validator.test("#233"));
    }

    @Test
    public void testDomainName3() {
        final FormatValidator validator = DomainNameValidator.INSTANCE;
        Assert.assertFalse(validator.test("p.qlogo.cn"));
        Assert.assertFalse(validator.test("i.pixiv.cat"));
        Assert.assertFalse(validator.test("i.mi.com"));
        Assert.assertFalse(validator.test("music.163.com"));
    }

    @Test
    public void testDomainName4() {
        final FormatValidator validator = DomainNameValidator.INSTANCE;
        Assert.assertFalse(validator.test("[7.100..3]"));
        Assert.assertFalse(validator.test("[24.08.39.133]"));
        Assert.assertFalse(validator.test("[004.67.68.69]"));
        Assert.assertFalse(validator.test("[8.97.270.33]"));
        Assert.assertFalse(validator.test("[4%.220.218.177]"));
        Assert.assertFalse(validator.test("[45.90.136]"));
        Assert.assertFalse(validator.test("[82.197.199.0.52]"));
    }

    @Test
    public void testIPV4Address1() {
        final FormatValidator validator = IPV4AddressValidator.INSTANCE;
        Assert.assertTrue(validator.test("216.146.35.35"));
        Assert.assertTrue(validator.test("8.8.8.8"));
        Assert.assertTrue(validator.test("1.1.1.1"));
        Assert.assertTrue(validator.test("10.10.0.21"));
    }

    @Test
    public void testIPV4Address2() {
        final FormatValidator validator = IPV4AddressValidator.INSTANCE;
        Assert.assertFalse(validator.test("4.7.3."));
        Assert.assertFalse(validator.test(".2.1.9"));
        Assert.assertFalse(validator.test("0203.205.137.76"));
        Assert.assertFalse(validator.test("203.201.197.077"));
        Assert.assertFalse(validator.test("101.003.47.68"));
        Assert.assertFalse(validator.test("00120.232.65.242"));
        Assert.assertFalse(validator.test("120.-232.23.143"));
        Assert.assertFalse(validator.test("28"));
        Assert.assertFalse(validator.test("76.91"));
        Assert.assertFalse(validator.test("101.32.113"));
        Assert.assertFalse(validator.test("52.83.193.114.7"));
        Assert.assertFalse(validator.test("52.83.193.114.7"));
        Assert.assertFalse(validator.test("29.35.185.44.232.30"));
    }

    @Test
    public void testWindowsFileName1() {
        final FormatValidator validator = WindowsFileNameValidator.INSTANCE;
        Assert.assertTrue(validator.test("be682ca9-3475-4c0c-bd1f-4532d3315e92.jpg"));
        Assert.assertTrue(validator.test("py.exe"));
        Assert.assertTrue(validator.test("desktop.ini"));
        Assert.assertTrue(validator.test("@windows-hello-V4.1.gif"));
        Assert.assertTrue(validator.test("tomcat-i18n-zh-CN.jar"));
        Assert.assertTrue(validator.test("__init__.cpython-38.pyc"));
        Assert.assertTrue(validator.test("good night.txt"));
        Assert.assertTrue(validator.test(".bash_history"));
        Assert.assertTrue(validator.test("NTUSER.DAT{e0095173-8afb-11eb-b13e-8efb84361009}.TM.blf"));
        Assert.assertTrue(validator.test("普通数据.csv"));
    }

    @Test
    @ReferencePage(title = "Lorem ipsum", href = "https://www.zhihu.com/question/19708165/answer/1929610818628068504")
    public void testWindowsFileName2() {
        final FormatValidator validator = WindowsFileNameValidator.INSTANCE;
        Assert.assertFalse(validator.test("?.jpeg"));
        Assert.assertFalse(validator.test("@3w/pc.zip"));
        Assert.assertFalse(validator.test("less*.pdf"));
        Assert.assertFalse(validator.test("NUL"));
        Assert.assertFalse(validator.test("foo."));
        Assert.assertFalse(validator.test("Ancient_Caldera|Archipelago|Debris_Field"));
        Assert.assertFalse(validator.test("Stop:0x0d000721(0xc000000000000001,0x0000000000000419)"));
        Assert.assertFalse(validator.test(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore " +
                        "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                        "nisi ut aliquip ex ea commodo consequat.Duis aute irure dolor in reprehenderit in voluptate " +
                        "velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " +
                        "proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        ));
        Assert.assertFalse(validator.test(
                "劳仑衣普桑，认至将指点效则机，最你更枝。想极整月正进好志次回总般，段然取向使张规军证回，世市总李率英茄持伴。用阶千样响领交出，" +
                        "器程办管据家元写，名其直金团。化达书据始价算每百青，金低给天济办作照明，取路豆学丽适市确。如提单各样备再成农各政，" +
                        "设头律走克美技说没，体交才路此在杠。响育油命转处他住有，一须通给对非交矿今该，花象更面据压来。与花断第然调，" +
                        "很处己队音，程承明邮。常系单要外史按机速引也书，个此少管品务美直管战，子大标蠢主盯写族般本。农现离门亲事以响规，" +
                        "局观先示从开示，动和导便命复机李，办队呆等需杯。见何细线名必子适取米制近，内信时型系节新候节好当我，" +
                        "队农否志杏空适花。又我具料划每地，对算由那基高放，育天孝。派则指细流金义月无采列，走压看计和眼提问接，" +
                        "作半极水红素支花。果都济素各半走，意红接器长标，等杏近乱共。层题提万任号，信来查段格，农张雨。省着素科程建持色被什，" +
                        "所界走置派农难取眼，并细杆至志本。"
        ));
    }

    @Test
    public void testSchemaObjectName1() {
        final FormatValidator validator = SchemaObjectNameValidator.INSTANCE;
        Assert.assertTrue(validator.test("mysql"));
        Assert.assertTrue(validator.test("time_zone"));
        Assert.assertTrue(validator.test("i18n"));
        Assert.assertTrue(validator.test("lock32"));
    }

    @Test
    public void testSchemaObjectName2() {
        final FormatValidator validator = SchemaObjectNameValidator.INSTANCE;
        Assert.assertFalse(validator.test(""));
        Assert.assertFalse(validator.test("8008"));
        Assert.assertFalse(validator.test("git "));
    }

    @Test
    public void testJavaIdentifier1() {
        final FormatValidator validator = JavaIdentifierValidator.INSTANCE;
        Assert.assertTrue(validator.test("Math"));
        Assert.assertTrue(validator.test("ByteArrayOutputStream"));
        Assert.assertTrue(validator.test("JarURLConnection"));
        Assert.assertTrue(validator.test("URI"));
        Assert.assertTrue(validator.test("Base64"));
        Assert.assertTrue(validator.test("UUID"));
        Assert.assertTrue(validator.test("j"));
        Assert.assertTrue(validator.test("String"));
        Assert.assertTrue(validator.test("i3"));
        Assert.assertTrue(validator.test("αρετη"));
        Assert.assertTrue(validator.test("MAX_VALUE"));
        Assert.assertTrue(validator.test("isLetterOrDigit"));
        Assert.assertTrue(validator.test("_lock"));
        Assert.assertTrue(validator.test("$1"));
        Assert.assertTrue(validator.test("Video$Header"));
    }

    @Test
    public void testJavaIdentifier2() {
        final FormatValidator validator = JavaIdentifierValidator.INSTANCE;
        Assert.assertFalse(validator.test(""));
        Assert.assertFalse(validator.test(" "));
        Assert.assertFalse(validator.test("\r\n"));
        Assert.assertFalse(validator.test("dock\r\n"));
        Assert.assertFalse(validator.test("Math2.2"));
        Assert.assertFalse(validator.test("Weak Hash Map"));
        Assert.assertFalse(validator.test("2025goal"));
        Assert.assertFalse(validator.test("@pic"));
        Assert.assertFalse(validator.test("jdk.tools"));
        Assert.assertFalse(validator.test("-dimension"));
        Assert.assertFalse(validator.test("short"));
        Assert.assertFalse(validator.test("true"));
        Assert.assertFalse(validator.test("null"));
        Assert.assertFalse(validator.test("<init>"));
        Assert.assertFalse(validator.test("<clinit>"));
    }

    @Test
    public void testJavaFullyQualifiedName1() {
        final FormatValidator validator = JavaFullyQualifiedNameValidator.SOURCE;
        Assert.assertTrue(validator.test("Main"));
        Assert.assertTrue(validator.test("indi.Starter"));
        Assert.assertTrue(validator.test("javax.sql.DataSource"));
        Assert.assertTrue(validator.test("jdk.nio.Channels"));
        Assert.assertTrue(validator.test("java.io.IOException"));
        Assert.assertTrue(validator.test("com.sun.crypto.provider.ChaCha20Cipher"));
        Assert.assertTrue(validator.test("junit.framework.JUnit4TestAdapter"));
    }

    @Test
    public void testJavaFullyQualifiedName2() {
        final FormatValidator validator = JavaFullyQualifiedNameValidator.SOURCE;
        Assert.assertFalse(validator.test("Main{}"));
        Assert.assertFalse(validator.test("indi.Starter()"));
        Assert.assertFalse(validator.test("javax.sql.DataSource[]"));
        Assert.assertFalse(validator.test("jdk.nio.Channels;"));
        Assert.assertFalse(validator.test("java.io.-IOException"));
        Assert.assertFalse(validator.test("@com.sun.crypto.provider.ChaCha20Cipher"));
        Assert.assertFalse(validator.test("+junit.framework.JUnit4TestAdapter"));
    }
}
