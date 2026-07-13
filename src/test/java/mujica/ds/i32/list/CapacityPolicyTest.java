package mujica.ds.i32.list;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

@CodeHistory(date = "2025/7/1")
public class CapacityPolicyTest {

    private static final int REPEAT = 30;

    private static final int SIZE = 600;

    private final RandomContext rc = new RandomContext();

    private void checkIteratorOrder(@NotNull CapacityPolicy policy) {
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
        checkIteratorOrder(LookUpCapacityPolicy.PAPER);
        checkIteratorOrder(LookUpCapacityPolicy.GOLDEN);
        checkIteratorOrder(LookUpCapacityPolicy.NATURAL);
        checkIteratorOrder(LookUpCapacityPolicy.PRIME_PAPER);
        checkIteratorOrder(LookUpCapacityPolicy.PRIME_GOLDEN);
        checkIteratorOrder(LookUpCapacityPolicy.PRIME_FIBONACCI);
        checkIteratorOrder(ShiftCapacityPolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new TwiceCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new HalfCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIteratorOrder(new Order1CapacityPolicy(rc));
        }
        checkIteratorOrder(ModuloLookUpCapacityPolicy.INSTANCE_59);
        checkIteratorOrder(ModuloLookUpCapacityPolicy.INSTANCE_67);
    }

    private void checkIndexedOrder(@NotNull CapacityPolicy policy) {
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
        checkIndexedOrder(LookUpCapacityPolicy.PAPER);
        checkIndexedOrder(LookUpCapacityPolicy.GOLDEN);
        checkIndexedOrder(LookUpCapacityPolicy.NATURAL);
        checkIndexedOrder(LookUpCapacityPolicy.PRIME_PAPER);
        checkIndexedOrder(LookUpCapacityPolicy.PRIME_GOLDEN);
        checkIndexedOrder(LookUpCapacityPolicy.PRIME_FIBONACCI);
        checkIndexedOrder(ShiftCapacityPolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new TwiceCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new HalfCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkIndexedOrder(new Order1CapacityPolicy(rc));
        }
        checkIndexedOrder(ModuloLookUpCapacityPolicy.INSTANCE_59);
        checkIndexedOrder(ModuloLookUpCapacityPolicy.INSTANCE_67);
    }

    private void checkNotSmallerCapacity(@NotNull CapacityPolicy policy) {
        Assert.assertTrue(policy.notSmallerCapacity(0) >= 0);
        final int max = policy.getInt(policy.intLength() - 1);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            int min = rc.nextInt(max) + 1;
            Assert.assertTrue(policy.notSmallerCapacity(min) >= min);
        }
    }

    @Test
    public void checkNotSmallerCapacity() {
        checkNotSmallerCapacity(LookUpCapacityPolicy.PAPER);
        checkNotSmallerCapacity(LookUpCapacityPolicy.GOLDEN);
        checkNotSmallerCapacity(LookUpCapacityPolicy.NATURAL);
        checkNotSmallerCapacity(LookUpCapacityPolicy.PRIME_PAPER);
        checkNotSmallerCapacity(LookUpCapacityPolicy.PRIME_GOLDEN);
        checkNotSmallerCapacity(LookUpCapacityPolicy.PRIME_FIBONACCI);
        checkNotSmallerCapacity(ShiftCapacityPolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new TwiceCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new HalfCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNotSmallerCapacity(new Order1CapacityPolicy(rc));
        }
        checkNotSmallerCapacity(ModuloLookUpCapacityPolicy.INSTANCE_59);
        checkNotSmallerCapacity(ModuloLookUpCapacityPolicy.INSTANCE_67);
    }

    private void checkNextCapacity(@NotNull CapacityPolicy policy) {
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
        checkNextCapacity(LookUpCapacityPolicy.PAPER);
        checkNextCapacity(LookUpCapacityPolicy.GOLDEN);
        checkNextCapacity(LookUpCapacityPolicy.NATURAL);
        checkNextCapacity(LookUpCapacityPolicy.PRIME_PAPER);
        checkNextCapacity(LookUpCapacityPolicy.PRIME_GOLDEN);
        checkNextCapacity(LookUpCapacityPolicy.PRIME_FIBONACCI);
        checkNextCapacity(ShiftCapacityPolicy.INSTANCE);
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new TwiceCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new HalfCapacityPolicy(rc));
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            checkNextCapacity(new Order1CapacityPolicy(rc));
        }
        checkNextCapacity(ModuloLookUpCapacityPolicy.INSTANCE_59);
        checkNextCapacity(ModuloLookUpCapacityPolicy.INSTANCE_67);
    }
}
