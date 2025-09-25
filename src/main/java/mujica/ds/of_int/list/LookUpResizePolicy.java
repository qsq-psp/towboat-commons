package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@CodeHistory(date = "2025/7/2")
public class LookUpResizePolicy extends ResizePolicy {

    private static final long serialVersionUID = 0x0ab3151d3950e646L;

    @NotNull
    final int[] array;

    final boolean isPrime;

    final boolean isQuadraticFull;

    protected LookUpResizePolicy(@NotNull int[] array, boolean isPrime, boolean isQuadraticFull) {
        super();
        this.array = array;
        this.isPrime = isPrime;
        this.isQuadraticFull = isQuadraticFull;
    }

    protected LookUpResizePolicy(@NotNull int[] array) {
        this(array, false, false);
    }

    @Override
    public int intLength() {
        return array.length;
    }

    @Override
    public int getInt(int index) {
        return array[index];
    }

    @Override
    public int notSmallerCapacity(int minCapacity) {
        int index = Arrays.binarySearch(array, minCapacity);
        if (index < 0) {
            index = -(index + 1);
        }
        try {
            return array[index];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int nextCapacity(int currentCapacity) {
        int index = Arrays.binarySearch(array, currentCapacity);
        if (index < 0) {
            index = -(index + 1);
        } else {
            index++;
        }
        if (index == array.length) {
            index--;
        }
        return array[index];
    }

    @Override
    public boolean notPrime() {
        return !isPrime;
    }

    @Override
    public boolean notQuadraticFull() {
        return !isQuadraticFull;
    }

    private static final long LIMIT = Integer.MAX_VALUE + 1L;

    @NotNull
    public static LookUpResizePolicy geometric(double initial, double multiplier) {
        if (!(multiplier > 1.0)) {
            throw new IllegalArgumentException();
        }
        final CopyOnResizeIntList intList = new CopyOnResizeIntList(null);
        int value0 = 0;
        while (initial < LIMIT) {
            int value1 = (int) initial;
            if (value0 >= value1) {
                throw new IllegalArgumentException();
            }
            value0 = value1;
            intList.offerLast(value1);
            initial *= multiplier;
        }
        return new LookUpResizePolicy(intList.toArray());
    }

    public static final ResizePolicy PAPER = geometric(3.0, Math.sqrt(2.0)); // 1.414...

    public static final ResizePolicy GOLDEN = geometric(Math.sqrt(10.0), 0.5 * (1.0 + Math.sqrt(5.0))); // 1.618...

    public static final ResizePolicy NATURAL = geometric(20.0, Math.E); // 2.71828...

    private static final int[] PRIME_PAPER_INTERNAL_DATA = {
            13, 19, 29, 43, 61, 89, 127, 181, 257, 367, 521, 739, 1049, 1487, 2111, 2999, 4243, 6007, 8501, 12037,
            17027, 24083, 34061, 48179, 68141, 96377, 136303, 192767, 272621, 385559, 545267, 771143, 1090577, 1542347,
            2181209, 3084721, 4362461, 6169463, 8724943, 12338933, 17449891, 24677893, 34899853, 49355881, 69799759,
            98711771, 139599527, 197423557, 279199073, 394847149, 558398221, 789694343, 1116796477, 1579388761
    };

    public static final ResizePolicy PRIME_PAPER = new LookUpResizePolicy(PRIME_PAPER_INTERNAL_DATA, true, false);

    private static final int[] PRIME_GOLDEN_INTERNAL_DATA = {
            19, 31, 53, 89, 149, 251, 409, 673, 1091, 1777, 2879, 4663, 7547, 12227, 19793, 32027, 51827, 83869, 135719,
            219599, 355321, 574933, 930269, 1505209, 2435501, 3940747, 6376303, 10317077, 16693387, 27010483, 43703921,
            70714471, 114418471, 185132977, 299551463, 484684457, 784235929, 1268920403, 2053156423
    };

    public static final ResizePolicy PRIME_GOLDEN = new LookUpResizePolicy(PRIME_GOLDEN_INTERNAL_DATA, true, false);

    private static final int[] PRIME_FIBONACCI_INTERNAL_DATA = {
            5, 11, 17, 29, 47, 79, 127, 211, 347, 563, 911, 1481, 2393, 3877, 6271, 10151, 16427, 26591, 43019, 69623,
            112643, 182279, 294923, 477209, 772139, 1249361, 2021501, 3270863, 5292367, 8563237, 13855607, 22418849,
            36274471, 58693331, 94967809, 153661163, 248628997, 402290201, 650919223, 1053209459, 1704128729
    };

    public static final ResizePolicy PRIME_FIBONACCI = new LookUpResizePolicy(PRIME_FIBONACCI_INTERNAL_DATA, true, false);
}
