package mujica.io.function;

import io.netty.buffer.ByteBuf;
import mujica.ds.of_int.IntSlot;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created on 2025/4/5.
 * Number of bytes consumed is determined by decoder itself, if there is not enough byte provided once, an Exception is thrown
 */
public interface ObjectDecoder {

    Object decode(@NotNull byte[] data, @NotNull IntSlot pointer);

    Object decode(@NotNull ByteBuffer in);

    Object decode(@NotNull ByteBuf in);

    Object decode(@NotNull InputStream is) throws IOException;
}
