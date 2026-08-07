package mujica.text.collation;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/2/27.
 */
@CodeHistory(date = "2026/2/27")
public class WrapBracketNode extends BracketNode {

    private static final long serialVersionUID = 0x7D66B743DF63E947L;

    @NotNull
    BracketNode body;

    int count;

    int type;

    public WrapBracketNode(@NotNull BracketNode body, int count, int type) {
        super();
        this.body = body;
        this.count = count;
        this.type = type;
    }

    public WrapBracketNode(@NotNull BracketNode body) {
        this(body, 1, 0);
    }

    @Override
    public int pairCount() {
        return body.pairCount() + count;
    }

    @Override
    public int maxDepth() {
        return body.maxDepth() + count;
    }

    @Override
    public int compareTo(@NotNull BracketNode that) {
        return that.compareFromWrap(this);
    }

    @Override
    protected int compareFromEpsilon(@NotNull EpsilonBracketNode that) {
        return -1;
    }

    @Override
    protected int compareFromWrap(@NotNull WrapBracketNode that) {
        int result = Integer.compare(that.type, this.type);
        if (result != 0) {
            return result;
        }
        result = Integer.compare(that.count, this.count);
        if (result != 0) {
            return result;
        }
        return that.body.compareTo(this.body);
    }

    @Override
    protected int compareFromConcatenation(@NotNull ConcatenationBracketNode that) {
        return 1;
    }

    @Override
    public void forEach(@NotNull LeftRightConsumer consumer) {
        for (int index = 0; index < count; index++) {
            consumer.acceptLeft();
        }
        body.forEach(consumer);
        for (int index = 0; index < count; index++) {
            consumer.acceptRight();
        }
    }

    @Override
    public void forEach(@NotNull TypedConsumer consumer) {
        for (int index = 0; index < count; index++) {
            consumer.acceptLeft(type);
        }
        body.forEach(consumer);
        for (int index = 0; index < count; index++) {
            consumer.acceptRight(type);
        }
    }

    @Override
    public void forEach(@NotNull BatchConsumer consumer) {
        consumer.acceptLeft(count, type);
        body.forEach(consumer);
        consumer.acceptRight(count, type);
    }

    @SuppressWarnings("StringRepeatCanBeUsed")
    @Override
    public void append(@NotNull Bracket[] brackets, @NotNull StringBuilder out) {
        final Bracket bracket = brackets[type];
        for (int index = 0; index < count; index++) {
            out.append(bracket.left);
        }
        body.append(brackets, out);
        for (int index = 0; index < count; index++) {
            out.append(bracket.right);
        }
    }
}
