package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2025/12/12")
class GitBashColorScheme extends ColorScheme {

    @NotNull
    @Override
    Color getColor(@NotNull TerminalDisplayColor terminalColor) {
        switch (terminalColor) {
            case BLACK:
                return new Color(0, 0, 0);
            case RED:
                return new Color(191, 0, 0);
            case GREEN:
                return new Color(0, 191, 0);
            case YELLOW:
                return new Color(191, 191, 0);
            case BLUE:
                return new Color(0, 0, 191);
            case MAGENTA:
                return new Color(191, 0, 191);
            case CYAN:
                return new Color(0, 191, 191);
            case WHITE:
                return new Color(191, 191, 191);
            case BRIGHT_BLACK:
                return new Color(64, 64, 64);
            case BRIGHT_RED:
                return new Color(255, 64, 64);
            case BRIGHT_GREEN:
                return new Color(64, 255, 64);
            case BRIGHT_YELLOW:
                return new Color(255, 255, 64);
            case BRIGHT_BLUE:
                return new Color(96, 96, 255);
            case BRIGHT_MAGENTA:
                return new Color(255, 64, 255);
            case BRIGHT_CYAN:
                return new Color(64, 255, 255);
            case BRIGHT_WHITE:
                return new Color(255, 255, 255);
            default:
                throw new IllegalArgumentException();
        }
    }
}
