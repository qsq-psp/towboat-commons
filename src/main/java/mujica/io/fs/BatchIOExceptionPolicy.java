package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/3/14")
public enum BatchIOExceptionPolicy {

    BREAK, CONTINUE, ROLLBACK;

    private static final long serialVersionUID = 0x392d7a0af0273ecfL;
}
