package mujica.ds.i32;

import mujica.ds.slot.SlotArrayAllocator;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@CodeHistory(date = "2026/6/26")
public class SlotArrayAllocatorBufferI32 implements SlotArrayAllocator<I32Slot, IntBuffer> {
    @NotNull
    @Override
    public I32Slot newSlot() {
        return new S32();
    }

    @Override
    public void move(@NotNull I32Slot src, @NotNull I32Slot dst) {
        dst.setI32(src.getI32());
    }

    @NotNull
    @Override
    public IntBuffer newArray(int length) {
        if (length > Integer.MAX_VALUE >> 2) {
            throw new IllegalArgumentException();
        }
        return ByteBuffer.allocateDirect(length << 2).asIntBuffer();
    }

    @Override
    public int length(@NotNull IntBuffer buffer) {
        return buffer.limit();
    }

    @Override
    public void load(@NotNull IntBuffer buffer, int index, @NotNull I32Slot slot) {
        slot.setI32(buffer.get(index));
    }

    @Override
    public void store(@NotNull IntBuffer buffer, int index, @NotNull I32Slot slot) {
        buffer.put(index, slot.getI32());
    }

    @Override
    public void exchange(@NotNull IntBuffer buffer, int index, @NotNull I32Slot slot) {
        buffer.put(index, slot.updateI32(buffer.get(index)));
    }

    @Override
    public void copy(@NotNull IntBuffer src, int srcOffset, @NotNull IntBuffer dst, int dstOffset, int length) {
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
