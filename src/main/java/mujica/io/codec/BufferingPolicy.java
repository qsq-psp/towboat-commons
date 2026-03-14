package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/4/17", project = "UltraIO", name = "YieldPolicy")
@CodeHistory(date = "2025/4/25")
public enum BufferingPolicy {

    GREEDY, NORMAL, MINIMAL;

    private static final long serialVersionUID = 0x6a2dddc2754b657bL;
}
