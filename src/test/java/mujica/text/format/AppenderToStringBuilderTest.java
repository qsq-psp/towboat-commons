package mujica.text.format;

import mujica.ds.of_int.PublicIntSlot;
import mujica.ds.of_long.PublicLongSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.BinaryAppender;
import mujica.text.number.HexadecimalAppender;
import mujica.text.number.IntegralAppender;
import mujica.text.number.MarkedIntegralAppender;
import mujica.text.sanitizer.CharSequenceAppender;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("SpellCheckingInspection")
@CodeHistory(date = "2026/1/28")
public class AppenderToStringBuilderTest {

    @BeforeClass
    public static void loadClasses() throws ReflectiveOperationException {
        AppenderToStringBuilder.create();
    }

    @Test
    public void caseDefaultNull() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("null", appender.apply(null));
        Assert.assertEquals("[null]", appender.apply(new Object[] {null}));
        Assert.assertEquals("[null, null]", appender.apply(new Void[] {null, null}));
        Assert.assertEquals("[null, null, null]", appender.apply(new Integer[] {null, null, null}));
        Assert.assertEquals("[null, [null], []]", appender.apply(new Object[] {null, new Object[] {null}, new Object[] {}}));
    }

    @Test
    public void caseConfigNull() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create().setNullString("-");
        Assert.assertEquals("-", appender.apply(null));
        Assert.assertEquals("[-, -]", appender.apply(new Void[] {null, null}));
        Assert.assertEquals("[-, [-], []]", appender.apply(new Object[] {null, new Object[] {null}, new Object[] {}}));
        appender.setNullString("?");
        Assert.assertEquals("[?, ?, ?]", appender.apply(new AtomicInteger[] {null, null, null}));
        Assert.assertEquals("[?, ?, ?]", appender.apply(new BigInteger[] {null, null, null}));
    }

    @Test
    public void caseDefaultBoolean() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("true", appender.apply(true));
        Assert.assertEquals("false", appender.apply(false));
        Assert.assertEquals("[true]", appender.apply(new boolean[] {true}));
        Assert.assertEquals("[false, false]", appender.apply(new boolean[] {false, false}));
        Assert.assertEquals("[false, false, false, true]", appender.apply(new boolean[] {false, false, false, true}));
        Assert.assertEquals("[true, false, false, true]", appender.apply(new Boolean[] {true, false, false, true}));
        Assert.assertEquals("[true, false, true, false]", appender.apply(new Object[] {true, false, true, false}));
    }

    @Test
    public void caseConfigBoolean() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().booleanTrueFalse();
        Assert.assertEquals("[False, False, True]", appender.apply(new boolean[] {false, false, true}));
        appender.config().booleanYesNo();
        Assert.assertEquals("[No, Yes, Yes]", appender.apply(new boolean[] {false, true, true}));
        appender.config().booleanOnOff();
        Assert.assertEquals("[Off, On, Off]", appender.apply(new boolean[] {false, true, false}));
    }

    @Test
    public void caseDefaultByte() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("-1", appender.apply((byte) -1));
        Assert.assertEquals("0", appender.apply((byte) 0));
        Assert.assertEquals("1", appender.apply((byte) 1));
        Assert.assertEquals("[101]", appender.apply(new byte[] {101}));
        Assert.assertEquals("[82, 127]", appender.apply(new byte[] {82, 127}));
        Assert.assertEquals("[-128, -5, 43]", appender.apply(new byte[] {-128, -5, 43}));
        Assert.assertEquals("[32, 33, 39]", appender.apply(new Byte[] {32, 33, 39}));
        Assert.assertEquals("[65, 67, 68]", appender.apply(new Number[] {(byte) 65, (byte) 67, (byte) 68}));
    }

    @Test
    public void caseConfigByte() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().booleanTrueFalse().use(new IntegralAppender());
        Assert.assertEquals("[-128, 127, -1, 1]", appender.apply(new byte[] {-128, 127, -1, 1}));
        appender.config().booleanYesNo().use(new IntegralAppender.Unsigned());
        Assert.assertEquals("[128, 127, 255, 1]", appender.apply(new byte[] {-128, 127, -1, 1}));
        appender.config().booleanOnOff().use(new MarkedIntegralAppender(new IntegralAppender()));
        Assert.assertEquals("(byte) 3", appender.apply((byte) 3));
        Assert.assertEquals("[(byte) 6]", appender.apply(new Number[] {(byte) 6}));
        Assert.assertEquals("[(byte) 1, (byte) 0, (byte) -1, (byte) -128, (byte) 127]", appender.apply(new byte[] {1, 0, -1, -128, 127}));
        appender.config().booleanTrueFalse().use(new MarkedIntegralAppender(new IntegralAppender.Unsigned()));
        Assert.assertEquals("[(byte) 1, (byte) 0, (byte) 255, (byte) 128, (byte) 127]", appender.apply(new byte[] {1, 0, -1, -128, 127}));
        appender.config().booleanYesNo().use(new HexadecimalAppender(false, true));
        Assert.assertEquals("0x00", appender.apply((byte) 0));
        Assert.assertEquals("0x10", appender.apply((byte) 16));
        Assert.assertEquals("0xa0", appender.apply((byte) 160));
        Assert.assertEquals("0xff", appender.apply((byte) 255));
        appender.config().booleanOnOff().use(new HexadecimalAppender(true, false));
        Assert.assertEquals("0x0", appender.apply((byte) 0));
        Assert.assertEquals("0xF", appender.apply((byte) 15));
        Assert.assertEquals("0x40", appender.apply((byte) 64));
        Assert.assertEquals("0xC0", appender.apply((byte) 192));
        Assert.assertEquals("0xFF", appender.apply((byte) 255));
        appender.config().use(new BinaryAppender(false, 1));
        Assert.assertEquals("[0b0, 0b1, 0b1_0, 0b1_1, 0b1_0_0]", appender.apply(new byte[] {0b0, 0b1, 0b1_0, 0b1_1, 0b1_0_0}));
        appender.config().use(new BinaryAppender(false, 2));
        Assert.assertEquals("[0b0, 0b1, 0b10, 0b1_00, 0b11_11]", appender.apply(new byte[] {0b0, 0b1, 0b10, 0b1_00, 0b11_11}));
        appender.config().use(new BinaryAppender(true, 3));
        Assert.assertEquals("[0b00_000_000, 0b00_010_000, 0b01_000_011, 0b10_100_100]", appender.apply(new byte[] {0b00_000_000, 0b00_010_000, 0b01_000_011, (byte) 0b10_100_100}));
        appender.config().use(new BinaryAppender(true, 4));
        Assert.assertEquals("[0b0000_0001, 0b0001_0010, 0b0011_0101, 0b0110_1101]", appender.apply(new byte[] {0b0000_0001, 0b0001_0010, 0b0011_0101, 0b0110_1101}));
    }

    @Test
    public void caseDefaultShort() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("0", appender.apply((short) 0));
        Assert.assertEquals("32767", appender.apply((short) 32767));
        Assert.assertEquals("[-32768]", appender.apply(new short[] {-32768}));
        Assert.assertEquals("[303, 304]", appender.apply(new Short[] {303, 304}));
        Assert.assertEquals("[-3080, 2044]", appender.apply(new Object[] {(short) -3080, (short) 2044}));
    }

    @Test
    public void caseConfigShort() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().booleanTrueFalse().use(new IntegralAppender());
        Assert.assertEquals("[32767, -32768, 8080]", appender.apply(new short[] {32767, -32768, 8080}));
        appender.config().booleanYesNo().use(new IntegralAppender.Unsigned());
        Assert.assertEquals("[32767, 32768, 8080, 65535, 65534]", appender.apply(new short[] {32767, -32768, 8080, -1, -2}));
        appender.config().booleanOnOff().use(new HexadecimalAppender(false, false));
        Assert.assertEquals("[0x0, 0xf, 0x10, 0xff, 0x100, 0xfff, 0x1000, 0xffff]", appender.apply(new short[] {0x0, 0xf, 0x10, 0xff, 0x100, 0xfff, 0x1000, -1}));
        appender.config().booleanTrueFalse().use(new HexadecimalAppender(true, true));
        Assert.assertEquals("[0x0000, 0x000F, 0x0010, 0x00FF, 0x0100, 0x0FFF, 0x1000, 0xFFFF]", appender.apply(new short[] {0x0, 0xf, 0x10, 0xff, 0x100, 0xfff, 0x1000, -1}));
        appender.config().booleanYesNo().use(new MarkedIntegralAppender(new HexadecimalAppender(false, false)));
        Assert.assertEquals("(short) 0x3", appender.apply((short) 3));
        Assert.assertEquals("[(short) 0x6]", appender.apply(new Object[] {(short) 6}));
        Assert.assertEquals("[(short) 0xc, (short) 0x77, (short) 0xaaa, (short) 0xeeee]", appender.apply(new short[] {0xc, 0x77, 0xaaa, (short) 0xeeee}));
        appender.config().booleanOnOff().use(new MarkedIntegralAppender(new HexadecimalAppender(true, true)));
        Assert.assertEquals("[(short) 0x0003, (short) 0x00BB, (short) 0x0AAA, (short) 0xCACA]", appender.apply(new short[] {0x3, 0xbb, 0xaaa, (short) 0xcaca}));
    }

    @Test
    public void caseDefaultChar() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("0", appender.apply('0'));
        Assert.assertEquals("A", appender.apply('A'));
        Assert.assertEquals("[]", appender.apply(new char[] {}));
        Assert.assertEquals("[Z]", appender.apply(new char[] {'Z'}));
        Assert.assertEquals("[a, c, c]", appender.apply(new char[] {'a', 'c', 'c'}));
        Assert.assertEquals("[t, v, x]", appender.apply(new Character[] {'t', 'v', 'x'}));
    }

    @Test
    public void caseConfigCharAsNumber() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().booleanTrueFalse().use(new IntegralAppender());
        Assert.assertEquals("0", appender.apply((char) 0));
        Assert.assertEquals("48", appender.apply((char) 48));
        Assert.assertEquals("128", appender.apply((char) 128));
        Assert.assertEquals("160", appender.apply((char) 160));
        Assert.assertEquals("[]", appender.apply(new char[] {}));
        Assert.assertEquals("[8192, 65533]", appender.apply(new char[] {8192, 65533}));
        appender.config().booleanYesNo().use(new MarkedIntegralAppender(new IntegralAppender()));
        Assert.assertEquals("(char) 97", appender.apply((char) 97));
        Assert.assertEquals("[(char) 10225, (char) 10864]", appender.apply(new char[] {10225, 10864}));
        appender.config().booleanOnOff().use(new HexadecimalAppender(false, false));
        Assert.assertEquals("0xd", appender.apply((char) 0xd));
        Assert.assertEquals("0xcb", appender.apply((char) 0xcb));
        Assert.assertEquals("0xa98", appender.apply((char) 0xa98));
        Assert.assertEquals("0x7654", appender.apply((char) 0x7654));
    }

    @Test
    public void caseDefaultInt() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("-1", appender.apply(-1));
        Assert.assertEquals("0", appender.apply(0));
        Assert.assertEquals("1", appender.apply(1));
        Assert.assertEquals("[4732974]", appender.apply(new int[] {4732974}));
        Assert.assertEquals("[90832, 12789773]", appender.apply(new int[] {90832, 12789773}));
        Assert.assertEquals("[1, -1, 0]", appender.apply(new int[] {1, -1, 0}));
        Assert.assertEquals("[2147483647, -2147483647, -2147483648]", appender.apply(new int[] {2147483647, -2147483647, -2147483648}));
        Assert.assertEquals("[2007491956, -2140482937, -2147443695]", appender.apply(new int[] {2007491956, -2140482937, -2147443695}));
    }

    @Test
    public void caseConfigInt() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().booleanTrueFalse().use(new IntegralAppender());
        Assert.assertEquals("-7", appender.apply(-7));
        Assert.assertEquals("[-121, 50]", appender.apply(new int[] {-121, 50}));
        appender.config().booleanYesNo().use(new IntegralAppender.Unsigned());
        Assert.assertEquals("4294967292", appender.apply(-4));
        Assert.assertEquals("[4294967198, 443]", appender.apply(new int[] {-98, 443}));
        appender.config().booleanOnOff().use(new HexadecimalAppender(false, false));
        Assert.assertEquals("0xa0a00", appender.apply(0xa0a00));
        Assert.assertEquals("[0x9, 0x1e, 0xc22, 0x800f, 0x170af]", appender.apply(new int[] {0x9, 0x1e, 0xc22, 0x800f, 0x170af}));
        appender.config().booleanTrueFalse().use(new HexadecimalAppender(false, true));
        Assert.assertEquals("0x00010001", appender.apply(0x10001));
        Assert.assertEquals("[0x00000000, 0x00000002, 0x40005000, 0xe000d000]", appender.apply(new int[] {0x0, 0x2, 0x40005000, 0xe000d000}));
        appender.config().booleanYesNo().use(new HexadecimalAppender(true, false));
        Assert.assertEquals("0xA0A00", appender.apply(0xa0a00));
        Assert.assertEquals("[0x9, 0x1E, 0xC22, 0x800F, 0x170AF]", appender.apply(new int[] {0x9, 0x1e, 0xc22, 0x800f, 0x170af}));
        appender.config().booleanOnOff().use(new HexadecimalAppender(true, true));
        Assert.assertEquals("0x00010001", appender.apply(0x10001));
        Assert.assertEquals("[0x00000000, 0x00000003, 0x40005000, 0xE000B000]", appender.apply(new int[] {0x0, 0x3, 0x40005000, 0xe000b000}));
        appender.config().use(new BinaryAppender(false, 4));
        Assert.assertEquals("[0b100_1100, 0b11_0000_1001, 0b1_0011_1000]", appender.apply(new int[] {0b100_1100, 0b11_0000_1001, 0b1_0011_1000}));
        appender.config().use(new BinaryAppender(false, 5));
        Assert.assertEquals("[0b1, 0b11, 0b100, 0b1101, 0b11111, 0b1_00000]", appender.apply(new int[] {0b1, 0b11, 0b100, 0b1101, 0b11111, 0b1_00000}));
        appender.config().use(new BinaryAppender(true, 8));
        Assert.assertEquals("0b00000000_01001000_00000000_00000010", appender.apply(0b00000000_01001000_00000000_00000010));
    }

    @Test
    public void caseConfigNumberInt() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().numberIntegral().use(new IntegralAppender());
        Assert.assertEquals("15", appender.apply(new AtomicInteger(15)));
        Assert.assertEquals("188", appender.apply(new PublicIntSlot(188)));
        appender.config().use(new HexadecimalAppender(false, false));
        Assert.assertEquals("0x1c", appender.apply(new AtomicInteger(0x1c)));
        Assert.assertEquals("0x22a", appender.apply(new PublicIntSlot(0x22a)));
    }

    @Test
    public void caseDefaultLong() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        Assert.assertEquals("-1", appender.apply(-1L));
        Assert.assertEquals("0", appender.apply(0L));
        Assert.assertEquals("1", appender.apply(1L));
        Assert.assertEquals("-9223372036854775808", appender.apply(-9223372036854775808L));
        Assert.assertEquals("9223372036854775807", appender.apply(9223372036854775807L));
        Assert.assertEquals("[9023372486854730803]", appender.apply(new long[] {9023372486854730803L}));
        Assert.assertEquals("[8521172486830732517, 8623333336822775013]", appender.apply(new long[] {8521172486830732517L, 8623333336822775013L}));
        Assert.assertEquals("[4374072407430730570, 7713703436807470003, 9193074780854800848]",
                appender.apply(new long[] {4374072407430730570L, 7713703436807470003L, 9193074780854800848L}));
        Assert.assertEquals("[7030764074073650, 6713604368778609, 6430078015402841, 9584775085406719]",
                appender.apply(new long[] {7030764074073650L, 6713604368778609L, 6430078015402841L, 9584775085406719L}));
    }

    @Test
    public void caseConfigLong() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().use(new IntegralAppender());
        Assert.assertEquals("-1", appender.apply(-1L));
        Assert.assertEquals("0", appender.apply(0L));
        Assert.assertEquals("1", appender.apply(1L));
        appender.config().use(new MarkedIntegralAppender(new IntegralAppender()));
        Assert.assertEquals("-1L", appender.apply(-1L));
        Assert.assertEquals("0L", appender.apply(0L));
        Assert.assertEquals("1L", appender.apply(1L));
        appender.config().use(new IntegralAppender.Unsigned());
        Assert.assertEquals("[18446744073709551615, 0, 1, 9223372036854775808]", appender.apply(new long[] {-1, 0, 1, Long.MIN_VALUE}));
        appender.config().use(new MarkedIntegralAppender(new IntegralAppender.Unsigned()));
        Assert.assertEquals("[18446744073709551615L, 0L, 1L, 9223372036854775808L]", appender.apply(new long[] {-1, 0, 1, Long.MIN_VALUE}));
    }

    @Test
    public void caseConfigNumberLong() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().numberIntegral().use(new IntegralAppender());
        Assert.assertEquals("15", appender.apply(new AtomicLong(15)));
        Assert.assertEquals("188", appender.apply(new PublicLongSlot(188)));
        appender.config().use(new HexadecimalAppender(false, false));
        Assert.assertEquals("0x1c", appender.apply(new AtomicLong(0x1c)));
        Assert.assertEquals("0x22a", appender.apply(new PublicLongSlot(0x22a)));
    }

    @Test
    public void caseLoop() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        final Object[] array = new Object[] {false, 12, null, null};
        array[2] = array;
        Assert.assertEquals("[false, 12, loop, null]", appender.apply(array));
        array[2] = new Object[] {array, true, 96};
        Assert.assertEquals("[false, 12, [loop, true, 96], null]", appender.apply(array));
        array[2] = 48;
        array[3] = new Object[] {-60, array, array};
        Assert.assertEquals("[false, 12, 48, [-60, loop, loop]]", appender.apply(array));
        array[3] = new int[0];
        array[0] = new Object[][] {{array, 0, array}};
        Assert.assertEquals("[[[loop, 0, loop]], 12, 48, []]", appender.apply(array));
    }

    @Test
    public void caseConfigLoop() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        final Object[] array = new Object[] {(long) 40, 30, 20, true};
        array[1] = array;
        appender.setNullString("!").config().numberIntegral().use(appender.newArrayStyle().setLoop("%"), Object.class);
        Assert.assertEquals("[!, [40, %, 20, true], !]", appender.apply(new Object[] {null, array, null}));
        appender.setNullString("!!").config().numberDecimal().use(appender.newArrayStyle().setLoop("%%"), Object.class);
        Assert.assertEquals("[[40, %%, 20, true], !!, []]", appender.apply(new Object[] {array, null, new byte[0]}));
        appender.setNullString("_").config().use(appender.newArrayStyle().setLoop("*"), Object.class);
        Assert.assertEquals("[[40, *, 20, true], [], _, _]", appender.apply(new Object[] {array, new float[0], null, null}));
    }

    @Test
    public void caseConfigArrayStyle() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().use(appender.newArrayStyle().setLeft("int[] {").setRight("}"), int[].class);
        Assert.assertEquals("[34, 35, 36]", appender.apply(new byte[] {34, 35, 36}));
        Assert.assertEquals("int[] {34, 35, 36}", appender.apply(new int[] {34, 35, 36}));
        appender.config().use(appender.newArrayStyle().setItemSeparator("; "), long[].class);
        Assert.assertEquals("[234, 235, 236]", appender.apply(new short[] {234, 235, 236}));
        Assert.assertEquals("[234; 235; 236]", appender.apply(new long[] {234, 235, 236}));
    }

    @Test
    public void caseConfigMatrixStyle() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().use(appender.newMatrixStyle(), int[][].class);
        Assert.assertEquals("[[1, 2], [3, 4]]", appender.apply(new byte[][] {{1, 2}, {3, 4}}));
        Assert.assertEquals("[1, 2; 3, 4]", appender.apply(new int[][] {{1, 2}, {3, 4}}));
        appender.config().use(appender.newMatrixStyle().setLeft("(").setRight(")"), boolean[][].class);
        Assert.assertEquals("[45, 46; 47, 48]", appender.apply(new int[][] {{45, 46}, {47, 48}}));
        Assert.assertEquals("[[x, y], [z, w]]", appender.apply(new char[][] {{'x', 'y'}, {'z', 'w'}}));
        Assert.assertEquals("(true, false; false, true)", appender.apply(new boolean[][] {{true, false}, {false, true}}));
    }

    @Test
    public void caseConfigJavaString() {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.Java.get();
        Assert.assertEquals("\"\"", appender.apply(""));
        Assert.assertEquals("\"[101]\"", appender.apply("[101]"));
        Assert.assertEquals("\"get\"", appender.apply("get"));
        Assert.assertEquals("{\"int\", \"out\", \"class\", \"Test\", null}", appender.apply(new String[] {"int", "out", "class", "Test", null}));
    }

    @Test
    public void caseConfigJsonString() {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.Json.get();
        Assert.assertEquals("\"\"", appender.apply(""));
        Assert.assertEquals("\"[101]\"", appender.apply("[101]"));
        Assert.assertEquals("\"get\"", appender.apply("get"));
        Assert.assertEquals("[\"-\", \"xx\", \"$\", \"function\", null]", appender.apply(new String[] {"-", "xx", "$", "function", null}));
        Assert.assertEquals("[\"()\", \"[]\", \"{}\", false, true]", appender.apply(new Object[] {"()", "[]", "{}", false, true}));
        Assert.assertEquals("[\"over\\\\ride\", \"over\\r\\nwrite\", \"\\\"COM\\\"\", \"\\\"'function'\\\"\"]", appender.apply(new String[] {"over\\ride", "over\r\nwrite", "\"COM\"", "\"'function'\""}));
    }

    @Test
    public void caseConfigJavascriptString() {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.JavaScript.get();
        Assert.assertEquals("''", appender.apply(""));
        Assert.assertEquals("'[101]'", appender.apply("[101]"));
        Assert.assertEquals("'apply'", appender.apply("apply"));
        Assert.assertEquals("['void', 'undefined', 'null', 'none', null]", appender.apply(new String[] {"void", "undefined", "null", "none", null}));
        Assert.assertEquals("['true', 'false', true, false]", appender.apply(new Object[] {"true", "false", true, false}));
        Assert.assertEquals("['\\r', '\\n', '\\\\']", appender.apply(new String[] {"\r", "\n", "\\"}));
        Assert.assertEquals("[\"abc 'def'\", `ghi 'jkl' \"mno\"`, '\\\\\\\\\\\\']", appender.apply(new String[] {"abc 'def'", "ghi 'jkl' \"mno\"", "\\\\\\"}));
        Assert.assertEquals("['80\\x1e', '90\\x1f']", appender.apply(new String[] {"80\u001e", "90\u001f"}));
    }

    @Test
    public void caseConfigPythonString() throws ReflectiveOperationException {
        final AppenderToStringBuilder appender = AppenderToStringBuilder.create();
        appender.config().numberIntegral().use(CharSequenceAppender.Python.AUTO);
        Assert.assertEquals("\"0x0\"", appender.apply("0x0"));
        Assert.assertEquals("\"appender\"", appender.apply("appender"));
        Assert.assertEquals("\"black, white\"", appender.apply("black, white"));
        Assert.assertEquals("[\"all\", \"some\", \"none\"]", appender.apply(new String[] {"all", "some", "none"}));
        Assert.assertEquals("[\"-1\", \"50%\", \"A\"]", appender.apply(new Object[] {"-1", "50%", "A"}));
        Assert.assertEquals("[\"user\\t007\", \"root\\a12\", \"phone\\f89\", \"panda\\b3\"]", appender.apply(new String[] {"user\t007", "root\u000712", "phone\f89", "panda\b3"}));
        Assert.assertEquals("[\"\\vgood\", \"\\x0ebetter\", \"\\x0fbest\"]", appender.apply(new Object[] {"\u000bgood", "\u000ebetter", "\u000fbest"}));
    }
}
