package mujica.io.stream;

import io.netty.buffer.ByteBufUtil;
import mujica.math.algebra.random.FuzzyContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2025/4/22.
 */
public class Base16StreamingCodecTest {

    @BeforeClass
    public static void initializeNetty() {
        ByteBufUtil.hexDump(new byte[0]);
    }

    private void caseEncodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
        try (Base16EncodeOutputStream os = new Base16EncodeOutputStream(actual)) {
            os.write(message);
            os.flush();
        }
        Assert.assertArrayEquals(code, actual.toByteArray());
    }

    private void caseEncodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (Base16EncodeInputStream is = new Base16EncodeInputStream(new ByteArrayInputStream(message))) {
            Assert.assertArrayEquals(code, is.readNBytes(code.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseDecodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
        try (Base16DecodeOutputStream os = new Base16DecodeOutputStream(actual)) {
            os.write(code);
            os.flush();
        }
        Assert.assertArrayEquals(message, actual.toByteArray());
    }

    private void caseDecodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (Base16DecodeInputStream is = new Base16DecodeInputStream(new ByteArrayInputStream(code))) {
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
    public void case1() throws IOException {
        caseAllStreams("f", "66");
    }

    @Test
    public void case2() throws IOException {
        caseAllStreams("fo", "666f");
    }

    @Test
    public void case3() throws IOException {
        caseAllStreams("foo", "666f6f");
    }

    @Test
    public void case4() throws IOException {
        caseAllStreams("foob", "666f6f62");
    }

    @Test
    public void case5() throws IOException {
        caseAllStreams("fooba", "666f6f6261");
    }

    @Test
    public void case6() throws IOException {
        caseAllStreams("foobar", "666f6f626172");
    }

    @Test
    public void case7() throws IOException {
        caseAllStreams("\r\n", "0d0a");
    }

    @Test
    public void case8() throws IOException {
        caseAllStreams("   ", "202020");
    }

    private static final int REPEAT = 25;

    private static final int SIZE = 45;

    private final FuzzyContext fc = new FuzzyContext();

    @Test
    public void fuzzAllStreams() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] message = fc.nextByteArray(fc.nextInt(SIZE));
            byte[] code = ByteBufUtil.hexDump(message).getBytes(StandardCharsets.UTF_8);
            caseAllStreams(message, code);
        }
    }

    @Test
    public void fuzzInputStreamAround() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            try (InputStream is = new Base16DecodeInputStream(new Base16EncodeInputStream(new ByteArrayInputStream(expected)))) {
                Assert.assertArrayEquals(expected, is.readNBytes(expected.length));
                Assert.assertEquals(-1, is.read());
            }
        }
    }

    @Test
    public void fuzzOutputStreamAround() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            ByteArrayOutputStream actual = new ByteArrayOutputStream(expected.length);
            try (OutputStream os = new Base16EncodeOutputStream(new Base16DecodeOutputStream(actual))) {
                os.write(expected);
                os.flush();
            }
            Assert.assertArrayEquals(expected, actual.toByteArray());
        }
    }
}
