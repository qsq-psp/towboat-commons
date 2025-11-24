package mujica.io.compress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.DefaultByteBufHolder;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/11/17.
 */
@CodeHistory(date = "2025/11/17")
public class ByteBufLookBackMemory extends DefaultByteBufHolder implements LookBackMemory {

    public ByteBufLookBackMemory(@NotNull ByteBuf content) {
        super(content);
    }

    @Override
    public void write(int value) {
        content().writeByte(value);
    }

    @Override
    public int write(@NotNull byte[] array, int offset, int length) {
        return 0;
    }

    @Override
    public byte copyAndWrite(int distance) {
        return 0;
    }

    @Override
    public int copyAndWrite(int distance, @NotNull byte[] array, int offset, int length) {
        return 0;
    }
}
