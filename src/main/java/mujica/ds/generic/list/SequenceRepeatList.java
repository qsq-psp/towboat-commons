package mujica.ds.generic.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * Created on 2026/1/25.
 */
@CodeHistory(date = "2026/1/25")
public class SequenceRepeatList<E> extends PeriodicList<E> {

    private static final long serialVersionUID = 0xB413AAA6B8A7DB51L;

    @NotNull
    protected List<E> sequence;

    public SequenceRepeatList(@NotNull List<E> sequence, int size) {
        super();
        this.sequence = sequence;
        this.size = size;
    }

    @Override
    public int period() {
        return sequence.size();
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        final int period = sequence.size();
        if (period > 0) {
            index %= period;
            return sequence.get(index);
        } else {
            return null;
        }
    }

    @Override
    public boolean contains(Object element) {
        final int limit = Math.min(size, sequence.size());
        for (int index = 0; index < limit; index++) {
            if (Objects.equals(sequence.get(index), element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(Object element) {
        final int limit = Math.min(size, sequence.size());
        for (int index = 0; index < limit; index++) {
            if (Objects.equals(sequence.get(index), element)) {
                return index;
            }
        }
        return -1;
    }
}
