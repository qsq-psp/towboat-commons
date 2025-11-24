package mujica.io.nest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2025/11/8.
 */
public class GzipReader implements NestReader<NestResourceKey.Gzip, BasicFileAttributes> { // GzipFileAttributes

    private static final int FLAG_TEXT          = 0x01;
    private static final int FLAG_HEADER_CRC    = 0x02;
    private static final int FLAG_EXTRA         = 0x04;
    private static final int FLAG_NAME          = 0x08;
    private static final int FLAG_COMMENT       = 0x10;
    private static final int FLAG_ALL           = FLAG_TEXT | FLAG_HEADER_CRC | FLAG_EXTRA | FLAG_NAME | FLAG_COMMENT;

    @Override
    public void readAsStream(@NotNull InputStream is, @NotNull StreamCallback<NestResourceKey.Gzip, BasicFileAttributes> callback) throws IOException {
        readMagic(is);
        int flags = is.read();
        if ((flags & ~FLAG_ALL) != 0) { // EOF check included
            throw new IOException("reserved bits in flags = " + flags);
        }
        final FileTime time = readModificationTime(is);
        int extraFlags = is.read(); // EOF check later
        int operatingSystem = is.read(); // EOF check later
        if ((flags & FLAG_EXTRA) != 0) {
            int length = readUnsignedShort(is);
            if (length != 0) {
                BufferedLimitedInputStream lis = BufferedLimitedInputStream.createWithRemaining(is, length);
                callback.read(NestResourceKeys.GZIP_EXTRA, EmptyFileAttributes.INSTANCE, lis);
                lis.drain();
            }
        }
    }

    @Override
    public void readAttributes(@NotNull InputStream is, @NotNull AttributesCallback<NestResourceKey.Gzip, BasicFileAttributes> callback) throws IOException {
        readMagic(is);
    }

    private void readMagic(@NotNull InputStream is) throws IOException {
        final int id1 = is.read();
        if (id1 != 0x1f) { // EOF check included
            throw new IOException("ID1 = " + id1);
        }
        final int id2 = is.read();
        if (id2 != 0x8b) { // EOF check included
            throw new IOException("ID2 = " + id2);
        }
        final int cm = is.read();
        if (cm != 0x08) { // EOF check included
            throw new IOException("compression method = " + cm);
        }
    }

    @Nullable
    private FileTime readModificationTime(@NotNull InputStream is) throws IOException {
        long timeStamp = 0L;
        for (int shift = 0; shift < Integer.SIZE; shift += Byte.SIZE) {
            timeStamp |= is.read() << shift; // little (intel) endian
        }
        if (timeStamp == 0) {
            return null;
        }
        if (timeStamp < 0) { // timeStamp == -1, one of bytes read is EOF
            throw new IOException();
        }
        return FileTime.from(timeStamp, TimeUnit.SECONDS);
    }

    private int readUnsignedShort(@NotNull InputStream is) throws IOException {
        return 0;
    }
}
