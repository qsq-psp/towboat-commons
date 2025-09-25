package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/8/25")
public class MethodNameFormatSegment implements StackTraceFormatSegment {

    private static final long serialVersionUID = 0x78F43AED22FF977AL;

    @Override
    public void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out) {
        out.append(frame.getMethodName());
    }
}
