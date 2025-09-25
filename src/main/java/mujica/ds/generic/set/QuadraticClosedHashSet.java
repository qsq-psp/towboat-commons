package mujica.ds.generic.set;

import mujica.ds.ConsistencyException;
import mujica.ds.InvariantException;
import mujica.ds.of_int.list.QuadraticProbingList;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/29")
public class QuadraticClosedHashSet<E> extends ClosedHashSet<E> {

    private static final long serialVersionUID = 0x4314aaa35701d3cbL;

    @NotNull
    transient QuadraticProbingList probingList;

    public QuadraticClosedHashSet(@Nullable ResizePolicy policy) {
        super(ResizePolicy.checkQuadraticFull(policy));
        probingList = new QuadraticProbingList(array.length);
    }

    QuadraticClosedHashSet(@NotNull ResizePolicy policy, @NotNull Object[] array) {
        super(policy, array);
        probingList = new QuadraticProbingList(array.length);
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        probingList = new QuadraticProbingList(array.length);
    }

    @NotNull
    @Override
    public QuadraticClosedHashSet<E> duplicate() {
        final QuadraticClosedHashSet<E> that = new QuadraticClosedHashSet<>(policy, array.clone());
        that.size = this.size;
        return that;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int modulo = array.length;
        if (modulo != probingList.intLength()) {
            consumer.accept(new ConsistencyException("modulo"));
            return;
        }
        final HashSet<Object> set = new HashSet<>();
        for (int k = 0; k < modulo; k++) {
            Object item = array[k];
            if (item instanceof CollectionConstant) {
                continue;
            }
            if (!set.add(item)) {
                consumer.accept(new InvariantException("identical item " + item));
            }
            for (int index : probingList.setHash(Objects.hashCode(item))) {
                if (k == index) {
                    break;
                }
                if (array[index] == CollectionConstant.EMPTY) {
                    consumer.accept(new InvariantException("empty interval from to " + k + " at " + index));
                }
            }
        }
        if (set.size() != size) {
            consumer.accept(new ConsistencyException("size", set.size(), size));
        }
    }

    @Override
    public boolean contains(Object element) {
        if (element instanceof CollectionConstant) {
            return false;
        }
        for (int index : probingList.setHash(Objects.hashCode(element))) {
            Object item = array[index];
            if (item == CollectionConstant.EMPTY) {
                return false;
            }
            if (Objects.equals(element, item)) {
                assert !(item instanceof CollectionConstant);
                return true;
            }
        }
        return false;
    }

    private boolean rehash() {
        // System.out.println(Arrays.toString(array));
        final int oldModulo = array.length;
        final int newModulo = policy.nextCapacity(oldModulo);
        // System.out.println("old " + oldModulo + " new " + newModulo);
        if (newModulo <= oldModulo) {
            // System.out.println("full");
            return false;
        }
        final Object[] newArray = new Object[newModulo];
        Arrays.fill(newArray, CollectionConstant.EMPTY);
        probingList.setModulo(newModulo);
        OA:
        for (Object object : array) {
            if (object instanceof CollectionConstant) {
                continue;
            }
            for (int index : probingList.setHash(Objects.hashCode(object))) {
                Object item = newArray[index];
                if (item == CollectionConstant.EMPTY) { // only empty, no removed
                    // System.out.println("index = " + index);
                    newArray[index] = object;
                    continue OA;
                }
            }
            // System.out.println("never");
            probingList.setModulo(oldModulo);
            return false;
        }
        array = newArray;
        modCount++;
        return true;
    }

    @Override
    public boolean add(E element) {
        if (element instanceof CollectionConstant) {
            throw new IllegalArgumentException();
        }
        do {
            if (policy.testLoadedSize(size + 1, array.length)) {
                // System.out.println("LoadedSize " + summaryToString() + " " + element);
                continue;
            }
            int linkLength = 0;
            int slotIndex = -1;
            for (int testIndex : probingList.setHash(Objects.hashCode(element))) {
                Object item = array[testIndex];
                if (item instanceof CollectionConstant) {
                    if (slotIndex == -1) {
                        slotIndex = testIndex;
                        if (policy.testLinkLength(linkLength, array.length)) {
                            // System.out.println("LinkLength " + summaryToString() + " " + element);
                            break;
                        }
                    }
                    if (item == CollectionConstant.EMPTY) {
                        array[slotIndex] = element;
                        size++;
                        modCount++;
                        return true;
                    }
                } else if (Objects.equals(element, item)) {
                    return false;
                }
                linkLength++;
            }
        } while (rehash());
        throw new RuntimeException();
    }

    @Override
    public boolean remove(Object element) {
        if (element instanceof CollectionConstant) {
            return false;
        }
        for (int index : probingList.setHash(Objects.hashCode(element))) {
            Object item = array[index];
            if (item == CollectionConstant.EMPTY) {
                return false;
            }
            if (Objects.equals(element, item)) {
                assert !(item instanceof CollectionConstant);
                array[index] = CollectionConstant.REMOVED;
                size--;
                modCount++;
                return true;
            }
        }
        return false;
    }
}
