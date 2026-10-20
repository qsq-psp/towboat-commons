package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/10/7")
public class SparseWeightTemplateLayout extends WeightTemplateLayout {

    public SparseWeightTemplateLayout(@NotNull WeightTemplate template, int gap, boolean transpose, boolean reversePage) {
        super(template, gap, transpose, reversePage);
    }

    public SparseWeightTemplateLayout(int gap, boolean transpose, boolean reversePage, boolean reverseLine) {
        this(new WeightTemplate(1, reverseLine), gap, transpose, reversePage);
    }

    @Override
    public void addLayoutComponent(@NotNull Component component, Object constraints) {
        WeightTemplate currentTemplate;
        if (constraints instanceof WeightTemplate) {
            currentTemplate = (WeightTemplate) constraints;
        } else {
            currentTemplate = template;
        }
        //...
    }
}
