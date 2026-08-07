package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/21")
public class ConcatenationNode extends ContainerNode {

    @NotNull
    @Override
    public ConcatenationNode duplicate() {
        final ConcatenationNode that = new ConcatenationNode();
        that.content.addAll(this.content);
        return that;
    }

    @NotNull
    @Override
    public ConcatenationNode reverse() {
        final int size = this.content.size();
        if (size <= 1) {
            if (size == 1) {
                GrammarNode this0 = this.content.get(0);
                GrammarNode that0 = this0.reverse();
                if (this0 != that0) {
                    ConcatenationNode that = new ConcatenationNode();
                    that.content.add(that0);
                    return that;
                }
            }
            return this;
        }
        final ConcatenationNode that = new ConcatenationNode();
        for (int index = size - 1; index >= 0; index--) {
            that.content.add(this.content.get(index));
        }
        return that;
    }

    @Override
    public void buildNFA(@NotNull NFA nfa, int s0, int s1) {
        if (content.isEmpty()) {
            nfa.add(new EpsilonTransition(s0, s1));
            return;
        }
        for (int i = content.size(); i >= 0; i--) {
            int s2;
            if (i == 0) {
                s2 = s0;
            } else {
                s2 = nfa.newState();
            }
            content.get(i).buildNFA(nfa, s2, s1);
            s1 = s2;
        }
    }
}
