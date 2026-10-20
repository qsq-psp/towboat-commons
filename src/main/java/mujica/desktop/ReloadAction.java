package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@CodeHistory(date = "2026/10/9")
public class ReloadAction extends AbstractAction {

    public ReloadAction() {
        super("reload");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, KeyEvent.CTRL_DOWN_MASK)); // Ctrl + F5
    }

    @Override
    public void actionPerformed(@NotNull ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (!(source instanceof Component)) {
            return;
        }
        Component component = (Component) source;
        while (component != null) {
            component = component.getParent();
            if (component instanceof Window) {
                reloadWindow((Window) component);
                break;
            }
        }
    }

    public static void reloadWindow(@NotNull Window window) {
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            JMenuBar menuBar = frame.getJMenuBar();
            if (menuBar instanceof LazyMenuBar) {
                ((LazyMenuBar) menuBar).reload();
            }
            Container contentPane = frame.getContentPane();
            if (contentPane instanceof LazyContentPane) {
                ((LazyContentPane) contentPane).reload();
            }
        } else if (window instanceof JDialog) {
            JDialog dialog = (JDialog) window;
            JMenuBar menuBar = dialog.getJMenuBar();
            if (menuBar instanceof LazyMenuBar) {
                ((LazyMenuBar) menuBar).reload();
            }
            Container contentPane = dialog.getContentPane();
            if (contentPane instanceof LazyContentPane) {
                ((LazyContentPane) contentPane).reload();
            }
        }
    }

    public static void reloadAll() {
        for (Window window : Window.getWindows()) {
            reloadWindow(window);
        }
    }
}
