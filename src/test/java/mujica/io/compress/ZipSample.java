package mujica.io.compress;

import mujica.io.nest.BufferedLimitedUniversalDataInputStream;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.HexEncoder;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.CRC32;

@CodeHistory(date = "2025/11/16", name = "SampleDescription") // can not extends Description because there is no public or protected constructor
@CodeHistory(date = "2025/11/16")
class ZipSample {

    private static final int INDEX_CRC32 = 0;
    private static final int INDEX_COMPRESSED_SIZE = 1;
    private static final int INDEX_UNCOMPRESSED_SIZE = 2;

    @NotNull
    final Description description;

    @NotNull
    final String fullName;

    ZipSample(@NotNull Description description, @NotNull String fullName) {
        super();
        this.description = description;
        this.fullName = fullName;
    }

    public void run(@NotNull RunNotifier notifier, @NotNull Class<?> clazz) {
        notifier.fireTestStarted(description);
        try (InputStream is = clazz.getResourceAsStream(fullName)) {
            readZip(Objects.requireNonNull(is));
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        } finally {
            notifier.fireTestFinished(description);
        }
    }

    private void readZip(@NotNull InputStream is) throws IOException {
        final BufferedLimitedUniversalDataInputStream bis = new BufferedLimitedUniversalDataInputStream(is);
        bis.setRemaining(30); // local file header fixed part length
        {
            int signature = bis.readIntelSignedInt(); // local file header signature
            if (signature != 0x04034b50) {
                throw new IOException("signature = 0x" + HexEncoder.LOWER_ENCODER.hex32(signature));
            }
        }
        bis.skipFully(2); // version needed to extract
        final int flags = bis.readIntelUnsignedShort(); // general purpose bit flag
        final int compressionMethod = bis.readIntelUnsignedShort();
        if (checkCompressionMethod(compressionMethod)) {
            return;
        }
        final long[] expected = new long[3];
        if ((flags & 0x0004) != 0) {
            bis.skipFully(16);
        } else {
            bis.skipFully(4);
            expected[INDEX_CRC32] = bis.readIntelUnsignedInt();
            expected[INDEX_COMPRESSED_SIZE] = bis.readIntelUnsignedInt();
            expected[INDEX_UNCOMPRESSED_SIZE] = bis.readIntelUnsignedInt();
        }
        {
            int variablePartLength = bis.readIntelUnsignedShort() // file name length
                    + bis.readIntelUnsignedShort(); // extra field length
            Assert.assertEquals(0, bis.setRemaining(variablePartLength));
            bis.skipFully(variablePartLength);
        }
        Assert.assertEquals(0, bis.getRemaining());
        long[] actual;
        if ((flags & 0x0004) != 0) {
            bis.setRemaining(Long.MAX_VALUE);
            actual = performTest(bis, compressionMethod);
            actual[INDEX_COMPRESSED_SIZE] = Long.MAX_VALUE - bis.getRemaining();
            bis.setRemaining(12);
            expected[INDEX_CRC32] = bis.readIntelUnsignedInt();
            expected[INDEX_COMPRESSED_SIZE] = bis.readIntelUnsignedInt();
            expected[INDEX_UNCOMPRESSED_SIZE] = bis.readIntelUnsignedInt();
        } else {
            bis.setRemaining(expected[INDEX_COMPRESSED_SIZE]);
            actual = performTest(bis, compressionMethod);
            actual[INDEX_COMPRESSED_SIZE] = expected[INDEX_COMPRESSED_SIZE] - bis.getRemaining();
        }
        Assert.assertArrayEquals(expected, actual);
    }

    private boolean checkCompressionMethod(int compressionMethod) throws IOException {
        switch (compressionMethod) {
            case 8:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("deflate-"));
                return false;
            case 9:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("deflate64-"));
                return false;
            case 12:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("bzip2-"));
                return true;
            case 14:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("lzma-"));
                return true;
            case 93:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("zstd-"));
                return true;
            case 98:
                Assert.assertTrue(description.getDisplayName().toLowerCase().startsWith("ppmd-"));
                return true;
            default:
                throw new IOException("compressionMethod = " + compressionMethod);
        }
    }

    @NotNull
    private long[] performTest(@NotNull InputStream is, int compressionMethod) throws IOException {
        switch (compressionMethod) {
            case 8:
                return testDeflate(is);
            case 9:
                return testDeflate64(is);
            default:
                throw new IOException("compressionMethod = " + compressionMethod);
        }
    }

    @NotNull
    private long[] testDeflate(@NotNull InputStream is) throws IOException {
        is = new TableInflateInputStream(is, new CyclicArrayRunBuffer(AbstractInflateInputStream.MAX_RUN_BUFFER_DISTANCE));
        return readUncompressedStream(is);
    }

    @NotNull
    private long[] testDeflate64(@NotNull InputStream is) throws IOException {
        is = new Inflate64InputStream(is);
        return readUncompressedStream(is);
    }

    @NotNull
    private long[] readUncompressedStream(@NotNull InputStream is) throws IOException {
        final int bufferSize = 1024;
        final byte[] buffer = new byte[bufferSize];
        final CRC32 crc32 = new CRC32();
        long uncompressedSize = 0L;
        while (true) {
            int count = is.read(buffer, 0, bufferSize);
            if (count <= 0) {
                break;
            }
            crc32.update(buffer, 0, count);
            uncompressedSize += count;
        }
        final long[] result = new long[3];
        result[INDEX_CRC32] = crc32.getValue();
        result[INDEX_UNCOMPRESSED_SIZE] = uncompressedSize;
        return result;
    }
}
