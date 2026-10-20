package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;

import javax.swing.*;
import java.awt.*;

@CodeHistory(date = "2026/10/10")
public class UrxParts extends LazyContentPane {

    transient JTextField urxInput;

    @Override
    public void load() {
        setName(getClass().getSimpleName());
        final int gap = 6;
        setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
        setLayout(new BorderLayout(gap, gap));
        {
            JPanel urxInputPanel = new JPanel(new BorderLayout(gap, gap));
            urxInputPanel.setName("urxInputPanel");
            urxInputPanel.add(new JLabel("URL/URI: "), BorderLayout.WEST);
            {
                urxInput = new JTextField();
                urxInput.setName("urxInput");
                urxInputPanel.add(urxInput, BorderLayout.CENTER);
            }
            {
                JButton pasteButton = new JButton("Paste");
                pasteButton.setName("pasteButton");
                urxInputPanel.add(pasteButton, BorderLayout.EAST);
            }
            add(urxInputPanel, BorderLayout.NORTH);
        }
        super.load();
    }
}
