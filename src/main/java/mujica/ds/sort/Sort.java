package mujica.ds.sort;

import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Objects;

@CodeHistory(date = "2025/3/15", name = "SortingAlgorithm")
@CodeHistory(date = "2026/7/10")
public interface Sort<A> {

    default boolean isDescending() {
        return false;
    }

    @Name(value = "稳定性", language = "zh")
    @ReferencePage(title = "稳定性", href = "https://oi-wiki.org/basic/sort-intro/#%E7%A8%B3%E5%AE%9A%E6%80%A7")
    default boolean isStable() {
        return false; // a sorting algorithm is stable if equal elements always reserve order after sorting
    }

    default boolean returnsReverseOrderNumber() {
        return false;
    }

    long sort(@NotNull A target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int endIndex);

    default long sort(@NotNull A target) {
        return sort(target, 0, Array.getLength(target));
    }

    default long sortPart(@NotNull A target, @Index(of = "target") int startIndex, @Index(of = "target", inclusive = false) int midIndex, @Index(of = "target", inclusive = false) int endIndex) {
        return sort(target, startIndex, endIndex);
    }

    default long sortUnique(@NotNull A target, @Index(of = "target") int startIndex, @NotNull @Index(of = "target", inclusive = false) I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
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
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
