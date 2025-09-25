package mujica.io.hash;

import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Created on 2025/4/9.
 */
public interface ByteStreamHash extends StreamHash, BytesHash {

    @Override
    void clear();

    @Override
    void start();

    void update(byte input);

    void update(@NotNull ByteSequence input);

    void update(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    void update(@NotNull byte[] array);

    void update(@NotNull ByteBuffer input);

    void update(@NotNull DataView input);

    @Override
    @NotNull
    DataView finish();

    @Override
    @NotNull
    DataView apply(@NotNull ByteSequence input);

    @Override
    DataView apply(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    @Override
    DataView apply(@NotNull byte[] array);

    @Override
    @NotNull
    DataView apply(@NotNull ByteBuffer input);

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
