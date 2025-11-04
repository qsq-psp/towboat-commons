package mujica.io.codec;

import io.netty.buffer.ByteBufInputStream;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@CodeHistory(date = "2025/4/29")
public final class InputStreamUtil {

    public static final int BUFFER_SIZE = 0x1000; // 4096 bytes, 4KiB

    public static final int MAX_BUFFER_SIZE = 0x40000000; // 1073741824 bytes, 1GiB

    public static InputStream buffered(@NotNull InputStream is) {
        if (is instanceof BufferedInputStream || is instanceof ByteArrayInputStream || is instanceof ByteBufInputStream) {
            return is;
        }
        return new BufferedInputStream(is, BUFFER_SIZE);
    }
}
