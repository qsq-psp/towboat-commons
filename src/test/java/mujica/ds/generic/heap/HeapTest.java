package mujica.ds.generic.heap;

import io.netty.buffer.ByteBufUtil;
import mujica.ds.of_int.PublicIntSlot;
import mujica.io.buffer.JdkObjectCodec;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

@CodeHistory(date = "2025/5/26", project = "Ultramarine", name = "PriorityHeapTest")
@CodeHistory(date = "2025/6/9")
public class HeapTest {

    @BeforeClass
    public static void initializeNetty() {
        ByteBufUtil.hexDump(new byte[0]);
    }

    private static final int REPEAT = 78;

    private static final int SIZE = 90;

    private final RandomContext rc = new RandomContext();

    @NotNull
    private String nextString() {
        final int length = rc.nextMinInt(7, 3) + 1;
        final char[] ca = new char[length];
        for (int index = 0; index < length; index++) {
            ca[index] = (char) ('a' + rc.nextMinInt(26, 5));
        }
        return new String(ca);
    }

    @NotNull
    private ArrayList<String> nextStringList() {
        final int size = rc.nextInt(SIZE);
        final ArrayList<String> list = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            list.add(nextString());
        }
        return list;
    }

    @NotNull
    private PriorityQueue<String> nextArrayPriorityQueue() {
        if (rc.nextBoolean()) {
            return new ArrayPriorityQueue<>(null, null, nextStringList());
        } else {
            return new ArrayPriorityQueue<>(null, null);
        }
    }

    @NotNull
    private PriorityQueue<String> nextBinaryPriorityQueue() {
        final AbstractPriorityQueue<String> queue = new BinaryPriorityQueue<>(null);
        if (rc.nextBoolean()) {
            queue.addAll(nextStringList());
        }
        return queue;
    }

    @NotNull
    private PriorityQueue<String> nextNAryPriorityQueue() {
        final AbstractPriorityQueue<String> queue = new NAryPriorityQueue<>(rc.nextInt(5) + 2, null);
        if (rc.nextBoolean()) {
            queue.addAll(nextStringList());
        }
        return queue;
    }

    @NotNull
    private <E> PriorityQueue<E> nextBinomialQueue() {
        return new BinomialQueue<>(null);
    }

    @NotNull
    private <E> PriorityQueue<E> nextFibonacciHeap() {
        return new FibonacciHeap<>(null);
    }

    @NotNull
    private <E> PriorityQueue<E> nextPairingHeap() {
        return new PairingHeap<>(null);
    }

    @NotNull
    private <E> PriorityQueue<E> nextLeftListTree() {
        return new LeftListTree<>(null);
    }

    @NotNull
    private <E> PriorityQueue<E> nextSkewHeap() {
        return new SkewHeap<>(null);
    }

    @NotNull
    private <E> PriorityQueue<E> nextPriorityQueue(@Nullable Comparator<E> comparator) {
        switch (rc.nextInt(8)) {
            default:
            case 0:
                return new ArrayPriorityQueue<>(comparator, null);
            case 1:
                return new BinaryPriorityQueue<>(comparator);
            case 2:
                return new NAryPriorityQueue<>(rc.nextInt(5) + 2, comparator);
            case 3:
                return new BinomialQueue<>(comparator);
            case 4:
                return new FibonacciHeap<>(comparator);
            case 5:
                return new PairingHeap<>(comparator);
            case 6:
                return new LeftListTree<>(comparator);
            case 7:
                return new SkewHeap<>(comparator);
        }
    }

    @SuppressWarnings({"ComparatorCombinators", "Convert2MethodRef"})
    @NotNull
    private PriorityQueue<String> nextPriorityQueue() {
        switch (rc.nextInt(4)) {
            default:
            case 0:
                return nextPriorityQueue(null);
            case 1:
                return nextPriorityQueue((a, b) -> a.compareTo(b));
            case 2:
                return nextPriorityQueue((a, b) -> b.compareTo(a));
            case 3:
                return nextPriorityQueue(Comparator.comparingInt(String::length));
        }
    }

    @NotNull
    private <E> PriorityQueue<E> nextLinkedPriorityQueue() {
        return new LinkedPriorityQueue<>(nextPriorityQueue(new LinkedComparator<>(null)));
    }

    @SuppressWarnings("unchecked")
    private void checkSerialization(@NotNull Supplier<PriorityQueue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            PriorityQueue<String> src = supplier.get();
            src.clear();
            PriorityQueue<String> dst = null;
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    src.offer(element);
                    PublicIntSlot.increase(map, element);
                }
                dst = (PriorityQueue<String>) JdkObjectCodec.copy(src);
                src.checkHealth();
                dst.checkHealth();
                for (int index = 0; index < size; index++) {
                    if (rc.nextBoolean()) {
                        src.offer(nextString());
                    } else {
                        src.remove();
                    }
                }
                while (true) {
                    String element = dst.poll();
                    if (element == null) {
                        break;
                    }
                    PublicIntSlot.decrease(map, element);
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(src);
                System.out.println(dst);
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    @Test
    public void checkSerialization() {
        checkSerialization(this::nextArrayPriorityQueue);
        checkSerialization(this::nextBinaryPriorityQueue);
        checkSerialization(this::nextNAryPriorityQueue);
        checkSerialization(this::nextBinomialQueue);
        checkSerialization(this::nextFibonacciHeap);
        checkSerialization(this::nextPairingHeap);
        checkSerialization(this::nextLeftListTree);
        checkSerialization(this::nextSkewHeap);
        checkSerialization(this::nextLinkedPriorityQueue);
    }

    private void checkClone(@NotNull Supplier<PriorityQueue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            PriorityQueue<String> src = supplier.get();
            src.clear();
            PriorityQueue<String> dst = null;
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    src.offer(element);
                    PublicIntSlot.increase(map, element);
                }
                dst = src.clone();
                src.checkHealth();
                dst.checkHealth();
                for (int index = 0; index < size; index++) {
                    if (rc.nextBoolean()) {
                        src.offer(nextString());
                    } else {
                        src.remove();
                    }
                }
                {
                    String element;
                    while ((element = dst.poll()) != null) {
                        PublicIntSlot.decrease(map, element);
                    }
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(src);
                System.out.println(dst);
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
    }

    @Test
    public void checkClone() {
        checkClone(this::nextArrayPriorityQueue);
        checkClone(this::nextBinaryPriorityQueue);
        checkClone(this::nextNAryPriorityQueue);
        checkClone(this::nextBinomialQueue);
        checkClone(this::nextFibonacciHeap);
        checkClone(this::nextPairingHeap);
        checkClone(this::nextLeftListTree);
        checkClone(this::nextSkewHeap);
        checkClone(this::nextLinkedPriorityQueue);
    }

    private void checkDuplicate(@NotNull Supplier<PriorityQueue<String>> supplier) {
        final Map<String, String> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            PriorityQueue<String> src = supplier.get();
            src.clear();
            PriorityQueue<String> dst = null;
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    src.offer(element);
                    map.put(element, element);
                }
                dst = src.duplicate(element -> new String(element.toCharArray()));
                src.checkHealth();
                dst.checkHealth();
                for (int index = 0; index < size; index++) {
                    if (rc.nextBoolean()) {
                        src.offer(nextString());
                    } else {
                        src.remove();
                    }
                }
                for (String dstElement : dst) {
                    String srcElement = map.get(dstElement);
                    Assert.assertNotNull(srcElement);
                    Assert.assertNotSame(srcElement, dstElement);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(src);
                System.out.println(dst);
                throw e;
            }
        }
    }

    @Test
    public void checkDuplicate() {
        checkDuplicate(this::nextArrayPriorityQueue);
        checkDuplicate(this::nextBinaryPriorityQueue);
        checkDuplicate(this::nextNAryPriorityQueue);
        checkDuplicate(this::nextBinomialQueue);
        checkDuplicate(this::nextFibonacciHeap);
        checkDuplicate(this::nextPairingHeap);
        checkDuplicate(this::nextLeftListTree);
        checkDuplicate(this::nextSkewHeap);
        checkDuplicate(this::nextLinkedPriorityQueue);
    }

    private void checkOffer(@NotNull Supplier<Queue<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Queue<String> queue = supplier.get();
            int size = queue.size();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                try {
                    if (rc.nextBoolean()) {
                        Assert.assertTrue(queue.add(element));
                    } else {
                        Assert.assertTrue(queue.offer(element));
                    }
                    size++;
                    Assert.assertEquals(size, queue.size());
                } catch (AssertionError e) {
                    System.out.println(queue);
                    System.out.println(element);
                    throw e;
                }
            }
        }
    }

    @Test
    public void checkOffer() {
        checkOffer(this::nextArrayPriorityQueue);
        checkOffer(this::nextBinaryPriorityQueue);
        checkOffer(this::nextNAryPriorityQueue);
        checkOffer(this::nextBinomialQueue);
        checkOffer(this::nextFibonacciHeap);
        checkOffer(this::nextPairingHeap);
        checkOffer(this::nextLeftListTree);
        checkOffer(this::nextSkewHeap);
        checkOffer(this::nextLinkedPriorityQueue);
    }

    private void checkPoll(@NotNull Supplier<Queue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    queue.offer(element);
                    PublicIntSlot.increase(map, element);
                }
                size = queue.size();
                while (size > 0) {
                    String element;
                    if (rc.nextBoolean()) {
                        element = queue.remove();
                    } else {
                        element = queue.poll();
                    }
                    size--;
                    Assert.assertEquals(size, queue.size());
                    PublicIntSlot.decrease(map, element);
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, Math.max(0, slot.value)); // slot.value <= 0
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkPoll() {
        checkPoll(this::nextArrayPriorityQueue);
        checkPoll(this::nextBinaryPriorityQueue);
        checkPoll(this::nextNAryPriorityQueue);
        checkPoll(this::nextBinomialQueue);
        checkPoll(this::nextFibonacciHeap);
        checkPoll(this::nextPairingHeap);
        checkPoll(this::nextLeftListTree);
        checkPoll(this::nextSkewHeap);
        checkPoll(this::nextLinkedPriorityQueue);
    }

    private void checkPeek(@NotNull Supplier<Queue<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    if (rc.nextBoolean()) {
                        Assert.assertTrue(queue.add(element));
                    } else {
                        Assert.assertTrue(queue.offer(element));
                    }
                }
                Assert.assertFalse(queue.isEmpty());
                size = queue.size();
                while (size > 0) {
                    String expected;
                    if (rc.nextBoolean()) {
                        expected = queue.element();
                    } else {
                        expected = queue.peek();
                    }
                    String actual;
                    if (rc.nextBoolean()) {
                        actual = queue.remove();
                    } else {
                        actual = queue.poll();
                    }
                    Assert.assertEquals(expected, actual);
                    size--;
                    Assert.assertEquals(size, queue.size());
                }
                Assert.assertTrue(queue.isEmpty());
                Assert.assertNull(queue.poll());
                Assert.assertNull(queue.peek());
            } catch (AssertionError e) {
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkPeek() {
        checkPeek(this::nextArrayPriorityQueue);
        checkPeek(this::nextBinaryPriorityQueue);
        checkPeek(this::nextNAryPriorityQueue);
        checkPeek(this::nextBinomialQueue);
        checkPeek(this::nextFibonacciHeap);
        checkPeek(this::nextPairingHeap);
        checkPeek(this::nextLeftListTree);
        checkPeek(this::nextSkewHeap);
        checkPeek(this::nextLinkedPriorityQueue);
    }

    private void checkOrder(@NotNull Supplier<Queue<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 2;
                for (int index = 0; index < size; index++) {
                    queue.offer(nextString());
                }
                String element0 = queue.remove();
                while (!queue.isEmpty()) {
                    String element1 = queue.remove();
                    Assert.assertTrue(element0.compareTo(element1) <= 0);
                    element0 = element1;
                }
            } catch (AssertionError e) {
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkOrder() {
        checkOrder(this::nextArrayPriorityQueue);
        checkOrder(this::nextBinaryPriorityQueue);
        checkOrder(this::nextNAryPriorityQueue);
        checkOrder(this::nextBinomialQueue);
        checkOrder(this::nextFibonacciHeap);
        checkOrder(this::nextPairingHeap);
        checkOrder(this::nextLeftListTree);
        checkOrder(this::nextSkewHeap);
        checkOrder(this::nextLinkedPriorityQueue);
    }

    private void checkAddAll(@NotNull Supplier<PriorityQueue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            PriorityQueue<String> dst = supplier.get();
            dst.clear();
            PriorityQueue<String> src = nextPriorityQueue();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    dst.offer(element);
                    PublicIntSlot.increase(map, element);
                }
                size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    src.offer(element);
                    PublicIntSlot.increase(map, element);
                }
                Assert.assertTrue(dst.addAll(src));
                src.checkHealth();
                dst.checkHealth();
                for (int index = 0; index < size; index++) {
                    if (rc.nextBoolean()) {
                        src.offer(nextString());
                    } else {
                        src.remove();
                    }
                }
                while (!dst.isEmpty()) {
                    PublicIntSlot.decrease(map, dst.remove());
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(dst);
                System.out.println(src);
                throw e;
            }
        }
    }

    @Test
    public void checkAddAll() {
        checkAddAll(this::nextArrayPriorityQueue);
        checkAddAll(this::nextBinaryPriorityQueue);
        checkAddAll(this::nextNAryPriorityQueue);
        checkAddAll(this::nextBinomialQueue);
        checkAddAll(this::nextFibonacciHeap);
        checkAddAll(this::nextPairingHeap);
        checkAddAll(this::nextLeftListTree);
        checkAddAll(this::nextSkewHeap);
        checkAddAll(this::nextLinkedPriorityQueue);
    }

    private void checkRemoveAndTransfer(@NotNull Supplier<AbstractPriorityQueue<String>> supplier) {
        //
    }

    private void checkRemoveAllAndTransfer(@NotNull Supplier<AbstractPriorityQueue<String>> supplier) {
        //
    }

    private void checkIterator(@NotNull Supplier<PriorityQueue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            PriorityQueue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    queue.offer(nextString());
                }
                for (String element : queue) {
                    PublicIntSlot.increase(map, element);
                }
                while (!queue.isEmpty()) {
                    PublicIntSlot.decrease(map, queue.remove());
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkIterator() {
        checkIterator(this::nextArrayPriorityQueue);
        checkIterator(this::nextBinaryPriorityQueue);
        checkIterator(this::nextNAryPriorityQueue);
        checkIterator(this::nextBinomialQueue);
        checkIterator(this::nextFibonacciHeap);
        checkIterator(this::nextPairingHeap);
        checkIterator(this::nextLeftListTree);
        checkIterator(this::nextSkewHeap);
        checkIterator(this::nextLinkedPriorityQueue);
    }

    private void checkIteratorRemove(@NotNull Supplier<Queue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 3;
                for (int index = 0; index < size; index++) {
                    queue.offer(nextString());
                }
                for (String element : queue) {
                    PublicIntSlot.increase(map, element);
                }
                {
                    size = rc.nextInt(size) + 1;
                    Iterator<String> iterator = queue.iterator();
                    for (int index = 0; index < size; index++) {
                        PublicIntSlot.decrease(map, iterator.next());
                    }
                    iterator.remove();
                    while (iterator.hasNext()) {
                        PublicIntSlot.decrease(map, iterator.next());
                    }
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
                String element0 = queue.remove();
                while (!queue.isEmpty()) {
                    String element1 = queue.remove();
                    Assert.assertTrue(element0.compareTo(element1) <= 0);
                    element0 = element1;
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkIteratorRemove() {
        checkIteratorRemove(this::nextArrayPriorityQueue);
        checkIteratorRemove(this::nextLeftListTree);
        checkIteratorRemove(this::nextSkewHeap);
    }

    private void checkRemoveIf(@NotNull Supplier<Queue<String>> supplier) {
        final Map<String, PublicIntSlot> map = new HashMap<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            map.clear();
            Queue<String> queue = supplier.get();
            try {
                for (int index = 0; index < SIZE; index++) {
                    queue.offer(nextString());
                }
                queue.removeIf(element -> {
                    if (rc.nextBoolean()) {
                        return true;
                    } else {
                        PublicIntSlot.increase(map, element);
                        return false;
                    }
                });
                String element0 = queue.remove();
                while (true) {
                    PublicIntSlot.decrease(map, element0);
                    if (queue.isEmpty()) {
                        break;
                    }
                    String element1 = queue.remove();
                    Assert.assertTrue(element0.compareTo(element1) <= 0);
                    element0 = element1;
                }
                for (PublicIntSlot slot : map.values()) {
                    Assert.assertEquals(0, slot.value);
                }
            } catch (AssertionError e) {
                System.out.println(map);
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkRemoveIf() {
        checkRemoveIf(this::nextArrayPriorityQueue);
        checkRemoveIf(this::nextBinaryPriorityQueue);
        checkRemoveIf(this::nextNAryPriorityQueue);
        checkRemoveIf(this::nextLeftListTree);
        checkRemoveIf(this::nextSkewHeap);
    }

    private void checkContains(@NotNull Supplier<Queue<String>> supplier) {
        final HashSet<String> set = new HashSet<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            set.clear();
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    String element = nextString();
                    queue.offer(element);
                    set.add(element);
                }
                for (String element : set) {
                    Assert.assertTrue(queue.contains(element));
                }
            } catch (AssertionError e) {
                System.out.println(set);
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkContains() {
        checkContains(this::nextBinaryPriorityQueue);
        checkContains(this::nextNAryPriorityQueue);
        checkContains(this::nextBinomialQueue);
        checkContains(this::nextFibonacciHeap);
        checkContains(this::nextPairingHeap);
        checkContains(this::nextLeftListTree);
        checkContains(this::nextSkewHeap);
        checkContains(this::nextLinkedPriorityQueue);
    }

    private void checkToArray(@NotNull Supplier<Queue<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Queue<String> queue = supplier.get();
            try {
                int size = rc.nextInt(SIZE) + 1;
                for (int index = 0; index < size; index++) {
                    queue.offer(nextString());
                }
                Object[] array = queue.toArray();
                Assert.assertEquals(queue.size(), array.length);
                int index = 0;
                for (String element : queue) {
                    Assert.assertEquals(element, array[index++]);
                }
                Assert.assertEquals(index, array.length);
            } catch (AssertionError e) {
                System.out.println(queue);
                throw e;
            }
        }
    }

    @Test
    public void checkToArray() {
        checkToArray(this::nextArrayPriorityQueue);
        checkToArray(this::nextBinaryPriorityQueue);
        checkToArray(this::nextNAryPriorityQueue);
        checkToArray(this::nextBinomialQueue);
        checkToArray(this::nextFibonacciHeap);
        checkToArray(this::nextPairingHeap);
        checkToArray(this::nextLeftListTree);
        checkToArray(this::nextSkewHeap);
        checkToArray(this::nextLinkedPriorityQueue);
    }
}
