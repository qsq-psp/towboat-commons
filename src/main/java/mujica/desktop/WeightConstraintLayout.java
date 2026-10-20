package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.IdentityHashMap;

@CodeHistory(date = "2019/3/23", project = "coo", name = "ListWeightLayout")
@CodeHistory(date = "2026/10/11")
public class WeightConstraintLayout implements LayoutManager2 {

    @CodeHistory(date = "2026/10/11")
    static class WeightInfo {

        final float weight;

        int lineMinimum;

        int pageMinimum;

        int linePreferred;

        int pagePreferred;

        WeightInfo(float weight) {
            super();
            this.weight = weight;
        }

        WeightInfo() {
            this(1.0f);
        }

        void invalidate() {
            lineMinimum = -1;
            linePreferred = -1;
            pageMinimum = -1;
            pagePreferred = -1;
        }
    }

    final IdentityHashMap<Component, WeightInfo> map = new IdentityHashMap<>();

    final boolean transpose;

    final boolean reverseLine;

    public WeightConstraintLayout(boolean transpose, boolean reverseLine) {
        super();
        this.transpose = transpose;
        this.reverseLine = reverseLine;
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
        if (constraints instanceof Number) {
            float weight = ((Number) constraints).floatValue();
            if (weight >= 0.0f) {
                map.put(component, new WeightInfo(weight));
            }
        }
    }

    @Override
    public void addLayoutComponent(String name, @NotNull Component component) {
        if (name != null) {
            try {
                float weight = Float.parseFloat(name);
                if (weight >= 0.0f) {
                    map.put(component, new WeightInfo(weight));
                }
            } catch (NumberFormatException ignore) {}
        }
    }

    @Override
    public void removeLayoutComponent(@NotNull Component component) {
        map.remove(component);
    }

    @Override
    public void invalidateLayout(@NotNull Container parent) {
        for (WeightInfo info : map.values()) {
            info.invalidate();
        }
    }

    @Override
    public Dimension minimumLayoutSize(@NotNull Container parent) {
        synchronized (parent.getTreeLock()) {
            int componentCount = parent.getComponentCount();
            for (int componentIndex = 0; componentIndex < componentCount; componentIndex++) {
                Component child = parent.getComponent(componentIndex);
                WeightInfo info = map.get(child);
                if (info == null) {
                    info = new WeightInfo();
                    map.put(child, info);
                }
                if (info.lineMinimum < 0 || info.pageMinimum < 0) {
                    Dimension minimum = child.getMinimumSize();
                    if (transpose) {
                        info.lineMinimum = minimum.height;
                        info.pageMinimum = minimum.width;
                    } else {
                        info.lineMinimum = minimum.width;
                        info.pageMinimum = minimum.height;
                    }
                }
            }
        }
        int line = 0;
        int page = 0;
        for (WeightInfo info : map.values()) {
            line += info.lineMinimum;
            page = Math.max(page, info.pageMinimum);
        }
        final Insets insets = parent.getInsets();
        if (transpose) {
            return new Dimension(
                    insets.left + page + insets.right,
                    insets.top + line + insets.bottom
            );
        } else {
            return new Dimension(
                    insets.left + line + insets.right,
                    insets.top + page + insets.bottom
            );
        }
    }

    @Override
    public Dimension preferredLayoutSize(@NotNull Container parent) {
        synchronized (parent.getTreeLock()) {
            int componentCount = parent.getComponentCount();
            for (int componentIndex = 0; componentIndex < componentCount; componentIndex++) {
                Component child = parent.getComponent(componentIndex);
                WeightInfo info = map.get(child);
                if (info == null) {
                    info = new WeightInfo();
                    map.put(child, info);
                }
                if (info.linePreferred < 0 || info.pagePreferred < 0) {
                    Dimension preferred = child.getPreferredSize();
                    if (transpose) {
                        info.linePreferred = preferred.height;
                        info.pagePreferred = preferred.width;
                    } else {
                        info.linePreferred = preferred.width;
                        info.pagePreferred = preferred.height;
                    }
                }
            }
        }
        int line = 0;
        int page = 0;
        for (WeightInfo info : map.values()) {
            line += info.linePreferred;
            page = Math.max(page, info.pagePreferred);
        }
        final Insets insets = parent.getInsets();
        if (transpose) {
            return new Dimension(
                    insets.left + page + insets.right,
                    insets.top + line + insets.bottom
            );
        } else {
            return new Dimension(
                    insets.left + line + insets.right,
                    insets.top + page + insets.bottom
            );
        }
    }

    @Override
    public Dimension maximumLayoutSize(@NotNull Container parent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public void layoutContainer(@NotNull Container parent) {
        int lineStart;
        int pageStart;
        int lineTarget;
        int pageTarget;
        {
            int width = parent.getWidth();
            int height = parent.getHeight();
            Insets insets = parent.getInsets();
            if (transpose) {
                if (reverseLine) {
                    lineStart = height - insets.bottom;
                    pageStart = width - insets.right;
                } else {
                    lineStart = insets.top;
                    pageStart = insets.left;
                }
                lineTarget = height - insets.top - insets.bottom;
                pageTarget = width - insets.left - insets.right;
            } else {
                if (reverseLine) {
                    lineStart = width - insets.right;
                    pageStart = height - insets.bottom;
                } else {
                    lineStart = insets.left;
                    pageStart = insets.top;
                }
                lineTarget = width - insets.left - insets.right;
                pageTarget = height - insets.top - insets.bottom;
            }
        }
        if (lineTarget <= 0 || pageTarget <= 0) {
            degenerate(parent);
            return;
        }
        synchronized (parent.getTreeLock()) {
            float weightAhead = 0.0f;
            int lineAhead = 0;
            int componentCount = parent.getComponentCount();
            for (int componentIndex = 0; componentIndex < componentCount; componentIndex++) {
                Component child = parent.getComponent(componentIndex);
                WeightInfo info = map.get(child);
                if (info == null) {
                    info = new WeightInfo();
                    map.put(child, info);
                }
                if (info.linePreferred < 0 || info.pagePreferred < 0) {
                    Dimension preferred = child.getPreferredSize();
                    if (transpose) {
                        info.linePreferred = preferred.height;
                        info.pagePreferred = preferred.width;
                    } else {
                        info.linePreferred = preferred.width;
                        info.pagePreferred = preferred.height;
                    }
                }
                weightAhead += info.weight;
                lineAhead += info.linePreferred;
            }

        }
    }

    static void degenerate(@NotNull Container parent) {
        synchronized (parent.getTreeLock()) {
            int componentCount = parent.getComponentCount();
            for (int componentIndex = 0; componentIndex < componentCount; componentIndex++) {
                parent.getComponent(componentIndex).setBounds(0, 0, 0, 0);
            }
        }
    }
}
