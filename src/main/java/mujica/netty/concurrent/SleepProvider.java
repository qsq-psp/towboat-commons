package mujica.netty.concurrent;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

@CodeHistory(date = "2026/9/24", project = "nettyon")
@CodeHistory(date = "2026/10/2")
public interface SleepProvider {

    @NotNull
    String sleepName();

    boolean isDeprecated();

    @Nullable // return null if unknown or uncertain
    Thread.State threadState();

    @Nullable // return null if unknown or uncertain
    VisualVmState visualVmState();

    @Nullable // return null if unknown or uncertain
    OshiState oshiState();

    @Nullable // return null if unknown or uncertain
    TimeUnit timeAccuracy();

    @NotNull
    SleepRunnable create();
}
