package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.*;
import java.awt.event.ActionEvent;

@CodeHistory(date = "2026/10/9")
public class SetLookAndFeelAction extends AbstractAction {

    @NotNull
    final String className;

    @Nullable
    final String metalTheme;

    public SetLookAndFeelAction(@NotNull String displayName, @NotNull String className, @Nullable String metalTheme) {
        super(displayName);
        this.className = className;
        this.metalTheme = metalTheme;
    }

    public SetLookAndFeelAction(@NotNull String className) {
        this(simpleClassName(className), className, null);
    }

    @NotNull
    private static String simpleClassName(@NotNull String className) {
        final int dotIndex = className.lastIndexOf('.');
        if (dotIndex == -1) {
            return className;
        }
        return className.substring(dotIndex + 1);
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent actionEvent) {
        if (metalTheme != null) {
            try {
                Class<?> metalThemeClass = Class.forName(metalTheme);
                if (MetalTheme.class.isAssignableFrom(metalThemeClass)) {
                    MetalLookAndFeel.setCurrentTheme((MetalTheme) metalThemeClass.getConstructor().newInstance());
                }
            } catch (Exception ignore) {}
        }
        try {
            UIManager.setLookAndFeel(className);
            updateUI();
        } catch (Exception ignore) {}
    }

    public static void updateUI() {
        for (Window window : Window.getWindows()) {
            updateUI(window);
        }
    }

    public static void updateUI(@NotNull Container container) {
        final int componentCount = container.getComponentCount();
        for (int componentIndex = 0; componentIndex < componentCount; componentIndex++) {
            Component component = container.getComponent(componentIndex);
            if (component instanceof Container) {
                updateUI((Container) component);
            }
        }
        if (container instanceof JComponent) {
            ((JComponent) container).updateUI();
        }
    }
}
