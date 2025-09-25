package mujica.io.hash;

import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2025/4/14", name = "HashOfBytes")
public interface BytesHash extends Hash {

    @NotNull
    DataView apply(@NotNull ByteSequence input);

    DataView apply(@NotNull byte[] array, @Index(of = "array") int offset, int length);

    DataView apply(@NotNull byte[] array);

    @NotNull
    DataView apply(@NotNull ByteBuffer input);

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
