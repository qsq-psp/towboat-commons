package mujica.text.regex;

import mujica.ds.of_int.set.IntervalIntSet;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/3/27", project = "infrastructure", name = "Repeat")
@CodeHistory(date = "2025/12/18")
public class RepeatNode extends SlotNode {

    protected IntervalIntSet count;

    public RepeatNode(@NotNull GrammarNode body) {
        super(body);
    }

    @NotNull
    @Override
    public RepeatNode duplicate() {
        return new RepeatNode(body);
    }

    @NotNull
    @Override
    public RepeatNode reverse() {
        final GrammarNode reverseBody = body.reverse();
        if (body != reverseBody) {
            return new RepeatNode(reverseBody);
        } else {
            return this;
        }
    }
}
