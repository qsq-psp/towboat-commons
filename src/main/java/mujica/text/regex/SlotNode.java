package mujica.text.regex;

import mujica.ds.generic.Slot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/12/17.
 */
@CodeHistory(date = "2021/9/24", project = "nettyon", name = "WrapUnit")
@CodeHistory(date = "2022/3/27", project = "infrastructure", name = "Wrap")
@CodeHistory(date = "2025/12/17")
public class SlotNode extends GrammarNode implements Slot<GrammarNode> {

    protected GrammarNode body;

    @Override
    public GrammarNode get() {
        return body;
    }

    @Override
    public GrammarNode set(@NotNull GrammarNode newBody) {
        final GrammarNode oldBody = body;
        body = newBody;
        return oldBody;
    }
}
