package org.slf4j;

import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/8/20.
 */
public class LevelSegment extends AlignedFormatSegment {

    private static final long serialVersionUID = 0x73F4D2F868ED3268L;

    public LevelSegment(int minLength, int maxLength, boolean padLeft, boolean truncateLeft) {
        super(minLength, maxLength, padLeft, truncateLeft);
    }

    public LevelSegment(boolean padLeft) {
        super(5, 5, padLeft, false);
    }

    public LevelSegment() {
        super(1, 5, false, false);
    }

    @Override
    public void append(@NotNull LogEvent event, @NotNull StringBuilder out) {
        final String levelName = event.level.name;
        if (maxLength == 1) {
            out.append(Character.toUpperCase(levelName.charAt(0)));
        } else {
            fit(levelName, out);
        }
    }
}
