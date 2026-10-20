package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/10/12")
public abstract class ConstantGapLayout extends LineLayout {

    final int gap;

    protected ConstantGapLayout(int gap, boolean transpose) {
        super(transpose);
        if (gap < 0) {
            throw new IllegalArgumentException();
        }
        this.gap = gap;
    }
}
