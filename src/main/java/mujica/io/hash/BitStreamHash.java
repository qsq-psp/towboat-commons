package mujica.io.hash;

import mujica.ds.bit.ReadOnlyBitArray;
import mujica.ds.i8.ReadOnlyI8Array;
import mujica.ds.i8.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/11")
public interface BitStreamHash extends ByteStreamHash, BitsHash {

    @Override
    void clear();

    @Override
    void start();

    void update(boolean input);

    void update(@NotNull ReadOnlyBitArray input);

    @Override
    void update(byte input);

    @Override
    void update(@NotNull ReadOnlyI8Array input);

    @Override
    void update(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    @Override
    void update(@NotNull byte[] array);

    @Override
    void update(@NotNull ByteBuffer input);

    @Override
    void update(@NotNull DataView input);

    @Override
    @NotNull
    DataView finish();

    @NotNull
    @Override
    DataView apply(@NotNull ReadOnlyBitArray input);

    @NotNull
    @Override
    DataView apply(@NotNull ReadOnlyI8Array input);

    @NotNull
    @Override
    DataView apply(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    @NotNull
    @Override
    DataView apply(@NotNull byte[] array);

    @NotNull
    @Override
    DataView apply(@NotNull ByteBuffer input);

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
