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
                buffer.flip();
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
                buffer.flip();
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

    /**
     * No instance
     */
    private ByteBufferUtil() {
        super();
    }
}
