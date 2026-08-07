package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2026/2/21.
 */
@CodeHistory(date = "2026/2/21")
public abstract class BracketNode implements Comparable<BracketNode>, Serializable {

    private static final long serialVersionUID = 0x184582CD8E3353D6L;

    public abstract int pairCount();

    public abstract int maxDepth();

    @Override
    public abstract int compareTo(@NotNull BracketNode that);

    protected abstract int compareFromEpsilon(@NotNull EpsilonBracketNode that);

    protected abstract int compareFromWrap(@NotNull WrapBracketNode that);

    protected abstract int compareFromConcatenation(@NotNull ConcatenationBracketNode that);

    @CodeHistory(date = "2026/3/6")
    public interface LeftRightConsumer {

        void acceptLeft();

        void acceptRight();
    }

    public abstract void forEach(@NotNull LeftRightConsumer consumer);

    @CodeHistory(date = "2026/3/6")
    public interface TypedConsumer {

        void acceptLeft(int type);

        void acceptRight(int type);
    }

    public abstract void forEach(@NotNull TypedConsumer consumer);

    @CodeHistory(date = "2026/3/6")
    public interface BatchConsumer {

        void acceptLeft(int count, int type);

        void acceptRight(int count, int type);
    }

    public abstract void forEach(@NotNull BatchConsumer consumer);

    @Deprecated
    public abstract void append(@NotNull Bracket[] brackets, @NotNull StringBuilder out);
}
