package mujica.ds.of_char.regex;

import mujica.ds.of_int.set.IntSet;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/3/28", project = "infrastructure", name = "ValueSet")
@CodeHistory(date = "2026/5/2")
public class AlphaNode extends GrammarNode {

    protected IntSet predicate;

    @Override
    public void buildNFA(@NotNull NFA nfa, int s0, int s1) {
        nfa.add(new AlphaTransition(s0, s1, predicate));
    }
}
