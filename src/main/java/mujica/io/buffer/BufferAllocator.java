package mujica.io.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created on 2025/3/4.
 * Heap, Direct, Mapped
 */
public interface BufferAllocator {

    ByteBuffer byteBuffer(int capacity);

    CharBuffer charBuffer(int capacity);
}
