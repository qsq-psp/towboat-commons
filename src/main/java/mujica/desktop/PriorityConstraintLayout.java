package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.IdentityHashMap;

@CodeHistory(date = "2026/10/12")
public class PriorityConstraintLayout extends ConstantGapLayout {

    final IdentityHashMap<Component, Integer> map = new IdentityHashMap<>();

    final boolean reverseLine;

    public PriorityConstraintLayout(int gap, boolean transpose, boolean reverseLine) {
        super(gap, transpose);
        this.reverseLine = reverseLine;
    }

    @Override
    public Dimension minimumLayoutSize(@NotNull Container parent) {
        return null;
    }

    @Override
    public Dimension preferredLayoutSize(@NotNull Container parent) {
        return null;
    }

    @Override
    public Dimension maximumLayoutSize(@NotNull Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(@NotNull Container parent) {

    }
}
