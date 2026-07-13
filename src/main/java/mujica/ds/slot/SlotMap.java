package mujica.ds.slot;

import mujica.ds.base.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@CodeHistory(date = "2026/7/8")
public interface SlotMap<KS, KA, VS, VA> extends DataStructure {

    @NotNull
    @Override
    SlotMap<KS, KA, VS, VA> duplicate();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    int size();

    void put(@NotNull KS keyIn, @NotNull VS valueSlot);

    boolean putIfAbsent(@NotNull KS keyIn, @NotNull VS valueIn);

    boolean putIfPresent(@NotNull KS keyIn, @NotNull VS valueIn);

    void clear();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
