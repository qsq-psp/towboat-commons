package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/10/12")
public abstract class CalculatedGapLayout extends LineLayout {

    public CalculatedGapLayout(boolean transpose) {
        super(transpose);
    }
}
