package org.slf4j;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/8/28")
public class ModuleNameFormatSegment implements StackTraceFormatSegment {

    private static final long serialVersionUID = 0x8B1AE21E9843C8F8L;

    @NotNull
    final String prefix, suffix, nullString;

    public ModuleNameFormatSegment(@NotNull String prefix, @NotNull String suffix, @NotNull String nullString) {
        super();
        this.prefix = prefix;
        this.suffix = suffix;
        this.nullString = nullString;
    }

    @Override
    public void append(@NotNull StackTraceElement frame, @NotNull StringBuilder out) {
        final String moduleName = frame.getModuleName();
        if (moduleName != null) {
            out.append(prefix).append(moduleName).append(suffix);
        } else {
            out.append(nullString);
        }
    }
}
