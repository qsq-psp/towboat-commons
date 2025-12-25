package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2025/12/11")
class ColorScheme {

    @NotNull
    Color getColor(@NotNull TerminalDisplayColor terminalColor) {
        switch (terminalColor) {
            case BLACK:
                return Color.BLACK;
            case RED:
                return Color.RED.darker();
            case GREEN:
                return Color.GREEN.darker();
            case YELLOW:
                return Color.YELLOW.darker();
            case BLUE:
                return Color.BLUE.darker();
            case MAGENTA:
                return Color.MAGENTA.darker();
            case CYAN:
                return Color.CYAN.darker();
            case WHITE:
                return Color.WHITE.darker();
            case BRIGHT_BLACK:
                return Color.BLACK.brighter();
            case BRIGHT_RED:
                return Color.RED;
            case BRIGHT_GREEN:
                return Color.GREEN;
            case BRIGHT_YELLOW:
                return Color.YELLOW;
            case BRIGHT_BLUE:
                return Color.BLUE;
            case BRIGHT_MAGENTA:
                return Color.MAGENTA;
            case BRIGHT_CYAN:
                return Color.CYAN;
            case BRIGHT_WHITE:
                return Color.WHITE;
            default:
                throw new IllegalArgumentException();
        }
    }

    @NotNull
    Color getForegroundColor(@Nullable TerminalDisplayColor terminalColor) {
        if (terminalColor == null) {
            terminalColor = TerminalDisplayColor.WHITE;
        }
        return getColor(terminalColor);
    }

    @NotNull
    Color getBackgroundColor(@Nullable TerminalDisplayColor terminalColor) {
        if (terminalColor == null) {
            terminalColor = TerminalDisplayColor.BLACK;
        }
        return getColor(terminalColor);
    }
}
