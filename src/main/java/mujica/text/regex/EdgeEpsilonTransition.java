package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;

@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/12/27")
public class EdgeEpsilonTransition extends EpsilonTransition {

    public static final int BOUNDARY = 2;

    @NotNull
    private final IntPredicate before, after;

    private final int configMask;

    public EdgeEpsilonTransition(int s0, int s1, @NotNull IntPredicate before, @NotNull IntPredicate after, int configMask) {
        super(s0, s1);
        this.before = before;
        this.after = after;
        this.configMask = configMask;
    }

    @Override
    public boolean test(@NotNull CursorIntSequence input) {
        final int cursorIndex = input.getCursorIndex();
        int beforeCode, afterCode;
        if (cursorIndex > 0) {
            beforeCode = before.test(input.getInt(cursorIndex - 1)) ? 1 : 0;
        } else {
            beforeCode = BOUNDARY;
        }
        if (cursorIndex < input.intLength()) {
            afterCode = after.test(input.getInt(cursorIndex)) ? 1 : 0;
        } else {
            afterCode = BOUNDARY;
        }
        return (configMask & (1 << (beforeCode * 3 + afterCode))) != 0;
    }
}
