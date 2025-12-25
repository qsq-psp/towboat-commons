package mujica.test;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created on 2025/11/22.
 */
@CodeHistory(date = "2025/11/22")
public class SwingManualTest extends ManualTest {

    protected static final JFrame FRAME = new JFrame(SwingManualTest.class.getSimpleName());

    private static final JProgressBar PROGRESS = new JProgressBar();

    private static final JPanel CHOICES = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));

    protected static void installComponents() {
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FRAME.setMinimumSize(new Dimension(640, 480));
        final JPanel rootPanel = new JPanel(new BorderLayout(8, 8));
        {
            PROGRESS.setModel(new DefaultBoundedRangeModel());
            PROGRESS.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            rootPanel.add(PROGRESS, BorderLayout.NORTH);
            CHOICES.removeAll();
            rootPanel.add(CHOICES, BorderLayout.SOUTH);
        }
        FRAME.setContentPane(rootPanel);
        FRAME.pack();
        FRAME.setLocationRelativeTo(null);
        FRAME.setVisible(true);
    }

    protected static void uninstallComponents() {
        FRAME.setVisible(false);
    }

    private static JComponent CONTENT;

    protected static void installContent(@NotNull Supplier<JComponent> supplier) {
        SwingUtilities.invokeLater(() -> {
            CONTENT = supplier.get();
            FRAME.getContentPane().add(CONTENT, BorderLayout.CENTER);
        });
    }

    @After
    public void uninstallContent() {
        // System.out.println("SwingManualTest.uninstallContent");
        SwingUtilities.invokeLater(() -> {
            if (CONTENT != null) {
                FRAME.getContentPane().remove(CONTENT);
                CONTENT = null;
            }
            CHOICES.removeAll();
            FRAME.revalidate();
            FRAME.repaint();
        });
    }

    protected static void passOrFail() throws InterruptedException {
        final AtomicInteger result = new AtomicInteger();
        SwingUtilities.invokeLater(() -> {
            JButton passButton = new JButton("Pass");
            passButton.addActionListener(ae -> {
                result.compareAndSet(0, 1);
                synchronized (result) {
                    result.notify();
                }
            });
            JButton failButton = new JButton("Fail");
            failButton.addActionListener(ae -> {
                result.compareAndSet(0, -1);
                synchronized (result) {
                    result.notify();
                }
            });
            CHOICES.removeAll();
            CHOICES.add(passButton);
            CHOICES.add(failButton);
            CHOICES.revalidate();
            CHOICES.repaint();
        });
        synchronized (result) {
            result.wait();
        }
        if (result.get() <= 0) {
            Assert.fail();
        }
    }
}
