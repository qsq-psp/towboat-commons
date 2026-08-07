package mujica.ds.i64;

import mujica.algebra.random.RandomContext;
import mujica.ds.i32.list.LookUpCapacityPolicy;
import mujica.ds.i32.list.ModuloLookUpCapacityPolicy;
import mujica.ds.i32.list.TwiceCapacityPolicy;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/8/5")
public class I64SlotCollectionTest {

    private static final int REPEAT_COUNT = 667;

    private static final int SMALL_COUNT = 7;

    private final RandomContext rc = new RandomContext();

    private final ArrayList<String> operations = new ArrayList<>();

    private final I64Slot slot = new I64();

    private Collection<Long> expected;

    private SlotCollection<I64Slot, long[]> actual;

    private void checkHealth() {
        operations.add("checkHealth");
        actual.checkHealth();
    }

    private void size() {
        operations.add("size");
        Assert.assertEquals(expected.size(), actual.size());
    }

    private void contains() {
        operations.add("contains");
        slot.setI64(rc.nextLong());
        final boolean expectedContains = expected.contains(slot.getI64());
        final boolean actualContains = actual.contains(slot);
        Assert.assertEquals(expectedContains, actualContains);
    }

    private void getItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("getItemAt");
            index = rc.nextInt(index);
            long expectedValue = ((List<Long>) expected).get(index);
            actual.getItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI64());
        } else {
            insertLastItem();
        }
    }

    private void setItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("setItemAt");
            index = rc.nextInt(index);
            slot.setI64(rc.nextByte());
            ((List<Long>) expected).set(index, slot.getI64());
            actual.setItemAt(index, slot);
        } else {
            insertLastItem();
        }
    }

    private void removeItemAtNull() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("removeItemAt");
            index = rc.nextInt(index);
            ((List<Long>) expected).remove(index);
            actual.removeItemAt(index, null);
        } else {
            insertLastItem();
        }
    }

    private void removeItemAtNotNull() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("removeItemAtNotNull");
            index = rc.nextInt(index);
            long expectedValue = ((List<Long>) expected).remove(index);
            actual.removeItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI64());
        } else {
            insertLastItem();
        }
    }

    private void getArbitraryItemNull() {
        if (actual.size() > rc.nextInt(SMALL_COUNT)) {
            operations.add("getArbitraryItemNull");
            actual.getArbitraryItem(slot, null);
            Assert.assertTrue(expected.contains(slot.getI64()));
        } else {
            insertLastItem();
        }
    }

    private void getArbitraryItemNotNull() {
        if (actual.size() > rc.nextInt(SMALL_COUNT)) {
            operations.add("getArbitraryItemNotNull");
            actual.getArbitraryItem(slot, rc);
            Assert.assertTrue(expected.contains(slot.getI64()));
        } else {
            insertLastItem();
        }
    }

    private void removeArbitraryItem() {
        if (actual.size() > rc.nextInt(SMALL_COUNT)) {
            operations.add("removeArbitraryItem");
            actual.removeArbitraryItem(slot, rc);
            Assert.assertTrue(expected.remove(slot.getI64())); // only for set
        } else {
            insertLastItem();
        }
    }

    private void insertLastItem() {
        operations.add("insertLastItem");
        slot.setI64(rc.nextLong());
        expected.add(slot.getI64());
        actual.insertLastItem(slot);
    }

    private void clear() {
        operations.add("clear");
        expected.clear();
        actual.clear();
    }

    private void allToArray() {
        operations.add("allToArray");
        long[] expectedArray;
        {
            int expectedSize = expected.size();
            expectedArray = new long[expectedSize];
            List<Long> expectedList = (List<Long>) expected;
            for (int index = 0; index < expectedSize; index++) {
                expectedArray[index] = expectedList.get(index);
            }
        }
        long[] actualArray = actual.allToArray();
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    private void iteratorPreviousGapPositive() {
        operations.add("iterator.previousGap(+)");
        final int expectedSize = expected.size();
        int gapIndex = rc.nextInt(expectedSize + 1);
        final SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(gapIndex);
        while (gapIndex-- > 0) {
            Assert.assertTrue(iterator.previousGap());
        }
        Assert.assertFalse(iterator.previousGap());
    }

    private void iteratorPreviousGapNegative() {
        operations.add("iterator.previousGap(-)");
        final int expectedSize = expected.size();
        int gapIndex = rc.nextInt(expectedSize + 1);
        final SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(gapIndex - expectedSize - 1);
        while (gapIndex-- > 0) {
            Assert.assertTrue(iterator.previousGap());
        }
        Assert.assertFalse(iterator.previousGap());
    }

    private void iteratorNextGapPositive() {
        operations.add("iterator.nextGap(+)");
        final int expectedSize = expected.size();
        int gapIndex = rc.nextInt(expectedSize + 1);
        final SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(gapIndex);
        while (gapIndex++ < expectedSize) {
            Assert.assertTrue(iterator.nextGap());
        }
        Assert.assertFalse(iterator.nextGap());
    }

    private void iteratorNextGapNegative() {
        operations.add("iterator.nextGap(-)");
        final int expectedSize = expected.size();
        int gapIndex = rc.nextInt(expectedSize + 1);
        final SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(gapIndex - expectedSize - 1);
        while (gapIndex++ < expectedSize) {
            Assert.assertTrue(iterator.nextGap());
        }
        Assert.assertFalse(iterator.nextGap());
    }

    private void iteratorGetPrevious() {
        final int size = expected.size();
        if (size > rc.nextInt(SMALL_COUNT)) {
            operations.add("iterator.getPrevious");
            int target = rc.nextInt(size);
            long expectedValue = ((List<Long>) expected).get(target);
            int reflect = rc.nextInt(target + 1);
            SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(-1);
            for (int index = size - 1; index > reflect; index--) {
                Assert.assertTrue(iterator.previousGap());
            }
            for (int index = reflect; index < target; index++) {
                Assert.assertTrue(iterator.nextGap());
            }
            iterator.getPrevious(slot);
            Assert.assertEquals(expectedValue, slot.getI64());
        } else {
            insertLastItem();
        }
    }

    private void iteratorGetNext() {
        final int size = expected.size();
        if (size > rc.nextInt(SMALL_COUNT)) {
            operations.add("iterator.getNext");
            int target = rc.nextInt(size);
            long expectedValue = ((List<Long>) expected).get(target);
            int reflect = rc.nextInt(target, size + 1);
            SlotCollection.GapIterator<I64Slot> iterator = actual.iterator(0);
            for (int index = 0; index < reflect; index++) {
                Assert.assertTrue(iterator.nextGap());
            }
            for (int index = reflect; index > target; index--) {
                Assert.assertTrue(iterator.previousGap());
            }
            iterator.getNext(slot);
            Assert.assertEquals(expectedValue, slot.getI64());
        } else {
            insertLastItem();
        }
    }

    private void listSwitch() {
        switch (rc.nextInt(18)) {
            case 0:
                checkHealth();
                break;
            case 1:
                size();
                break;
            case 2:
                contains();
                break;
            case 3:
                getItemAt();
                break;
            case 4:
                setItemAt();
                break;
            case 5:
                removeItemAtNull();
                break;
            case 6:
                removeItemAtNotNull();
                break;
            case 7:
                getArbitraryItemNull();
                break;
            case 8:
                getArbitraryItemNotNull();
                break;
            case 9:
                insertLastItem();
                break;
            case 10:
                clear();
                break;
            case 11:
                allToArray();
                break;
            case 12:
                iteratorPreviousGapPositive();
                break;
            case 13:
                iteratorPreviousGapNegative();
                break;
            case 14:
                iteratorNextGapPositive();
                break;
            case 15:
                iteratorNextGapNegative();
                break;
            case 16:
                iteratorGetPrevious();
                break;
            case 17:
                iteratorGetNext();
                break;
        }
    }

    private void listLoop() {
        operations.clear();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                listSwitch();
            }
        } catch (Throwable e) {
            System.out.println(operations);
            System.out.println(expected);
            System.out.println(actual);
            throw e;
        }
    }

    private void checkList(@NotNull Supplier<Collection<Long>> supplier) {
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI64ForTest.WithHashI64(new TwiceCapacityPolicy(12)));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI64ForTest.WithHashI64(new TwiceCapacityPolicy(13)));
        listLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI64ForTest.WithHashI64(new TwiceCapacityPolicy(14)));
        listLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(new SlotArrayAllocatorI64ForTest(), I64.I64NodeAllocator2.INSTANCE);
        listLoop();
    }

    @Test
    public void checkList() {
        checkList(ArrayList::new);
        checkList(LinkedList::new);
    }

    private void setSwitch() {
        switch (rc.nextInt(15)) {
            case 0:
                checkHealth();
                break;
            case 1:
                size();
                break;
            case 2:
            case 3:
                contains();
                break;
            case 4:
                getArbitraryItemNull();
                break;
            case 5:
                getArbitraryItemNotNull();
                break;
            case 6:
                removeArbitraryItem();
                break;
            case 7:
            case 8:
            case 9:
                insertLastItem();
                break;
            case 10:
                clear();
                break;
            case 11:
                iteratorPreviousGapPositive();
                break;
            case 12:
                iteratorPreviousGapNegative();
                break;
            case 13:
                iteratorNextGapPositive();
                break;
            case 14:
                iteratorNextGapNegative();
                break;
        }
    }

    private void setLoop() {
        operations.clear();
        try {
            for (int repeatIndex = 0; repeatIndex < REPEAT_COUNT; repeatIndex++) {
                setSwitch();
            }
        } catch (Throwable e) {
            System.out.println(operations);
            System.out.println(expected);
            System.out.println(actual);
            throw e;
        }
    }

    private void checkSet(@NotNull Supplier<Collection<Long>> supplier) {
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Linear<>(new SlotArrayAllocatorI64ForTest.WithHashI64(new TwiceCapacityPolicy(17)));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Quadratic<>(new SlotArrayAllocatorI64ForTest.WithHashI64(ModuloLookUpCapacityPolicy.INSTANCE_59));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI64ForTest.WithHashI64(LookUpCapacityPolicy.PRIME_GOLDEN));
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link1<>(new SlotArrayAllocatorI64ForTest.WithHashI64(TwiceCapacityPolicy.INSTANCE), I64.I64NodeAllocator1.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link2<>(new SlotArrayAllocatorI64ForTest.WithHashI64(TwiceCapacityPolicy.INSTANCE), I64.I64NodeAllocator2.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link3<>(new SlotArrayAllocatorI64ForTest.WithHashI64(TwiceCapacityPolicy.INSTANCE), I64.I64NodeAllocator3.INSTANCE);
        setLoop();
    }

    @Test
    public void checkSet() {
        checkSet(HashSet::new);
        checkSet(LinkedHashSet::new);
    }
}
