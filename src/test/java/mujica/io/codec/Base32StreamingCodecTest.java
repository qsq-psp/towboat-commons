package mujica.io.codec;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created in UltraIO on 2025/4/28.
 * Recreated on 2025/4/30.
 */
@CodeHistory(date = "2025/4/28", project = "UltraIO")
@CodeHistory(date = "2025/4/30")
public class Base32StreamingCodecTest {

    private void caseEncodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
        try (OutputStream os = new Base32EncodeOutputStream(actual)) {
            os.write(message);
            os.flush();
        }
        Assert.assertArrayEquals(code, actual.toByteArray());
    }

    private void caseEncodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (InputStream is = new Base32EncodeInputStream(new ByteArrayInputStream(message))) {
            Assert.assertArrayEquals(code, is.readNBytes(code.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseDecodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
        try (OutputStream os = new Base32DecodeOutputStream(actual)) {
            os.write(code);
            os.flush();
        }
        Assert.assertArrayEquals(message, actual.toByteArray());
    }

    private void caseDecodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (InputStream is = new Base32DecodeInputStream(new ByteArrayInputStream(code))) {
            Assert.assertArrayEquals(message, is.readNBytes(message.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseAllStreams(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        caseEncodeOutputStream(message, code);
        caseEncodeInputStream(message, code);
        caseDecodeOutputStream(message, code);
        caseDecodeInputStream(message, code);
    }

    private void caseAllStreams(@NotNull String message, @NotNull String code) throws IOException {
        caseAllStreams(message.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void caseAllStreams1() throws IOException {
        caseAllStreams("f", "MY======");
    }

    @Test
    public void caseAllStreams2() throws IOException {
        caseAllStreams("fo", "MZXQ====");
    }

    @Test
    public void caseAllStreams3() throws IOException {
        caseAllStreams("foo", "MZXW6===");
    }

    @Test
    public void caseAllStreams4() throws IOException {
        caseAllStreams("foob", "MZXW6YQ=");
    }

    @Test
    public void caseAllStreams5() throws IOException {
        caseAllStreams("fooba", "MZXW6YTB");
    }

    @Test
    public void caseAllStreams6() throws IOException {
        caseAllStreams("foobar", "MZXW6YTBOI======");
    }

    @Test
    public void caseAllStreams7() throws IOException {
        caseAllStreams("foobar2", "MZXW6YTBOIZA====");
    }

    @Test
    public void caseAllStreams8() throws IOException {
        caseAllStreams("foobar20", "MZXW6YTBOIZDA===");
    }

    @Test
    public void caseAllStreams9() throws IOException {
        caseAllStreams("foobar207", "MZXW6YTBOIZDANY=");
    }

    @Test
    public void caseAllStreams10() throws IOException {
        caseAllStreams("foobar2077", "MZXW6YTBOIZDANZX");
    }

    private void caseBadCodeOutputStream(@NotNull byte[] code) throws IOException {
        try (OutputStream os = new Base32DecodeOutputStream(OutputStream.nullOutputStream())) {
            os.write(code);
            os.flush();
        }
    }

    private void caseBadCodeOutputStream(@NotNull String code) throws IOException {
        caseBadCodeOutputStream(code.getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream1() throws IOException {
        caseBadCodeOutputStream(" F F F F");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream2() throws IOException {
        caseBadCodeOutputStream("T T T T ");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream3() throws IOException {
        caseBadCodeOutputStream("V/V/V/V/");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream4() throws IOException {
        caseBadCodeOutputStream("=QQQQQ=Q");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream5() throws IOException {
        caseBadCodeOutputStream("================");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream6() throws IOException {
        caseBadCodeOutputStream("S=======");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream7() throws IOException {
        caseBadCodeOutputStream("BB====BB");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream8() throws IOException {
        caseBadCodeOutputStream("呦呦鹿鸣食野之苹");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream9() throws IOException {
        caseBadCodeOutputStream("谋事在人成事在天");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void caseBadCodeInputStream(@NotNull byte[] code) throws IOException {
        try (Base32DecodeInputStream is = new Base32DecodeInputStream(new ByteArrayInputStream(code))) {
            while (is.read() != -1);
        }
    }

    private void caseBadCodeInputStream(@NotNull String code) throws IOException {
        caseBadCodeInputStream(code.getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream1() throws IOException {
        caseBadCodeInputStream("11======");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream2() throws IOException {
        caseBadCodeInputStream("54\r\nUUUU");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream3() throws IOException {
        caseBadCodeInputStream("=r=t=y=u");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream4() throws IOException {
        caseBadCodeInputStream("z=x==c=v");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream5() throws IOException {
        caseBadCodeInputStream("g======k");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream6() throws IOException {
        caseBadCodeInputStream("333333335=======");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream7() throws IOException {
        caseBadCodeInputStream("========");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream8() throws IOException {
        caseBadCodeInputStream("22222222天干物燥小心火烛");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream9() throws IOException {
        caseBadCodeInputStream("WWWWWWWW豫章故郡洪都新府");
    }

    private static final int REPEAT = 114;

    private static final int SIZE = 130;

    private final FuzzyContext fc = new FuzzyContext();
    
    private void setPolicyFrequently(@NotNull BufferingPolicyOwner owner, boolean delayed, boolean immediately) throws IOException {
        switch (fc.nextInt() & 0x7) {
            case 0:
                if (delayed) {
                    owner.setPolicyDelayed(BufferingPolicy.GREEDY);
                }
                break;
            case 1:
                if (delayed) {
                    owner.setPolicyDelayed(BufferingPolicy.NORMAL);
                }
                break;
            case 2:
                if (delayed) {
                    owner.setPolicyDelayed(BufferingPolicy.MINIMAL);
                }
                break;
            case 3:
                if (immediately) {
                    owner.setPolicyImmediately(BufferingPolicy.GREEDY);
                }
                break;
            case 4:
                if (immediately) {
                    owner.setPolicyImmediately(BufferingPolicy.NORMAL);
                }
                break;
            case 5:
                if (immediately) {
                    owner.setPolicyImmediately(BufferingPolicy.MINIMAL);
                }
                break;

        }
    }

    private void checkSetPolicyFrequently(@NotNull byte[] message, @NotNull byte[] code, boolean delayed, boolean immediately) throws IOException {
        {
            ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
            try (Base32EncodeOutputStream os = new Base32EncodeOutputStream(actual)) {
                for (byte octet : message) {
                    setPolicyFrequently(os, delayed, immediately);
                    os.write(octet);
                }
                os.flush();
            }
            Assert.assertArrayEquals(code, actual.toByteArray());
        }
        {
            int length = code.length;
            byte[] actual = new byte[length];
            try (Base32EncodeInputStream is = new Base32EncodeInputStream(new ByteArrayInputStream(message))) {
                for (int index = 0; index < length; index++) {
                    setPolicyFrequently(is, delayed, immediately);
                    actual[index] = (byte) is.read();
                }
                Assert.assertArrayEquals(code, actual);
                Assert.assertEquals(-1, is.read());
            }
        }
        {
            ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
            try (Base32DecodeOutputStream os = new Base32DecodeOutputStream(actual)) {
                for (byte octet : code) {
                    setPolicyFrequently(os, delayed, immediately);
                    os.write(octet);
                }
                os.flush();
            }
            Assert.assertArrayEquals(message, actual.toByteArray());
        }
        {
            int length = message.length;
            byte[] actual = new byte[length];
            try (Base32DecodeInputStream is = new Base32DecodeInputStream(new ByteArrayInputStream(code))) {
                for (int index = 0; index < length; index++) {
                    setPolicyFrequently(is, delayed, immediately);
                    actual[index] = (byte) is.read();
                }
                Assert.assertArrayEquals(message, actual);
                Assert.assertEquals(-1, is.read());
            }
        }
    }

    private void checkSetPolicyFrequently(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        checkSetPolicyFrequently(message, code, true, false);
        checkSetPolicyFrequently(message, code, false, true);
        checkSetPolicyFrequently(message, code, true, true);
    }

    private void checkSetPolicyFrequently(@NotNull String message, @NotNull String code) throws IOException {
        checkSetPolicyFrequently(message.getBytes(StandardCharsets.UTF_8), code.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void checkSetPolicyFrequently1() throws IOException {
        checkSetPolicyFrequently("(--YT--)", "FAWS2WKUFUWSS===");
    }

    @Test
    public void checkSetPolicyFrequently2() throws IOException {
        checkSetPolicyFrequently("[abc|xyz]", "LNQWEY34PB4XUXI=");
    }

    @Test
    public void checkSetPolicyFrequently3() throws IOException {
        checkSetPolicyFrequently("{0,1,2,3}", "PMYCYMJMGIWDG7I=");
    }

    @Test
    public void checkSetPolicyFrequently4() throws IOException {
        checkSetPolicyFrequently("A!B?C$D*", "IEQUEP2DERCCU===");
    }

    @Test
    public void checkSetPolicyFrequently5() throws IOException {
        checkSetPolicyFrequently("::selection", "HI5HGZLMMVRXI2LPNY======");
    }

    @Test
    public void checkSetPolicyFrequently6() throws IOException {
        checkSetPolicyFrequently("<article></article>", "HRQXE5DJMNWGKPR4F5QXE5DJMNWGKPQ=");
    }

    @Test
    public void checkSetPolicyFrequently7() throws IOException {
        checkSetPolicyFrequently("#990152", "EM4TSMBRGUZA====");
    }

    @Test
    public void checkSetPolicyFrequently8() throws IOException {
        checkSetPolicyFrequently("粉骨碎身浑不怕，要留清白在人间。", "46ZIT2NKVDT2FDXIXKV6NNMR4S4I3ZUASXX3ZDHIU2A6PFMZ424ILZ4ZXXSZZKHEXK5OTF5U4OAIE===");
    }

    @Test
    public void checkSetPolicyFrequently9() throws IOException {
        checkSetPolicyFrequently("永和九年，岁在癸丑，暮春之初，会于会稽山阴之兰亭，修禊事也。", "42YLRZMSRTSLTHPFXG2O7PEM4WZIDZM4VDTZTOHEXCI67PEM42NK5ZUYUXSLTC7FRCO67PEM4S6JVZF2R3SLZGXHVC66LMNR5GMLJZFZRPSYLMHEXKW67PEM4S725Z5GRLSLVC7EXGP6HAEC");
    }

    @Test
    public void fuzzInputStreamAround() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            byte[] actual = new byte[expected.length];
            try (InputStream is = new Base32DecodeInputStream(new Base32EncodeInputStream(new ByteArrayInputStream(expected)))) {
                is.readNBytes(actual, 0, actual.length);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, is.read());
            } catch (Throwable e) {
                System.err.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(expected)));
                System.err.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(actual)));
                throw e;
            }
        }
    }

    @Test
    public void fuzzOutputStreamAround() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            ByteArrayOutputStream actual = new ByteArrayOutputStream(expected.length);
            try {
                try (OutputStream os = new Base32EncodeOutputStream(new Base32DecodeOutputStream(actual))) {
                    os.write(expected);
                    os.flush();
                }
                Assert.assertArrayEquals(expected, actual.toByteArray());
            } catch (Throwable e) {
                System.err.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(expected)));
                System.err.println(ByteBufUtil.prettyHexDump(Unpooled.wrappedBuffer(actual.toByteArray())));
                throw e;
            }
        }
    }
}
