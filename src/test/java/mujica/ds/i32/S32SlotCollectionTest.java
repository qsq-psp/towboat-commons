package mujica.ds.i32;

import mujica.algebra.random.FuzzyContext;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;

@CodeHistory(date = "2026/8/8")
public class S32SlotCollectionTest {

    private static final int REPEAT_COUNT = 855;

    private static final int SMALL_COUNT = 18;

    private final FuzzyContext fc = new FuzzyContext();

    private final ArrayList<String> operations = new ArrayList<>();

    private final S32 slot = new S32();

    private Collection<Integer> expected;

    private SlotCollection<S32, int[]> actual;

    private void duplicate() {
        operations.add("duplicate");
        actual = actual.duplicate();
    }

    private void checkHealth() {
        operations.add("checkHealth");
        actual.checkHealth();
    }

    private void getSize() {
        operations.add("getSize");
        Assert.assertEquals(expected.size(), actual.getSize());
    }

    private void contains() {
        operations.add("contains");
        slot.setI32(fc.nextInt());
        final boolean expectedContains = expected.contains(slot.getI32());
        final boolean actualContains = actual.contains(slot);
        Assert.assertEquals(expectedContains, actualContains);
    }

    private void countItem() {
        operations.add("countItem");
        final int target = fc.nextInt();
        slot.setI32(0);
        expected.forEach(value -> {
            if (value == target) {
                slot.setI32(slot.getI32() + 1);
            }
        });
        final int expectedCount = slot.getI32();
        slot.setI32(target);
        final int actualCount = actual.countItem(slot);
        Assert.assertEquals(expectedCount, actualCount);
    }

    private void add() {
        operations.add("insertLastItem");
        slot.setI32(fc.nextInt());
        expected.add(slot.getI32());
        actual.insertLastItem(slot);
    }

    private void remove() {
        if (actual.getSize() > fc.nextInt(SMALL_COUNT)) {
            operations.add("removeFirstItem");
            int expectedValue = ((Queue<Integer>) expected).remove();
            actual.removeFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getI32());
        } else {
            add();
        }
    }

    private void element() {
        if (actual.getSize() > fc.nextInt(SMALL_COUNT)) {
            operations.add("getFirstItem");
            int expectedValue = ((Queue<Integer>) expected).element();
            actual.getFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getI32());
        } else {
            add();
        }
    }

    private void clear() {
        operations.add("clear");
        expected.clear();
        actual.clear();
    }

    private void queueSwitch() {
        switch (fc.nextInt(11)) {
            case 0:
                duplicate();
                break;
            case 1:
                checkHealth();
                break;
            case 2:
                getSize();
                break;
            case 3:
                contains();
                break;
            case 4:
                countItem();
                break;
            case 5:
            case 6:
            case 7:
                add();
                break;
            case 8:
                remove();
                break;
            case 9:
                element();
                break;
            case 10:
                clear();
                break;
        }
    }

    private void queueLoop() {
        operations.clear();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                queueSwitch();
            }
        } catch (Throwable e) {
            System.out.println(operations);
            System.out.println(expected);
            System.out.println(actual);
            throw e;
        }
    }

    private void fuzzQueue(@NotNull Supplier<Collection<Integer>> supplier) {
        expected = supplier.get();
        actual = new SearchSlotTree2<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new CountSlotTree2<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new SortedSlotList<>(new S32Arithmetic.S32HashSAC(new TwiceCapacityPolicy(20)));
        queueLoop();
        expected = supplier.get();
        actual = new BinaryPrioritySlotQueue<>(new S32Arithmetic.S32HashSAC(new TwiceCapacityPolicy(21)));
        queueLoop();
        expected = supplier.get();
        actual = new NAryPrioritySlotQueue<>(new S32Arithmetic.S32HashSAC(new TwiceCapacityPolicy(22)), 3);
        queueLoop();
        expected = supplier.get();
        actual = new NAryPrioritySlotQueue<>(new S32Arithmetic.S32HashSAC(new TwiceCapacityPolicy(23)), 8);
        queueLoop();
        expected = supplier.get();
        actual = new BinomialSlotQueue<>(new S32Arithmetic.S32HashSAC(new TwiceCapacityPolicy(3)), S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new FibonacciSlotHeap<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new PairingSlotHeap<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new LeftListSlotTree<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
        expected = supplier.get();
        actual = new SkewSlotHeap<>(S32Arithmetic.INSTANCE, S32.S32NodeAllocator2.INSTANCE);
        queueLoop();
    }

    @Test
    public void fuzzQueue() {
        fuzzQueue(PriorityQueue::new);
    }
}
