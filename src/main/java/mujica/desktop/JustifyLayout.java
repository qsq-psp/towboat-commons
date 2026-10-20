package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;
import java.util.function.IntSupplier;

@CodeHistory(date = "2026/10/14")
public class JustifyLayout extends CalculatedGapLayout {

    @NotNull
    final Justify justify;

    final boolean reverseLine;

    public JustifyLayout(@NotNull Justify justify, boolean transpose, boolean reverseLine) {
        super(transpose);
        this.justify = justify;
        this.reverseLine = reverseLine;
    }

    @NotNull
    Dimension layoutSize(@NotNull Container parent, @NotNull Function<Component, Dimension> sizeFunction) {
        long width = 0L;
        long height = 0L;
        synchronized (parent.getTreeLock()) {
            for (Component component : new ComponentIterable(parent, false, true)) {
                Dimension dimension = sizeFunction.apply(component);
                if (transpose) {
                    width = Math.max(width, dimension.width);
                    height += dimension.height;
                } else {
                    width += dimension.width;
                    height = Math.max(height, dimension.height);
                }
            }
        }
        return new Dimension(
                (int) Math.min(width, Integer.MAX_VALUE),
                (int) Math.min(height, Integer.MAX_VALUE)
        );
    }

    @Override
    @NotNull
    public Dimension minimumLayoutSize(@NotNull Container parent) {
        return layoutSize(parent, Component::getMinimumSize);
    }

    @Override
    @NotNull
    public Dimension preferredLayoutSize(@NotNull Container parent) {
        return layoutSize(parent, Component::getPreferredSize);
    }

    @Override
    @NotNull
    public Dimension maximumLayoutSize(@NotNull Container parent) {
        return layoutSize(parent, Component::getMaximumSize);
    }

    @NotNull
    IntSupplier gapSupplier(@NotNull Container parent, int containerWidth, int containerHeight) {
        int visibleCount = 0;
        for (Component component : new ComponentIterable(parent, false, true)) {
            visibleCount++;
            Dimension dimension = component.getPreferredSize();
            containerWidth -= dimension.width;
            containerHeight -= dimension.height;
        }
        switch (justify) {
            case CENTER:
                visibleCount = 2; // only use once
                break;
            case START:
            case END:
                visibleCount = 1; // actually not used
                break;
            case GAP_BETWEEN:
                visibleCount--;
                break;
            case GAP_AROUND:
                visibleCount++;
                break;
        }
        if (visibleCount > 0) {
            if (transpose) {
                return new FloorDivideDistributedSizeSupplier(containerHeight, visibleCount);
            } else {
                return new FloorDivideDistributedSizeSupplier(containerWidth, visibleCount);
            }
        } else {
            return () -> 0;
        }
    }

    @Override
    public void layoutContainer(@NotNull Container parent) {
        final Insets insets = parent.getInsets();
        int containerX = insets.left;
        int containerY = insets.top;
        int containerWidth = parent.getWidth() - insets.left - insets.right;
        int containerHeight = parent.getHeight() - insets.top - insets.bottom;
        synchronized (parent.getTreeLock()) {
            IntSupplier supplier;
            switch (justify) {
                case CENTER:
                    supplier = gapSupplier(parent, containerWidth, containerHeight);
                    if (transpose) {
                        containerY += supplier.getAsInt();
                    } else {
                        containerX += supplier.getAsInt();
                    }
                    // no break here
                case START:
                    for (Component component : new ComponentIterable(parent, reverseLine, false)) {
                        if (component.isVisible()) {
                            Dimension dimension = component.getPreferredSize();
                            if (transpose) {
                                component.setBounds(containerX, containerY, containerWidth, dimension.height);
                                containerY += dimension.height;
                            } else {
                                component.setBounds(containerX, containerY, dimension.width, containerHeight);
                                containerX += dimension.width;
                            }
                        } else {
                            component.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                case END:
                    for (Component component : new ComponentIterable(parent, !reverseLine, false)) {
                        if (component.isVisible()) {
                            Dimension dimension = component.getPreferredSize();
                            if (transpose) {
                                containerHeight -= dimension.height;
                                component.setBounds(containerX, containerY + containerHeight, containerWidth, dimension.height);
                            } else {
                                containerWidth -= dimension.width;
                                component.setBounds(containerX + containerWidth, containerY, dimension.width, containerHeight);
                            }
                        } else {
                            component.setBounds(containerX + containerWidth, containerY + containerHeight, 0, 0);
                        }
                    }
                    break;
                case GAP_BETWEEN:
                case GAP_AFTER:
                    supplier = gapSupplier(parent, containerWidth, containerHeight);
                    for (Component component : new ComponentIterable(parent, reverseLine, false)) {
                        if (component.isVisible()) {
                            Dimension dimension = component.getPreferredSize();
                            if (transpose) {
                                component.setBounds(containerX, containerY, containerWidth, dimension.height);
                                containerY += dimension.height;
                                containerY += supplier.getAsInt();
                            } else {
                                component.setBounds(containerX, containerY, dimension.width, containerHeight);
                                containerX += dimension.width;
                                containerX += supplier.getAsInt();
                            }
                        } else {
                            component.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                case GAP_BEFORE:
                case GAP_AROUND:
                    supplier = gapSupplier(parent, containerWidth, containerHeight);
                    for (Component component : new ComponentIterable(parent, reverseLine, false)) {
                        if (component.isVisible()) {
                            Dimension dimension = component.getPreferredSize();
                            if (transpose) {
                                containerY += supplier.getAsInt();
                                component.setBounds(containerX, containerY, containerWidth, dimension.height);
                                containerY += dimension.height;
                            } else {
                                containerX += supplier.getAsInt();
                                component.setBounds(containerX, containerY, dimension.width, containerHeight);
                                containerX += dimension.width;
                            }
                        } else {
                            component.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }
}
