package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/5/5")
public abstract class BitBlockBitHashCore extends ByteBlockBitHashCore {

    public BitBlockBitHashCore() {
        super();
    }

    @NotNull
    @Override
    public abstract BitBlockBitHashCore clone();

    public abstract int blockBits();

    @Override
    public int blockBytes() {
        return (blockBits() + Byte.SIZE - 1) / Byte.SIZE;
    }
}
