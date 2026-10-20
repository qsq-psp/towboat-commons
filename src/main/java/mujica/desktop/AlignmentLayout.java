package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntSupplier;

@CodeHistory(date = "2026/10/18")
public class AlignmentLayout extends JustifyLayout {

    @NotNull
    final Alignment alignment;

    public AlignmentLayout(@NotNull Justify justify, @NotNull Alignment alignment, boolean transpose, boolean reverseLine) {
        super(justify, transpose, reverseLine);
        this.alignment = alignment;
    }

    int alignX(@NotNull Dimension childDimension, int parentWidth, @NotNull Component child, @NotNull Container parent) {
        float position = 0.5f;
        switch (alignment) {
            case START:
                return 0;
            case END:
                return parentWidth - childDimension.width;
            case FROM_CHILD_LAYOUT:
                if (child instanceof JComponent) {
                    LayoutManager layout = ((JComponent) child).getLayout();
                    if (layout instanceof LayoutManager2) {
                        position = ((LayoutManager2) layout).getLayoutAlignmentX((Container) child);
                        break;
                    }
                }
                // no break here
            case FROM_CHILD:
                position = child.getAlignmentX();
                break;
            case FROM_PARENT:
                position = parent.getAlignmentX();
                break;
        }
        position = Math.max(0.0f, Math.min(position, 1.0f));
        return Math.round(position * (parentWidth - childDimension.width));
    }

    int alignY(@NotNull Dimension childDimension, int parentHeight, @NotNull Component child, @NotNull Container parent) {
        float position = 0.5f;
        switch (alignment) {
            case START:
                return 0;
            case END:
                return parentHeight - childDimension.height;
            case BASELINE:
                // todo
                break;
            case FROM_CHILD_LAYOUT:
                if (child instanceof JComponent) {
                    LayoutManager layout = ((JComponent) child).getLayout();
                    if (layout instanceof LayoutManager2) {
                        position = ((LayoutManager2) layout).getLayoutAlignmentY((Container) child);
                        break;
                    }
                }
                // no break here
            case FROM_CHILD:
                position = child.getAlignmentY();
                break;
            case FROM_PARENT:
                position = parent.getAlignmentY();
                break;
        }
        position = Math.max(0.0f, Math.min(position, 1.0f));
        return Math.round(position * (parentHeight - childDimension.height));
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
                    for (Component child : new ComponentIterable(parent, reverseLine, false)) {
                        if (child.isVisible()) {
                            Dimension dimension = child.getPreferredSize();
                            if (transpose) {
                                int x = alignX(dimension, containerWidth, child, parent);
                                child.setBounds(x, containerY, dimension.width, dimension.height);
                                containerY += dimension.height;
                            } else {
                                int y = alignY(dimension, containerHeight, child, parent);
                                child.setBounds(containerX, y, dimension.width, dimension.height);
                                containerX += dimension.width;
                            }
                        } else {
                            child.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                case END:
                    for (Component child : new ComponentIterable(parent, !reverseLine, false)) {
                        if (child.isVisible()) {
                            Dimension dimension = child.getPreferredSize();
                            if (transpose) {
                                containerHeight -= dimension.height;
                                int x = alignX(dimension, containerWidth, child, parent);
                                child.setBounds(x, containerY + containerHeight, dimension.width, dimension.height);
                            } else {
                                containerWidth -= dimension.width;
                                int y = alignY(dimension, containerHeight, child, parent);
                                child.setBounds(containerX + containerWidth, y, dimension.width, dimension.height);
                            }
                        } else {
                            child.setBounds(containerX + containerWidth, containerY + containerHeight, 0, 0);
                        }
                    }
                    break;
                case GAP_BETWEEN:
                case GAP_AFTER:
                    supplier = gapSupplier(parent, containerWidth, containerHeight);
                    for (Component child : new ComponentIterable(parent, reverseLine, false)) {
                        if (child.isVisible()) {
                            Dimension dimension = child.getPreferredSize();
                            if (transpose) {
                                int x = alignX(dimension, containerWidth, child, parent);
                                child.setBounds(x, containerY, dimension.width, dimension.height);
                                containerY += dimension.height;
                                containerY += supplier.getAsInt();
                            } else {
                                int y = alignY(dimension, containerHeight, child, parent);
                                child.setBounds(containerX, y, dimension.width, dimension.height);
                                containerX += dimension.width;
                                containerX += supplier.getAsInt();
                            }
                        } else {
                            child.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                case GAP_BEFORE:
                case GAP_AROUND:
                    supplier = gapSupplier(parent, containerWidth, containerHeight);
                    for (Component child : new ComponentIterable(parent, reverseLine, false)) {
                        if (child.isVisible()) {
                            Dimension dimension = child.getPreferredSize();
                            if (transpose) {
                                containerY += supplier.getAsInt();
                                int x = alignX(dimension, containerWidth, child, parent);
                                child.setBounds(x, containerY, dimension.width, dimension.height);
                                containerY += dimension.height;
                            } else {
                                containerX += supplier.getAsInt();
                                int y = alignY(dimension, containerHeight, child, parent);
                                child.setBounds(containerX, y, dimension.width, dimension.height);
                                containerX += dimension.width;
                            }
                        } else {
                            child.setBounds(containerX, containerY, 0, 0);
                        }
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }
}
