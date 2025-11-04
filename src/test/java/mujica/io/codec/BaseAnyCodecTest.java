package mujica.io.codec;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.io.*;

/**
 * Created on 2025/5/6.
 */
public class BaseAnyCodecTest {

    private void caseEncodeOutputStream(@NotNull BaseAnyCodec codec, @NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
        try (OutputStream os = new BaseAnyEncodeOutputStream(actual, codec)) {
            os.write(message);
            os.flush();
        }
        Assert.assertArrayEquals(code, actual.toByteArray());
    }

    private void caseEncodeInputStream(@NotNull BaseAnyCodec codec, @NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (InputStream is = new BaseAnyEncodeInputStream(new ByteArrayInputStream(message), codec)) {
            Assert.assertArrayEquals(code, is.readNBytes(code.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseDecodeOutputStream(@NotNull BaseAnyCodec codec, @NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
        try (OutputStream os = new BaseAnyDecodeOutputStream(actual, codec)) {
            os.write(code);
            os.flush();
        }
        Assert.assertArrayEquals(message, actual.toByteArray());
    }

    private void caseDecodeInputStream(@NotNull BaseAnyCodec codec, @NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (InputStream is = new BaseAnyDecodeInputStream(new ByteArrayInputStream(code), codec)) {
            Assert.assertArrayEquals(message, is.readNBytes(message.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseAllStreams(@NotNull BaseAnyCodec codec, @NotNull byte[] message, @NotNull byte[] code) throws IOException {
        caseEncodeOutputStream(codec, message, code);
        caseEncodeInputStream(codec, message, code);
        caseDecodeOutputStream(codec, message, code);
        caseDecodeInputStream(codec, message, code);
    }
}
