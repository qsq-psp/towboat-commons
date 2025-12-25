package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2025/12/12")
class CmdColorScheme extends ColorScheme {

    @NotNull
    @Override
    Color getColor(@NotNull TerminalDisplayColor terminalColor) {
        switch (terminalColor) {
            case BLACK:
                return new Color(12, 12, 12);
            case RED:
                return new Color(197, 15, 31);
            case GREEN:
                return new Color(19, 161, 14);
            case YELLOW:
                return new Color(193, 156, 0);
            case BLUE:
                return new Color(0, 55, 218);
            case MAGENTA:
                return new Color(136, 23, 152);
            case CYAN:
                return new Color(58, 150, 221);
            case WHITE:
                return new Color(204, 204, 204);
            case BRIGHT_BLACK:
                return new Color(118, 118, 118);
            case BRIGHT_RED:
                return new Color(231, 72, 86);
            case BRIGHT_GREEN:
                return new Color(22, 198, 12);
            case BRIGHT_YELLOW:
                return new Color(249, 241, 165);
            case BRIGHT_BLUE:
                return new Color(59, 120, 255);
            case BRIGHT_MAGENTA:
                return new Color(180, 0, 158);
            case BRIGHT_CYAN:
                return new Color(97, 214, 214);
            case BRIGHT_WHITE:
                return new Color(242, 242, 242);
            default:
                throw new IllegalArgumentException();
        }
    }
}
