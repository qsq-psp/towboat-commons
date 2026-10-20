package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/17")
public abstract class HashSlotMap<KS, KA, VS, VA> extends AbstractSlotMap<KS, VS> {

    private static final long serialVersionUID = 0x14813c4acd0033a8L;

    @NotNull
    final SlotArrayAllocator.HashSAA<KS, KA> hashFunction;

    @NotNull
    final SlotArrayAllocator.PolicySAA<VS, VA> valueAllocator;

    int size;

    @Name(value = "modCount", language = "en")
    transient int version;

    protected HashSlotMap(@NotNull SlotArrayAllocator.HashSAA<KS, KA> hashFunction, @NotNull SlotArrayAllocator.PolicySAA<VS, VA> valueAllocator) {
        super();
        this.hashFunction = hashFunction;
        this.valueAllocator = valueAllocator;
    }

    @NotNull
    @Override
    protected SlotAllocator<KS> getKeyAllocator() {
        return hashFunction;
    }

    @Override
    @NotNull
    public SlotAllocator<VS> getValueAllocator() {
        return valueAllocator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public <KT extends KS> void forEachKey(@NotNull Consumer<KT> consumer, @NotNull KT consumedKeySlot) {
        final int expectedVersion = version;
        for (Entry<KS, VS> entry : this) {
            entry.getKey(consumedKeySlot);
            consumer.accept(consumedKeySlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <VT extends VS> void forEachValue(@NotNull Consumer<VT> consumer, @NotNull VT consumedValueSlot) {
        final int expectedVersion = version;
        for (Entry<KS, VS> entry : this) {
            entry.getValue(consumedValueSlot);
            consumer.accept(consumedValueSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public <KT extends KS, VT extends VS> void forEachEntry(@NotNull BiConsumer<KT, VT> consumer, @NotNull KT consumedKeySlot, @NotNull VT consumedValueSlot) {
        final int expectedVersion = version;
        for (Entry<KS, VS> entry : this) {
            entry.getKey(consumedKeySlot);
            entry.getValue(consumedValueSlot);
            consumer.accept(consumedKeySlot, consumedValueSlot);
            if (version != expectedVersion) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
