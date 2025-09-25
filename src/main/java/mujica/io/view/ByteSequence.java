package mujica.io.view;

import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Created on 2025/4/16.
 * A simple data read interface like {@link CharSequence}, to be wrapped by {@link DataView}
 */
public interface ByteSequence {

    int byteLength();

    byte getByte(int index);

    ByteSequence EMPTY = new ByteSequence() {
        @Override
        public int byteLength() {
            return 0;
        }
        @Override
        public byte getByte(int index) {
            throw new IndexOutOfBoundsException();
        }
    };

    @NotNull
    static ByteSequence of(@NotNull byte[] array) {
        if (array.length == 0) {
            return EMPTY;
        }
        return new ByteSequence() {
            @Override
            public int byteLength() {
                return array.length;
            }
            @Override
            public byte getByte(int index) {
                return array[index];
            }
        };
    }

    @NotNull
    static ByteSequence of(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        if (offset < 0 || length < 0 || (offset + length) > array.length) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return EMPTY;
        }
        return new ByteSequence() {
            @Override
            public int byteLength() {
                return length;
            }
            @Override
            public byte getByte(int index) {
                if (index < 0 || index >= length) {
                    throw new IndexOutOfBoundsException();
                }
                return array[offset + index];
            }
        };
    }

    @NotNull
    static ByteSequence absolute(@NotNull ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return of(buffer.array(), buffer.arrayOffset(), buffer.limit());
        } else {
            return new ByteSequence() {
                @Override
                public int byteLength() {
                    return buffer.limit();
                }

                @Override
                public byte getByte(int index) {
                    return buffer.get(index);
                }
            };
        }
    }

    @NotNull
    static ByteSequence relative(@NotNull ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return of(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
        } else {
            return new ByteSequence() {
                @Override
                public int byteLength() {
                    return buffer.remaining();
                }

                @Override
                public byte getByte(int index) {
                    return buffer.get(buffer.position() + index);
                }
            };
        }
    }
}
