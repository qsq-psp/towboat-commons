package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/8/24")
public interface MapNodeAllocator1<N, KS, VS> extends NodeAllocator1<N> {

    @NotNull
    KS getKeySlot();

    @NotNull
    VS getValueSlot();

    @Override
    @NotNull
    N newNode();

    @Override
    @NotNull
    N[] newNodeArray(int length);

    @Override
    int getTag(@NotNull N self);

    @Override
    void setTag(@NotNull N self, int newTag);

    @Override
    N getFirstLink(@NotNull N self);

    @Override
    void setFirstLink(@NotNull N self, N node);
}
