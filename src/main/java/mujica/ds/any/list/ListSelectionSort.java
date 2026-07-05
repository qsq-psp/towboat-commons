package mujica.ds.any.list;

import mujica.ds.any.set.CollectionConstant;
import mujica.ds.i32.I32Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created on 2026/2/10.
 */
@CodeHistory(date = "2026/2/10")
@ReferencePage(title = "选择排序", href = "https://oi-wiki.org/basic/selection-sort/")
public class ListSelectionSort<T> extends SortingAlgorithm<List<T>> {

    @NotNull
    final Comparator<T> comparator;

    public ListSelectionSort(@NotNull Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    @NotNull
    @Override
    public MonotonicityDirection monotonicity() {
        return MonotonicityDirection.ASCENDING;
    }

    @Override
    public long sort(@NotNull List<T> list, int startIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < endIndex) {
            int selectedIndex = startIndex;
            T selectedValue = list.get(selectedIndex); // select max
            for (int index = startIndex + 1; index < endIndex; index++) {
                T value = list.get(index);
                if (comparator.compare(value, selectedValue) > 0) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            endIndex--;
            if (selectedIndex != endIndex) {
                list.set(selectedIndex, list.set(endIndex, selectedValue));
            }
        }
        return operationCount;
    }

    @Override
    public long sort(@NotNull List<T> list) {
        return sort(list, 0, list.size());
    }

    @Override
    public long sortPart(@NotNull List<T> list, int startIndex, int midIndex, int endIndex) {
        long operationCount = 0L;
        while (startIndex < midIndex) {
            int selectedIndex = startIndex;
            T selectedValue = list.get(selectedIndex); // select min
            for (int index = startIndex + 1; index < endIndex; index++) {
                T value = list.get(index);
                if (comparator.compare(selectedValue, value) > 0) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            if (selectedIndex != startIndex) {
                list.set(selectedIndex, list.set(startIndex, selectedValue));
            }
            startIndex++;
        }
        return operationCount;
    }

    @SuppressWarnings("unchecked")
    @Override
    public long sortUnique(@NotNull List<T> list, int startIndex, @NotNull I32Slot endSlot) {
        final int endIndex = endSlot.getI32();
        long operationCount = 0L;
        int readIndex = startIndex;
        int writeIndex = startIndex;
        Object lastValue = CollectionConstant.EMPTY;
        while (readIndex < endIndex) {
            int selectedIndex = readIndex;
            T selectedValue = list.get(selectedIndex); // select min
            for (int index = readIndex + 1; index < endIndex; index++) {
                T value = list.get(index);
                if (comparator.compare(selectedValue, value) > 0) {
                    selectedIndex = index;
                    selectedValue = value;
                    operationCount++;
                }
            }
            list.set(selectedIndex, list.get(readIndex));
            if (lastValue == CollectionConstant.EMPTY || comparator.compare((T) lastValue, selectedValue) != 0) {
                list.set(writeIndex++, selectedValue);
                lastValue = selectedValue;
            }
            readIndex++;
        }
        endSlot.setI32(writeIndex);
        return operationCount;
    }
}
