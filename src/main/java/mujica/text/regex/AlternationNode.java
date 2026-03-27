package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/23.
 */
@CodeHistory(date = "2025/12/23")
public class AlternationNode extends ContainerNode {

    @NotNull
    @Override
    public GrammarNode duplicate() {
        final AlternationNode that = new AlternationNode();
        that.content.addAll(this.content);
        return that;
    }

    @NotNull
    @Override
    public GrammarNode reverse() {
        final AlternationNode that = new AlternationNode();
        boolean changed = false;
        for (GrammarNode thisI : this.content) {
            GrammarNode thatI = thisI.reverse();
            if (thisI != thatI) {
                changed = true;
            }
            that.content.add(thatI);
        }
        if (changed) {
            return that;
        } else {
            return this;
        }
    }

    @Override
    public void buildNFA(@NotNull NFA nfa, int s0, int s1) {
        for (GrammarNode child : content) {
            child.buildNFA(nfa, s0, s1);
        }
    }
}
