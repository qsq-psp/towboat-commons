package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/7/18")
public interface NodeAllocator1<N> extends Serializable {

    @NotNull
    N newNode();

    default void releaseNode(@NotNull N node) {
        // pass
    }

    N[] newNodeArray(int length);

    default void releaseNodeArray(@NotNull N[] array) {
        // pass
    }

    int getTag(@NotNull N self);

    void setTag(@NotNull N self, int newTag);

    N getFirstLink(@NotNull N self);

    void setFirstLink(@NotNull N self, N node);
}
