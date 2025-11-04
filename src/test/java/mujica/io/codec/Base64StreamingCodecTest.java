package mujica.io.codec;

import mujica.math.algebra.random.FuzzyContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created on 2025/4/27.
 */
public class Base64StreamingCodecTest {

    private void caseEncodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
        try (Base64EncodeOutputStream os = new Base64EncodeOutputStream(actual)) {
            os.write(message);
            os.flush();
        }
        Assert.assertArrayEquals(code, actual.toByteArray());
    }

    private void caseEncodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (Base64EncodeInputStream is = new Base64EncodeInputStream(new ByteArrayInputStream(message))) {
            Assert.assertArrayEquals(code, is.readNBytes(code.length));
            Assert.assertEquals(-1, is.read());
        }
    }

    private void caseDecodeOutputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        final ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
        try (Base64DecodeOutputStream os = new Base64DecodeOutputStream(actual)) {
            os.write(code);
            os.flush();
        }
        Assert.assertArrayEquals(message, actual.toByteArray());
    }

    private void caseDecodeInputStream(@NotNull byte[] message, @NotNull byte[] code) throws IOException {
        try (Base64DecodeInputStream is = new Base64DecodeInputStream(new ByteArrayInputStream(code))) {
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
        caseAllStreams("fo", "Zm8=");
    }

    @Test
    public void case2() throws IOException {
        caseAllStreams("foo", "Zm9v");
    }

    @Test
    public void case3() throws IOException {
        caseAllStreams("foob", "Zm9vYg==");
    }

    @Test
    public void case4() throws IOException {
        caseAllStreams("fooba", "Zm9vYmE=");
    }

    @Test
    public void case5() throws IOException {
        caseAllStreams("foobar", "Zm9vYmFy");
    }

    private void caseBadCodeOutputStream(@NotNull byte[] code) throws IOException {
        try (Base64DecodeOutputStream os = new Base64DecodeOutputStream(OutputStream.nullOutputStream())) {
            os.write(code);
            os.flush();
        }
    }

    private void caseBadCodeOutputStream(@NotNull String code) throws IOException {
        caseBadCodeOutputStream(code.getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream1() throws IOException {
        caseBadCodeOutputStream(" A A");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream2() throws IOException {
        caseBadCodeOutputStream("(0)");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream3() throws IOException {
        caseBadCodeOutputStream("==");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream4() throws IOException {
        caseBadCodeOutputStream("C==");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream5() throws IOException {
        caseBadCodeOutputStream("HH=Q444");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream6() throws IOException {
        caseBadCodeOutputStream("HH===UU3");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream7() throws IOException {
        caseBadCodeOutputStream("虚与委蛇");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeOutputStream8() throws IOException {
        caseBadCodeOutputStream("马到成功");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void caseBadCodeInputStream(@NotNull byte[] code) throws IOException {
        try (Base64DecodeInputStream is = new Base64DecodeInputStream(new ByteArrayInputStream(code))) {
            while (is.read() != -1);
        }
    }

    private void caseBadCodeInputStream(@NotNull String code) throws IOException {
        caseBadCodeInputStream(code.getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream1() throws IOException {
        caseBadCodeInputStream("T T ");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream2() throws IOException {
        caseBadCodeInputStream("<p>");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream3() throws IOException {
        caseBadCodeInputStream("=i=");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream4() throws IOException {
        caseBadCodeInputStream("E==");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream5() throws IOException {
        caseBadCodeInputStream("HH=0000");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream6() throws IOException {
        caseBadCodeInputStream("HH===333");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream7() throws IOException {
        caseBadCodeInputStream("烫烫烫烫");
    }

    @Test(expected = IOException.class)
    public void caseBadCodeInputStream8() throws IOException {
        caseBadCodeInputStream("云淡风轻");
    }

    private static final int REPEAT = 88;

    private static final int SIZE = 102;

    private final FuzzyContext fc = new FuzzyContext();

    private void fuzzSetPolicyOnce(@NotNull BufferingPolicy policy0, @NotNull BufferingPolicy policy1, boolean immediately) throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] message = fc.nextByteArray(fc.nextInt(SIZE) + 2);
            byte[] code = Base64.getEncoder().encode(message);
            {
                ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
                try (Base64EncodeOutputStream os = new Base64EncodeOutputStream(actual, policy0)) {
                    int split = fc.nextInt(message.length - 1) + 1;
                    os.write(message, 0, split);
                    if (immediately) {
                        os.setPolicyImmediately(policy1);
                    } else {
                        os.setPolicyDelayed(policy1);
                    }
                    os.write(message, split, message.length - split);
                    os.flush();
                }
                Assert.assertArrayEquals(code, actual.toByteArray());
            }
            {
                byte[] actual = new byte[code.length];
                try (Base64EncodeInputStream is = new Base64EncodeInputStream(new ByteArrayInputStream(message), policy0)) {
                    int split = fc.nextInt(code.length - 1) + 1;
                    is.readNBytes(actual, 0, split);
                    if (immediately) {
                        is.setPolicyImmediately(policy1);
                    } else {
                        is.setPolicyDelayed(policy1);
                    }
                    is.readNBytes(actual, split, code.length - split);
                    Assert.assertArrayEquals(code, actual);
                    Assert.assertEquals(-1, is.read());
                }
            }
            {
                ByteArrayOutputStream actual = new ByteArrayOutputStream(message.length);
                try (Base64DecodeOutputStream os = new Base64DecodeOutputStream(actual, policy0)) {
                    int split = fc.nextInt(code.length - 1) + 1;
                    os.write(code, 0, split);
                    if (immediately) {
                        os.setPolicyImmediately(policy1);
                    } else {
                        os.setPolicyDelayed(policy1);
                    }
                    os.write(code, split, code.length - split);
                    os.flush();
                }
                Assert.assertArrayEquals(message, actual.toByteArray());
            }
            {
                byte[] actual = new byte[message.length];
                try (Base64DecodeInputStream is = new Base64DecodeInputStream(new ByteArrayInputStream(code), policy0)) {
                    int split = fc.nextInt(message.length - 1) + 1;
                    is.readNBytes(actual, 0, split);
                    if (immediately) {
                        is.setPolicyImmediately(policy1);
                    } else {
                        is.setPolicyDelayed(policy1);
                    }
                    is.readNBytes(actual, split, message.length - split);
                    Assert.assertArrayEquals(message, actual);
                    Assert.assertEquals(-1, is.read());
                }
            }
        }
    }

    @Test
    public void fuzzSetPolicyOnceDelayed() throws IOException {
        fuzzSetPolicyOnce(BufferingPolicy.GREEDY, BufferingPolicy.NORMAL, false);
        fuzzSetPolicyOnce(BufferingPolicy.GREEDY, BufferingPolicy.MINIMAL, false);
        fuzzSetPolicyOnce(BufferingPolicy.NORMAL, BufferingPolicy.MINIMAL, false);
        fuzzSetPolicyOnce(BufferingPolicy.NORMAL, BufferingPolicy.GREEDY, false);
        fuzzSetPolicyOnce(BufferingPolicy.MINIMAL, BufferingPolicy.GREEDY, false);
        fuzzSetPolicyOnce(BufferingPolicy.MINIMAL, BufferingPolicy.NORMAL, false);
    }

    @Test
    public void fuzzSetPolicyOnceImmediately() throws IOException {
        fuzzSetPolicyOnce(BufferingPolicy.GREEDY, BufferingPolicy.NORMAL, true);
        fuzzSetPolicyOnce(BufferingPolicy.GREEDY, BufferingPolicy.MINIMAL, true);
        fuzzSetPolicyOnce(BufferingPolicy.NORMAL, BufferingPolicy.MINIMAL, true);
        fuzzSetPolicyOnce(BufferingPolicy.NORMAL, BufferingPolicy.GREEDY, true);
        fuzzSetPolicyOnce(BufferingPolicy.MINIMAL, BufferingPolicy.GREEDY, true);
        fuzzSetPolicyOnce(BufferingPolicy.MINIMAL, BufferingPolicy.NORMAL, true);
    }

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

    private void fuzzSetPolicyFrequently(boolean delayed, boolean immediately) throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] message = fc.nextByteArray(fc.nextInt(SIZE) + 1);
            byte[] code = Base64.getEncoder().encode(message);
            {
                ByteArrayOutputStream actual = new ByteArrayOutputStream(code.length);
                try (Base64EncodeOutputStream os = new Base64EncodeOutputStream(actual)) {
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
                try (Base64EncodeInputStream is = new Base64EncodeInputStream(new ByteArrayInputStream(message))) {
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
                try (Base64DecodeOutputStream os = new Base64DecodeOutputStream(actual)) {
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
                try (Base64DecodeInputStream is = new Base64DecodeInputStream(new ByteArrayInputStream(code))) {
                    for (int index = 0; index < length; index++) {
                        setPolicyFrequently(is, delayed, immediately);
                        actual[index] = (byte) is.read();
                    }
                    Assert.assertArrayEquals(message, actual);
                    Assert.assertEquals(-1, is.read());
                }
            }
        }
    }

    @Test
    public void fuzzSetPolicyFrequentlyDelayed() throws IOException {
        fuzzSetPolicyFrequently(true, false);
    }

    @Test
    public void fuzzSetPolicyFrequentlyImmediately() throws IOException {
        fuzzSetPolicyFrequently(false, true);
    }

    @Test
    public void fuzzSetPolicyFrequentlyMixed() throws IOException {
        fuzzSetPolicyFrequently(true, true);
    }

    @Test
    public void fuzzInputStreamAround() throws IOException {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            byte[] expected = fc.nextByteArray(fc.nextInt(SIZE));
            try (InputStream is = new Base64DecodeInputStream(new Base64EncodeInputStream(new ByteArrayInputStream(expected)))) {
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
            try (OutputStream os = new Base64EncodeOutputStream(new Base64DecodeOutputStream(actual))) {
                os.write(expected);
                os.flush();
            }
            Assert.assertArrayEquals(expected, actual.toByteArray());
        }
    }
}
