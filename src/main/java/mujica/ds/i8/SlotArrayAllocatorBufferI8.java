package mujica.ds.i8;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorBufferI8 implements SlotArrayAllocator<I8Slot, ByteBuffer> {

    @NotNull
    @Override
    public I8Slot newSlot() {
        return new I8();
    }

    @Override
    public void move(@NotNull I8Slot src, @NotNull I8Slot dst) {
        dst.setI8(src.getI8());
    }

    @NotNull
    @Override
    public ByteBuffer newArray(int length) {
        return ByteBuffer.allocateDirect(length);
    }

    @Override
    public int length(@NotNull ByteBuffer buffer) {
        return buffer.limit();
    }

    @Override
    public void load(@NotNull ByteBuffer buffer, int index, @NotNull I8Slot slot) {
        slot.setI8(buffer.get(index));
    }

    @Override
    public void store(@NotNull ByteBuffer buffer, int index, @NotNull I8Slot slot) {
        buffer.put(index, slot.getI8());
    }

    @Override
    public void exchange(@NotNull ByteBuffer buffer, int index, @NotNull I8Slot slot) {
        buffer.put(index, slot.updateI8(buffer.get(index)));
    }

    @Override
    public void exchange(@NotNull ByteBuffer buffer, int i, int j) {
        final byte temp = buffer.get(i);
        buffer.put(i, buffer.get(j));
        buffer.put(j, temp);
    }

    @Override
    public void copy(@NotNull ByteBuffer src, int srcOffset, @NotNull ByteBuffer dst, int dstOffset, int length) {
        if (src == dst) {
            if (srcOffset != dstOffset) {
                for (int index = 0; index < length; index++) {
                    dst.put(dstOffset++, src.get(srcOffset++));
                }
            }
            return;
        }
        final int srcNewLimit = srcOffset + length;
        final int srcOldLimit = src.limit();
        final int srcPosition = src.position();
        if (srcOldLimit < srcNewLimit) {
            throw new IndexOutOfBoundsException();
        }
        try {
            dst.put(src.position(srcOffset).limit(srcNewLimit));
        } finally {
            src.limit(srcOldLimit).position(srcPosition);
        }
    }
}
