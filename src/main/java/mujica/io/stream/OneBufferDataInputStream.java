package mujica.io.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import mujica.io.function.IOConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@CodeHistory(date = "2026/2/24")
@ReferenceCode(groupId = "JDK", artifactId = "java.base", version = "12", fullyQualifiedName = "java.io.PushbackInputStream")
public class OneBufferDataInputStream extends ZeroBufferDataInputStream {

    @DataType("u8+{-1}")
    private int back = -1;

    public OneBufferDataInputStream(@NotNull InputStream in) {
        super(in);
    }

    public void unread(int value) throws IOException {
        if (back != -1) {
            throw new IOException("full");
        }
        back = value & 0xff;
    }

    @Override
    @DataType("u8+{-1}")
    public int read() throws IOException {
        final int value = back;
        if (value != -1) {
            back = -1;
            return value;
        }
        return in.read();
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        assert length >= 0;
        if (length <= 0) {
            return 0;
        }
        if (back != -1) {
            array[offset] = (byte) back;
            back = -1;
            return 1;
        }
        return in.read(array, offset, length);
    }

    protected void teeToByteBuf(@NotNull IOConsumer<ByteBuf> consumer) throws IOException {
        final ByteBuf byteBuf = Unpooled.buffer();
        if (back != 0) {
            byteBuf.writeByte(back);
        }
        final InputStream originalIn = in;
        try (OutputStream os = new ByteBufOutputStream(byteBuf)) {
            in = new TeeInputStream(originalIn, os);
            consumer.accept(byteBuf);
        } finally {
            in = originalIn;
            byteBuf.release();
        }
    }
}
