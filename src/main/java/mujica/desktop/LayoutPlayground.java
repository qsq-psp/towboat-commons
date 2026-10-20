package mujica.desktop;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.function.Supplier;

@CodeHistory(date = "2026/10/18")
public abstract class LayoutPlayground extends LazyContentPane {

    static final String[] WORDS = {
            "aberration", "adenine", "archer",
            "babel", "benzene", "boulevard",
            "cabbage", "centenarian", "conduit",
            "determinant", "disengage", "dopamine",
            "eclipse", "ecosystem", "eel",
            "fiddle", "flint", "forensic",
            "gallop", "gamut", "guava",
            "havoc", "headless", "hydrogenation",
            "idiomatic", "immuneglobulin", "incandescence",
            "joy", "jumbo", "jury",
            "kerosene", "kettle", "knight",
            "lagoon", "lateral", "lexeme",
            "madder", "manuscript", "Mediterranean",
            "neolithic", "Netherlander", "neurotransmitter",
            "obsidian", "of", "ounce"
    };

    @NotNull
    final RandomContext rc = new RandomContext();

    Container target;

    Supplier<Border> borderSupplier;

    Object constraint;

    float alignmentX = Float.NaN;

    float alignmentY = Float.NaN;

    LayoutPlayground() {
        super();
    }

    void addToTarget(@NotNull Supplier<Component> supplier) {
        final Container target = this.target;
        if (target != null) {
            Component component = supplier.get();
            if (component instanceof JComponent) {
                if (0.0f <= alignmentX && alignmentX <= 1.0f) {
                    ((JComponent) component).setAlignmentX(alignmentX);
                }
                if (0.0f <= alignmentY && alignmentY <= 1.0f) {
                    ((JComponent) component).setAlignmentY(alignmentY);
                }
            }
            target.add(component, constraint);
            target.revalidate();
            target.repaint();
        }
    }

    boolean isRTL() {
        final Container target = this.target;
        return target != null && !target.getComponentOrientation().isLeftToRight();
    }

    void setRTL(boolean rtl) {
        final Container target = this.target;
        if (target != null) {
            target.setComponentOrientation(rtl ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
            target.revalidate();
            target.repaint();
        }
    }

    void setBorderSupplier(Supplier<Border> borderSupplier) {
        this.borderSupplier = borderSupplier;
        final Container target = this.target;
        if (target instanceof JComponent && borderSupplier != null) {
            ((JComponent) target).setBorder(borderSupplier.get()); // revalidate and repaint included
        }
    }

    void setConstraint(Object constraint) {
        this.constraint = constraint;
    }

    @NotNull
    JPanel buildMiscPanel() {
        final JPanel panel = new JPanel();
        panel.setName("miscPanel");
        panel.setBorder(BorderFactory.createTitledBorder("misc"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        {
            JCheckBox rtlCheckBox = new JCheckBox("right to left");
            rtlCheckBox.setName("rtlCheckBox");
            rtlCheckBox.setSelected(isRTL());
            rtlCheckBox.addActionListener(actionEvent -> setRTL(rtlCheckBox.isSelected()));
            panel.add(rtlCheckBox);
        }
        return panel;
    }

    @NotNull
    JPanel buildBorderSupplierPanel() {
        final JPanel panel = new JPanel(null);
        panel.setName("borderSupplierPanel");
        panel.setBorder(BorderFactory.createTitledBorder("border"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        final ButtonGroup buttonGroup = new ButtonGroup();
        {
            JRadioButton nullBorderButton = new JRadioButton("null");
            nullBorderButton.setName("nullBorderButton");
            buttonGroup.add(nullBorderButton);
            nullBorderButton.setSelected(true);
            nullBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> null));
            panel.add(nullBorderButton);
        }
        {
            JRadioButton empty10BorderButton = new JRadioButton("empty(10)");
            empty10BorderButton.setName("empty10BorderButton");
            buttonGroup.add(empty10BorderButton);
            empty10BorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            panel.add(empty10BorderButton);
        }
        {
            JRadioButton empty20BorderButton = new JRadioButton("empty(20)");
            empty20BorderButton.setName("empty20BorderButton");
            buttonGroup.add(empty20BorderButton);
            empty20BorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createEmptyBorder(20, 20, 20, 20)));
            panel.add(empty20BorderButton);
        }
        {
            JRadioButton emptyRandomBorderButton = new JRadioButton("empty(random)");
            emptyRandomBorderButton.setName("emptyRandomBorderButton");
            buttonGroup.add(emptyRandomBorderButton);
            emptyRandomBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createEmptyBorder(
                    rc.nextInt(30), rc.nextInt(30), rc.nextInt(30), rc.nextInt(30)
            )));
            panel.add(emptyRandomBorderButton);
        }
        {
            JRadioButton etchedRaisedBorderButton = new JRadioButton("etched(raised)");
            etchedRaisedBorderButton.setName("etchedRaisedBorderButton");
            buttonGroup.add(etchedRaisedBorderButton);
            etchedRaisedBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
            panel.add(etchedRaisedBorderButton);
        }
        {
            JRadioButton etchedLoweredBorderButton = new JRadioButton("etched(lowered)");
            etchedLoweredBorderButton.setName("etchedLoweredBorderButton");
            buttonGroup.add(etchedLoweredBorderButton);
            etchedLoweredBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
            panel.add(etchedLoweredBorderButton);
        }
        {
            JRadioButton lineBorderButton = new JRadioButton("line(blue)");
            lineBorderButton.setName("lineBorderButton");
            buttonGroup.add(lineBorderButton);
            lineBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createLineBorder(Color.BLUE)));
            panel.add(lineBorderButton);
        }
        {
            JRadioButton titleBorderButton = new JRadioButton("title");
            titleBorderButton.setName("titleBorderButton");
            buttonGroup.add(titleBorderButton);
            titleBorderButton.addActionListener(actionEvent -> setBorderSupplier(() -> BorderFactory.createTitledBorder(WORDS[rc.nextInt(WORDS.length)])));
            panel.add(titleBorderButton);
        }
        return panel;
    }

    @NotNull
    JPanel buildAddComponentPanel() {
        final JPanel panel = new JPanel();
        panel.setName("addComponentPanel");
        panel.setBorder(BorderFactory.createTitledBorder("add"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        {
            JButton addLabelButton = new JButton("JLabel");
            addLabelButton.setName("addLabelButton");
            addLabelButton.addActionListener(actionEvent -> addToTarget(() -> new JLabel(WORDS[rc.nextInt(WORDS.length)] + " " + WORDS[rc.nextInt(WORDS.length)])));
            panel.add(addLabelButton);
        }
        {
            JButton addButtonButton = new JButton("JButton");
            addButtonButton.setName("addButtonButton");
            addButtonButton.addActionListener(actionEvent -> addToTarget(() -> new JButton(WORDS[rc.nextInt(WORDS.length)])));
            panel.add(addButtonButton);
        }
        {
            JButton addTextFieldButton = new JButton("JTextField");
            addTextFieldButton.setName("addTextFieldButton");
            addTextFieldButton.addActionListener(actionEvent -> addToTarget(() -> new JTextField(WORDS[rc.nextInt(WORDS.length)])));
            panel.add(addTextFieldButton);
        }
        {
            JButton addSliderButton = new JButton("JSlider");
            addSliderButton.setName("addSliderButton");
            addSliderButton.addActionListener(actionEvent -> addToTarget(() -> new JSlider(0, 100, rc.nextInt(101))));
            panel.add(addSliderButton);
        }
        {
            JButton addProgressBarButton = new JButton("JProgressBar");
            addProgressBarButton.setName("addProgressBarButton");
            addProgressBarButton.addActionListener(actionEvent -> addToTarget(() -> {
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setValue(1 + rc.nextInt(99));
                return progressBar;
            }));
            panel.add(addProgressBarButton);
        }
        return panel;
    }

    // class TabbedPlaygrounds and register to EveryThingMenuBar
}
