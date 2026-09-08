package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/6/1", name = "BiasedNode.Frame")
@CodeHistory(date = "2025/6/5", name = "SisterNode.Frame")
@CodeHistory(date = "2026/8/9")
public class TraversalFrame<N> {

    @Nullable
    final TraversalFrame<N> parent;

    @NotNull
    final N node;

    int stage;

    public TraversalFrame(@Nullable TraversalFrame<N> parent, @NotNull N node) {
        super();
        this.node = node;
        this.parent = parent;
    }
}
