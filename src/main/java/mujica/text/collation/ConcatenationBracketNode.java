package mujica.text.collation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2026/2/21.
 */
public class ConcatenationBracketNode extends BracketNode {

    @NotNull
    final List<BracketNode> list;

    public ConcatenationBracketNode(@NotNull List<BracketNode> list) {
        super();
        this.list = list;
    }

    public ConcatenationBracketNode() {
        this(new ArrayList<>());
    }
}
