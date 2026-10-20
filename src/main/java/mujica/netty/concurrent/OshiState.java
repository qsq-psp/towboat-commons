package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferenceCode;

@CodeHistory(date = "2026/9/25")
@ReferenceCode(groupId = "com.github.oshi", artifactId = "oshi-core", version = "6.4.7", fullyQualifiedName = "oshi.software.os.OSProcess.State")
public enum OshiState {
    /**
     * Intermediate state in process creation
     */
    NEW,
    /**
     * Actively executing process
     */
    RUNNING,
    /**
     * Interruptible sleep state
     */
    SLEEPING,
    /**
     * Blocked, uninterruptible sleep state
     */
    WAITING,
    /**
     * Intermediate state in process termination
     */
    ZOMBIE,
    /**
     * Stopped by the user, such as for debugging
     */
    STOPPED,
    /**
     * Other or unknown states not defined
     */
    OTHER,
    /**
     * The state resulting if the process fails to update statistics, probably due to termination.
     */
    INVALID,
    /**
     * Special case of waiting if the process has been intentionally suspended (Windows only)
     */
    SUSPENDED;

    private static final long serialVersionUID = 0x59ED466CA48743C4L;
}
