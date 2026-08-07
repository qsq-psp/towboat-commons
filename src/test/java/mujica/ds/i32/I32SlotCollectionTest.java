package mujica.ds.i32;

import mujica.algebra.random.RandomContext;
import mujica.ds.i32.list.*;
import mujica.ds.slot.*;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/7/27")
public class I32SlotCollectionTest {

    private static final int REPEAT_COUNT = 855;

    private static final int SMALL_COUNT = 9;

    private final RandomContext rc = new RandomContext();

    private final ArrayList<String> operations = new ArrayList<>();

    private final I32Slot slot = new I32();

    private Collection<Integer> expected;

    private SlotCollection<I32Slot, int[]> actual;

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

    private void contains() {
        operations.add("contains");
        slot.setI32(rc.nextInt());
        final boolean expectedContains = expected.contains(slot.getI32());
        final boolean actualContains = actual.contains(slot);
        Assert.assertEquals(expectedContains, actualContains);
    }

    private void firstIndexOf() {
        operations.add("firstIndexOf");
        slot.setI32(rc.nextInt());
        final int expectedIndex = ((List<Integer>) expected).indexOf(slot.getI32());
        final int actualIndex = actual.firstIndexOf(slot);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    private void getItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("getItemAt");
            index = rc.nextInt(index);
            int expectedValue = ((List<Integer>) expected).get(index);
            actual.getItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI32());
        } else {
            insertLastItem();
        }
    }

    private void setItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("setItemAt");
            index = rc.nextInt(index);
            slot.setI32(rc.nextByte());
            ((List<Integer>) expected).set(index, slot.getI32());
            actual.setItemAt(index, slot);
        } else {
            insertLastItem();
        }
    }

    private void exchangeItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("exchangeItemAt");
            index = rc.nextInt(index);
            slot.setI32(rc.nextByte());
            int expectedValue = ((List<Integer>) expected).set(index, slot.getI32());
            actual.exchangeItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI32());
        } else {
            insertLastItem();
        }
    }

    private void removeItemAtNull() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("removeItemAt");
            index = rc.nextInt(index);
            ((List<Integer>) expected).remove(index);
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
            int expectedValue = ((List<Integer>) expected).remove(index);
            actual.removeItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI32());
        } else {
            insertLastItem();
        }
    }

    private void insertItemBefore() {
        operations.add("insertItemBefore");
        int index = expected.size() + 1;
        index = rc.nextInt(index);
        slot.setI32(rc.nextByte());
        ((List<Integer>) expected).add(index, slot.getI32());
        actual.insertItemBefore(index, slot);
    }

    private void getArbitraryItemNull() {
        if (actual.size() > rc.nextInt(SMALL_COUNT)) {
            operations.add("getArbitraryItemNull");
            actual.getArbitraryItem(slot, null);
            Assert.assertTrue(expected.contains(slot.getI32()));
        } else {
            insertLast();
        }
    }

    private void getArbitraryItemNotNull() {
        if (actual.size() > rc.nextInt(SMALL_COUNT)) {
            operations.add("getArbitraryItemNotNull");
            actual.getArbitraryItem(slot, rc);
            Assert.assertTrue(expected.contains(slot.getI32()));
        } else {
            insertLast();
        }
    }

    private void insertLastItem() {
        operations.add("insertLastItem");
        slot.setI32(rc.nextInt());
        expected.add(slot.getI32());
        actual.insertLastItem(slot);
    }

    private void insertLast() {
        operations.add("insertLast");
        final int[] array = rc.nextIntArray(rc.nextInt(SMALL_COUNT));
        {
            ArrayList<Integer> list = new ArrayList<>();
            for (int value : array) {
                list.add(value);
            }
            expected.addAll(list);
        }
        actual.insertLast(array);
    }

    private void truncate() {
        operations.add("truncate");
        final int oldSize = expected.size();
        if (oldSize > rc.nextInt(SMALL_COUNT)) {
            int newSize = rc.nextInt(oldSize);
            ((List<Integer>) expected).subList(newSize, oldSize).clear();
            actual.truncate(newSize);
        } else {
            insertLastItem();
        }
    }

    private void clear() {
        operations.add("clear");
        expected.clear();
        actual.clear();
    }

    private void allToArray() {
        operations.add("allToArray");
        int[] expectedArray;
        {
            int expectedSize = expected.size();
            expectedArray = new int[expectedSize];
            List<Integer> expectedList = (List<Integer>) expected;
            for (int index = 0; index < expectedSize; index++) {
                expectedArray[index] = expectedList.get(index);
            }
        }
        int[] actualArray = actual.allToArray();
        Assert.assertArrayEquals(expectedArray, actualArray);
    }

    private void removeIfSmaller() {
        operations.add("removeIfSmaller");
        final int gate = rc.nextInt();
        expected.removeIf(value -> value < gate);
        actual.removeIf(theSlot -> theSlot.getI32() < gate, slot);
    }

    private void removeIfLarger() {
        operations.add("removeIfLarger");
        final int gate = rc.nextInt();
        expected.removeIf(value -> value > gate);
        actual.removeIf(theSlot -> theSlot.getI32() > gate, slot);
    }

    private void listSwitch() {
        switch (rc.nextInt(20)) {
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
                contains();
                break;
            case 4:
                firstIndexOf();
                break;
            case 5:
                getItemAt();
                break;
            case 6:
                setItemAt();
                break;
            case 7:
                exchangeItemAt();
                break;
            case 8:
                removeItemAtNull();
                break;
            case 9:
                removeItemAtNotNull();
                break;
            case 10:
                insertItemBefore();
                break;
            case 11:
                getArbitraryItemNull();
                break;
            case 12:
                getArbitraryItemNotNull();
                break;
            case 13:
                insertLastItem();
                break;
            case 14:
                insertLast();
                break;
            case 15:
                truncate();
                break;
            case 16:
                clear();
                break;
            case 17:
                allToArray();
                break;
            case 18:
                removeIfSmaller();
                break;
            case 19:
                removeIfLarger();
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

    private void checkList(@NotNull Supplier<Collection<Integer>> supplier) {
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(new HalfCapacityPolicy(0x3f4f5f6f)));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_PAPER));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(new HalfCapacityPolicy(0x3e4e5e6e)));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_GOLDEN));
        listLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(new HalfCapacityPolicy(0x30405060)));
        listLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_FIBONACCI));
        listLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(new SlotArrayAllocatorI32ForTest(), I32.I32NodeAllocator2.INSTANCE);
        listLoop();
    }

    @Test
    public void checkList() {
        checkList(ArrayList::new);
        checkList(LinkedList::new);
    }

    private void setSwitch() {
        switch (rc.nextInt(14)) {
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
            case 4:
                contains();
                break;
            case 5:
                getArbitraryItemNull();
                break;
            case 6:
                getArbitraryItemNotNull();
                break;
            case 7:
            case 8:
            case 9:
                insertLastItem();
                break;
            case 10:
                insertLast();
                break;
            case 11:
                clear();
                break;
            case 12:
                removeIfSmaller();
                break;
            case 13:
                removeIfLarger();
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

    private void checkSet(@NotNull Supplier<Collection<Integer>> supplier) {
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Linear<>(new SlotArrayAllocatorI32ForTest.WithHashI32(ShiftCapacityPolicy.INSTANCE));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Linear<>(new SlotArrayAllocatorI32ForTest.WithHashI32(TwiceCapacityPolicy.INSTANCE));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Quadratic<>(new SlotArrayAllocatorI32ForTest.WithHashI32(ModuloLookUpCapacityPolicy.INSTANCE_59));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Quadratic<>(new SlotArrayAllocatorI32ForTest.WithHashI32(ModuloLookUpCapacityPolicy.INSTANCE_67));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_PAPER));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_GOLDEN));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI32ForTest.WithHashI32(LookUpCapacityPolicy.PRIME_FIBONACCI));
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link1<>(new SlotArrayAllocatorI32ForTest.WithHashI32(TwiceCapacityPolicy.INSTANCE), I32.I32NodeAllocator1.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link2<>(new SlotArrayAllocatorI32ForTest.WithHashI32(TwiceCapacityPolicy.INSTANCE), I32.I32NodeAllocator2.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link3<>(new SlotArrayAllocatorI32ForTest.WithHashI32(TwiceCapacityPolicy.INSTANCE), I32.I32NodeAllocator3.INSTANCE);
        setLoop();
    }

    @Test
    public void checkSet() {
        checkSet(HashSet::new);
        checkSet(LinkedHashSet::new);
    }
}
