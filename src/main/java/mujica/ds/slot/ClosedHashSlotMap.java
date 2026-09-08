package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@CodeHistory(date = "2026/8/19")
public class ClosedHashSlotMap<KS, KA, VS, VA> extends HashSlotMap<KS, KA, VS, VA> {

    static final int SPECIAL_EMPTY = -1;

    static final int SPECIAL_REMOVED = -2;

    transient int modulo;

    @NotNull
    KA keyTable;

    @NotNull
    VA valueTable;

    @NotNull
    int[] hashCache;

    public ClosedHashSlotMap(@NotNull SlotArrayAllocator.HashSAA<KS, KA> hashFunction, @NotNull SlotArrayAllocator.PolicySAA<VS, VA> valueAllocator) {
        super(hashFunction, valueAllocator);
        this.modulo = hashFunction.getCapacityPolicy().initialCapacity();
        this.keyTable = hashFunction.newArray(modulo);
        this.valueTable = valueAllocator.newArray(modulo);
        this.hashCache = new int[modulo];
        Arrays.fill(hashCache, SPECIAL_EMPTY);
    }

    @NotNull
    @Override
    public SlotMap<KS, VS> duplicateNew() {
        return new ClosedHashSlotMap<>(hashFunction, valueAllocator);
    }

    @NotNull
    @Override
    public SlotMap<KS, VS> duplicate() {
        return null;
    }

    @NotNull
    @Override
    public SlotMap<KS, VS> clone() {
        return null;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean keyPresent(@NotNull KS keyIn) {
        return false;
    }

    @Override
    public void get(@NotNull KS keyIn, @NotNull VS valueOut) {

    }

    @Override
    public void set(@NotNull KS keyIn, @NotNull VS valueIn) {

    }

    @Override
    public void exchange(@NotNull KS keyIn, @NotNull VS valueInOut) {

    }

    @Override
    public boolean setIfAbsent(@NotNull KS keyIn, @NotNull VS valueIn) {
        return false;
    }

    @Override
    public boolean setIfPresent(@NotNull KS keyIn, @NotNull VS valueIn) {
        return false;
    }

    @Override
    public boolean exchangeIfPresent(@NotNull KS keyIn, @NotNull VS valueInOut) {
        return false;
    }

    @Override
    public boolean remove(@NotNull KS keyIn, @Nullable VS valueOut) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public <KT extends KS> void forEachKey(@NotNull Consumer<KT> consumer, @NotNull KT consumedKeySlot) {

    }

    @Override
    public <VT extends VS> void forEachValue(@NotNull Consumer<VT> consumer, @NotNull VT consumedValueSlot) {

    }

    @Override
    public <KT extends KS, VT extends VS> void forEachEntry(@NotNull BiConsumer<KT, VT> consumer, @NotNull KT consumedKeySlot, @NotNull VT consumedValueSlot) {

    }

    @NotNull
    @Override
    public Iterator<Entry<KS, VS>> iterator() {
        return null;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return null;
    }

    @NotNull
    @Override
    public String detailToString() {
        return null;
    }
}
