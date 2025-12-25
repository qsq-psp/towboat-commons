package mujica.io.buffer;

import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.test.SwingManualTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

@CodeHistory(date = "2025/12/6")
public class TerminalDisplayTest extends SwingManualTest {

    /*
    private static final char FILLED_SQUARE = '\u25a0';

    private static void printSampleForeground() {
        final StringBuilder sb = new StringBuilder();
        final TerminalStyleTransition transition = new TerminalStyleTransition();
        for (TerminalDisplayColor terminalColor : TerminalDisplayColor.values()) {
            transition.copy();
            transition.setDst(new TerminalStyle(null, terminalColor, null));
            transition.stringify(sb);
            sb.append(FILLED_SQUARE);
        }
        transition.reset();
        transition.stringify(sb);
        System.out.println(sb.toString());
    }
    //*/

    @BeforeClass
    public static void installUI() {
        SwingUtilities.invokeLater(SwingManualTest::installComponents);
    }

    @AfterClass
    public static void uninstallUI() {
        SwingUtilities.invokeLater(SwingManualTest::uninstallComponents);
    }

    private static final RandomContext rc = new RandomContext();

    private static final TerminalDisplayColor[] COLORS;

    static {
        final TerminalDisplayColor[] NOT_NULL_COLORS = TerminalDisplayColor.values();
        final int length = NOT_NULL_COLORS.length;
        COLORS = new TerminalDisplayColor[length + 1];
        System.arraycopy(NOT_NULL_COLORS, 0, COLORS, 1, length);
    }

    @Nullable
    private static Boolean nextBoxBoolean() {
        if (rc.nextInt(4) == 0) {
            return rc.nextBoolean();
        } else {
            return null;
        }
    }

    @NotNull
    private static TerminalStyle nextTerminalStyle() {
        final int length = COLORS.length;
        int foregroundIndex = rc.nextMinInt(length, 2);
        int backgroundIndex = rc.nextMinInt(length, 3);
        if (foregroundIndex == backgroundIndex && foregroundIndex != 0) {
            backgroundIndex = (backgroundIndex + 1) % length;
        }
        return new TerminalStyle(
                COLORS[foregroundIndex],
                COLORS[backgroundIndex],
                nextBoxBoolean(),
                nextBoxBoolean(),
                nextBoxBoolean()
        );
    }

    private static final ColorScheme COLOR_SCHEME = new IntellijDarculaColorScheme();

    private static void setSwingStyle(@NotNull TerminalStyle src, @NotNull Style dst) {
        if (src.foreground != null) {
            StyleConstants.setForeground(dst, COLOR_SCHEME.getForegroundColor(src.foreground));
        }
        if (src.background != null) {
            StyleConstants.setBackground(dst, COLOR_SCHEME.getBackgroundColor(src.background));
        }
        if (src.bold != null) {
            StyleConstants.setBold(dst, src.bold);
        }
        if (src.underline != null) {
            StyleConstants.setUnderline(dst, src.underline);
        }
        if (src.through != null) {
            StyleConstants.setStrikeThrough(dst, src.through);
        }
    }

    private static final String[] WORDS = {
            "apply", "blanket", "collection", "drop", "erase", "finish", "great",
            "have", "inside", "just", "kick", "lift", "moon", "need",
            "oppose", "pavement", "quality", "roll", "shovel", "timber",
            "user", "volatile", "world", "your", "zygote"
    };

    private JTextPane checkTerminalDisplayTextPane(@NotNull String[] words) {
        final TerminalStyleTransition transition = new TerminalStyleTransition();
        transition.reset();
        final StringBuilder sb = new StringBuilder();
        final JTextPane textPane = new JTextPane();
        textPane.setForeground(COLOR_SCHEME.getForegroundColor(null));
        textPane.setBackground(COLOR_SCHEME.getBackgroundColor(null));
        final StyledDocument document = textPane.getStyledDocument();
        final Style parentStyle = document.addStyle("base", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));
        StyleConstants.setFontFamily(parentStyle, "Consolas");
        StyleConstants.setFontSize(parentStyle, 20);
        for (String word : words) {
            TerminalStyle terminalStyle = nextTerminalStyle();
            System.out.println(terminalStyle);
            transition.copy();
            transition.setDst(terminalStyle);
            transition.stringify(sb);
            sb.append(word);
            try {
                Style childStyle = document.addStyle(word, parentStyle);
                setSwingStyle(terminalStyle, childStyle);
                document.insertString(document.getLength(), word, childStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        transition.reset();
        transition.stringify(sb);
        System.out.println(sb.toString());
        return textPane;
    }

    private void checkTerminalDisplay(@NotNull String[] words) throws InterruptedException {
        installContent(() -> checkTerminalDisplayTextPane(words));
        passOrFail();
    }

    @Test
    public void checkTerminalDisplay3() throws InterruptedException {
        checkTerminalDisplay(new String[] {
                "Indicate",
                "dependence",
                "municipality"
        });
    }

    @Test
    public void checkTerminalDisplay4() throws InterruptedException {
        checkTerminalDisplay(new String[] {
                "Dispenser",
                "sunbathing",
                "antidote",
                "nuisance"
        });
    }

    @Test
    public void checkTerminalDisplay5() throws InterruptedException {
        checkTerminalDisplay(new String[] {
                "Stockholm",
                "propulsion",
                "obsolescence",
                "interpreter",
                "environment"
        });
    }

    @Test
    public void checkTerminalDisplay6() throws InterruptedException {
        checkTerminalDisplay(new String[] {
                "Representation",
                "domestication",
                "dialysis",
                "commission",
                "dam",
                "sundae"
        });
    }
}
