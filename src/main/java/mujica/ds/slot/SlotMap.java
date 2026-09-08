package mujica.ds.slot;

import mujica.ds.common.DataStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@CodeHistory(date = "2026/7/8")
public interface SlotMap<KS, VS> extends DataStructure, Iterable<SlotMap.Entry<KS, VS>> {

    @NotNull
    SlotMap<KS, VS> duplicateNew();

    @NotNull
    @Override
    SlotMap<KS, VS> duplicate();

    @NotNull
    SlotMap<KS, VS> clone();

    @Override
    void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    int size();

    boolean keyPresent(@NotNull KS keyIn);

    void get(@NotNull KS keyIn, @NotNull VS valueOut);

    void set(@NotNull KS keyIn, @NotNull VS valueIn);

    void exchange(@NotNull KS keyIn, @NotNull VS valueInOut);

    boolean setIfAbsent(@NotNull KS keyIn, @NotNull VS valueIn);

    boolean setIfPresent(@NotNull KS keyIn, @NotNull VS valueIn);

    boolean exchangeIfPresent(@NotNull KS keyIn, @NotNull VS valueInOut);

    boolean remove(@NotNull KS keyIn, @Nullable VS valueOut);

    void clear();

    <KT extends KS> void forEachKey(@NotNull Consumer<KT> consumer, @NotNull KT consumedKeySlot);

    <VT extends VS> void forEachValue(@NotNull Consumer<VT> consumer, @NotNull VT consumedValueSlot);

    <KT extends KS, VT extends VS> void forEachEntry(@NotNull BiConsumer<KT, VT> consumer, @NotNull KT consumedKeySlot, @NotNull VT consumedValueSlot);

    @CodeHistory(date = "2026/8/7")
    interface Entry<KS, VS> {

        void getKey(@NotNull KS keyOut);

        void getValue(@NotNull VS valueOut);

        void setValue(@NotNull VS valueIn);

        void exchangeValue(@NotNull VS valueInOut);
    }

    @NotNull
    @Override
    Iterator<Entry<KS, VS>> iterator();

    @NotNull
    @Override
    String summaryToString();

    @NotNull
    @Override
    String detailToString();
}
