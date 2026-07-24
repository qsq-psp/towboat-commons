package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/18")
public interface NodeAccess2<N> extends NodeAccess1<N> {

    @Override
    N getFirstLink(@NotNull N self);

    @Override
    void setFirstLink(@NotNull N self, N node);

    N getSecondLink(@NotNull N self);

    void setSecondLink(@NotNull N self, N node);
}
