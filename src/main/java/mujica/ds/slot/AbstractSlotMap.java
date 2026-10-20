package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/10")
public abstract class AbstractSlotMap<KS, VS> implements SlotMap<KS, VS> {

    private static final long serialVersionUID = 0xbbc38c2ea200d1f3L;

    @NotNull
    @Override
    public abstract SlotMap<KS, VS> duplicateNew();

    @NotNull
    @Override
    public abstract SlotMap<KS, VS> duplicate();

    @NotNull
    @Override
    public abstract SlotMap<KS, VS> clone();

    @Override
    public abstract void checkHealth(@NotNull Consumer<RuntimeException> consumer);

    @NotNull
    protected abstract SlotAllocator<KS> getKeyAllocator();

    @NotNull
    protected abstract SlotAllocator<VS> getValueAllocator();

    @Override
    public void exchange(@NotNull KS keyIn, @NotNull VS valueInOut) {
        final VS tempSlot = getValueAllocator().newSlot();
        try {
            get(keyIn, tempSlot);
            set(keyIn, valueInOut);
            getValueAllocator().move(tempSlot, valueInOut);
        } finally {
            getValueAllocator().releaseSlot(tempSlot);
        }
    }

    @Override
    public boolean setIfAbsent(@NotNull KS keyIn, @NotNull VS valueIn) {
        if (keyPresent(keyIn)) {
            return false;
        } else {
            set(keyIn, valueIn);
            return true;
        }
    }

    @Override
    public boolean setIfPresent(@NotNull KS keyIn, @NotNull VS valueIn) {
        if (keyPresent(keyIn)) {
            set(keyIn, valueIn);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean exchangeIfPresent(@NotNull KS keyIn, @NotNull VS valueInOut) {
        if (keyPresent(keyIn)) {
            exchange(keyIn, valueInOut);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public <KT extends KS> void forEachKey(@NotNull Consumer<KT> consumer, @NotNull KT consumedKeySlot) {
        for (Entry<KS, VS> entry : this) {
            entry.getKey(consumedKeySlot);
            consumer.accept(consumedKeySlot);
        }
    }

    @Override
    public <VT extends VS> void forEachValue(@NotNull Consumer<VT> consumer, @NotNull VT consumedValueSlot) {
        for (Entry<KS, VS> entry : this) {
            entry.getValue(consumedValueSlot);
            consumer.accept(consumedValueSlot);
        }
    }

    @Override
    public <KT extends KS, VT extends VS> void forEachEntry(@NotNull BiConsumer<KT, VT> consumer, @NotNull KT consumedKeySlot, @NotNull VT consumedValueSlot) {
        for (Entry<KS, VS> entry : this) {
            entry.getKey(consumedKeySlot);
            entry.getValue(consumedValueSlot);
            consumer.accept(consumedKeySlot, consumedValueSlot);
        }
    }
}
