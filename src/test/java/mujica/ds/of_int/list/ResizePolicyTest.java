package mujica.ds.of_int.list;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2025/7/1")
public class ResizePolicyTest {

    private static final int REPEAT = 30;

    private static final int SIZE = 600;

    private final RandomContext rc = new RandomContext();

    private void checkIteratorOrder(@NotNull ResizePolicy policy) {
        int i = 0;
        int v0 = -1;
        for (int v1 : policy) {
            Assert.assertTrue(v0 < v1);
            v0 = v1;
            if (++i >= SIZE) {
                break;
            }
        }
    }

    @Test
    public void checkIteratorOrder() {
        checkIteratorOrder(LookUpResizePolicy.PAPER);
        checkIteratorOrder(LookUpResizePolicy.GOLDEN);
        checkIteratorOrder(LookUpResizePolicy.NATURAL);
        checkIteratorOrder(LookUpResizePolicy.PRIME_PAPER);
        checkIteratorOrder(LookUpResizePolicy.PRIME_GOLDEN);
        checkIteratorOrder(LookUpResizePolicy.PRIME_FIBONACCI);
        checkIteratorOrder(ShiftResizePolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new TwiceResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new HalfResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new Order1ResizePolicy(rc));
        }
        checkIteratorOrder(ModuloLookUpResizePolicy.INSTANCE_59);
        checkIteratorOrder(ModuloLookUpResizePolicy.INSTANCE_67);
    }

    private void checkIndexedOrder(@NotNull ResizePolicy policy) {
        final int[] array = rc.nextIntArray(policy.intLength(), SIZE);
        Arrays.sort(array);
        int writeIndex = 0;
        {
            int value0 = -1;
            for (int readIndex = 0; readIndex < SIZE; readIndex++) {
                int value1 = array[readIndex];
                if (value0 != value1) {
                    array[writeIndex++] = value1;
                    value0 = value1;
                }
            }
        }
        {
            int value0 = -1;
            for (int readIndex = 0; readIndex < writeIndex; readIndex++) {
                int value1 = policy.getInt(array[readIndex]);
                Assert.assertTrue(value0 < value1);
                value0 = value1;
            }
        }
    }

    @Test
    public void checkIndexedOrder() {
        checkIndexedOrder(LookUpResizePolicy.PAPER);
        checkIndexedOrder(LookUpResizePolicy.GOLDEN);
        checkIndexedOrder(LookUpResizePolicy.NATURAL);
        checkIndexedOrder(LookUpResizePolicy.PRIME_PAPER);
        checkIndexedOrder(LookUpResizePolicy.PRIME_GOLDEN);
        checkIndexedOrder(LookUpResizePolicy.PRIME_FIBONACCI);
        checkIndexedOrder(ShiftResizePolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new TwiceResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new HalfResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new Order1ResizePolicy(rc));
        }
        checkIndexedOrder(ModuloLookUpResizePolicy.INSTANCE_59);
        checkIndexedOrder(ModuloLookUpResizePolicy.INSTANCE_67);
    }

    private void checkNotSmallerCapacity(@NotNull ResizePolicy policy) {
        Assert.assertTrue(policy.notSmallerCapacity(0) >= 0);
        final int max = policy.getInt(policy.intLength() - 1);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int min = rc.nextInt(max) + 1;
            Assert.assertTrue(policy.notSmallerCapacity(min) >= min);
        }
    }

    @Test
    public void checkNotSmallerCapacity() {
        checkNotSmallerCapacity(LookUpResizePolicy.PAPER);
        checkNotSmallerCapacity(LookUpResizePolicy.GOLDEN);
        checkNotSmallerCapacity(LookUpResizePolicy.NATURAL);
        checkNotSmallerCapacity(LookUpResizePolicy.PRIME_PAPER);
        checkNotSmallerCapacity(LookUpResizePolicy.PRIME_GOLDEN);
        checkNotSmallerCapacity(LookUpResizePolicy.PRIME_FIBONACCI);
        checkNotSmallerCapacity(ShiftResizePolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new TwiceResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new HalfResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new Order1ResizePolicy(rc));
        }
        checkNotSmallerCapacity(ModuloLookUpResizePolicy.INSTANCE_59);
        checkNotSmallerCapacity(ModuloLookUpResizePolicy.INSTANCE_67);
    }

    private void checkNextCapacity(@NotNull ResizePolicy policy) {
        final int length = policy.intLength();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int index = rc.nextInt(length);
            int currentCapacity = policy.getInt(index);
            int nextCapacity = policy.nextCapacity(currentCapacity);
            try {
                if (index + 1 < length) {
                    Assert.assertTrue(currentCapacity < nextCapacity);
                    Assert.assertEquals(policy.getInt(index + 1), nextCapacity);
                } else {
                    Assert.assertEquals(currentCapacity, nextCapacity);
                }
            } catch (AssertionError e) {
                System.out.println(policy);
                System.out.println("index = " + index + ", currentCapacity = " + currentCapacity + ", nextCapacity = " + nextCapacity);
                throw e;
            }
        }
    }

    @Test
    public void checkNextCapacity() {
        checkNextCapacity(LookUpResizePolicy.PAPER);
        checkNextCapacity(LookUpResizePolicy.GOLDEN);
        checkNextCapacity(LookUpResizePolicy.NATURAL);
        checkNextCapacity(LookUpResizePolicy.PRIME_PAPER);
        checkNextCapacity(LookUpResizePolicy.PRIME_GOLDEN);
        checkNextCapacity(LookUpResizePolicy.PRIME_FIBONACCI);
        checkNextCapacity(ShiftResizePolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new TwiceResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new HalfResizePolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new Order1ResizePolicy(rc));
        }
        checkNextCapacity(ModuloLookUpResizePolicy.INSTANCE_59);
        checkNextCapacity(ModuloLookUpResizePolicy.INSTANCE_67);
    }
}
