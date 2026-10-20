package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/9/20")
@FunctionalInterface
public interface HeapRunnable {

    boolean run(boolean canContinue);
}
