package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@CodeHistory(date = "2026/10/9")
public class LazyMenuBar extends JMenuBar {

    @CodeHistory(date = "2026/10/9")
    private class LoadListener extends ComponentAdapter {

        boolean loaded = false;

        @Override
        public void componentShown(ComponentEvent componentEvent) {
            if (!loaded) {
                loaded = true;
                load();
            }
            SwingUtilities.invokeLater(() -> removeComponentListener(this));
        }
    }

    public LazyMenuBar() {
        super();
        addComponentListener(new LoadListener());
    }

    public void load() {
        revalidate();
        repaint();
    }

    public void reload() {
        removeAll();
        load();
    }

    public void loadLater() {
        SwingUtilities.invokeLater(this::load);
    }

    public void reloadLater() {
        SwingUtilities.invokeLater(this::reload);
    }
}
