package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/18")
public interface NodeAccess1<N> {

    @NotNull
    N newNode();

    default void releaseNode(@NotNull N node) {
        // pass
    }

    N getFirstLink(@NotNull N self);

    void setFirstLink(@NotNull N self, N node);
}
