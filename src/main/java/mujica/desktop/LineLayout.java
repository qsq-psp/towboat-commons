package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/10/14")
public abstract class LineLayout implements LayoutManager2 {

    final boolean transpose;

    public LineLayout(boolean transpose) {
        super();
        this.transpose = transpose;
    }

    @Override
    public float getLayoutAlignmentX(@NotNull Container parent) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(@NotNull Container parent) {
        return 0.5f;
    }

    @Override
    public void addLayoutComponent(@NotNull Component component, Object constraints) {
        // pass
    }

    @Override
    public void addLayoutComponent(String name, @NotNull Component component) {
        // pass
    }

    @Override
    public void removeLayoutComponent(@NotNull Component component) {
        // pass
    }

    @Override
    public void invalidateLayout(@NotNull Container parent) {
        // pass
    }
}
