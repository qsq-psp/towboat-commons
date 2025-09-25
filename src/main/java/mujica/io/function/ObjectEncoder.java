package mujica.io.function;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created in Ultramarine on 2023/9/13, named BinarySerializer.
 * Recreated on 2025/4/5.
 * Both output accept bytes and do not have capacity limit.
 */
public interface ObjectEncoder {

    void encode(Object value, @NotNull ByteBuf out);

    void encode(Object value, @NotNull OutputStream os) throws IOException;
}
