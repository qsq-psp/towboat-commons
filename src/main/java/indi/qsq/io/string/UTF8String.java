package indi.qsq.io.string;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2024/6/29.
 */
public class UTF8String extends IOString {

    @NotNull
    final byte[] bytes;

    public UTF8String(@NotNull String string) {
        super(string);
        bytes = string.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public int byteLength() {
        return bytes.length;
    }

    @Override
    public void writeInto(@NotNull OutputStream os) throws IOException {
        os.write(bytes);
    }

    @Override
    public void addInto(@NotNull ByteBuffer buf) throws BufferOverflowException {
        buf.put(bytes);
    }

    @Override
    public void addInto(@NotNull ByteBuf buf) {
        buf.writeBytes(bytes);
    }
}
