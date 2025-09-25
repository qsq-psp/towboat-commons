package mujica.io.hash;

import mujica.ds.of_boolean.BitSequence;
import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@CodeHistory(date = "2025/4/13")
public abstract class EachBitStreamHash implements BitStreamHash {

    @NotNull
    public abstract ByteOrder bitOrder();

    @NotNull
    @Override
    public abstract EachBitStreamHash clone();

    @Override
    public abstract void clear();

    @Override
    public abstract void start();

    @Override
    public abstract void update(boolean input);

    @Override
    @NotNull
    public abstract DataView finish();

    @Override
    public void update(@NotNull BitSequence input) {
        final int bitLength = input.bitLength();
        for (int index = 0; index < bitLength; index++) {
            update(input.getBit(index));
        }
    }

    @Override
    public void update(byte input) {
        if (bitOrder() == ByteOrder.LITTLE_ENDIAN) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                update((input & (1 << shift)) != 0);
            }
        } else {
            for (int shift = Byte.SIZE - 1; shift >= 0; shift--) {
                update((input & (1 << shift)) != 0);
            }
        }
    }

    @Override
    public void update(@NotNull ByteSequence input) {
        final int byteLength = input.byteLength();
        for (int index = 0; index < byteLength; index++) {
            update(input.getByte(index));
        }
    }

    @Override
    public void update(@NotNull byte[] array, int offset, int length) {
        update(ByteSequence.of(array, offset, length));
    }

    @Override
    public void update(@NotNull byte[] array) {
        update(ByteSequence.of(array));
    }

    @Override
    public void update(@NotNull ByteBuffer input) {
        while (input.hasRemaining()) {
            update(input.get());
        }
    }

    @Override
    public void update(@NotNull DataView input) {
        update((BitSequence) input);
    }

    @NotNull
    @Override
    public DataView apply(@NotNull BitSequence input) {
        start();
        update(input);
        return finish();
    }

    @NotNull
    @Override
    public DataView apply(@NotNull ByteSequence input) {
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
