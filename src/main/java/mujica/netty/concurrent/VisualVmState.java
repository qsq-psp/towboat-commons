package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2026/9/24", project = "nettyon")
@CodeHistory(date = "2026/9/27")
@ReferenceCode(groupId = "", artifactId = "", version = "", fullyQualifiedName = "org.graalvm.visualvm.lib.jfluid.global.CommonConstants")
public enum VisualVmState {

    RUNNING(new Color(58, 228, 103)),
    SLEEPING(new Color(155, 134, 221)),
    MONITOR(new Color(160, 240, 238)),
    WAIT(new Color(255, 228, 90)),
    PARK(new Color(230, 128, 30));

    private static final long serialVersionUID = 0x82F51292D8324CDCL;

    @NotNull
    final Color color;

    VisualVmState(@NotNull Color color) {
        this.color = color;
    }
}
