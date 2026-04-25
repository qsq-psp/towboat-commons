package mujica.io.stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@CodeHistory(date = "2025/4/29", name = "InputStreamUtil")
@CodeHistory(date = "2026/3/27")
public final class OIO {

    public static final int BUFFER_SIZE = 0x1000; // 4096 bytes, 4KiB

    public static final int MAX_BUFFER_SIZE = 0x40000000; // 1073741824 bytes, 1GiB

    public static Object copy(Object obj) throws Exception {
        final ByteBuf buf = Unpooled.buffer();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteBufOutputStream(buf)); // use ByteArrayOutputStream
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(buf));
            return ois.readObject();
        } finally {
            buf.release();
        }
    }

    @NotNull
    public static InputStream buffered(@NotNull InputStream is) {
        if (is instanceof BufferedInputStream || is instanceof ByteArrayInputStream || is instanceof ByteBufInputStream) {
            return is;
        }
        return new BufferedInputStream(is, BUFFER_SIZE);
    }

    @NotNull
    public static OutputStream buffered(@NotNull OutputStream os) {
        if (os instanceof BufferedOutputStream || os instanceof ByteArrayOutputStream || os instanceof ByteBufOutputStream) {
            return os;
        }
        return new BufferedOutputStream(os, BUFFER_SIZE);
    }

    @NotNull
    public static Reader buffered(@NotNull Reader reader) {
        if (reader instanceof BufferedReader || reader instanceof CharArrayReader) {
            return reader;
        }
        return new BufferedReader(reader, BUFFER_SIZE);
    }

    public static Writer buffered(@NotNull Writer writer) {
        if (writer instanceof BufferedWriter || writer instanceof CharArrayWriter) {
            return writer;
        }
        return new BufferedWriter(writer, BUFFER_SIZE);
    }

    @NotNull
    public static char[] readAllChars(@NotNull Reader reader) throws IOException {
        return readNChars(reader, Integer.MAX_VALUE);
    }

    @NotNull
    public static char[] readNChars(@NotNull Reader reader, int maxLength) throws IOException {
        return new char[0];
    }

    public static int readNChars(@NotNull Reader reader, @NotNull char[] array, int offset, int length) throws IOException {
        int count = 0;
        while (count < length) {
            int delta = reader.read(array, offset + count, length - count);
            if (delta <= 0) {
                break;
            }
            count += delta;
        }
        return count;
    }

    public static void skipNChars(@NotNull Reader reader, long count) throws IOException {
        if (count <= 0L) {
            return;
        }
        final long actual = reader.skip(count);
        if (actual < count) {
            count -= actual;
            while (count > 0 && reader.read() != -1) {
                count--;
            }
            if (count != 0) {
                throw new EOFException();
            }
        } else if (actual > count) {
            throw new IOException("Unable to skip exactly");
        }
    }
}
