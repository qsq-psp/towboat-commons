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
    abstract class NumberSegment implements Comparable<NumberSegment>, Serializable {

        final int charCount;

        NumberSegment(int charCount) {
            this.charCount = charCount;
        }

        @Override
        public abstract int compareTo(@NotNull NumberSegment that);

        public abstract int compareToInt(@NotNull IntNumberSegment that);

        public abstract int compareToLong(@NotNull LongNumberSegment that);

        public abstract int compareToBigInteger(@NotNull BigIntegerNumberSegment that);
    }

    @CodeHistory(date = "2025/11/1")
    class IntNumberSegment extends NumberSegment {

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
        public int compareToInt(@NotNull IntNumberSegment that) {
            return Integer.compare(this.intValue, that.intValue);
        }

        @Override
        public int compareToLong(@NotNull LongNumberSegment that) {
            return Long.compare(this.intValue, that.longValue);
        }

        @Override
        public int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return BigInteger.valueOf(this.intValue).compareTo(that.bigIntegerValue);
        }
    }

    @CodeHistory(date = "2025/11/1")
    class LongNumberSegment extends NumberSegment {

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
        public int compareToInt(@NotNull IntNumberSegment that) {
            return Long.compare(this.longValue, that.intValue);
        }

        @Override
        public int compareToLong(@NotNull LongNumberSegment that) {
            return Long.compare(this.longValue, that.longValue);
        }

        @Override
        public int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return BigInteger.valueOf(this.longValue).compareTo(that.bigIntegerValue);
        }
    }

    @CodeHistory(date = "2025/11/1")
    class BigIntegerNumberSegment extends NumberSegment {

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
        public int compareToInt(@NotNull IntNumberSegment that) {
            return this.bigIntegerValue.compareTo(BigInteger.valueOf(that.intValue));
        }

        @Override
        public int compareToLong(@NotNull LongNumberSegment that) {
            return this.bigIntegerValue.compareTo(BigInteger.valueOf(that.longValue));
        }

        @Override
        public int compareToBigInteger(@NotNull BigIntegerNumberSegment that) {
            return this.bigIntegerValue.compareTo(that.bigIntegerValue);
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
                    new BigInteger(source, radix)
            );
        }
    }

    @NotNull
    NumberSegment[] getSegments() {
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

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
