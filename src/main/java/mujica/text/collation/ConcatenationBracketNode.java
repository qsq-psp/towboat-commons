package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2026/2/21.
 */
@CodeHistory(date = "2026/2/21")
public class ConcatenationBracketNode extends BracketNode {

    private static final long serialVersionUID = 0x53719944F24691B9L;

    @NotNull
    final List<BracketNode> list;

    public ConcatenationBracketNode(@NotNull List<BracketNode> list) {
        super();
        this.list = list;
    }

    public ConcatenationBracketNode() {
        this(new ArrayList<>());
    }

    @Override
    public int pairCount() {
        int sum = 0;
        for (BracketNode item : list) {
            sum += item.pairCount();
        }
        return sum;
    }

    @Override
    public int maxDepth() {
        int max = 0;
        for (BracketNode item : list) {
            max = Math.max(max, item.maxDepth());
        }
        return max;
    }

    @Override
    public int compareTo(@NotNull BracketNode that) {
        return that.compareFromConcatenation(this);
    }

    @Override
    protected int compareFromEpsilon(@NotNull EpsilonBracketNode that) {
        return -1;
    }

    @Override
    protected int compareFromWrap(@NotNull WrapBracketNode that) {
        return -1;
    }

    @Override
    protected int compareFromConcatenation(@NotNull ConcatenationBracketNode that) {
        final int thisSize = this.list.size();
        final int thatSize = that.list.size();
        final int minSize = Math.min(thisSize, thatSize);
        for (int index = 0; index < minSize; index++) {
            int result = that.list.get(index).compareTo(this.list.get(index));
            if (result != 0) {
                return result;
            }
        }
        return Integer.compare(thatSize, thisSize);
    }

    @Override
    public void forEach(@NotNull LeftRightConsumer consumer) {
        for (BracketNode item : list) {
            item.forEach(consumer);
        }
    }

    @Override
    public void forEach(@NotNull TypedConsumer consumer) {
        for (BracketNode item : list) {
            item.forEach(consumer);
        }
    }

    @Override
    public void forEach(@NotNull BatchConsumer consumer) {
        for (BracketNode item : list) {
            item.forEach(consumer);
        }
    }

    @Override
    public void append(@NotNull Bracket[] brackets, @NotNull StringBuilder out) {
        for (BracketNode item : list) {
            item.append(brackets, out);
        }
    }
}
