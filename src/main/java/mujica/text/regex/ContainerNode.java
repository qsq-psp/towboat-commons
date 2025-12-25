package mujica.text.regex;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@CodeHistory(date = "2021/10/18", project = "va", name = "ContainerElement")
@CodeHistory(date = "2022/3/27", project = "infrastructure", name = "Container")
@CodeHistory(date = "2025/12/16")
public abstract class ContainerNode extends GrammarNode {

    @NotNull
    protected final ArrayList<GrammarNode> content;

    protected ContainerNode() {
        super();
        content = new ArrayList<>();
    }
}
