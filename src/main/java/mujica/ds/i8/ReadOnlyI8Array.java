package mujica.ds.i8;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/16", name = "ReadOnlyI8Array")
@CodeHistory(date = "2026/7/5")
public interface ReadOnlyI8Array {
    
    int byteLength();

    byte getByte(int index);

    ReadOnlyI8Array EMPTY = new ReadOnlyI8Array() {
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
    static ReadOnlyI8Array of(@NotNull byte[] array) {
        if (array.length == 0) {
            return EMPTY;
        }
        return new ReadOnlyI8Array() {
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
    static ReadOnlyI8Array of(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        if (offset < 0 || length < 0 || (offset + length) > array.length) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return EMPTY;
        }
        return new ReadOnlyI8Array() {
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
    static ReadOnlyI8Array absolute(@NotNull ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return of(buffer.array(), buffer.arrayOffset(), buffer.limit());
        } else {
            return new ReadOnlyI8Array() {
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
    static ReadOnlyI8Array relative(@NotNull ByteBuffer buffer) {
        if (buffer.hasArray()) {
            return of(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
        } else {
            return new ReadOnlyI8Array() {
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
