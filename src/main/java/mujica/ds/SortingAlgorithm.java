package mujica.ds;

import mujica.ds.generic.list.MonotonicityDirection;
import mujica.ds.of_double.list.NotANumberSortingPolicy;
import mujica.ds.of_int.IntSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Objects;

@CodeHistory(date = "2025/3/15")
public abstract class SortingAlgorithm<T> {

    @Name(value = "单调性", language = "zh")
    @NotNull
    public abstract MonotonicityDirection monotonicity();

    /**
     * A sorting algorithm is stable if equal elements always reserve order after sorting
     */
    @Name(value = "稳定性", language = "zh")
    @ReferencePage(title = "稳定性", href = "https://oi-wiki.org/basic/sort-intro/#%E7%A8%B3%E5%AE%9A%E6%80%A7")
    public boolean stable() {
        return false;
    }

    public boolean returnsReverseOrderNumber() {
        return false;
    }

    @NotNull
    public NotANumberSortingPolicy nanPolicy() {
        return NotANumberSortingPolicy.ANY_POSITION;
    }

    public abstract long sort(@NotNull T target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int endIndex);

    public long sort(@NotNull T target) {
        return sort(target, 0, Array.getLength(target)); // java.lang.IllegalArgumentException: Argument is not an array
    }

    public long sortPart(@NotNull T target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int midIndex, @Index(of = "target", inclusive = false) int endIndex) {
        return sort(target, startIndex, endIndex);
    }

    public long sortUnique(@NotNull T target, @Index(of = "target") int startIndex, @NotNull @Index(of = "target", inclusive = false) IntSlot endSlot) {
        final int endIndex = endSlot.getInt();
        if (endIndex - startIndex <= 1) {
            return 0L;
        }
        long operationCount = sort(target, startIndex, endIndex);
        Object previousElement = Array.get(target, startIndex++);
        int writeIndex = startIndex;
        for (int readIndex = startIndex; readIndex < endIndex; readIndex++) {
            Object currentElement = Array.get(target, readIndex);
            if (Objects.equals(previousElement, currentElement)) {
                continue;
            }
            Array.set(target, writeIndex++, currentElement);
            previousElement = currentElement;
            operationCount++;
        }
        endSlot.setInt(writeIndex);
        return operationCount;
    }
}
