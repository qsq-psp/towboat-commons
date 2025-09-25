package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2022/5/27", project = "Ultramarine")
@CodeHistory(date = "2025/3/10")
public class FuzzyContext extends FreeFloatContext {

    private static final long serialVersionUID = 0xda4d96d9243d7da1L;

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
        switch (source.next(4)) { // from 0x0 to 0xf
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
            default:
                return super.nextByte();
        }
    }

    @NotNull
    @Override
    public byte[] nextByteArray(int length) {
        final byte[] array = new byte[length];
        fill(array, 0, length);
        return array;
    }

    public void fill(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        switch (source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
                Arrays.fill(array, fromIndex, toIndex, nextByte());
                break;
            case 0x3:
                fillGray(array, fromIndex, toIndex);
                break;
            case 0x4:
            case 0x5:
            case 0x6:
                fillLinear(array, fromIndex, toIndex);
                break;
            case 0x7:
                fillGeometric(array, fromIndex, toIndex);
                break;
            case 0x8:
            case 0x9:
            case 0xa:
                fillSegmented(array, fromIndex, toIndex);
                break;
            case 0xb:
            case 0xc:
                fillPeriodic(array, fromIndex, toIndex);
                break;
            default:
                for (int index = fromIndex; index < toIndex; index++) {
                    array[index] = nextByte();
                }
                break;
        }
    }

    private void fillGray(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (byte) ((value >> 1) ^ value); // binary to gray
        }
    }

    private void fillLinear(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int addend = nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (byte) value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int multiplier = nextInt() | 0x1;
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (byte) value;
            value += multiplier;
        }
    }

    private void fillSegmented(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        final int length = toIndex - fromIndex;
        if (length < 3) {
            for (int index = fromIndex; index < toIndex; index++) {
                array[index] = nextByte();
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
                fill(array, fromIndex + index0, fromIndex + index1);
            }
            index0 = index1;
        }
        assert index0 == length;
    }

    private void fillPeriodic(@NotNull byte[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        final int length = toIndex - fromIndex;
        if (length < 3) {
            for (int index = fromIndex; index < toIndex; index++) {
                array[index] = nextByte();
            }
            return;
        }
        final int period = nextInt(2, length);
        int midIndex = fromIndex + period;
        fill(array, fromIndex, midIndex);
        final int copyIndex = fromIndex + period;
        for (int index0 = 0; index0 < period; index0++) {
            byte value = array[fromIndex + index0];
            for (int index1 = copyIndex + index0; index1 < toIndex; index1 += period) {
                array[index1] = value;
            }
        }
    }

    private static final short[] SPECIAL_SHORT = {
            21, 22, 23, 25, 53, 68, 80, 110, 123, 143, 443, 520,
            1433, 1521, 3306, 3389, 6379, 8080, 8443, 8888, 12345, (short) 33434,
            9, 99, 999, 9999, 10, 100, 1000, 10000
    };

    @Override
    public short nextShort() {
        switch (source.next(4)) { // from 0x0 to 0xf
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
                return (byte) (1 << source.next(4));
            case 0xa:
                return (byte) ((1 << source.next(4)) - 1);
            case 0xb:
                return repeat2s(nextByte());
            case 0xc:
                return SPECIAL_SHORT[nextInt(SPECIAL_SHORT.length)];
            default: // 0xd, 0xe, 0xf
                return super.nextShort();
        }
    }

    private short repeat2s(byte value) {
        final int masked = 0xff & value;
        return (short) ((masked << 8) | masked);
    }

    @NotNull
    @Override
    public short[] nextShortArray(int length) {
        final short[] array = new short[length];
        fill(array, 0, length);
        return array;
    }

    public void fill(@NotNull short[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        switch (source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
            case 0x2:
                Arrays.fill(array, fromIndex, toIndex, nextShort());
                break;
            case 0x3:
            case 0x4:
                fillGray(array, fromIndex, toIndex);
                break;
            case 0x5:
            case 0x6:
            case 0x7:
                fillLinear(array, fromIndex, toIndex);
                break;
            case 0x8:
            case 0x9:
                fillGeometric(array, fromIndex, toIndex);
            case 0xa:
            case 0xb:
            case 0xc:
                // segmented
            case 0xd:
            case 0xe:
                // periodic
            default:
                for (int index = fromIndex; index < toIndex; index++) {
                    array[index] = nextShort();
                }
                break;
        }
    }

    private void fillGray(@NotNull short[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = super.nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (short) ((value >> 1) ^ value); // binary to gray
        }
    }

    private void fillLinear(@NotNull short[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int addend = nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (short) value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull short[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int multiplier = nextInt() | 0x1;
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = (short) value;
            value += multiplier;
        }
    }

    private static final int[] SPECIAL_INT = {
            80,
            100,
            8080,
            10000,
            0x8badf00d,
            0x1badb002,
            0xb16b00b5,
            0xbaadf00d,
            0xbaddcafe,
            0xcafebabe,
            0xcafed00d,
            0xd15ea5e,
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
            0x0ff1ce,
            0x00bab10c,
            0xfaceb00c,
            0xdeadd00d,
            0xdabbad00,
            0x1ceb00da,
            0x67452301,
            0xefcdab89,
            0x98badcfe,
            0x10325476,
            0xc3d2e1f0,
            0x6a09e667,
            0xbb67ae85,
            0x3c6ef372,
            0xa54ff53a,
            0x510e527f,
            0x9b05688c,
            0x1f83d9ab,
            0x5be0cd19,
            0x8088405,
            0x343fd,
            0x43fd43fd,
            0x7fffffed,
            0x10dcd,
            0x10101,
            0x1010101,
    };

    private int specialInt() {
        final int value = SPECIAL_INT[nextInt(SPECIAL_INT.length)];
        switch (source.next(4)) { // from 0x0 to 0xf
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
                return Integer.rotateRight(value, source.next(5));
            default:
                return value;
        }
    }

    @Override
    public int nextInt() {
        switch (source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
                return 0;
            case 0x2:
            case 0x3:
                return -1;
            case 0x4:
                return 1;
            case 0x5:
                return Integer.MIN_VALUE;
            case 0x6:
                return Integer.MAX_VALUE;
            case 0x7:
                return 1 << source.next(5);
            case 0x8:
                return (1 << source.next(5)) - 1;
            case 0x9:
                return repeat2i(nextByte());
            case 0xa:
                return repeat2i(nextShort());
            case 0xb:
            case 0xc:
                return specialInt();
            default:
                return super.nextInt();
        }
    }

    private int repeat2i(byte value) {
        final int masked = 0xff & value;
        return (masked << 24) | (masked << 16) | (masked << 8) | masked;
    }

    private int repeat2i(short value) {
        final int masked = 0xffff & value;
        return (masked << 16) | masked;
    }

    @NotNull
    @Override
    public int[] nextIntArray(int length) {
        final int[] array = new int[length];
        fill(array, 0, length);
        return array;
    }

    public void fill(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        switch (source.next(4)) { // from 0x0 to 0xf
            case 0x0:
            case 0x1:
                Arrays.fill(array, fromIndex, toIndex, nextInt());
                break;
            case 0x2:
                fillGray(array, fromIndex, toIndex);
                break;
            case 0x3:
            case 0x4:
            case 0x5:
                fillLinear(array, fromIndex, toIndex);
                break;
            case 0x6:
            case 0x7:
                fillGeometric(array, fromIndex, toIndex);
                break;
            case 0x8:
            case 0x9:
            case 0xa:
                fillSegmented(array, fromIndex, toIndex);
                break;
            case 0xb:
            case 0xc:
                fillPeriodic(array, fromIndex, toIndex);
                break;
            default:
                for (int index = fromIndex; index < toIndex; index++) {
                    array[index] = nextInt();
                }
                break;
        }
    }

    private void fillGray(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = super.nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = ((value >> 1) ^ value); // binary to gray
        }
    }

    private void fillLinear(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int addend = nextInt();
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = value;
            value += addend;
        }
    }

    private void fillGeometric(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        int value = nextInt();
        final int multiplier = nextInt() | 0x1;
        for (int index = fromIndex; index < toIndex; index++) {
            array[index] = value;
            value += multiplier;
        }
    }

    private void fillSegmented(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        final int length = toIndex - fromIndex;
        if (length < 3) {
            for (int index = fromIndex; index < toIndex; index++) {
                array[index] = nextByte();
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
                fill(array, fromIndex + index0, fromIndex + index1);
            }
            index0 = index1;
        }
        assert index0 == length;
    }

    private void fillPeriodic(@NotNull int[] array, @Index(of = "array") int fromIndex, @Index(of = "array", inclusive = false) int toIndex) {
        final int length = toIndex - fromIndex;
        if (length < 3) {
            for (int index = fromIndex; index < toIndex; index++) {
                array[index] = nextByte();
            }
            return;
        }
        final int period = nextInt(2, length);
        int midIndex = fromIndex + period;
        fill(array, fromIndex, midIndex);
        final int copyIndex = fromIndex + period;
        for (int index0 = 0; index0 < period; index0++) {
            int value = array[fromIndex + index0];
            for (int index1 = copyIndex + index0; index1 < toIndex; index1 += period) {
                array[index1] = value;
            }
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
            0x6a09e667f3bcc908L, // SHA512 constant
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
        switch (source.next(4)) { // from 0x0 to 0xf
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
                return Long.rotateLeft(value, source.next(6));
            default:
                return value;
        }
    }

    @Override
    public long nextLong() {
        switch (source.next(5)) { // from 0x00 to 0x1f
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
                return Long.MIN_VALUE + 1L;
            case 0x0a:
                return 1L << source.next(6);
            case 0x0b:
                return (1L << source.next(6)) - 1L;
            case 0x10:
                return repeat2l(nextByte());
            case 0x11:
                return repeat2l(nextShort());
            case 0x12:
                return repeat2l(nextInt());
            case 0x13:
            case 0x14:
            case 0x15:
            case 0x16:
                return specialLong();
            default:
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
}
