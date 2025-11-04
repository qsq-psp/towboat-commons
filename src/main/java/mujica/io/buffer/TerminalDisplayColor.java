package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "DisplayColor")
@CodeHistory(date = "2025/10/21")
public enum TerminalDisplayColor {

    BLACK(30, 40),
    RED(31, 41),
    GREEN(32, 42),
    YELLOW(33, 43),
    BLUE(34, 44),
    MAGENTA(35, 45),
    CYAN(36, 46),
    WHITE(37, 47),
    BRIGHT_BLACK(90, 100),
    BRIGHT_RED(91, 101),
    BRIGHT_GREEN(92, 102),
    BRIGHT_YELLOW(93, 103),
    BRIGHT_BLUE(94, 104),
    BRIGHT_MAGENTA(95, 105),
    BRIGHT_CYAN(96, 106),
    BRIGHT_WHITE(97, 107);

    private static final long serialVersionUID = 0xdae021783780d7beL;

    public final int foregroundCode, backgroundCode;

    TerminalDisplayColor(int foregroundCode, int backgroundCode) {
        this.foregroundCode = foregroundCode;
        this.backgroundCode = backgroundCode;
    }
}
