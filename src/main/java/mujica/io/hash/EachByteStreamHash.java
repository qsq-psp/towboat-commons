package mujica.io.hash;

import mujica.ds.i8.ReadOnlyI8Array;
import mujica.ds.i8.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/15")
public abstract class EachByteStreamHash implements ByteStreamHash {

    @NotNull
    @Override
    public abstract EachByteStreamHash clone();

    @Override
    public abstract void clear();

    @Override
    public abstract void start();

    @Override
    public abstract void update(byte input);

    @Override
    @NotNull
    public abstract DataView finish();

    @Override
    public void update(@NotNull ReadOnlyI8Array input) {
        final int byteLength = input.byteLength();
        for (int index = 0; index < byteLength; index++) {
            update(input.getByte(index));
        }
    }

    @Override
    public void update(@NotNull byte[] array, int offset, int length) {
        update(ReadOnlyI8Array.of(array, offset, length));
    }

    @Override
    public void update(@NotNull byte[] array) {
        update(ReadOnlyI8Array.of(array));
    }

    @Override
    public void update(@NotNull ByteBuffer input) {
        while (input.hasRemaining()) {
            update(input.get());
        }
    }

    @Override
    public void update(@NotNull DataView input) {
        update((ReadOnlyI8Array) input);
    }

    @NotNull
    @Override
    public DataView apply(@NotNull ReadOnlyI8Array input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull byte[] array, int offset, int length) {
        start();
        update(array, offset, length);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull byte[] array) {
        start();
        update(array);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull ByteBuffer input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull DataView input) {
        start();
        update(input);
        return finish();
    }
}
