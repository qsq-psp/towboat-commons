package mujica.ds.generic.set;

import mujica.ds.ConsistencyException;
import mujica.ds.InvariantException;
import mujica.ds.of_int.list.ResizePolicy;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

@CodeHistory(date = "2025/6/5")
public class LinearClosedHashSet<E> extends ClosedHashSet<E> {

    private static final long serialVersionUID = 0xfafaaa10a53c8fdaL;

    public LinearClosedHashSet(@Nullable ResizePolicy policy) {
        super(policy);
    }

    LinearClosedHashSet(@NotNull ResizePolicy policy, @NotNull Object[] array) {
        super(policy, array);
    }

    @NotNull
    @Override
    public LinearClosedHashSet<E> duplicate() {
        final LinearClosedHashSet<E> that = new LinearClosedHashSet<>(policy, array.clone());
        that.size = this.size;
        return that;
    }

    private int arrayIndex(Object object, int modulo) {
        int hash;
        if (object != null) {
            hash = Integer.MAX_VALUE & object.hashCode();
        } else {
            hash = 0;
        }
        return hash % modulo;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int modulo = array.length;
        final HashSet<Object> set = new HashSet<>();
        for (int k = 0; k < modulo; k++) {
            Object item = array[k];
            if (item instanceof CollectionConstant) {
                continue;
            }
            if (!set.add(item)) {
                consumer.accept(new InvariantException("identical item " + item));
            }
            int i0 = arrayIndex(item, modulo);
            for (int i = i0; i != k; i = (i + 1) % modulo) {
                if (array[i] == CollectionConstant.EMPTY) {
                    consumer.accept(new InvariantException("empty interval from " + i0 + " to " + k + " at " + i));
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
        final int modulo = array.length;
        int i = arrayIndex(element, modulo);
        for (int k = 0; k < modulo; k++) {
            Object item = array[i];
            if (item == CollectionConstant.EMPTY) {
                return false;
            }
            if (Objects.equals(element, item)) {
                assert !(item instanceof CollectionConstant);
                return true;
            }
            if (++i == modulo) {
                i = 0;
            }
        }
        return false;
    }

    /**
     * @return true if success
     */
    private boolean rehash() {
        final int oldModulo = array.length;
        final int newModulo = policy.nextCapacity(oldModulo);
        if (newModulo <= oldModulo) {
            return false;
        }
        final Object[] newArray = new Object[newModulo];
        Arrays.fill(newArray, CollectionConstant.EMPTY);
        OA:
        for (Object object : array) {
            if (object instanceof CollectionConstant) {
                continue;
            }
            int i = arrayIndex(object, newModulo);
            for (int k = 0; k < newModulo; k++) {
                Object item = newArray[i];
                if (item == CollectionConstant.EMPTY) { // only empty, no removed
                    newArray[i] = object;
                    continue OA;
                }
                if (++i == newModulo) {
                    i = 0;
                }
            }
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
            int modulo = array.length;
            if (policy.testLoadedSize(size + 1, modulo)) {
                continue;
            }
            int testIndex = arrayIndex(element, modulo);
            int slotIndex = -1;
            for (int k = 0; k < modulo; k++) {
                Object item = array[testIndex];
                if (item instanceof CollectionConstant) {
                    if (slotIndex == -1) {
                        slotIndex = testIndex;
                        if (policy.testLinkLength(k, modulo)) {
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
                if (++testIndex == modulo) {
                    testIndex = 0;
                }
            }
        } while (rehash());
        throw new RuntimeException();
    }

    @Override
    public boolean remove(Object element) {
        if (element instanceof CollectionConstant) {
            return false;
        }
        final int modulo = array.length;
        int i = arrayIndex(element, modulo);
        for (int k = 0; k < modulo; k++) {
            Object item = array[i];
            if (item == CollectionConstant.EMPTY) {
                return false;
            }
            if (Objects.equals(element, item)) {
                assert !(item instanceof CollectionConstant);
                array[i] = CollectionConstant.REMOVED;
                size--;
                modCount++;
                return true;
            }
            if (++i == modulo) {
                i = 0;
            }
        }
        return false;
    }
}
