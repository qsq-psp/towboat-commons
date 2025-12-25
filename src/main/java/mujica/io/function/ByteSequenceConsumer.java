package mujica.io.function;

import io.netty.buffer.ByteBuf;
import mujica.io.hash.ByteSequence;
import mujica.reflect.function.ByteConsumer;
import mujica.reflect.function.ByteSupplier;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/8")
public interface ByteSequenceConsumer extends ByteConsumer {

    @Override
    void accept(byte value);

    default void accept(@NotNull ByteSequence sequence) {
        final int length = sequence.byteLength();
        for (int index = 0; index < length; index++) {
            accept(sequence.getByte(index));
        }
    }

    default void accept(@NotNull ByteSupplier supplier, int count) {
        while (count-- > 0) {
            accept(supplier.getAsByte());
        }
    }

    default void accept(@NotNull byte[] array, @Index(of = "array") int offset, int length) {
        if (offset < 0 || length < 0) {
            throw new IndexOutOfBoundsException();
        }
        final int limit = offset + length;
        if (limit < 0 || limit > array.length) {
            throw new IndexOutOfBoundsException();
        }
        while (offset < limit) {
            accept(array[offset++]);
        }
    }

    default void accept(@NotNull byte[] array) {
        accept(array, 0, array.length);
    }

    default void accept(@NotNull ByteBuffer buffer) {
        while (buffer.hasRemaining()) {
            accept(buffer.get());
        }
    }

    default void accept(@NotNull ByteBuf buffer) {
        while (buffer.isReadable()) {
            accept(buffer.readByte());
        }
    }

    default void accept(@NotNull InputStream is) throws IOException {
        int value;
        while ((value = is.read()) != -1) {
            accept((byte) value);
        }
    }
}
