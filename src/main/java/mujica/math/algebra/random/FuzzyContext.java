package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2022/5/27", project = "Ultramarine")
@CodeHistory(date = "2025/3/10")
public class FuzzyContext extends FreeFloatingPointContext {

    private static final long serialVersionUID = 0xda4d96d9243d7da1L;

    private static final int IN_SEGMENT = 0x01;
    private static final int IN_PERIODIC = 0x02;
    private static final int IN_MAPPED = 0x04;

    public FuzzyContext(@NotNull RandomSource source, int flags) {
        super(source, flags);
    }

    public FuzzyContext(@NotNull RandomSource source) {
        super(source);
    }

    public FuzzyContext(int flags) {
        super(flags);
    }

    public FuzzyContext() {
        super();
    }

    @Override
    public byte nextByte() {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
            case 0x3:
                return 0x00;
            case 0x4:
            case 0x5:
            case 0x6:
                return (byte) 0xff;
            case 0x7:
            case 0x8:
                return 0x01;
            case 0x9:
            case 0xa:
                return (byte) 0x80;
            case 0xb:
                return (byte) 0x7f;
            case 0xc:
                return (byte) 0xfe;
            case 0xd:
                return (byte) (1 << source.next(3));
            case 0xe:
                return (byte) ((1 << source.next(3)) - 1);
            case 0xf:
                return super.nextByte();
            default:
                throw new IllegalStateException();
        }
    }

    @NotNull
    @Override
    public byte[] nextByteArray(int length) {
        final byte[] array = new byte[length];
        fillByteArray(array, 0, length);
        return array;
    }

    public void fillByteArray(@NotNull byte[] array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        fillByteArray(array, startIndex, endIndex, 0);
    }

    private void fillByteArray(@NotNull byte[] array, int startIndex, int endIndex, int flag) {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
                fillGray(array, startIndex, endIndex);
                break;
            case 0x1:
            case 0x2:
                fillLinear(array, startIndex, endIndex);
                break;
            case 0x3:
                fillGeometric(array, startIndex, endIndex);
                break;
            case 0x4:
                flag |= IN_SEGMENT;
                // no break here
            case 0x5:
            case 0x6:
            case 0x7:
                fillSegmented(array, startIndex, endIndex, flag);
                break;
            case 0x8:
                flag |= IN_PERIODIC;
                // no break here
            case 0x9:
            case 0xa:
            case 0xb:
                fillPeriodic(array, startIndex, endIndex, flag);
                break;
            case 0xc:
                flag |= IN_MAPPED;
                // no break here
            case 0xd:
            case 0xe:
            case 0xf:
                fillMapped(array, startIndex, endIndex, flag);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void fillGray(@NotNull byte[] array, int startIndex, int endIndex) {
        int value = 0xff & nextByte();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = (byte) ((value >> 1) ^ value); // binary to gray
            value++;
        }
    }

    private void fillLinear(@NotNull byte[] array, int startIndex, int endIndex) {
        int value = nextByte();
        final int addend = nextByte();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = (byte) value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull byte[] array, int startIndex, int endIndex) {
        int value = nextByte();
        final int multiplier = nextByte() | 0x1;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = (byte) value;
            value *= multiplier;
        }
    }

    private void fillSegmented(@NotNull byte[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_SEGMENT) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = super.nextByte(); // fillUniform
            }
            return;
        }
        final int segmentCount = 2 + nextMinInt(length - 2, 4);
        final int[] segments = nextIntArray(length, segmentCount);
        segments[0] = length;
        Arrays.sort(segments);
        int index0 = 0;
        for (int segmentIndex = 0; segmentIndex < segmentCount; segmentIndex++) {
            int index1 = segments[segmentIndex];
            if (index0 < index1) {
                fillByteArray(array, startIndex + index0, startIndex + index1, flag | IN_SEGMENT);
            }
            index0 = index1;
        }
        assert index0 == length;
    }

    private void fillPeriodic(@NotNull byte[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_PERIODIC) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = nextByte(); // fillEach
            }
            return;
        }
        final int period = nextInt(2, length);
        int midIndex = startIndex + period;
        fillByteArray(array, startIndex, midIndex, flag | IN_PERIODIC);
        final int copyIndex = startIndex + period;
        for (int index0 = 0; index0 < period; index0++) {
            byte value = array[startIndex + index0];
            for (int index1 = copyIndex + index0; index1 < endIndex; index1 += period) {
                array[index1] = value;
            }
        }
    }

    private void fillMapped(@NotNull byte[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        final int shift = nextInt(Byte.SIZE) + 1;
        final int mapSize = 1 << shift;
        if (length <= mapSize || (flags & IN_MAPPED) != 0) {
            Arrays.fill(array, startIndex, endIndex, nextByte()); // fillSame
            return;
        }
        final byte[] map = new byte[mapSize];
        fillByteArray(map, 0, mapSize, IN_MAPPED);
        fillByteArray(array, startIndex, endIndex, flag | IN_MAPPED);
        final int mask = mapSize - 1;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = map[mask & array[index]];
        }
    }

    private static final short[] SPECIAL_SHORT = {
            21, 22, 23, 25, 53, 68, 80, 110, 123, 143, 443, 520, // small ports
            1433, 1521, 3306, 3389, 6379, 8080, 8443, 8888, 12345, (short) 33434, // large ports
            9, 99, 999, 9999, 10, 100, 1000, 10000 // decimal
    };

    @Override
    public short nextShort() {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
                return 0;
            case 0x2:
            case 0x3:
                return -1;
            case 0x4:
            case 0x5:
                return 1;
            case 0x6:
                return Short.MIN_VALUE;
            case 0x7:
                return Short.MAX_VALUE;
            case 0x8:
                return Short.MIN_VALUE + 1;
            case 0x9:
                return (short) (1 << source.next(4));
            case 0xa:
                return (short) ((1 << source.next(4)) - 1);
            case 0xb:
                return (short) (0xff & nextByte());
            case 0xc:
                return SPECIAL_SHORT[nextInt(SPECIAL_SHORT.length)];
            default: // 3 values
                return super.nextShort();
        }
    }

    private static final int[] SPECIAL_INT = {
            Float.floatToRawIntBits(1.0f),
            Float.floatToRawIntBits(-1.0f),
            Float.floatToRawIntBits(Float.MIN_VALUE),
            Float.floatToRawIntBits(Float.MAX_VALUE),
            Float.floatToRawIntBits(Float.NEGATIVE_INFINITY),
            Float.floatToRawIntBits(Float.POSITIVE_INFINITY),
            Float.floatToRawIntBits(Float.MIN_NORMAL),
            Float.floatToRawIntBits(Float.NaN),
            // idioms (24)
            0x8badf00d,
            0x1badb002,
            0xb16b00b5,
            0xbaadf00d,
            0xbaddcafe,
            0xcafebabe,
            0xcafed00d,
            0x0d15ea5e,
            0xdeadbabe,
            0xdeadbeef,
            0xdeadc0de,
            0xdeaddead,
            0xdeadfa11,
            0xdead10cc,
            0xdefec8ed,
            0xfacefeed,
            0xfee1dead,
            0xe011cfd0,
            0x000ff1ce,
            0x00bab10c,
            0xfaceb00c,
            0xdeadd00d,
            0xdabbad00,
            0x1ceb00da,
            // MD4 and MD5 initial constants (4)
            0x67452301,
            0xefcdab89,
            0x98badcfe,
            0x10325476,
            // SHA1 initial constants (5)
            0x67452301,
            0xefcdab89,
            0x98badcfe,
            0x10325476,
            0xc3d2e1f0,
            // SHA1 addend constants (4)
            0x5a827999,
            0x6ed9eba1,
            0x8f1bbcdc,
            0xca62c1d6,
            // SHA224 initial constants (8)
            0xc1059ed8,
            0x367cd507,
            0x3070dd17,
            0xf70e5939,
            0xffc00b31,
            0x68581511,
            0x64f98fa7,
            0xbefa4fa4,
            // SM3 initial constants (8)
            0x7380166f,
            0x4914b2b9,
            0x172442d7,
            0xda8a0600,
            0xa96f30bc,
            0x163138aa,
            0xe38dee4d,
            0xb0fb0e4e,
            // SM3 rotate constants (2)
            0x79cc4519,
            0x7a879d8a,
            // Blake IV from org.apache.commons.codec.digest.Blake3 (8)
            0x6a09e667,
            0xbb67ae85,
            0x3c6ef372,
            0xa54ff53a,
            0x510e527f,
            0x9b05688c,
            0x1f83d9ab,
            0x5be0cd19,
            // xxHash32 primes (5)
            (int) 2654435761L,
            (int) 2246822519L,
            (int) 3266489917L,
            668265263,
            374761393,
            // CRC32 polynomial
            0x04c11db7,
            // CRC32C polynomial
            0x1edc6f41,
            // Koopman polynomial
            0x741b8cd7,
            // ChaCha20 initial vectors (4)
            0x61707865,
            0x3320646e,
            0x79622d32,
            0x6b206574,
            // pkware encryption keys (3)
            305419896,
            591751049,
            878082192
    };

    private int specialInt() {
        final int value = SPECIAL_INT[nextInt(SPECIAL_INT.length)];
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
                return Integer.reverseBytes(value);
            case 0x3:
            case 0x4:
                return Integer.reverse(value);
            case 0x5:
                return Integer.reverse(Integer.reverseBytes(value));
            case 0x6:
                return Integer.rotateRight(value, (int) source.next(5));
            default: // 9 values
                return value;
        }
    }

    @Override
    public int nextInt() {
        switch ((int) source.next(4)) { // from 0x00 to 0x1f
            case 0x00:
            case 0x01:
            case 0x02:
                return 0;
            case 0x03:
            case 0x04:
            case 0x05:
                return -1;
            case 0x06:
                return 1;
            case 0x07:
                return Integer.MIN_VALUE;
            case 0x08:
                return Integer.MAX_VALUE;
            case 0x09:
                return 1 << source.next(5);
            case 0x0a:
                return ~(1 << source.next(5));
            case 0x0b:
                return -(1 << source.next(5));
            case 0x0c:
                return (1 << source.next(5)) - 1;
            case 0x0d:
                return -1 << source.next(5);
            case 0x0e:
                return 0xff & nextByte();
            case 0x0f:
                return repeat2i(nextByte());
            case 0x10:
                return 0xffff & nextShort();
            case 0x11:
                return repeat2i(nextShort());
            case 0x12:
            case 0x13:
            case 0x14:
            case 0x15:
                return specialInt();
            default: // 10 values
                return super.nextInt();
        }
    }

    private int repeat2i(byte value) {
        final int unsigned = 0xff & value;
        return (unsigned << 24) | (unsigned << 16) | (unsigned << 8) | unsigned;
    }

    private int repeat2i(short value) {
        final int unsigned = 0xffff & value;
        return (unsigned << 16) | unsigned;
    }

    @NotNull
    @Override
    public int[] nextIntArray(int length) {
        final int[] array = new int[length];
        fillIntArray(array, 0, length);
        return array;
    }

    public void fillIntArray(@NotNull int[] array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        fillIntArray(array, startIndex, endIndex, 0);
    }

    private void fillIntArray(@NotNull int[] array, int startIndex, int endIndex, int flag) {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
                fillGray(array, startIndex, endIndex);
                break;
            case 0x1:
            case 0x2:
                fillLinear(array, startIndex, endIndex);
                break;
            case 0x3:
                fillGeometric(array, startIndex, endIndex);
                break;
            case 0x4:
                flag |= IN_SEGMENT;
                // no break here
            case 0x5:
            case 0x6:
            case 0x7:
                fillSegmented(array, startIndex, endIndex, flag);
                break;
            case 0x8:
                flag |= IN_PERIODIC;
                // no break here
            case 0x9:
            case 0xa:
            case 0xb:
                fillPeriodic(array, startIndex, endIndex, flag);
                break;
            case 0xc:
                flag |= IN_MAPPED;
                // no break here
            case 0xd:
            case 0xe:
            case 0xf:
                fillMapped(array, startIndex, endIndex, flag);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void fillGray(@NotNull int[] array, int startIndex, int endIndex) {
        int value = super.nextInt();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = ((value >> 1) ^ value); // binary to gray
            value++;
        }
    }

    private void fillLinear(@NotNull int[] array, int startIndex, int endIndex) {
        int value = nextInt();
        final int addend = nextInt();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull int[] array, int startIndex, int endIndex) {
        int value = nextInt();
        final int multiplier = nextInt() | 0x1;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = value;
            value *= multiplier;
        }
    }

    private void fillSegmented(@NotNull int[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_SEGMENT) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = super.nextInt(); // fillUniform
            }
            return;
        }
        final int segmentCount = 2 + nextMinInt(length - 2, 4);
        final int[] segments = nextIntArray(length, segmentCount);
        segments[0] = length;
        Arrays.sort(segments);
        int index0 = 0;
        for (int segmentIndex = 0; segmentIndex < segmentCount; segmentIndex++) {
            int index1 = segments[segmentIndex];
            if (index0 < index1) {
                fillIntArray(array, startIndex + index0, startIndex + index1, flag | IN_SEGMENT);
            }
            index0 = index1;
        }
        assert index0 == length;
    }

    private void fillPeriodic(@NotNull int[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_PERIODIC) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = nextInt(); // fillEach
            }
            return;
        }
        final int period = nextInt(2, length);
        int midIndex = startIndex + period;
        fillIntArray(array, startIndex, midIndex, flag | IN_PERIODIC);
        final int copyIndex = startIndex + period;
        for (int index0 = 0; index0 < period; index0++) {
            int value = array[startIndex + index0];
            for (int index1 = copyIndex + index0; index1 < endIndex; index1 += period) {
                array[index1] = value;
            }
        }
    }

    private void fillMapped(@NotNull int[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        final int shift = nextInt(10) + 1;
        final int mapSize = 1 << shift;
        if (length <= mapSize || (flag & IN_MAPPED) != 0) {
            Arrays.fill(array, startIndex, endIndex, nextInt()); // fillSame
            return;
        }
        final int[] map = new int[mapSize];
        fillIntArray(map, 0, mapSize, IN_MAPPED);
        fillIntArray(array, startIndex, endIndex, flag | IN_MAPPED);
        final int mask = mapSize - 1;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = map[mask & array[index]];
        }
    }

    private static final long[] SPECIAL_LONG = {
            Double.doubleToRawLongBits(1.0),
            Double.doubleToRawLongBits(-1.0),
            Double.doubleToRawLongBits(Double.MIN_VALUE),
            Double.doubleToRawLongBits(Double.MAX_VALUE),
            Double.doubleToRawLongBits(Double.NEGATIVE_INFINITY),
            Double.doubleToRawLongBits(Double.POSITIVE_INFINITY),
            Double.doubleToRawLongBits(Double.MIN_NORMAL),
            Double.doubleToRawLongBits(Double.NaN),
            // SHA512 constants (8)
            0x6a09e667f3bcc908L,
            0xbb67ae8584caa73bL,
            0x3c6ef372fe94f82bL,
            0xa54ff53a5f1d36f1L,
            0x510e527fade682d1L,
            0x9b05688c2b3e6c1fL,
            0x1f83d9abfb41bd6bL,
            0x5be0cd19137e2179L,
    };

    private long specialLong() {
        final long value = SPECIAL_LONG[nextInt(SPECIAL_LONG.length)];
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
                return Long.reverseBytes(value);
            case 0x3:
            case 0x4:
                return Long.reverse(value);
            case 0x5:
                return Long.reverse(Long.reverseBytes(value));
            case 0x6:
                return Long.rotateLeft(value, (int) source.next(6));
            default: // 9 values
                return value;
        }
    }

    @Override
    public long nextLong() {
        switch ((int) source.next(5)) { // from 0x00 to 0x1f
            case 0x00:
            case 0x01:
            case 0x02:
                return 0L;
            case 0x03:
            case 0x04:
            case 0x05:
                return -1L;
            case 0x06:
                return 1L;
            case 0x07:
                return Long.MIN_VALUE;
            case 0x08:
                return Long.MAX_VALUE;
            case 0x09:
                return 1L << source.next(6);
            case 0x0a:
                return ~(1L << source.next(6));
            case 0x0b:
                return -(1L << source.next(6));
            case 0x0c:
                return (1L << source.next(6)) - 1L;
            case 0x0d:
                return -1L << source.next(6);
            case 0x0e:
                return 0xffL & nextByte();
            case 0x0f:
                return repeat2l(nextByte());
            case 0x10:
                return 0xffffL & nextShort();
            case 0x11:
                return repeat2l(nextShort());
            case 0x12:
                return 0xffffffffL & nextInt();
            case 0x13:
                return repeat2l(nextInt());
            case 0x14:
            case 0x15:
            case 0x16:
            case 0x17:
                return specialLong();
            default: // 8 values
                return super.nextLong();
        }
    }

    private long repeat2l(byte value) {
        final long masked = 0xffL & value;
        return (masked << 56) | (masked << 48) | (masked << 40) | (masked << 32)
                | (masked << 24) | (masked << 16) | (masked << 8) | masked;
    }

    private long repeat2l(short value) {
        final long masked = 0xffffL & value;
        return (masked << 48) | (masked << 32) | (masked << 16) | masked;
    }

    private long repeat2l(int value) {
        final long masked = 0xffffffffL & value;
        return (masked << 32) | masked;
    }

    @NotNull
    @Override
    public long[] nextLongArray(int length) {
        final long[] array = new long[length];
        fillLongArray(array, 0, length);
        return array;
    }

    public void fillLongArray(@NotNull long[] array, @Index(of = "array") int startIndex, @Index(of = "array", inclusive = false) int endIndex) {
        fillLongArray(array, startIndex, endIndex, 0);
    }

    private void fillLongArray(@NotNull long[] array, int startIndex, int endIndex, int flag) {
        switch ((int) source.next(4)) { // from 0x0 to 0xf
            case 0x0:
                fillGray(array, startIndex, endIndex);
                break;
            case 0x1:
            case 0x2:
                fillLinear(array, startIndex, endIndex);
                break;
            case 0x3:
                fillGeometric(array, startIndex, endIndex);
                break;
            case 0x4:
                flag |= IN_SEGMENT;
                // no break here
            case 0x5:
            case 0x6:
            case 0x7:
                fillSegmented(array, startIndex, endIndex, flag);
                break;
            case 0x8:
                flag |= IN_PERIODIC;
                // no break here
            case 0x9:
            case 0xa:
            case 0xb:
                fillPeriodic(array, startIndex, endIndex, flag);
                break;
            case 0xc:
                flag |= IN_MAPPED;
                // no break here
            case 0xd:
            case 0xe:
            case 0xf:
                fillMapped(array, startIndex, endIndex, flag);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void fillGray(@NotNull long[] array, int startIndex, int endIndex) {
        long value = super.nextLong();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = ((value >> 1) ^ value); // binary to gray
            value++;
        }
    }

    private void fillLinear(@NotNull long[] array, int startIndex, int endIndex) {
        long value = nextLong();
        final long addend = nextLong();
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull long[] array, int startIndex, int endIndex) {
        long value = nextInt();
        final long multiplier = nextLong() | 0x1L;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = value;
            value *= multiplier;
        }
    }

    private void fillSegmented(@NotNull long[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_SEGMENT) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = super.nextLong(); // fillUniform
            }
            return;
        }
        final int segmentCount = 2 + nextMinInt(length - 2, 4);
        final int[] segments = nextIntArray(length, segmentCount);
        segments[0] = length;
        Arrays.sort(segments);
        int index0 = 0;
        for (int segmentIndex = 0; segmentIndex < segmentCount; segmentIndex++) {
            int index1 = segments[segmentIndex];
            if (index0 < index1) {
                fillLongArray(array, startIndex + index0, startIndex + index1, flag | IN_SEGMENT);
            }
            index0 = index1;
        }
        assert index0 == length;
    }

    private void fillPeriodic(@NotNull long[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        if (length < 3 || (flag & IN_PERIODIC) != 0) {
            for (int index = startIndex; index < endIndex; index++) {
                array[index] = nextLong(); // fillEach
            }
            return;
        }
        final int period = nextInt(2, length);
        int midIndex = startIndex + period;
        fillLongArray(array, startIndex, midIndex, flag | IN_PERIODIC);
        final int copyIndex = startIndex + period;
        for (int index0 = 0; index0 < period; index0++) {
            long value = array[startIndex + index0];
            for (int index1 = copyIndex + index0; index1 < endIndex; index1 += period) {
                array[index1] = value;
            }
        }
    }

    private void fillMapped(@NotNull long[] array, int startIndex, int endIndex, int flag) {
        final int length = endIndex - startIndex;
        final int shift = nextInt(11) + 1;
        final int mapSize = 1 << shift;
        if (length <= mapSize || (flag & IN_MAPPED) != 0) {
            Arrays.fill(array, startIndex, endIndex, nextLong()); // fillSame
            return;
        }
        final long[] map = new long[mapSize];
        fillLongArray(map, 0, mapSize, IN_MAPPED);
        fillLongArray(array, startIndex, endIndex, flag | IN_MAPPED);
        final int mask = mapSize - 1;
        for (int index = startIndex; index < endIndex; index++) {
            array[index] = map[mask & (int) array[index]];
        }
    }

    private long bitExpansion(int exponentBits0, int mantissaBits0, int exponentBits1, int mantissaBits1) {
        final long src = nextIEEE754(exponentBits0, mantissaBits0, flags);
        long dst = 0L;
        int expandedBitShift = nextInt(mantissaBits0);
        dst |= src & ((1L << expandedBitShift) - 1L);
        if ((src & (1L << expandedBitShift)) != 0L) {
            dst |= ((1L << (mantissaBits1 - mantissaBits0)) - 1L) << expandedBitShift;
        }
        dst |= (src & ((1L << mantissaBits0) - (1L << expandedBitShift))) << (mantissaBits1 - mantissaBits0);
        expandedBitShift = nextInt(exponentBits0) + mantissaBits0;
        dst |= (src & ((1L << expandedBitShift) - (1L << mantissaBits0))) << (mantissaBits1 - mantissaBits0);
        if ((src & (1L << expandedBitShift)) != 0L) {
            dst |= ((1L << (exponentBits1 - exponentBits0)) - 1L) << (expandedBitShift - mantissaBits0 + mantissaBits1);
        }
        dst |= (src & ((1L << (exponentBits0 + mantissaBits0)) - (1L << expandedBitShift))) << ((exponentBits1 + mantissaBits1) - (exponentBits0 + mantissaBits0));
        if ((src & (1L << (exponentBits0 + mantissaBits0))) != 0L) {
            dst |= 1L << (exponentBits1 + mantissaBits1);
        }
        return dst;
    }

    @Override
    protected long nextFloatBits() {
        switch ((int) source.next(2)) { // from 0x0 to 0x3
            case 0x0:
                return nextIEEE754(8, 23, flags);
            case 0x1:
                return bitExpansion(3, 4, 8, 23);
            case 0x2:
                return bitExpansion(5, 6, 8, 23);
            case 0x3:
                return bitExpansion(7, 8, 8, 23);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected long nextDoubleBits() {
        switch ((int) source.next(2)) { // from 0x0 to 0x3
            case 0x0:
                return nextIEEE754(11, 52, flags);
            case 0x1:
                return bitExpansion(4, 5, 11, 52);
            case 0x2:
                return bitExpansion(6, 7, 11, 52);
            case 0x3:
                return bitExpansion(8, 9, 11, 52);
            default:
                throw new IllegalStateException();
        }
    }
}
