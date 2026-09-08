package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;

@CodeHistory(date = "2026/8/17")
public class DisplayedNodeContext<N> {

    @NotNull
    final IdentityHashMap<N, Rectangle2D.Float> boxMap = new IdentityHashMap<>();

    @NotNull
    final N[] roots;

    public DisplayedNodeContext(@NotNull N[] roots) {
        super();
        this.roots = roots;
    }
}
