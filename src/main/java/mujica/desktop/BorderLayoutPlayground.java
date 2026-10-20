package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/10/19")
public class BorderLayoutPlayground extends LayoutPlayground {

    static final String[] CONSTRAINTS = new String[] {
            BorderLayout.NORTH,
            BorderLayout.SOUTH,
            BorderLayout.EAST,
            BorderLayout.WEST,
            BorderLayout.CENTER,
            BorderLayout.BEFORE_FIRST_LINE,
            BorderLayout.AFTER_LAST_LINE,
            BorderLayout.BEFORE_LINE_BEGINS,
            BorderLayout.AFTER_LINE_ENDS
    };

    @NotNull
    JPanel buildConstraintPanel() {
        final JPanel panel = new JPanel(null);
        panel.setName("constraintPanel");
        panel.setBorder(BorderFactory.createTitledBorder("constraint"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final ButtonGroup buttonGroup = new ButtonGroup();
        for (String string : CONSTRAINTS) {
            JRadioButton radioButton = new JRadioButton(string);
            radioButton.setName("constraintRadioButton");
            buttonGroup.add(radioButton);
            radioButton.setSelected(BorderLayout.CENTER.equals(string));
            radioButton.addActionListener(actionEvent -> setConstraint(string));
            panel.add(radioButton);
        }
        return panel;
    }

    @Override
    public void load() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        {
            JPanel target = new JPanel(new BorderLayout());
            target.setName("target");
            Supplier<Border> borderSupplier = this.borderSupplier;
            if (borderSupplier != null) {
                target.setBorder(borderSupplier.get());
            }
            add(target, BorderLayout.CENTER);
            this.target = target;
        }
        {
            JPanel interactive = new JPanel(new GridLayout(1, 3, 10, 10));
            interactive.setName("interactive");
            interactive.add(buildMiscPanel());
            interactive.add(buildBorderSupplierPanel());
            interactive.add(buildAddComponentPanel());
            interactive.add(buildConstraintPanel());
            add(interactive, BorderLayout.SOUTH);
        }
        super.load();
    }
}
