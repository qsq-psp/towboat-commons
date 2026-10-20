package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Supplier;

@CodeHistory(date = "2026/9/23", project = "coo", name = "LazyPanelUI")
@CodeHistory(date = "2026/10/9")
public class LazyContentPane extends JComponent implements Accessible {

    @CodeHistory(date = "2026/10/9")
    private class LoadListener extends ComponentAdapter {

        boolean loaded = false;

        @Override
        public void componentShown(@NotNull ComponentEvent componentEvent) {
            // System.out.println(LazyContentPane.this.getClass() + " LoadListener.componentShown");
            if (!loaded) {
                loaded = true;
                load();
            }
            SwingUtilities.invokeLater(() -> removeComponentListener(this));
        }
    }

    public LazyContentPane() {
        super();
        addComponentListener(new LoadListener());
    }

    public void load() {
        revalidate();
        repaint();
    }

    public void unload() {
        removeAll();
        revalidate();
        repaint();
    }

    public void reload() {
        removeAll();
        load();
    }

    public void reloadIfLoaded() {
        if (getComponentCount() > 0) {
            reload();
        }
    }

    public static void loadIntoFrame(@NotNull Supplier<? extends LazyContentPane> supplier) {
        final JFrame frame = new JFrame(LocaleContentTreesPane.class.getSimpleName());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final LazyMenuBar menuBar = new EveryThingMenuBar();
        final LazyContentPane contentPane = supplier.get();
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(contentPane);
        frame.setSize(new Dimension(1000, 800));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        menuBar.loadLater();
        contentPane.loadLater();
    }

    public void loadLater() {
        SwingUtilities.invokeLater(this::load);
    }

    public void unloadLater() {
        SwingUtilities.invokeLater(this::unload);
    }

    public void reloadLater() {
        SwingUtilities.invokeLater(this::reload);
    }

    public void reloadIfLoadedLater() {
        SwingUtilities.invokeLater(this::reloadIfLoadedLater);
    }

    public static void loadIntoFrameLater(@NotNull Supplier<? extends LazyContentPane> supplier) {
        SwingUtilities.invokeLater(() -> LazyContentPane.loadIntoFrame(supplier));
    }

    @CodeHistory(date = "2026/10/17")
    public static class OpenAction extends AbstractAction {

        @NotNull
        final String className;

        LazyContentPane contentPane;

        public OpenAction(@NotNull String displayName, @NotNull String className) {
            super(displayName);
            this.className = className;
        }

        public OpenAction(@NotNull String className) {
            this(simpleClassName(className), className);
        }

        @NotNull
        private static String simpleClassName(@NotNull String className) {
            final int dotIndex = className.lastIndexOf('.');
            if (dotIndex == -1) {
                return className;
            }
            return className.substring(dotIndex + 1);
        }

        @NotNull
        public LazyContentPane getContentPane() {
            LazyContentPane contentPane = this.contentPane;
            if (contentPane == null) {
                try {
                    contentPane = (LazyContentPane) Class.forName(className).getConstructor().newInstance();
                } catch (Exception ignore) {}
                if (contentPane != null) {
                    this.contentPane = contentPane;
                } else {
                    contentPane = new LazyContentPane();
                }
            }
            contentPane.reload();
            return contentPane;
        }

        @Override
        public void actionPerformed(@NotNull ActionEvent actionEvent) {
            final Object source = actionEvent.getSource();
            if (!(source instanceof Component)) {
                return;
            }
            Component component = (Component) source;
            while (true) {
                Container container = component.getParent();
                if (container == null) {
                    break;
                }
                component = container;
            }
            if (component instanceof RootPaneContainer) {
                ((RootPaneContainer) component).setContentPane(getContentPane());
            }
        }
    }

    @Override
    @NotNull
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleLazyPane();
        }
        return accessibleContext;
    }

    @CodeHistory(date = "2026/10/10")
    protected class AccessibleLazyPane extends AccessibleAWTContainer {

        private static final long serialVersionUID = 0xB9CFF5CC0E473795L;

        @Override
        @NotNull
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }
    }
}
