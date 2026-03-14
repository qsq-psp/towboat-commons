package mujica.io.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@CodeHistory(date = "2025/4/29")
public final class InputStreamUtil { // rename to OIO

    public static final int BUFFER_SIZE = 0x1000; // 4096 bytes, 4KiB

    public static final int MAX_BUFFER_SIZE = 0x40000000; // 1073741824 bytes, 1GiB

    @NotNull
    public static InputStream buffer(@NotNull InputStream is) {
        if (is instanceof BufferedInputStream || is instanceof ByteArrayInputStream || is instanceof ByteBufInputStream) {
            return is;
        }
        return new BufferedInputStream(is, BUFFER_SIZE);
    }

    public static Object copy(Object obj) throws Exception {
        final ByteBuf buf = Unpooled.buffer();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteBufOutputStream(buf));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(buf));
            return ois.readObject();
        } finally {
            buf.release();
        }
    }
}
