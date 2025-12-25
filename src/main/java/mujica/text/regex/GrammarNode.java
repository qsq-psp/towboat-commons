package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2021/9/15", project = "nettyon", name = "Unit")
@CodeHistory(date = "2021/10/17", project = "va", name = "GrammarElement")
@CodeHistory(date = "2022/3/27", project = "infrastructure", name = "Grammar")
@CodeHistory(date = "2025/12/15")
public abstract class GrammarNode {

    protected GrammarNode() {
        super();
    }

    public void buildNFA(@NotNull NFA nfa, int s0, int s1) {
        nfa.add(new EpsilonTransition(s0, s1));
    }
}
