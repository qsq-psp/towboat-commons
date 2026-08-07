package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/12/16")
public class IndexEpsilonTransition extends EpsilonTransition {

    /**
     * slot index in input sequence; a sequence of length n has (n + 1) slots
     * index >= 0 : from start (the first slot is 0)
     * index < 0: from end (the last slot is -1)
     */
    final int index;

    public IndexEpsilonTransition(int s0, int s1, int index) {
        super(s0, s1);
        this.index = index;
    }

    @NotNull
    public IndexEpsilonTransition duplicate() {
        return new IndexEpsilonTransition(s0, s1, index);
    }

    @NotNull
    public IndexEpsilonTransition reverse() {
        return new IndexEpsilonTransition(s1, s0, ~index);
    }

    @Override
    public boolean test(@NotNull CursorIntSequence input) {
        if (index >= 0) {
            return input.getCursorIndex() == index;
        } else {
            return input.getCursorIndex() - input.intLength() == index + 1;
        }
    }
}
