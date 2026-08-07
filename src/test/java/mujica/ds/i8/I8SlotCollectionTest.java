package mujica.ds.i8;

import mujica.algebra.random.RandomContext;
import mujica.ds.i32.list.*;
import mujica.ds.slot.*;
import mujica.io.stream.OIO;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/7/26")
public class I8SlotCollectionTest {

    private static final int REPEAT_COUNT = 1440;

    private static final int SMALL_COUNT = 10;

    private final RandomContext rc = new RandomContext();

    private final ArrayList<String> operations = new ArrayList<>();

    private final I8Slot slot = new I8();

    private Collection<Byte> expected;

    private SlotCollection<I8Slot, byte[]> actual;

    @SuppressWarnings("unchecked")
    private void serialize() {
        operations.add("serialize");
        try {
            actual = (SlotCollection<I8Slot, byte[]>) OIO.copy(actual);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void objectClone() {
        operations.add("clone");
        actual = actual.clone();
    }

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
        slot.setI8(rc.nextByte());
        final boolean expectedContains = expected.contains(slot.getI8());
        final boolean actualContains = actual.contains(slot);
        Assert.assertEquals(expectedContains, actualContains);
    }

    private void firstIndexOf() {
        operations.add("firstIndexOf");
        slot.setI8(rc.nextByte());
        final int expectedIndex = ((List<Byte>) expected).indexOf(slot.getI8());
        final int actualIndex = actual.firstIndexOf(slot);
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    private void getItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("getItemAt");
            index = rc.nextInt(index);
            byte expectedValue = ((List<Byte>) expected).get(index);
            actual.getItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        } else {
            insertLastItem();
        }
    }

    private void setItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("setItemAt");
            index = rc.nextInt(index);
            slot.setI8(rc.nextByte());
            ((List<Byte>) expected).set(index, slot.getI8());
            actual.setItemAt(index, slot);
        } else {
            insertLastItem();
        }
    }

    private void removeItemAt() {
        int index = expected.size();
        if (index > rc.nextInt(SMALL_COUNT)) {
            operations.add("removeItemAt");
            index = rc.nextInt(index);
            byte expectedValue = ((List<Byte>) expected).remove(index);
            actual.removeItemAt(index, slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        } else {
            insertLastItem();
        }
    }

    private void insertItemBefore() {
        operations.add("insertItemBefore");
        int index = expected.size() + 1;
        index = rc.nextInt(index);
        slot.setI8(rc.nextByte());
        ((List<Byte>) expected).add(index, slot.getI8());
        actual.insertItemBefore(index, slot);
    }

    private void getFirstItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("getFirstItem");
            byte expectedValue = ((Deque<Byte>) expected).getFirst();
            actual.getFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        }
    }

    private void getLastItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("getLastItem");
            byte expectedValue = ((Deque<Byte>) expected).getLast();
            actual.getLastItem(slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        }
    }

    private void removeFirstItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("removeFirstItem");
            byte expectedValue = ((Deque<Byte>) expected).removeFirst();
            actual.removeFirstItem(slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        }
    }

    private void removeLastItem() {
        if (expected.isEmpty()) {
            insertFirstItem();
        } else {
            operations.add("removeLastItem");
            byte expectedValue = ((Deque<Byte>) expected).removeLast();
            actual.removeLastItem(slot);
            Assert.assertEquals(expectedValue, slot.getI8());
        }
    }

    private void removeFirstEqualItem() {
        operations.add("removeFirstEqualItem");
        slot.setI8(rc.nextByte());
        final boolean expectedSuccess = expected.remove(slot.getI8());
        final boolean actualSuccess = actual.removeFirstEqualItem(slot);
        Assert.assertEquals(expectedSuccess, actualSuccess);
    }

    private void insertFirstItem() {
        operations.add("insertFirstItem");
        slot.setI8(rc.nextByte());
        ((Deque<Byte>) expected).addFirst(slot.getI8());
        actual.insertFirstItem(slot);
    }

    private void insertLastItem() {
        operations.add("insertLastItem");
        slot.setI8(rc.nextByte());
        expected.add(slot.getI8());
        actual.insertLastItem(slot);
    }

    private void clear() {
        operations.add("clear");
        expected.clear();
        actual.clear();
    }

    private void listSwitch() {
        switch (rc.nextInt(16)) {
            case 0:
                serialize();
                break;
            case 1:
                objectClone();
                break;
            case 2:
                duplicate();
                break;
            case 3:
                checkHealth();
                break;
            case 4:
                size();
                break;
            case 5:
                contains();
                break;
            case 6:
                firstIndexOf();
                break;
            case 7:
                getItemAt();
                break;
            case 8:
                setItemAt();
                break;
            case 9:
                removeItemAt();
                break;
            case 10:
            case 11:
                insertItemBefore();
                break;
            case 12:
                removeFirstEqualItem();
                break;
            case 13:
            case 14:
                insertLastItem();
                break;
            case 15:
                clear();
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

    private void checkList(@NotNull Supplier<Collection<Byte>> supplier) {
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new HalfCapacityPolicy(0x3f4f5f6f)));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.PAPER));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new HalfCapacityPolicy(0x3e4e5e6e)));
        listLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.GOLDEN));
        listLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new HalfCapacityPolicy(0x30405060)));
        listLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.NATURAL));
        listLoop();
        expected = supplier.get();
        actual = new CyclicSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new HalfCapacityPolicy(0x31415161)));
        listLoop();
        expected = supplier.get();
        actual = new CyclicSlotList<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        listLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(SlotArrayAllocatorI8.INSTANCE, I8.I8NodeAllocator2.INSTANCE);
        listLoop();
    }

    @Test
    public void checkList() {
        checkList(ArrayList::new);
        checkList(LinkedList::new);
    }

    private void dequeSwitch() {
        switch (rc.nextInt(19)) {
            case 0:
                serialize();
                break;
            case 1:
                objectClone();
                break;
            case 2:
                duplicate();
                break;
            case 3:
                checkHealth();
                break;
            case 4:
                size();
                break;
            case 5:
                contains();
                break;
            case 6:
                getFirstItem();
                break;
            case 7:
                getLastItem();
                break;
            case 8:
            case 9:
                removeFirstItem();
                break;
            case 10:
            case 11:
                removeLastItem();
                break;
            case 12:
            case 13:
            case 14:
                insertFirstItem();
                break;
            case 15:
            case 16:
            case 17:
                insertLastItem();
                break;
            case 18:
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

    private void checkDeque(@NotNull Supplier<Collection<Byte>> supplier) {
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        dequeLoop();
        expected = supplier.get();
        actual = new CopyOnWriteSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new LinearCapacityPolicy(rc)));
        dequeLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        dequeLoop();
        expected = supplier.get();
        actual = new CopyOnResizeSlotList<>(new SlotArrayAllocatorI8.WithHashI8(ModuloLookUpCapacityPolicy.INSTANCE_59));
        dequeLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        dequeLoop();
        expected = supplier.get();
        actual = new CenterAlignedSlotList<>(new SlotArrayAllocatorI8.WithHashI8(ModuloLookUpCapacityPolicy.INSTANCE_67));
        dequeLoop();
        expected = supplier.get();
        actual = new CyclicSlotList<>(new SlotArrayAllocatorI8.WithHashI8(new LinearCapacityPolicy(rc)));
        dequeLoop();
        expected = supplier.get();
        actual = new CyclicSlotList<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        dequeLoop();
        expected = supplier.get();
        actual = new LinkedSlotList<>(SlotArrayAllocatorI8.INSTANCE, I8.I8NodeAllocator2.INSTANCE);
        dequeLoop();
    }

    @Test
    public void checkDeque() {
        checkDeque(ArrayDeque::new);
        checkDeque(LinkedList::new);
    }

    private void setSwitch() {
        switch (rc.nextInt(16)) {
            case 0:
                serialize();
                break;
            case 1:
                objectClone();
                break;
            case 2:
                duplicate();
                break;
            case 3:
                checkHealth();
                break;
            case 4:
            case 5:
                size();
                break;
            case 6:
            case 7:
            case 8:
                contains();
                break;
            case 9:
            case 10:
                removeFirstEqualItem();
                break;
            case 11:
            case 12:
            case 13:
            case 14:
                insertLastItem();
                break;
            case 15:
                clear();
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

    private void checkSet(@NotNull Supplier<Collection<Byte>> supplier) {
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Linear<>(new SlotArrayAllocatorI8.WithHashI8(ShiftCapacityPolicy.INSTANCE));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Linear<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Quadratic<>(new SlotArrayAllocatorI8.WithHashI8(ModuloLookUpCapacityPolicy.INSTANCE_59));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Quadratic<>(new SlotArrayAllocatorI8.WithHashI8(ModuloLookUpCapacityPolicy.INSTANCE_67));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.PRIME_PAPER));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.PRIME_GOLDEN));
        setLoop();
        expected = supplier.get();
        actual = new ClosedHashSlotSet.Congruent<>(new SlotArrayAllocatorI8.WithHashI8(LookUpCapacityPolicy.PRIME_FIBONACCI));
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link1<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE), I8.I8NodeAllocator1.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link2<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE), I8.I8NodeAllocator2.INSTANCE);
        setLoop();
        expected = supplier.get();
        actual = new OpenHashSlotSet.Link3<>(new SlotArrayAllocatorI8.WithHashI8(TwiceCapacityPolicy.INSTANCE), I8.I8NodeAllocator3.INSTANCE);
        setLoop();
    }

    @Test
    public void checkSet() {
        checkSet(HashSet::new);
        checkSet(LinkedHashSet::new);
    }
}
