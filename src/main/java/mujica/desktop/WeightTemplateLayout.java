package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/10/3")
public class WeightTemplateLayout extends ConstantGapLayout {

    @NotNull
    final WeightTemplate template;

    @NotNull
    final WeightTemplate.Active activeTemplate;

    final boolean reversePage;

    public WeightTemplateLayout(@NotNull WeightTemplate template, int gap, boolean transpose, boolean reversePage) {
        super(gap, transpose);
        this.template = template;
        this.activeTemplate = new WeightTemplate.Active(template);
        this.reversePage = reversePage;
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