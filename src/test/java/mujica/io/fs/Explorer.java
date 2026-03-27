package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

import static mujica.reflect.source.SwingManualTest.GAP;

@CodeHistory(date = "2026/3/14")
public class Explorer {

    static final List<FileSystemProvider> PROVIDERS = FileSystemProvider.installedProviders();

    static final AbstractListModel<FileSystemProvider> MODEL = new AbstractListModel<>() {

        @Override
        public int getSize() {
            return PROVIDERS.size();
        }

        @Override
        public FileSystemProvider getElementAt(int index) {
            return PROVIDERS.get(index);
        }
    };

    @NotNull
    static JPanel startPage() {
        final JPanel page = new JPanel(new BorderLayout(GAP, GAP));
        page.setName("startPage");
        page.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        page.add(new JLabel("FileSystemProvider.installedProviders()"), BorderLayout.NORTH);
        final JList<FileSystemProvider> list = new JList<>(MODEL);
        page.add(new JScrollPane(list), BorderLayout.CENTER);
        {
            JPanel bottom = new JPanel(new BorderLayout(GAP, GAP));
            bottom.setName("bottom");
            bottom.add(new JLabel("URI:"), BorderLayout.WEST);
            JTextField textField = new JTextField();
            textField.setName("uriTextField");
            bottom.add(textField, BorderLayout.CENTER);
            {
                JButton button = new JButton("Open");
                button.setName("submitButton");
                bottom.add(button, BorderLayout.EAST);
            }
            page.add(bottom, BorderLayout.SOUTH);
        }
        return page;
    }

    static void startFrame() {
        final JFrame frame = new JFrame(Explorer.class.getSimpleName());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(startPage());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Test
    public void start() throws InterruptedException {
        SwingUtilities.invokeLater(Explorer::startFrame);
        synchronized (this) {
            wait();
        }
    }
}
