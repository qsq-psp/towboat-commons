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

@CodeHistory(date = "2025/7/4")
public class CongruentClosedHashSet<E> extends ClosedHashSet<E> {

    public CongruentClosedHashSet(@Nullable ResizePolicy policy) {
        super(ResizePolicy.checkPrime(policy));
    }

    CongruentClosedHashSet(@NotNull ResizePolicy policy, @NotNull Object[] array) {
        super(policy, array);
    }

    @NotNull
    @Override
    public CongruentClosedHashSet<E> duplicate() {
        final CongruentClosedHashSet<E> that = new CongruentClosedHashSet<>(policy, array.clone());
        that.size = this.size;
        return that;
    }

    private int positiveHash(Object object) {
        if (object == null) {
            return 0;
        } else {
            return Integer.MAX_VALUE & object.hashCode();
        }
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int modulo = array.length;
        final HashSet<Object> set = new HashSet<>();
        for (int k0 = 0; k0 < modulo; k0++) {
            Object item = array[k0];
            if (item instanceof CollectionConstant) {
                continue;
            }
            if (!set.add(item)) {
                consumer.accept(new InvariantException("identical item " + item));
            }
            int i = positiveHash(item);
            int u = i / modulo;
            i = i - u * modulo; // from 0 to (modulo - 1)
            u = u % (modulo - 1) + 1; // from 1 to (modulo - 1)
            int i0 = i;
            for (int j = 0; j < modulo; j++) {
                if (i == k0) {
                    break;
                }
                if (array[i] == CollectionConstant.EMPTY) {
                    consumer.accept(new InvariantException("empty interval from " + i0 + " to " + k0 + " at " + i));
                }
                i += u;
                if (i < modulo) {
                    continue;
                }
                i -= modulo;
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
        int i = positiveHash(element);
        int u = i / modulo;
        i = i - u * modulo; // from 0 to (modulo - 1)
        u = u % (modulo - 1) + 1; // from 1 to (modulo - 1)
        for (int k = 0; k < modulo; k++) {
            Object item = array[i];
            if (item == CollectionConstant.EMPTY) {
                return false;
            }
            if (Objects.equals(element, item)) {
                assert !(item instanceof CollectionConstant);
                return true;
            }
            i += u;
            if (i < modulo) {
                continue;
            }
            i -= modulo;
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
            int i = positiveHash(object);
            int u = i / newModulo;
            i = i - u * newModulo; // from 0 to (modulo - 1)
            u = u % (newModulo - 1) + 1; // from 1 to (modulo - 1)
            for (int k = 0; k < newModulo; k++) {
                Object item = newArray[i];
                if (item == CollectionConstant.EMPTY) { // only empty, no removed
                    newArray[i] = object;
                    continue OA;
                }
                i += u;
                if (i < newModulo) {
                    continue;
                }
                i -= newModulo;
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
            int testIndex = positiveHash(element);
            int deltaIndex = testIndex / modulo;
            testIndex = testIndex - deltaIndex * modulo; // from 0 to (modulo - 1)
            deltaIndex = deltaIndex % (modulo - 1) + 1; // from 1 to (modulo - 1)
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
                testIndex += deltaIndex;
                if (testIndex < modulo) {
                    continue;
                }
                testIndex -= modulo;
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
        int i = positiveHash(element);
        int u = i / modulo;
        i = i - u * modulo; // from 0 to (modulo - 1)
        u = u % (modulo - 1) + 1; // from 1 to (modulo - 1)
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
            i += u;
            if (i < modulo) {
                continue;
            }
            i -= modulo;
        }
        return false;
    }
}
