package mujica.io.buffer;

import mujica.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.source.SwingManualTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

@CodeHistory(date = "2025/12/6")
public class TerminalDisplayTest extends SwingManualTest {

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
        final int foregroundIndex = rc.nextMinInt(length, 2);
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

    @CodeHistory(date = "2026/3/25")
    private static class StyledWord {

        @NotNull
        final TerminalStyle style = nextTerminalStyle();

        @NotNull
        final String word;

        private StyledWord(@NotNull String word) {
            super();
            this.word = word;
        }
    }

    @NotNull
    private JTextPane confirmTerminalDisplayTextPane(@NotNull StyledWord[] styledWords) {
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
        for (StyledWord styledWord : styledWords) {
            transition.copy();
            transition.setDst(styledWord.style);
            transition.stringify(sb);
            sb.append(styledWord.word);
            try {
                Style childStyle = document.addStyle(styledWord.word, parentStyle);
                setSwingStyle(styledWord.style, childStyle);
                document.insertString(document.getLength(), styledWord.word, childStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        transition.reset();
        transition.stringify(sb);
        System.out.println(sb.toString());
        return textPane;
    }

    private void confirmTerminalDisplay(@NotNull String[] words) throws InterruptedException {
        final int length = words.length;
        final StyledWord[] styledWords = new StyledWord[length];
        for (int index = 0; index < length; index++) {
            styledWords[index] = new StyledWord(words[index]);
        }
        installContent(() -> confirmTerminalDisplayTextPane(styledWords));
        passOrFail();
    }

    @Test
    public void confirmTerminalDisplay3() throws InterruptedException {
        confirmTerminalDisplay(new String[] {
                "Indicate",
                "dependence",
                "municipality"
        });
    }

    @Test
    public void confirmTerminalDisplay4() throws InterruptedException {
        confirmTerminalDisplay(new String[] {
                "Dispenser",
                "sunbathing",
                "antidote",
                "nuisance"
        });
    }

    @Test
    public void confirmTerminalDisplay5() throws InterruptedException {
        confirmTerminalDisplay(new String[] {
                "Stockholm",
                "propulsion",
                "obsolescence",
                "interpreter",
                "environment"
        });
    }

    @Test
    public void confirmTerminalDisplay6() throws InterruptedException {
        confirmTerminalDisplay(new String[] {
                "Representation",
                "domestication",
                "dialysis",
                "commission",
                "dam",
                "sundae"
        });
    }
}
