package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/27.
 */
@CodeHistory(date = "2025/8/27")
public class DeclaringClassFormatSegment implements StackTraceFormatSegment {

    private static final long serialVersionUID = 0x1164F274017B6157L;

    @Override
    public void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out) {
        out.append(frame.getClassName());
    }
}
