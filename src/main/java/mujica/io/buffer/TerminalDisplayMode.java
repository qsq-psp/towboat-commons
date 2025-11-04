package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "DisplayMode")
@CodeHistory(date = "2025/10/21")
public enum TerminalDisplayMode {

    HIGHLIGHT(1),
    UNDERLINE(4),
    BLINK(5),
    REVERT(7),
    INVISIBLE(8);

    private static final long serialVersionUID = 0xa4c5c7e0b5f9a4a5L;

    public final int code;

    TerminalDisplayMode(int code) {
        this.code = code;
    }
}
