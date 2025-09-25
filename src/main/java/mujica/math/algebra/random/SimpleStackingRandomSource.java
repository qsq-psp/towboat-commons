package mujica.math.algebra.random;

import mujica.ds.generic.list.TruncateList;
import mujica.reflect.function.StateDuplicator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/5/9")
public class SimpleStackingRandomSource implements StackingRandomSource {

    @NotNull
    private final RandomSource current;

    @NotNull
    private final UnaryOperator<RandomSource> newDuplicator;

    @NotNull
    private final StateDuplicator<RandomSource> stateDuplicator;

    private final TruncateList<RandomSource> stack = new TruncateList<>();

    public SimpleStackingRandomSource(@NotNull RandomSource root, @NotNull UnaryOperator<RandomSource> newDuplicator, @NotNull StateDuplicator<RandomSource> stateDuplicator) {
        super();
        this.current = root;
        this.newDuplicator = newDuplicator;
        this.stateDuplicator = stateDuplicator;
    }

    @Override
    public int next(int bitCount) {
        return current.next(bitCount);
    }

    @Override
    public void save() {
        stack.add(newDuplicator.apply(current));
    }

    @Override
    public void restore() throws NoSuchElementException {
        try {
            stateDuplicator.duplicate(stack.removeLast(), current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void reset() throws NoSuchElementException {
        try {
            stateDuplicator.duplicate(stack.getFirst(), current);
            stack.clear();
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }
}
