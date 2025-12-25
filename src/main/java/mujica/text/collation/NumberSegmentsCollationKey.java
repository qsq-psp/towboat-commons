package mujica.text.collation;

import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.text.CollationKey;

@CodeHistory(date = "2023/3/30", project = "LeetInAction", name = "IntegralCollationKey")
@CodeHistory(date = "2023/4/18", project = "Ultramarine")
@CodeHistory(date = "2025/3/4")
public class NumberSegmentsCollationKey extends CollationKey {

    static int parseDigit(int ch, int radix) {
        if ('0' <= ch) {
            if (radix <= 10) {
                if (ch < '0' + radix) {
                    return ch - '0';
                }
            } else {
                if (ch <= '9') {
                    return ch - '0';
                } else if ('A' <= ch && ch < Base16Case.UPPER + radix) {
                    return ch - Base16Case.UPPER;
                } else if ('a' <= ch && ch < Base16Case.LOWER + radix) {
                    return ch - Base16Case.LOWER;
                }
            }
        }
        return -1;
    }

    @NotNull
    static String toZero(@NotNull String source, int radix) {
        final int length = source.length();
        StringBuilder sb = null;
        int startIndex = 0; // not-number state
        for (int endIndex = 0; endIndex < length; endIndex++) {
            if (parseDigit(source.charAt(endIndex), radix) == -1) {
                if (startIndex == -1) {
                    sb.append('0');
                    startIndex = endIndex; // not-number state
                }
            } else {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                if (startIndex != -1) {
                    sb.append(source, startIndex, endIndex);
                    startIndex = -1; // number state
                }
            }
        }
        if (sb == null) {
            return source;
        } else if (startIndex == -1) {
            sb.append('0');
        } else {
            sb.append(source, startIndex, length);
        }
        return sb.toString();
    }

    private final int radix;

    private final String zero;

    NumberSegmentsCollationKey(@NotNull String source, int radix) {
        super(source);
        this.radix = radix;
        this.zero = toZero(source, radix);
    }

    public int weakCompareTo(@NotNull NumberSegmentsCollationKey that) {
        return this.zero.compareTo(that.zero);
    }

    @CodeHistory(date = "2025/11/1")
    private abstract class NumberSegment implements Comparable<NumberSegment>, Serializable {

        private static final long serialVersionUID = 0xcd15b301952adbbbL;

        final int charCount;

        NumberSegment(int charCount) {
            this.charCount = charCount;
        }

        @Override
        public abstract int compareTo(@NotNull NumberSegment that);

        abstract int compareToInt(@NotNull IntNumberSegment that);

        abstract int compareToLong(@NotNull LongNumberSegment that);

        abstract int compareToBigInteger(@NotNull BigIntegerNumberSegment that);

        abstract int byteCount();

        abstract void fillBytes(@NotNull byte[] array, int offset, int length);
    }

    @CodeHistory(date = "2025/11/1")
    private class IntNumberSegment extends NumberSegment {

        private static final long serialVersionUID = 0x4dae4f9d9efba05bL;

        final int intValue;

        IntNumberSegment(int charCount, int intValue) {
            super(charCount);
            this.intValue = intValue;
        }

        @Override
        public int compareTo(@NotNull NumberSegment that) {
            return -that.compareToInt(this);
        }

        @Override
        int compareToInt(@NotNull IntNumberSegment that) {
            return Integer.compare(this.intValue, that.intValue);
        }

        @Override
        int compareToLong(@NotNull LongNumberSegment that) {
            return Long.compare(this.intValue, that.longValue);
        }

        @Override
        int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return BigInteger.valueOf(this.intValue).compareTo(that.bigIntegerValue);
        }

        @Override
        int byteCount() {
            int value = intValue;
            for (int count = 0; count < 4; count++) {
                if (value == 0) {
                    return count;
                }
                value >>>= Byte.SIZE;
            }
            return 4;
        }

        @Override
        void fillBytes(@NotNull byte[] array, int offset, int length) {
            for (int index = length - 1; index >= 0; index--) {
                array[offset++] = (byte) (intValue >>> (index << 3));
            }
        }
    }

    @CodeHistory(date = "2025/11/1")
    private class LongNumberSegment extends NumberSegment {

        private static final long serialVersionUID = 0xefb27b05cd6beae1L;

        final long longValue;

        LongNumberSegment(int charCount, long longValue) {
            super(charCount);
            this.longValue = longValue;
        }

        @Override
        public int compareTo(@NotNull NumberSegment that) {
            return -that.compareToLong(this);
        }

        @Override
        int compareToInt(@NotNull IntNumberSegment that) {
            return Long.compare(this.longValue, that.intValue);
        }

        @Override
        int compareToLong(@NotNull LongNumberSegment that) {
            return Long.compare(this.longValue, that.longValue);
        }

        @Override
        int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return BigInteger.valueOf(this.longValue).compareTo(that.bigIntegerValue);
        }

        @Override
        int byteCount() {
            long value = longValue;
            for (int count = 0; count < 8; count++) {
                if (value == 0L) {
                    return count;
                }
                value >>>= Byte.SIZE;
            }
            return 8;
        }

        @Override
        void fillBytes(@NotNull byte[] array, int offset, int length) {
            for (int index = length - 1; index >= 0; index--) {
                array[offset++] = (byte) (longValue >>> (index << 3));
            }
        }
    }

    @CodeHistory(date = "2025/11/1")
    private class BigIntegerNumberSegment extends NumberSegment {

        private static final long serialVersionUID = 0x3058fb127b447bfbL;

        @NotNull
        final BigInteger bigIntegerValue;

        BigIntegerNumberSegment(int charCount, @NotNull BigInteger bigIntegerValue) {
            super(charCount);
            this.bigIntegerValue = bigIntegerValue;
        }

        @Override
        public int compareTo(@NotNull NumberSegment that) {
            return -that.compareToBigInteger(this);
        }

        @Override
        int compareToInt(@NotNull IntNumberSegment that) {
            return this.bigIntegerValue.compareTo(BigInteger.valueOf(that.intValue));
        }

        @Override
        int compareToLong(@NotNull LongNumberSegment that) {
            return this.bigIntegerValue.compareTo(BigInteger.valueOf(that.longValue));
        }

        @Override
        int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return this.bigIntegerValue.compareTo(that.bigIntegerValue);
        }

        @Override
        int byteCount() {
            return (bigIntegerValue.bitLength() + 7) >>> 3; // exclude sign bit
        }

        @Override
        void fillBytes(@NotNull byte[] dst, int offset, int length) {
            final byte[] src = bigIntegerValue.toByteArray(); // include sign bit
            System.arraycopy(src, src.length - length, dst, offset, length);
        }
    }

    private transient SoftReference<NumberSegment[]> segments;

    private int calculateNumberSegmentCount() {
        int count = 0;
        for (int index = zero.length() - 1; index >= 0; index--) {
            if (zero.charAt(index) == '0') {
                count++;
            }
        }
        return count;
    }

    @NotNull
    private NumberSegment parse(@NotNull String source, int startIndex, int endIndex) {
        final int charCount = endIndex - startIndex;
        if (charCount < 6) {
            return new IntNumberSegment(
                    charCount,
                    Integer.parseInt(source, startIndex, endIndex, radix)
            );
        } else if (charCount < 13) {
            return new LongNumberSegment(
                    charCount,
                    Long.parseLong(source, startIndex, endIndex, radix)
            );
        } else {
            return new BigIntegerNumberSegment(
                    charCount,
                    new BigInteger(source.substring(startIndex, endIndex), radix)
            );
        }
    }

    @NotNull
    private NumberSegment[] getSegments() {
        NumberSegment[] array;
        {
            SoftReference<NumberSegment[]> reference = segments;
            if (reference != null) {
                array = reference.get();
                if (array != null) {
                    return array;
                }
            }
        }
        array = new NumberSegment[calculateNumberSegmentCount()];
        int numberIndex = 0;
        final String source = getSourceString();
        final int sourceLength = source.length();
        int startIndex = -1; // not-number state
        for (int endIndex = 0; endIndex < sourceLength; endIndex++) {
            if (parseDigit(source.charAt(endIndex), radix) == -1) {
                if (startIndex != -1) {
                    array[numberIndex++] = parse(source, startIndex, endIndex);
                    startIndex = -1; // not-number state
                }
            } else {
                if (startIndex == -1) {
                    startIndex = endIndex; // number state
                }
            }
        }
        if (startIndex != -1) {
            array[numberIndex++] = parse(source, startIndex, sourceLength);
        }
        assert numberIndex == array.length;
        segments = new SoftReference<>(array);
        return array;
    }

    static int normalCompare(@NotNull NumberSegment[] segmentsA, @NotNull NumberSegment[] segmentsB) {
        final int length = Math.min(segmentsA.length, segmentsB.length);
        for (int index = 0; index < length; index++) {
            int result = segmentsA[index].compareTo(segmentsB[index]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public int normalCompareTo(@NotNull NumberSegmentsCollationKey that) {
        final int result = this.zero.compareTo(that.zero);
        if (result != 0) {
            return result;
        }
        return normalCompare(this.getSegments(), that.getSegments());
    }

    static int strongCompare(@NotNull NumberSegment[] segmentsA, @NotNull NumberSegment[] segmentsB) {
        final int length = Math.min(segmentsA.length, segmentsB.length);
        for (int index = 0; index < length; index++) {
            int result = Integer.compare(segmentsA[index].charCount, segmentsB[index].charCount);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public int strongCompareTo(@NotNull NumberSegmentsCollationKey that) {
        int result = this.zero.compareTo(that.zero);
        if (result != 0) {
            return result;
        }
        result = normalCompare(this.getSegments(), that.getSegments());
        if (result != 0) {
            return result;
        }
        result = Integer.compare(this.radix, that.radix);
        if (result != 0) {
            return result;
        }
        return strongCompare(this.getSegments(), that.getSegments());
    }

    @Override
    public int compareTo(@NotNull CollationKey target) {
        return strongCompareTo((NumberSegmentsCollationKey) target);
    }

    private static void fillInt(int value, @NotNull byte[] array, int offset) {
        for (int shift = 24; shift >= 0; shift -= 8) {
            array[offset++] = (byte) (value >>> shift);
        }
    }

    @Override
    public byte[] toByteArray() {
        final NumberSegment[] segments = getSegments();
        final int segmentCount = segments.length;
        final int zeroLength = zero.length();
        int position = zeroLength * 3 + 1;
        final int[] segmentByteCounts = new int[segmentCount];
        for (int index = 0; index < segmentCount; index++) {
            int byteCount = segments[index].byteCount();
            position += 4 + byteCount;
            segmentByteCounts[index] = byteCount;
        }
        position += 1 + 4 * segmentCount;
        final byte[] array = new byte[position];
        position = 0;
        {
            int index = 0;
            while (true) {
                if (index >= zeroLength) {
                    array[position++] = 0;
                    break;
                }
                int ch = zero.charAt(index++);
                array[position++] = 1;
                array[position++] = (byte) (ch >> 8);
                array[position++] = (byte) (ch);
            }
        }
        for (int index = 0; index < segmentCount; index++) {
            int byteCount = segmentByteCounts[index];
            fillInt(byteCount, array, position);
            position += 4;
            segments[index].fillBytes(array, position, byteCount);
            position += byteCount;
        }
        array[position++] = (byte) radix;
        for (NumberSegment segment : segments) {
            fillInt(segment.charCount, array, position);
        }
        return array;
    }
}
