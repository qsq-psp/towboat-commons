package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@CodeHistory(date = "2021/10/17", project = "va")
@CodeHistory(date = "2022/3/28", project = "infrastructure")
@CodeHistory(date = "2025/5/24")
@DirectSubclass({IndexEpsilonTransition.class, EdgeEpsilonTransition.class})
public class EpsilonTransition extends Transition implements Predicate<CursorIntSequence> {

    public EpsilonTransition(int s0, int s1) {
        super(s0, s1);
    }

    @NotNull
    public EpsilonTransition duplicate() {
        return new EpsilonTransition(s0, s1);
    }

    @NotNull
    public EpsilonTransition reverse() {
        if (s0 != s1) {
            return new EpsilonTransition(s1, s0);
        } else {
            return this;
        }
    }

    @Override
    public boolean test(@NotNull CursorIntSequence input) {
        return true;
    }
}
