package mujica.math.algebra.random;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * Created in existence on 2018/7/5, named WrappedRandom.
 * Created by Hiro in bone on 2018/11/6, named RandomX.
 * Created in va on 2020/7/20, named PrimitiveRandom.
 * Recreated in infrastructure on 2022/4/2.
 * Recreated in Ultramarine on 2022/5/26.
 * Moved here on 2025/3/3.
 */
public class RandomContext implements Serializable {

    private static final long serialVersionUID = 0x0cd1e2a8e072e5d5L;

    @NotNull
    protected RandomSource source;

    public RandomContext(@NotNull RandomSource source) {
        super();
        this.source = source;
    }

    public RandomContext() {
        this(new LocalRandomSource());
    }

    public boolean nextBoolean() {
        return source.next(1) != 0;
    }

    public byte nextByte() {
        return (byte) source.next(Byte.SIZE);
    }

    @NotNull
    public byte[] nextByteArray(int length) {
        final byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextByte();
        }
        return array;
    }

    public short nextShort() {
        return (short) source.next(Short.SIZE);
    }

    @NotNull
    public short[] nextShortArray(int length) {
        final short[] array = new short[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextShort();
        }
        return array;
    }

    public int nextInt() {
        return source.next(Integer.SIZE);
    }

    public int nextNonNegativeInt() {
        return source.next(Integer.SIZE - 1);
    }

    public int nextInt(int to) {
        if (to < 2) {
            if (to < 1) {
                throw new ArithmeticException("Random range");
            }
            return 0;
        }
        int x;
        while (true) {
            x = nextNonNegativeInt();
            if (x + to > 0) {
                break;
            }
            if (x + (Integer.MAX_VALUE % to + 1) % to > 0) {
                break;
            }
        }
        return x % to;
    }

    public int nextInt(int from, int to) {
        if (from < to) {
            final int range = to - from;
            if (range > 0) {
                return from + nextInt(range);
            } else {
                while (true) {
                    int x = nextInt();
                    if (from <= x && x < to) {
                        return x;
                    }
                }
            }
        }
        throw new ArithmeticException("Random range");
    }

    private static final double EPS = 0x1p-16;

    /**
     * Range : x >= 0
     * PDF = C(n, x) * p ^ x * (1 - p) ^ (n - x)
     * mean = n * p
     * variance = n * p * (1 - p)
     */
    public int nextBinomial(int n, double p) {
        int x = 0;
        if (Math.abs(p - 0.5) < EPS) {
            while (n > Integer.SIZE) {
                x += Integer.bitCount(nextInt());
                n -= Integer.SIZE;
            }
            if (n > 0) {
                x += Integer.bitCount(source.next(n));
            }
        } else {
            while (n > 0) {
                if (nextDouble() < p) {
                    x++;
                }
                n--;
            }
        }
        return x;
    }

    /**
     * Range : x >= 0
     * PDF = p * (1 - p) ^ x
     * mean = 1 / p - 1
     * variance = (1 - p) / (p * p)
     */
    public int nextGeometric(double p) {
        final int ip = (int) (Integer.MAX_VALUE * p);
        if (ip <= 0) {
            throw new IllegalArgumentException();
        }
        int x = 0;
        while (nextNonNegativeInt() > ip) {
            x++;
        }
        return x;
    }

    public int nextGeometric(int n, double p) {
        return nextGeometric(p) % n;
    }

    public int nextDefaultGeometric(int n) {
        if (n > 1) {
            return nextGeometric(-Math.expm1(Math.E / (1 - n))) % n;
        } else if (n < 1) {
            throw new IllegalArgumentException();
        } else {
             return 0;
        }
    }

    /**
     * Range : x >= 0
     * PDF = lambda ^ x * e ^ -lambda / x!
     * mean = lambda
     * variance = lambda
     */
    public int nextPoisson(double lambda) {
        final double limit = Math.exp(-lambda);
        double p = nextDouble();
        int x = 0;
        while (p > limit) {
            p *= nextDouble();
            x++;
        }
        return x;
    }

    public int nextMinInt(int to, int count) {
        if (count <= 0) {
            return Integer.MAX_VALUE;
        }
        if (to < 1) {
            throw new ArithmeticException("Random range");
        }
        if (to > 1) {
            int min = Integer.MAX_VALUE;
            int g = (Integer.MAX_VALUE % to + 1) % to;
            for (int i = 0; i < count; i++) {
                int x;
                do {
                    x = nextNonNegativeInt();
                } while (x + g <= 0);
                min = Math.min(min, x % to);
            }
            return min;
        } else {
            return 0;
        }
    }

    @NotNull
    public int[] nextIntArray(int length) {
        final int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextInt();
        }
        return array;
    }

    @NotNull
    public int[] nextIntArray(int to, int length) {
        if (to < 1) {
            throw new ArithmeticException("Random range");
        }
        final int[] array = new int[length];
        if (to > 1) {
            int g = (Integer.MAX_VALUE % to + 1) % to;
            for (int i = 0; i < length; i++) {
                int x;
                do {
                    x = nextNonNegativeInt();
                } while (x + g <= 0);
                array[i] = x % to;
            }
        }
        return array;
    }

    @NotNull
    public int[] nextIntArray(int from, int to, int length) {
        if (from < to) {
            final int[] array = new int[length];
            final int range = to - from;
            if (range > 1) {
                int g = (Integer.MAX_VALUE % to + 1) % to;
                for (int i = 0; i < length; i++) {
                    int x;
                    do {
                        x = nextNonNegativeInt();
                    } while (x + g <= 0);
                    array[i] = x % to;
                }
            } else if (range < 0) {
                for (int i = 0; i < length; i++) {
                    while (true) {
                        int x = nextInt();
                        if (from <= x && x < to) {
                            array[i] = x;
                            break;
                        }
                    }
                }
            }
            return array;
        }
        throw new ArithmeticException("Random range");
    }

    public void shuffledCardinal(@NotNull int[] array) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            int j = nextInt(i + 1);
            if (i != j) {
                array[i] = array[j];
                array[j] = i;
            } else {
                array[i] = i;
            }
        }
    }

    @NotNull
    public int[] shuffledCardinal(int length) {
        final int[] array = new int[length];
        shuffledCardinal(array);
        return array;
    }

    public void distribute(@NotNull int[] array, int sum) {
        final int to = array.length;
        if (to < 1) {
            if (sum > 0) {
                throw new IllegalArgumentException();
            }
        } else if (to > 1) {
            if (sum > 0) {
                int g = (Integer.MAX_VALUE % to + 1) % to;
                do {
                    int x;
                    do {
                        x = nextNonNegativeInt();
                    } while (x + g <= 0);
                    array[x % to]++;
                } while (--sum > 0);
            }
        } else {
            array[0] += sum;
        }
    }

    @NotNull
    public int[] distributed(int length, int sum) {
        final int[] array = new int[length];
        distribute(array, sum);
        return array;
    }

    public void redistribute(@NotNull int[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
            array[i] = 0;
        }
        distribute(array, sum);
    }

    public long nextLong() {
        return (((long) source.next(Integer.SIZE)) << Integer.SIZE) | (source.next(Integer.SIZE) & 0xffffffffL);
    }

    public long nextNonNegativeLong() {
        return (((long) source.next(Integer.SIZE - 1)) << Integer.SIZE) | (source.next(Integer.SIZE) & 0xffffffffL);
    }

    public long nextLong(long to) {
        if (to < 2) {
            if (to < 1) {
                throw new ArithmeticException("Random range");
            }
            return 0;
        }
        long x;
        while (true) {
            x = nextNonNegativeLong();
            if (x + to > 0) {
                break;
            }
            if (x + (Long.MAX_VALUE % to + 1) % to > 0) {
                break;
            }
        }
        return x % to;
    }

    public long nextLong(long from, long to) {
        if (from < to) {
            final long range = to - from;
            if (range > 0) {
                return from + nextLong(range);
            } else {
                while (true) {
                    long x = nextLong();
                    if (from <= x && x < to) {
                        return x;
                    }
                }
            }
        }
        throw new ArithmeticException("Random range");
    }

    @NotNull
    public long[] nextLongArray(int length) {
        final long[] array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextLong();
        }
        return array;
    }

    @NotNull
    public long[] nextLongArray(long to, int length) {
        if (to < 1) {
            throw new ArithmeticException("Random range");
        }
        final long[] array = new long[length];
        if (to > 1) {
            long g = (Long.MAX_VALUE % to + 1) % to;
            for (int i = 0; i < length; i++) {
                long x;
                do {
                    x = nextNonNegativeLong();
                } while (x + g <= 0);
                array[i] = x % to;
            }
        }
        return array;
    }

    @NotNull
    public long[] nextLongArray(long from, long to, int length) {
        if (from < to) {
            final long[] array = new long[length];
            final long range = to - from;
            if (range > 1) {
                long g = (Long.MAX_VALUE % to + 1) % to;
                for (int i = 0; i < length; i++) {
                    long x;
                    do {
                        x = nextNonNegativeLong();
                    } while (x + g <= 0);
                    array[i] = x % to;
                }
            } else if (range < 0) {
                for (int i = 0; i < length; i++) {
                    while (true) {
                        long x = nextLong();
                        if (from <= x && x < to) {
                            array[i] = x;
                            break;
                        }
                    }
                }
            }
            return array;
        }
        throw new ArithmeticException("Random range");
    }

    private int nextFloatMantissa() {
        return source.next(24);
    }

    protected static final float UNIT_FLOAT = 0x1.0p-24f;

    public float nextFloat() {
        return UNIT_FLOAT * nextFloatMantissa();
    }

    public float nextFloat(float from, float to) {
        return from + (to - from) * nextFloat();
    }

    @NotNull
    public float[] nextFloatArray(int length) {
        final float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextFloat();
        }
        return array;
    }

    private long nextDoubleMantissa() {
        return ((long) (source.next(26)) << 27) + source.next(27);
    }

    protected static final double UNIT_DOUBLE = 0x1.0p-53d;

    /**
     * PDF = 1.0 (0.0 <= x < 1.0)
     */
    public double nextDouble() {
        return UNIT_DOUBLE * nextDoubleMantissa();
    }

    public double nextDouble(double from, double to) {
        return from + (to - from) * nextDouble();
    }

    /**
     * PDF = 2.0 * x (0.0 <= x < 1.0)
     */
    public double nextSlope() {
        return UNIT_DOUBLE * Math.min(nextDoubleMantissa(), nextDoubleMantissa());
    }

    /**
     * PDF = 4.0 * x (0.0 <= x < 0.5)
     * PDF = 4.0 - 4.0 * x (0.5 <= x < 1.0)
     */
    public double nextUnitHill() {
        return (0.5 * UNIT_DOUBLE) * (nextDoubleMantissa() + nextDoubleMantissa());
    }

    /**
     * PDF = 1.0 + x (-1.0 <= x < 0.0)
     * PDF = 1.0 - x (0.0 <= x < 1.0)
     */
    public double nextBipolarHill() {
        return UNIT_DOUBLE * (nextDoubleMantissa() - nextDoubleMantissa());
    }

    public static long min(long a, long b, long c) {
        return Math.min(a, Math.min(b, c));
    }

    public double nextQuadraticSlope() {
        return UNIT_DOUBLE * min(nextDoubleMantissa(), nextDoubleMantissa(), nextDoubleMantissa());
    }

    public static long median(long a, long b, long c) {
        return min(Math.max(a, b), Math.max(b, c), Math.max(c, a));
    }

    /**
     * PDF = 6.0 * x * (1.0 - x) (0.0 <= x < 1.0)
     */
    public double nextQuadraticHill() {
        return UNIT_DOUBLE * median(nextDoubleMantissa(), nextDoubleMantissa(), nextDoubleMantissa());
    }

    public double nextGaussian() {
        double x, y, s;
        do {
            x = 2.0 * nextDouble() - 1.0;
            y = 2.0 * nextDouble() - 1.0;
            s = x * x + y * y;
        } while (s >= 1.0 || s == 0.0);
        double multiplier = StrictMath.sqrt(-2.0 * Math.log(s) / s);
        return multiplier * x;
    }

    @NotNull
    public double[] nextDoubleArray(int length) {
        final double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = nextDouble();
        }
        return array;
    }

    @NotNull
    public double[] nextGaussianArray(int length) {
        final double[] array = new double[length];
        for (int i = 1; i < length; i += 2) {
            double x, y, s;
            do {
                x = 2.0 * nextDouble() - 1.0;
                y = 2.0 * nextDouble() - 1.0;
                s = x * x + y * y;
            } while (s >= 1.0 || s == 0.0);
            double multiplier = StrictMath.sqrt(-2.0 * Math.log(s) / s);
            array[i - 1] = multiplier * x;
            array[i] = multiplier * y;
        }
        if ((length & 0x1) != 0) {
            array[length - 1] = nextGaussian();
        }
        return array;
    }

    @NotNull
    public BigInteger nextBigInteger(int bitLength) {
        final int byteCount = (bitLength + Byte.SIZE - 1) >> 3;
        final byte[] bytes = new byte[byteCount];
        for (int index = 0; index < byteCount; index++) {
            bytes[index] = (byte) source.next(Byte.SIZE);
        }
        bytes[0] &= 0xff >> ((byteCount << 3) - bitLength);
        return new BigInteger(1, bytes);
    }

    @NotNull
    public BigInteger nextBigInteger(@NotNull BigInteger to) {
        return BigInteger.ZERO;
    }

    @NotNull
    public BigInteger nextBigInteger(@NotNull BigInteger from, @NotNull BigInteger to) {
        return BigInteger.ZERO;
    }

    /**
     * Added in 2022/7/27
     * For HashConsumer unit tests
     */
    public <T> void rotateList(@NotNull List<T> list) {
        final int size = list.size();
        if (size > 1) {
            int shift = nextInt(size);
            if (shift != 0) {
                ArrayList<T> temp = new ArrayList<>(list.subList(shift, size));
                temp.addAll(list.subList(0, shift));
                list.clear();
                list.addAll(temp);
            }
        }
    }

    /**
     * Added in 2022/7/27
     * For HashConsumer unit tests
     */
    @NotNull
    public <T> ArrayList<T> rotateCopy(@NotNull Collection<T> collection) {
        final int size = collection.size();
        if (size > 1) {
            int shift = nextInt(size);
            if (shift != 0) {
                ArrayList<T> list = new ArrayList<>(size);
                for (int i = 0; i < shift; i++) {
                    list.add(null);
                }
                Iterator<T> iterator = collection.iterator();
                for (int i = shift; i < size; i++) {
                    list.add(iterator.next());
                }
                for (int i = 0; i < shift; i++) {
                    list.set(i, iterator.next());
                }
                return list;
            }
        }
        return new ArrayList<>(collection);
    }

    public <T> void shuffleArray(@NotNull T[] array) {
        final int length = array.length;
        if (length > 1) {
            for (int i = 1; i < length; i++) {
                int j = nextInt(i + 1);
                if (i != j) {
                    T t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
    }

    public <T> void shuffleArray(@NotNull T[] array, int fromIndex, int toIndex) {
        final int length = toIndex - fromIndex;
        if (length > 1) {
            for (int i = 1; i < length; i++) {
                int j = nextInt(i + 1);
                if (i != j) {
                    T t = array[fromIndex + i];
                    array[fromIndex + i] = array[fromIndex + j];
                    array[fromIndex + j] = t;
                }
            }
        }
    }

    public void shuffleArray(@NotNull int[] array) {
        final int length = array.length;
        if (length > 1) {
            for (int i = 1; i < length; i++) {
                int j = nextInt(i + 1);
                if (i != j) {
                    int t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
    }

    public void shuffleArray(@NotNull long[] array) {
        final int length = array.length;
        if (length > 1) {
            for (int i = 1; i < length; i++) {
                int j = nextInt(i + 1);
                if (i != j) {
                    long t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
    }

    public <T> void shuffleList(@NotNull List<T> list) {
        final int size = list.size();
        if (size > 1) {
            for (int i = 1; i < size; i++) {
                int j = nextInt(i + 1);
                if (i != j) {
                    list.set(i, list.set(j, list.get(i)));
                }
            }
        }
    }

    /**
     * Added in 2022/7/27
     * For HashConsumer unit tests
     */
    @NotNull
    public <T> ArrayList<T> shuffleCopy(@NotNull Collection<T> collection) {
        final ArrayList<T> list = new ArrayList<>(collection);
        shuffleList(list);
        return list;
    }

    @NotNull
    public <T> Comparator<T> shuffleComparator() {
        return new ShuffleComparator<>();
    }

    private class ShuffleComparator<T> implements Comparator<T>, Serializable {

        private static final long serialVersionUID = 0x6e5d1a3ba8fe0ec2L;

        @SuppressWarnings("ComparatorMethodParameterNotUsed")
        @Override
        public int compare(T a, T b) {
            return nextBoolean() ? 1 : -1;
        }

        @Override
        public Comparator<T> reversed() {
            return this;
        }

        @Override
        public Comparator<T> thenComparing(Comparator<? super T> other) {
            return this; // this can compare any two, never equal
        }

        @Override
        public int hashCode() {
            return (int) serialVersionUID;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ShuffleComparator;
        }
    }

    public <T> T select(@NotNull T[] array) {
        return array[nextInt(array.length)];
    }

    public <T> T select(@NotNull T[] array, @Nullable T fallback) {
        final int length = array.length;
        if (length == 0) {
            return fallback;
        }
        return array[nextInt(length)];
    }

    public <T> T select(@NotNull Iterable<T> iterable, @Nullable T fallback) {
        int count = 0;
        for (T t : iterable) {
            if (nextInt(++count) == 0) {
                fallback = t;
            }
        }
        return fallback;
    }

    public <T> T selectPolynomial(@NotNull T[] array, int order) {
        int index = array.length;
        if (order > 0) {
            index = nextMinInt(index, 1 + order);
        } else if (order < 0) {
            index = index - 1 - nextMinInt(index, 1 - order);
        } else {
            index = nextInt(index);
        }
        return array[index];
    }

    public <T> T selectBinomial(@NotNull T[] array, double p) {
        return array[nextBinomial(array.length, p)];
    }

    public <T> T selectGeometric(@NotNull T[] array, double p) {
        return array[nextGeometric(array.length, p)];
    }

    public <T> T selectDefaultGeometric(@NotNull T[] array) {
        return array[nextDefaultGeometric(array.length)];
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void randomizeHashCodes() {
        for (int i = 0xfff & nextInt(); i > 0; i--) {
            (new Object()).hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RandomContext && this.source == ((RandomContext) obj).source;
    }
}
