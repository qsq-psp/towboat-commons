package mujica.ds.bit;

import mujica.algebra.random.RandomContext;
import mujica.ds.slot.LinkedBlockSlotList;
import mujica.ds.slot.LinkedSlotList;
import mujica.ds.slot.SlotCollection;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/7/28")
public class BitSlotCollectionTest {

    private static final int REPEAT_COUNT = 1505;

    private final RandomContext rc = new RandomContext();

    private final ArrayList<String> operations = new ArrayList<>();

    private final BitSlot slot = new Bit();

    private Collection<Boolean> expected;

    private SlotCollection<BitSlot, ?> actual;

    private void duplicate() {
        operations.add("duplicate");
        actual = actual.duplicate();
    }

    private void checkHealth() {
        operations.add("checkHealth");
        actual.checkHealth();
    }

    private void size() {
        operations.add("size");
        Assert.assertEquals(expected.size(), actual.size());
    }

    private void getFirstItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("getFirstItem");
            boolean expectedValue = ((Deque<Boolean>) expected).getFirst();
            actual.getFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getBit());
        }
    }

    private void getLastItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("getLastItem");
            boolean expectedValue = ((Deque<Boolean>) expected).getLast();
            actual.getLastItem(slot);
            Assert.assertEquals(expectedValue, slot.getBit());
        }
    }

    private void removeFirstItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("removeFirstItem");
            boolean expectedValue = ((Deque<Boolean>) expected).removeFirst();
            actual.removeFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getBit());
        }
    }

    private void removeLastItem() {
        if (expected.isEmpty()) {
            if (rc.nextBoolean()) {
                insertFirstItem();
            } else {
                insertLastItem();
            }
        } else {
            operations.add("removeLastItem");
            boolean expectedValue = ((Deque<Boolean>) expected).removeLast();
            actual.removeLastItem(slot);
            Assert.assertEquals(expectedValue, slot.getBit());
        }
    }

    private void insertFirstItem() {
        operations.add("insertFirstItem");
        slot.setBit(rc.nextBoolean());
        ((Deque<Boolean>) expected).addFirst(slot.getBit());
        actual.insertFirstItem(slot);
    }

    private void insertLastItem() {
        operations.add("insertLastItem");
        slot.setBit(rc.nextBoolean());
        ((Deque<Boolean>) expected).addLast(slot.getBit());
        actual.insertLastItem(slot);
    }

    private void clear() {
        operations.add("clear");
        expected.clear();
        actual.clear();
    }

    private void dequeSwitch() {
        switch (rc.nextInt(10)) {
            case 0:
                duplicate();
                break;
            case 1:
                checkHealth();
                break;
            case 2:
                size();
                break;
            case 3:
                getFirstItem();
                break;
            case 4:
                getLastItem();
                break;
            case 5:
                removeFirstItem();
                break;
            case 6:
                removeLastItem();
                break;
            case 7:
                insertFirstItem();
                break;
            case 8:
                insertLastItem();
                break;
            case 9:
                clear();
                break;
        }
    }

    private void dequeLoop() {
        operations.clear();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                dequeSwitch();
            }
        } catch (Throwable e) {
            System.out.println(operations);
            System.out.println(expected);
            System.out.println(actual);
            throw e;
        }
    }

    private void checkDeque(@NotNull Supplier<Collection<Boolean>> supplier) {
        expected = supplier.get();
        actual = new LinkedSlotList<>(SlotArrayAllocatorB.INSTANCE, Bit.BitNodeAllocator2.INSTANCE);
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(SlotArrayAllocatorI8B.INSTANCE, Bit.BitNodeAllocator2.INSTANCE);
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(SlotArrayAllocatorI32B.INSTANCE, Bit.BitNodeAllocator2.INSTANCE);
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedBlockSlotList<>(SlotArrayAllocatorB.INSTANCE, rc.nextInt(20, 30));
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedBlockSlotList<>(SlotArrayAllocatorI8B.INSTANCE, rc.nextInt(80, 100));
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedBlockSlotList<>(SlotArrayAllocatorI32B.INSTANCE, rc.nextInt(400, 500));
        dequeLoop();
    }

    @Test
    public void checkDeque() {
        checkDeque(ArrayDeque::new);
        checkDeque(LinkedList::new);
    }
}
