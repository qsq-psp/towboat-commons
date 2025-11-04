package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;

@CodeHistory(date = "2025/4/26")
public final class ReaderUtil {

    public static final int BUFFER_SIZE = 0x800; // 2048 chars, 4KiB

    public static final int MAX_BUFFER_SIZE = 0x20000000; // 536870912 chars, 1GiB

    public static Reader buffered(@NotNull Reader reader) {
        if (reader instanceof BufferedReader || reader instanceof CharArrayReader) {
            return reader;
        }
        return new BufferedReader(reader, BUFFER_SIZE);
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

    private ReaderUtil() {
        super();
    }
}
