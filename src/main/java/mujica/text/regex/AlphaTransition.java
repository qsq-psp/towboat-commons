package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntPredicate;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/5/24")
public class AlphaTransition extends Transition implements IntPredicate {

    @NotNull
    protected final IntPredicate predicate;

    public AlphaTransition(int s0, int s1, @NotNull IntPredicate predicate) {
        super(s0, s1);
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public AlphaTransition duplicate() {
        return new AlphaTransition(s0, s1, predicate);
    }

    @NotNull
    @Override
    public AlphaTransition reverse() {
        if (s0 != s1) {
            return new AlphaTransition(s1, s0, predicate);
        } else {
            return this;
        }
    }

    @Override
    public boolean test(int value) {
        return predicate.test(value);
    }
}
