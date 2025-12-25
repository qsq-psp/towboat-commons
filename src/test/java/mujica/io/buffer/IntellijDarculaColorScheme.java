package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2025/12/13")
public class IntellijDarculaColorScheme extends ColorScheme {

    @NotNull
    @Override
    Color getColor(@NotNull TerminalDisplayColor terminalColor) {
        switch (terminalColor) {
            case BLACK:
                return new Color(255, 255, 255);
            case RED:
                return new Color(255, 107, 104);
            case GREEN:
            case BRIGHT_GREEN:
                return new Color(168, 192, 35);
            case YELLOW:
                return new Color(214, 191, 85);
            case BLUE:
                return new Color(83, 148, 236);
            case MAGENTA:
                return new Color(174, 138, 190);
            case CYAN:
                return new Color(41, 153, 153);
            case WHITE:
                return new Color(153, 153, 153);
            case BRIGHT_BLACK:
                return new Color(85, 85, 85);
            case BRIGHT_RED:
                return new Color(255, 135, 133);
            case BRIGHT_YELLOW:
                return new Color(255, 255, 0);
            case BRIGHT_BLUE:
                return new Color(126, 174, 241);
            case BRIGHT_MAGENTA:
                return new Color(255, 153, 255);
            case BRIGHT_CYAN:
                return new Color(97, 214, 214);
            case BRIGHT_WHITE:
                return new Color(31, 31, 31);
            default:
                throw new IllegalArgumentException();
        }
    }

    @NotNull
    @Override
    Color getForegroundColor(@Nullable TerminalDisplayColor terminalColor) {
        if (terminalColor != null) {
            return getColor(terminalColor);
        } else {
            return new Color(187, 187, 187);
        }
    }

    @NotNull
    @Override
    Color getBackgroundColor(@Nullable TerminalDisplayColor terminalColor) {
        if (terminalColor != null) {
            return getColor(terminalColor);
        } else {
            return new Color(43, 43, 43);
        }
    }
}
