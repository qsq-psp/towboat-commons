package mujica.io.codec;

import io.netty.buffer.ByteBufUtil;
import mujica.io.codec.Base16Charset;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

@CodeHistory(date = "2025/4/26")
public class Base16CharsetTest {

    @BeforeClass
    public static void initializeNetty() {
        ByteBufUtil.hexDump(new byte[0]);
    }

    private static final int REPEAT = 25;

    private static final int SIZE = 45;

    private final FuzzyContext fc = new FuzzyContext();

    @Test
    public void fuzzDecodeLower() {
        final Base16Charset charset = new Base16Charset(false);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] bytes = fc.nextByteArray(fc.nextInt(SIZE));
            String string = ByteBufUtil.hexDump(bytes);
            Assert.assertArrayEquals(bytes, string.getBytes(charset));
        }
    }

    @Test
    public void fuzzDecodeUpper() {
        final Base16Charset charset = new Base16Charset(true);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] bytes = fc.nextByteArray(fc.nextInt(SIZE));
            String string = ByteBufUtil.hexDump(bytes).toUpperCase();
            Assert.assertArrayEquals(bytes, string.getBytes(charset));
        }
    }

    @Test
    public void fuzzWriterLower() throws IOException {
        final Base16Charset charset = new Base16Charset(false);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            ByteArrayOutputStream actual = new ByteArrayOutputStream(expected.length);
            try (Writer writer = new OutputStreamWriter(actual, charset)) {
                writer.write(ByteBufUtil.hexDump(expected));
                writer.flush();
            }
            Assert.assertArrayEquals(expected, actual.toByteArray());
        }
    }

    @Test
    public void fuzzWriterUpper() throws IOException {
        final Base16Charset charset = new Base16Charset(true);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            ByteArrayOutputStream actual = new ByteArrayOutputStream(expected.length);
            try (Writer writer = new OutputStreamWriter(actual, charset)) {
                writer.write(ByteBufUtil.hexDump(expected).toUpperCase());
                writer.flush();
            }
            Assert.assertArrayEquals(expected, actual.toByteArray());
        }
    }

    @Test
    public void fuzzReaderLower() throws IOException {
        final Base16Charset charset = new Base16Charset(false);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] bytes = fc.nextByteArray(fc.nextInt(SIZE) + 1);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), charset))) {
                Assert.assertEquals(reader.readLine(), ByteBufUtil.hexDump(bytes));
                Assert.assertEquals(-1, reader.read());
            }
        }
    }

    @Test
    public void fuzzReaderUpper() throws IOException {
        final Base16Charset charset = new Base16Charset(true);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] bytes = fc.nextByteArray(fc.nextInt(SIZE) + 1);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes), charset))) {
                Assert.assertEquals(reader.readLine(), ByteBufUtil.hexDump(bytes).toUpperCase());
                Assert.assertEquals(-1, reader.read());
            }
        }
    }
}
