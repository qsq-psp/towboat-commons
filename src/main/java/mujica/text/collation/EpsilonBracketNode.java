package mujica.text.collation;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/2/21.
 */
public class EpsilonBracketNode extends BracketNode {

    private static final long serialVersionUID = 0x755CC452ECA28F82L;

    @Override
    public int pairCount() {
        return 0;
    }

    @Override
    public int maxDepth() {
        return 0;
    }

    @Override
    public int compareTo(@NotNull BracketNode that) {
        return that.compareFromEpsilon(this);
    }

    @Override
    protected int compareFromEpsilon(@NotNull EpsilonBracketNode that) {
        return 0; // equal
    }

    @Override
    protected int compareFromWrap(@NotNull WrapBracketNode that) {
        return 1; // epsilon is smaller
    }

    @Override
    protected int compareFromConcatenation(@NotNull ConcatenationBracketNode that) {
        return 1; // epsilon is smaller
    }

    @Override
    public void forEach(@NotNull LeftRightConsumer consumer) {
        // pass
    }

    @Override
    public void forEach(@NotNull TypedConsumer consumer) {
        // pass
    }

    @Override
    public void forEach(@NotNull BatchConsumer consumer) {
        // pass
    }

    @Override
    public void append(@NotNull Bracket[] brackets, @NotNull StringBuilder out) {
        // pass
    }
}
