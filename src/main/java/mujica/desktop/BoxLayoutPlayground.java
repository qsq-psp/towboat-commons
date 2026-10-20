package mujica.desktop;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/10/19")
public class BoxLayoutPlayground extends LayoutPlayground {

    int axis = BoxLayout.X_AXIS;

    void setAxis(int axis) {
        this.axis = axis;
        final Container target = this.target;
        if (target == null) {
            return;
        }
        target.setLayout(new BoxLayout(target, axis));
        target.revalidate();
        target.repaint();
    }

    @NotNull
    JPanel buildAxisPanel() {
        final JPanel panel = new JPanel(null);
        panel.setName("constraintPanel");
        panel.setBorder(BorderFactory.createTitledBorder("constraint"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final ButtonGroup buttonGroup = new ButtonGroup();
        {
            JRadioButton radioButton = new JRadioButton("x");
            radioButton.setName("yAxisXRadioButton");
            buttonGroup.add(radioButton);
            radioButton.setSelected(axis == BoxLayout.X_AXIS);
            radioButton.addActionListener(actionEvent -> setAxis(BoxLayout.X_AXIS));
            panel.add(radioButton);
        }
        {
            JRadioButton radioButton = new JRadioButton("y");
            radioButton.setName("xAxisRadioButton");
            buttonGroup.add(radioButton);
            radioButton.setSelected(axis == BoxLayout.Y_AXIS);
            radioButton.addActionListener(actionEvent -> setAxis(BoxLayout.Y_AXIS));
            panel.add(radioButton);
        }
        {
            JRadioButton radioButton = new JRadioButton("line");
            radioButton.setName("lineAxisRadioButton");
            buttonGroup.add(radioButton);
            radioButton.setSelected(axis == BoxLayout.LINE_AXIS);
            radioButton.addActionListener(actionEvent -> setAxis(BoxLayout.LINE_AXIS));
            panel.add(radioButton);
        }
        {
            JRadioButton radioButton = new JRadioButton("page");
            radioButton.setName("pageAxisRadioButton");
            buttonGroup.add(radioButton);
            radioButton.setSelected(axis == BoxLayout.PAGE_AXIS);
            radioButton.addActionListener(actionEvent -> setAxis(BoxLayout.PAGE_AXIS));
            panel.add(radioButton);
        }
        return panel;
    }

    @Override
    public void load() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        {
            JPanel target = new JPanel(null);
            target.setLayout(new BoxLayout(target, axis));
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
            interactive.add(buildAxisPanel());
            interactive.add(buildAddComponentPanel());
            add(interactive, BorderLayout.SOUTH);
        }
        super.load();
    }
}
