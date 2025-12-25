package mujica.io.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import mujica.ds.of_int.list.*;
import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.ds.of_int.map.CompatibleIntSlotMap;
import mujica.ds.of_int.map.IntMap;
import mujica.math.algebra.random.FuzzyContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.*;

@CodeHistory(date = "2025/10/6", name = "DeflateCodecTest")
@CodeHistory(date = "2025/11/25")
public class InflateTest {

    @BeforeClass
    public static void initializeNetty() {
        final ByteBuf buf = Unpooled.buffer();
        ByteBufUtil.hexDump(buf);
        buf.release();
    }

    private static final int REPEAT = 100;

    private static final int SIZE = 92000;

    private final FuzzyContext fc = new FuzzyContext();

    private int nextCompressionLevel() {
        return fc.nextInt(Deflater.NO_COMPRESSION, Deflater.BEST_COMPRESSION + 1);
    }

    @NotNull
    private ResizePolicy nextRunBufferResizePolicy() {
        switch (fc.nextInt(10)) {
            case 0:
            case 1:
                return new Order1ResizePolicy(fc);
            case 2:
                return new TwiceResizePolicy(fc.nextInt(2, 20));
            case 3:
                return ShiftResizePolicy.INSTANCE;
            case 4:
                return LookUpResizePolicy.PAPER;
            case 5:
                return LookUpResizePolicy.GOLDEN;
            case 6:
                return LookUpResizePolicy.NATURAL;
            case 7:
                return LookUpResizePolicy.PRIME_PAPER;
            case 8:
                return LookUpResizePolicy.PRIME_GOLDEN;
            case 9:
                return LookUpResizePolicy.PRIME_FIBONACCI;
            default:
                throw new IllegalStateException();
        }
    }

    @NotNull
    private RunBuffer nextRunBuffer(int maxDistance) {
        switch (fc.nextInt(6)) {
            case 0:
                return new CyclicArrayRunBuffer(maxDistance);
            case 1:
                return new CyclicArrayRunBuffer(maxDistance, nextRunBufferResizePolicy());
            case 2:
                return new ArrayCopyRunBuffer(maxDistance);
            case 3:
                return new BlockRunBuffer(maxDistance, fc.nextInt(2, 20));
            case 4:
                return new ByteBufRunBuffer();
            case 5:
                return new StrictByteBufRunBuffer();
            default:
                throw new IllegalStateException();
        }
    }

    @NotNull
    private RunBuffer nextRunBuffer() {
        return nextRunBuffer(AbstractInflateInputStream.MAX_RUN_BUFFER_DISTANCE + fc.nextInt(0x1000));
    }

    private Supplier<IntMap> nextDecodeMapSupplier() {
        switch (fc.nextInt(2)) {
            case 0:
                return CompatibleIntMap::new;
            case 1:
                return CompatibleIntSlotMap::new;
            default:
                throw new IllegalStateException();
        }
    }

    @NotNull
    private InputStream nextInflateInputStream(@NotNull InputStream in) {
        switch (fc.nextInt(10)) {
            case 0:
                return new IntMapInflateInputStream.Prefix(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 1:
                return new IntMapInflateInputStream.LengthValue(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 2:
                return new IntMapInflateInputStream.ValueLength(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 3:
                return new ObjectMapInflateInputStream(in, nextRunBuffer());
            case 4:
            case 5:
                return new TreeInflateInputStream(in, nextRunBuffer());
            case 6:
            case 7:
                return new TableInflateInputStream(in, nextRunBuffer());
            case 8:
                return new JdkInflateInputStream(in, fc.nextInt(0x80, 0x1000));
            case 9:
                return new InflaterInputStream(in, new Inflater(true));
            default:
                throw new IllegalStateException();
        }
    }

    @Test
    public void fuzzInflateInputStream() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            byte[] expected = fc.nextByteArray(size);
            bos.reset();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bos, new Deflater(nextCompressionLevel(), true))) {
                dos.write(expected);
                dos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            InputStream is = null;
            try {
                is = nextInflateInputStream(new ByteArrayInputStream(compressed));
                actual = is.readNBytes(size);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, is.read());
            } catch (Throwable e) {
                if (is != null) {
                    System.out.println(is);
                }
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    @NotNull
    private AbstractInflateInputStream.Constructor2 nextInflateInputStreamConstructor2() {
        switch (fc.nextInt(10)) {
            case 0:
                return (in, maxDistance) -> new IntMapInflateInputStream.Prefix(in, nextRunBuffer(maxDistance), nextDecodeMapSupplier());
            case 1:
                return (in, maxDistance) -> new IntMapInflateInputStream.Prefix(in, nextRunBuffer(maxDistance + fc.nextInt(0x1000)), nextDecodeMapSupplier());
            case 2:
                return (in, maxDistance) -> new IntMapInflateInputStream.LengthValue(in, nextRunBuffer(maxDistance), nextDecodeMapSupplier());
            case 3:
                return (in, maxDistance) -> new IntMapInflateInputStream.ValueLength(in, nextRunBuffer(maxDistance), nextDecodeMapSupplier());
            case 4:
                return (in, maxDistance) -> new ObjectMapInflateInputStream(in, nextRunBuffer(maxDistance));
            case 5:
            case 6:
                return (in, maxDistance) -> new TreeInflateInputStream(in, nextRunBuffer(maxDistance));
            case 7:
            case 8:
                return (in, maxDistance) -> new TableInflateInputStream(in, nextRunBuffer(maxDistance));
            case 9:
                return (in, maxDistance) -> new JdkInflateInputStream(in, fc.nextInt(0x80, 0x1000));
            default:
                throw new IllegalStateException();
        }
    }

    @Test
    public void fuzzZlibInputStream() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            byte[] expected = fc.nextByteArray(size);
            bos.reset();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(bos, new Deflater(nextCompressionLevel(), false))) {
                dos.write(expected);
                dos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            InputStream is = null;
            try {
                is = WrapperZlibInputStream.create(new ByteArrayInputStream(compressed), nextInflateInputStreamConstructor2());
                actual = is.readNBytes(size);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, is.read());
            } catch (Throwable e) {
                if (is != null) {
                    System.out.println(is);
                }
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    @NotNull
    private AbstractInflateInputStream.Constructor1 nextInflateInputStreamConstructor1() {
        switch (fc.nextInt(10)) {
            case 0:
                return in -> new IntMapInflateInputStream.Prefix(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 1:
                return in -> new IntMapInflateInputStream.LengthValue(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 2:
                return in -> new IntMapInflateInputStream.ValueLength(in, nextRunBuffer(), nextDecodeMapSupplier());
            case 3:
                return in -> new ObjectMapInflateInputStream(in, nextRunBuffer());
            case 4:
            case 5:
                return in -> new TreeInflateInputStream(in, nextRunBuffer());
            case 6:
            case 7:
            case 8:
                return in -> new TableInflateInputStream(in, nextRunBuffer());
            case 9:
                return in -> new JdkInflateInputStream(in, fc.nextInt(0x80, 0x1000));
            default:
                throw new IllegalStateException();
        }
    }

    @Test
    public void fuzzGzipInputStream() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int size = fc.nextInt(SIZE);
            byte[] expected = fc.nextByteArray(size);
            bos.reset();
            try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
                gos.write(expected);
                gos.flush();
            }
            byte[] compressed = bos.toByteArray();
            byte[] actual = expected;
            InputStream is = null;
            try {
                is = WrapperGzipInputStream.create(new ByteArrayInputStream(compressed), nextInflateInputStreamConstructor1());
                actual = is.readNBytes(size);
                Assert.assertArrayEquals(expected, actual);
                Assert.assertEquals(-1, is.read());
            } catch (Throwable e) {
                if (is != null) {
                    System.out.println(is);
                }
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }
}
