package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

@CodeHistory(date = "2025/5/28", project = "Ultramarine", name = "RepeatList")
@CodeHistory(date = "2026/1/24")
public class ConcatList<E> extends AbstractList<E> implements Serializable {

    private static final long serialVersionUID = 0xE74F4D31FFED0C4EL;

    @NotNull
    final List<List<E>> segments;

    int[] offsets;

    ConcatList(@NotNull List<List<E>> segments) {
        super();
        this.segments = segments;
    }

    public ConcatList() {
        this(new ArrayList<>());
    }

    public void updateOffsets() {
        if (offsets != null) {
            return;
        }
        final int length = segments.size();
        offsets = new int[length];
        int position = 0;
        for (int index = 0; index < length; index++) {
            position += segments.get(index).size();
            offsets[index] = position;
        }
    }

    @Override
    public E get(int index) {
        updateOffsets();
        int low = 0;
        int high = offsets.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (index < offsets[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        if (low != 0) {
            index -= offsets[low - 1];
        }
        return segments.get(low).get(index);
    }

    @Override
    public int indexOf(Object element) {
        for (List<E> segment : segments) {
            int index = segment.indexOf(element);
            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object element) {
        for (int segmentIndex = segments.size() - 1; segmentIndex >= 0; segmentIndex--) {
            int index = segments.get(segmentIndex).lastIndexOf(element);
            if (index != -1) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        updateOffsets();
        return offsets[offsets.length - 1];
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean add(E element) {
        if (segments.add(List.of(element))) {
            offsets = null;
            modCount++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object element) {
        for (List<E> segment : segments) {
            if (segment.remove(element)) {
                offsets = null;
                modCount++;
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        segments.clear();
        offsets = null;
        modCount++;
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return new Iterator<>() {

            @NotNull
            final Iterator<List<E>> iterator0 = segments.iterator();

            Iterator<E> iterator1;

            {
                Iterator<E> iterator1 = null;
                while (iterator0.hasNext()) {
                    iterator1 = iterator0.next().iterator();
                    if (iterator1.hasNext()) {
                        break;
                    }
                }
                if (iterator1 == null) {
                    iterator1 = Collections.emptyIterator();
                }
                this.iterator1 = iterator1;
            }

            @Override
            public boolean hasNext() {
                return iterator1.hasNext();
            }

            @Override
            public E next() {
                final E element = iterator1.next();
                while (!iterator1.hasNext() && iterator0.hasNext()) {
                    iterator1 = iterator0.next().iterator();
                }
                return element;
            }
        };
    }
}
