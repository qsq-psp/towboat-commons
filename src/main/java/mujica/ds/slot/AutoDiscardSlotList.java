package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/11/4", name = "ArrayCopyLookBackMemory")
@CodeHistory(date = "2025/11/26", name = "ArrayCopyRunBuffer")
@CodeHistory(date = "2026/7/26")
public class AutoDiscardSlotList<S, A> extends CopyOnResizeSlotList<S, A> {

    int reservedSize;

    public AutoDiscardSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, int reservedSize) {
        super(allocator);
        if (reservedSize < 0) {
            throw new IllegalArgumentException();
        }
        this.reservedSize = reservedSize;
    }

    AutoDiscardSlotList(@NotNull SlotArrayAllocator.PolicySAA<S, A> allocator, int reservedSize, @NotNull A array, int endIndex) {
        super(allocator, array, endIndex);
        this.reservedSize = reservedSize;
    }

    @NotNull
    @Override
    public SlotCollection<S, A> duplicateNew() {
        return new AutoDiscardSlotList<>(allocator, reservedSize);
    }

    @NotNull
    @Override
    public AutoDiscardSlotList<S, A> duplicate() {
        final int newCapacity = Math.max(allocator.getCapacityPolicy().initialCapacity(), endIndex);
        final A newArray = allocator.newArray(newCapacity);
        allocator.copy(array, 0, newArray, 0, endIndex);
        return new AutoDiscardSlotList<>(allocator, reservedSize, newArray, endIndex);
    }

    public void setReservedSize(int reservedSize) {
        if (reservedSize < 0) {
            throw new IllegalArgumentException();
        }
        this.reservedSize = reservedSize;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "reservedSize = " + reservedSize + ", size = " + endIndex + ", capacity = " + allocator.length(array) + ", version = " + version;
    }
}
