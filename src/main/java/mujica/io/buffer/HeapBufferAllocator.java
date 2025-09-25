package mujica.io.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created on 2025/3/14.
 */
public class HeapBufferAllocator implements BufferAllocator {

    @Override
    public ByteBuffer byteBuffer(int capacity) {
        return ByteBuffer.allocate(capacity);
    }

    @Override
    public CharBuffer charBuffer(int capacity) {
        return CharBuffer.allocate(capacity);
    }
}
