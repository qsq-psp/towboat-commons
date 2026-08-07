package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2021/9/24", project = "nettyon", name = "WrapUnit")
@CodeHistory(date = "2022/3/27", project = "infrastructure", name = "Wrap")
@CodeHistory(date = "2025/12/17")
@DirectSubclass({RepeatNode.class})
public abstract class SlotNode extends GrammarNode {

    @NotNull
    protected GrammarNode body;

    protected SlotNode(@NotNull GrammarNode body) {
        super();
        this.body = body;
    }

    @NotNull
    public GrammarNode getBody() {
        return body;
    }

    public void setBody(@NotNull GrammarNode newBody) {
        body = newBody;
    }
}
