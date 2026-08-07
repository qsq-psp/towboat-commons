package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/18")
public interface NodeAllocator3<N> extends NodeAllocator2<N> {

    @Override
    N getFirstLink(@NotNull N self);

    @Override
    void setFirstLink(@NotNull N self, N node);

    @Override
    N getSecondLink(@NotNull N self);

    @Override
    void setSecondLink(@NotNull N self, N node);

    N getThirdLink(@NotNull N self);

    void setThirdLink(@NotNull N self, N node);
}
