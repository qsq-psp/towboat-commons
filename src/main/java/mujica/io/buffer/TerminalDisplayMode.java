package mujica.io.buffer;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "DisplayMode")
@CodeHistory(date = "2025/10/21")
public enum TerminalDisplayMode {

    BOLD(1, 22),
    UNDERLINE(4, 24),
    THROUGH(9, 29);

    private static final long serialVersionUID = 0xa4c5c7e0b5f9a4a5L;

    final int codeOn, codeOff;

    TerminalDisplayMode(int codeOn, int codeOff) {
        this.codeOn = codeOn;
        this.codeOff = codeOff;
    }

    public int getCode(boolean on) {
        return on ? codeOn : codeOff;
    }
}
