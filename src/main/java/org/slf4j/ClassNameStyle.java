package org.slf4j;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2025/8/26.
 */
public class ClassNameStyle implements Serializable {

    private static final long serialVersionUID = 0xAC12CDE18E09669AL;

    @NotNull
    final ClassNameSegmentStyle packageStyle;

    @NotNull
    final ClassNameSegmentStyle classStyle;

    final int stripDepth;

    public ClassNameStyle(@NotNull ClassNameSegmentStyle packageStyle, @NotNull ClassNameSegmentStyle classStyle, int stripDepth) {
        super();
        if (!(0 <= stripDepth)) {
            throw new IllegalArgumentException();
        }
        this.packageStyle = packageStyle;
        this.classStyle = classStyle;
        this.stripDepth = stripDepth;
    }
}
