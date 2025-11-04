package mujica.io.buffer;

import mujica.io.codec.InputStreamUtil;
import mujica.reflect.function.IOConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * Created on 2025/3/3.
 */
@CodeHistory(date = "2025/3/3")
public final class ByteBufferUtil {

    @NotNull
    public static ByteBuffer read(@NotNull Consumer<ByteBuffer> consumer, int initialCapacity) {
        BufferOverflowException last = null;
        for (int capacity = initialCapacity; 0 < capacity && capacity <= InputStreamUtil.MAX_BUFFER_SIZE; capacity <<= 1) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(capacity);
                consumer.accept(buffer);
                return buffer;
            } catch (BufferOverflowException current) {
                last = current;
            }
        }
        if (last == null) {
            last = new BufferOverflowException();
        }
        throw last;
    }

    @NotNull
    public static ByteBuffer read(@NotNull IOConsumer<ByteBuffer> consumer, int initialCapacity) throws IOException {
        BufferOverflowException last = null;
        for (int capacity = initialCapacity; 0 < capacity && capacity <= InputStreamUtil.MAX_BUFFER_SIZE; capacity <<= 1) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(capacity);
                consumer.accept(buffer);
                return buffer;
            } catch (BufferOverflowException current) {
                last = current;
            }
        }
        if (last == null) {
            last = new BufferOverflowException();
        }
        throw last;
    }
    
    @NotNull
    public static ByteBuffer read(@NotNull InputStream is, int initialCapacity) throws IOException {
        byte[] oldArray = null;
        for (int capacity = initialCapacity; 0 < capacity && capacity <= InputStreamUtil.MAX_BUFFER_SIZE; capacity <<= 1) {
            byte[] newArray = new byte[capacity];
            int position;
            if (oldArray != null) {
                position = oldArray.length;
                System.arraycopy(oldArray, 0, newArray, 0, position);
            } else {
                position = 0;
            }
            while (position < capacity) {
                int count = is.read(newArray, position, capacity - position);
                if (count <= 0) {
                    return ByteBuffer.wrap(newArray, 0, position);
                }
                position += count;
            }
            assert position == capacity;
            oldArray = newArray;
        }
        throw new BufferOverflowException();
    }

    @NotNull
    public static ByteBuffer read(@NotNull Consumer<ByteBuffer> consumer) {
        return read(consumer, InputStreamUtil.BUFFER_SIZE);
    }

    @NotNull
    public static ByteBuffer read(@NotNull IOConsumer<ByteBuffer> consumer) throws IOException {
        return read(consumer, InputStreamUtil.BUFFER_SIZE);
    }

    @NotNull
    public static ByteBuffer read(@NotNull InputStream is) throws IOException {
        return read(is, InputStreamUtil.BUFFER_SIZE);
    }

    @ReferencePage(title = "JVMS12 Chapter 4. The class File Format", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
    @NotNull
    public static String readMUTF8(@NotNull ByteBuffer buffer) {
        final int length = 0xffff & buffer.getShort(); // read unsigned short as byte length
        final int oldLimit = buffer.limit();
        final int newLimit = buffer.position() + length;
        if (newLimit > oldLimit) {
            throw new BufferUnderflowException();
        }
        buffer.limit(newLimit);
        try {
            char[] chars = new char[length]; // estimated maximum size; the actual string may be smaller
            int charIndex = 0;
            while (buffer.hasRemaining()) {
                int b0 = buffer.get();
                if ((b0 & 0x80) == 0) {
                    chars[charIndex++] = (char) b0;
                } else if ((b0 & 0xe0) == 0xc0) {
                    int b1 = buffer.get();
                    chars[charIndex++] = (char) (((b0 & 0x1f) << 6) | (b1 & 0x3f));
                } else {
                    int b1 = buffer.get();
                    int b2 = buffer.get();
                    chars[charIndex++] = (char) (((b0 & 0x0f) << 12) | ((b1 & 0x3f) << 6) | (b2 & 0x3f));
                }
            }
            return new String(chars, 0, charIndex);
        } finally {
            buffer.limit(oldLimit);
        }
    }

    @ReferencePage(title = "JVMS12 Chapter 4. The class File Format", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
    public static void writeMUTF8(@NotNull String string, @NotNull ByteBuffer buffer) {
        final int position = buffer.position();
        try {
            buffer.putShort((short) 0); // reserve a slot for an unsigned short, fill it later
            int length = string.length();
            for (int index = 0; index < length; index++) {
                int ch = string.charAt(index);
                // ...
            }
        } catch (BufferOverflowException e) {
            buffer.position(position);
            throw e;
        }
    }

    /**
     * No instance
     */
    private ByteBufferUtil() {
        super();
    }
}
