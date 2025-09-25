package mujica.ds.generic.set;

import io.netty.buffer.ByteBufUtil;
import mujica.ds.of_int.list.*;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Supplier;

@CodeHistory(date = "2025/6/13")
public class AxiomSetTest {

    @BeforeClass
    public static void initializeNetty() {
        ByteBufUtil.hexDump(new byte[0]);
    }

    private static final int REPEAT = 78;

    private static final int SIZE = 90;

    private final RandomContext rc = new RandomContext();

    @NotNull
    private String nextString() {
        final int length = rc.nextMinInt(7, 4) + 1;
        final char[] ca = new char[length];
        for (int index = 0; index < length; index++) {
            ca[index] = (char) ('a' + rc.nextMinInt(26, 6));
        }
        return new String(ca);
    }

    @NotNull
    private ResizePolicy nextResizePolicy() {
        switch (rc.nextInt(16)) {
            default:
            case 0:
                return LookUpResizePolicy.PAPER;
            case 1:
                return LookUpResizePolicy.NATURAL;
            case 2:
                return LookUpResizePolicy.GOLDEN;
            case 3:
            case 4:
            case 5:
                return ShiftResizePolicy.INSTANCE;
            case 6:
            case 7:
            case 8:
            case 9:
                return new TwiceResizePolicy(rc);
            case 10:
            case 11:
            case 12:
                return new HalfResizePolicy(rc);
            case 13:
                return new Order1ResizePolicy(rc);
            case 14:
                return ModuloLookUpResizePolicy.INSTANCE_59;
            case 15:
                return ModuloLookUpResizePolicy.INSTANCE_67;
        }
    }

    @NotNull
    private ResizePolicy nextPrimeResizePolicy() {
        switch (rc.nextInt(3)) {
            default:
            case 0:
                return LookUpResizePolicy.PRIME_PAPER;
            case 1:
                return LookUpResizePolicy.PRIME_GOLDEN;
            case 2:
                return LookUpResizePolicy.PRIME_FIBONACCI;
        }
    }

    @NotNull
    private ResizePolicy nextQuadraticResizePolicy() {
        switch (rc.nextInt(2)) {
            default:
            case 0:
                return ModuloLookUpResizePolicy.INSTANCE_59;
            case 1:
                return ModuloLookUpResizePolicy.INSTANCE_67;
        }
    }

    @NotNull
    private <E> AxiomSet<E> nextLinearClosedHashSet() {
        return new LinearClosedHashSet<>(nextResizePolicy());
    }

    @NotNull
    private <E> AxiomSet<E> nextCongruentClosedHashSet() {
        return new CongruentClosedHashSet<>(nextPrimeResizePolicy());
    }

    @NotNull
    private <E> AxiomSet<E> nextQuadraticClosedHashSet() {
        return new QuadraticClosedHashSet<>(nextQuadraticResizePolicy());
    }

    @NotNull
    private <E> AxiomSet<E> nextLinkOpenHashSet() {
        return new LinkOpenHashSet<>(nextResizePolicy());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void checkHealth(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            AxiomSet<String> set = supplier.get();
            list.add(set.toString());
            for (int index = 0; index < SIZE; index++) {
                switch (rc.nextInt(5)) {
                    case 0:
                        set.contains(nextString());
                        break;
                    default:
                    case 1:
                        set.add(nextString());
                        break;
                    case 2:
                        set.remove(nextString());
                        break;
                }
                String string = set.toString();
                if (!string.equals(list.get(list.size() - 1))) {
                    list.add(string);
                }
            }
            try {
                set.checkHealth();
            } catch (RuntimeException re) {
                for (String string : list) {
                    System.out.println(string);
                }
                throw re;
            }
        }
    }

    @Test
    public void checkHealth() {
        checkHealth(this::nextLinearClosedHashSet);
        checkHealth(this::nextCongruentClosedHashSet);
        checkHealth(this::nextQuadraticClosedHashSet);
        checkHealth(this::nextLinkOpenHashSet);
    }

    private void checkAdd(@NotNull Supplier<AxiomSet<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                Assert.assertEquals(expected.add(element), actual.add(element)); // no boolean implementation, cast to Object
            }
        }
    }

    @Test
    public void checkAdd() {
        checkAdd(this::nextLinearClosedHashSet);
        checkAdd(this::nextCongruentClosedHashSet);
        checkAdd(this::nextQuadraticClosedHashSet);
        checkAdd(this::nextLinkOpenHashSet);
    }

    private void checkSize(@NotNull Supplier<AxiomSet<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
                Assert.assertEquals(expected.size(), actual.size());
            }
        }
    }

    @Test
    public void checkSize() {
        checkSize(this::nextLinearClosedHashSet);
        checkSize(this::nextCongruentClosedHashSet);
        checkSize(this::nextQuadraticClosedHashSet);
        checkSize(this::nextLinkOpenHashSet);
    }

    private void checkRemove(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>(SIZE);
        for (int index = 0; index < SIZE; index++) {
            list.add(nextString());
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
                if (rc.nextBoolean()) {
                    list.set(index, element);
                }
            }
            rc.shuffleList(list);
            for (String element : list) {
                Assert.assertEquals(expected.remove(element), actual.remove(element)); // no boolean implementation, cast to Object
            }
        }
    }

    @Test
    public void checkRemove() {
        checkRemove(this::nextLinearClosedHashSet);
        checkRemove(this::nextCongruentClosedHashSet);
        checkRemove(this::nextQuadraticClosedHashSet);
        checkRemove(this::nextLinkOpenHashSet);
    }

    private void checkContains(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>(SIZE);
        for (int index = 0; index < SIZE; index++) {
            list.add(nextString());
        }
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
                if (rc.nextBoolean()) {
                    list.set(index, element);
                }
            }
            rc.shuffleList(list);
            for (String element : list) {
                Assert.assertEquals(expected.contains(element), actual.contains(element)); // no boolean implementation, cast to Object
            }
        }
    }

    @Test
    public void checkContains() {
        checkContains(this::nextLinearClosedHashSet);
        checkContains(this::nextCongruentClosedHashSet);
        checkContains(this::nextQuadraticClosedHashSet);
        checkContains(this::nextLinkOpenHashSet);
    }

    private void checkIterator(@NotNull Supplier<AxiomSet<String>> supplier) {
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
            }
            try {
                expected.removeIf(element -> {
                    if (rc.nextBoolean()) {
                        Assert.assertTrue(actual.remove(element));
                        return true;
                    } else {
                        return false;
                    }
                });
                for (String element : actual) {
                    Assert.assertTrue(expected.remove(element));
                }
                Assert.assertTrue(expected.isEmpty());
            } catch (AssertionError e) {
                System.out.println(expected);
                System.out.println(actual);
                throw e;
            }
        }
    }

    @Test
    public void checkIterator() {
        checkIterator(this::nextLinearClosedHashSet);
        checkIterator(this::nextCongruentClosedHashSet);
        checkIterator(this::nextQuadraticClosedHashSet);
        checkIterator(this::nextLinkOpenHashSet);
    }

    private void checkIteratorRemove(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
            }
            list.clear();
            try {
                for (Iterator<String> it = actual.iterator(); it.hasNext();) {
                    String element = it.next();
                    if (rc.nextBoolean()) {
                        // System.out.println(actual);
                        it.remove();
                        expected.remove(element);
                        list.add(element);
                    }
                }
                rc.shuffleList(list);
                for (String element : expected) {
                    Assert.assertTrue(actual.contains(element));
                }
                Assert.assertEquals(expected.size(), actual.size());
                for (String element : list) {
                    Assert.assertFalse(actual.contains(element));
                }
            } catch (Throwable e) {
                System.out.println(actual);
                throw e;
            }
        }
    }

    @Test
    public void checkIteratorRemove() {
        checkIteratorRemove(this::nextLinearClosedHashSet);
        checkIteratorRemove(this::nextCongruentClosedHashSet);
        checkIteratorRemove(this::nextQuadraticClosedHashSet);
        checkIteratorRemove(this::nextLinkOpenHashSet);
    }

    private void checkRemoveIf(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            Set<String> expected = new HashSet<>();
            Set<String> actual = supplier.get();
            list.clear();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                expected.add(element);
                actual.add(element);
                if (rc.nextBoolean()) {
                    list.add(element);
                }
            }
            for (String element : list) {
                expected.remove(element);
                actual.remove(element); // do not use removeAll
            }
            actual.removeAll(list);
            for (Iterator<String> it = actual.iterator(); it.hasNext();) {
                String element = it.next();
                if (rc.nextBoolean()) {
                    it.remove();
                    expected.remove(element);
                    list.add(element);
                }
            }
            rc.shuffleList(list);
            for (String element : expected) {
                Assert.assertTrue(actual.contains(element));
            }
            Assert.assertEquals(expected.size(), actual.size());
            for (String element : list) {
                Assert.assertFalse(actual.contains(element));
            }
        }
    }

    @Test
    public void checkRemoveIf() {
        checkRemoveIf(this::nextLinearClosedHashSet);
        checkRemoveIf(this::nextCongruentClosedHashSet);
        checkRemoveIf(this::nextQuadraticClosedHashSet);
        checkRemoveIf(this::nextLinkOpenHashSet);
    }

    private void checkGetArbitrary(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            list.clear();
            AxiomSet<String> set = supplier.get();
            for (int index = 0; index < SIZE; index++) {
                String element = nextString();
                set.add(element);
                list.add(element);
            }
            for (int index = 0; index < SIZE; index++) {
                String element;
                if (rc.nextBoolean()) {
                    element = set.getArbitrary(rc);
                } else {
                    element = set.getArbitrary(null);
                }
                Assert.assertTrue(set.contains(element));
                if (rc.nextBoolean()) {
                    set.add(nextString());
                } else {
                    set.remove(list.get(rc.nextInt(list.size())));
                }
            }
            set.checkHealth();
        }
    }

    @Test
    public void checkGetArbitrary() {
        checkGetArbitrary(this::nextLinearClosedHashSet);
        checkGetArbitrary(this::nextCongruentClosedHashSet);
        checkGetArbitrary(this::nextQuadraticClosedHashSet);
        checkGetArbitrary(this::nextLinkOpenHashSet);
    }

    private void checkRemoveArbitrary(@NotNull Supplier<AxiomSet<String>> supplier) {
        final ArrayList<String> list = new ArrayList<>();
        for (int repeatIndex = 0; repeatIndex < REPEAT; repeatIndex++) {
            list.clear();
            AxiomSet<String> set = supplier.get();
            int size = rc.nextInt(SIZE);
            for (int index = 0; index < size; index++) {
                String element = nextString();
                set.add(element);
                list.add(element);
            }
            size = set.size() / 2;
            for (int index = 0; index < size; index++) {
                String element;
                if (rc.nextBoolean()) {
                    element = set.removeArbitrary(rc);
                } else {
                    element = set.removeArbitrary(null);
                }
                Assert.assertFalse(set.contains(element));
                if (rc.nextBoolean()) {
                    set.add(nextString());
                } else {
                    set.remove(list.get(rc.nextInt(list.size())));
                }
            }
            set.checkHealth();
        }
    }

    @Test
    public void checkRemoveArbitrary() {
        checkRemoveArbitrary(this::nextLinearClosedHashSet);
        checkRemoveArbitrary(this::nextCongruentClosedHashSet);
        checkRemoveArbitrary(this::nextQuadraticClosedHashSet);
        checkRemoveArbitrary(this::nextLinkOpenHashSet);
    }
}
