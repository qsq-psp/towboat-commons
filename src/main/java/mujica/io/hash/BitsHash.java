package mujica.io.hash;

import mujica.ds.of_boolean.BitSequence;
import mujica.io.view.ByteSequence;
import mujica.io.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/4/11", name = "HashOfBits")
public interface BitsHash extends BytesHash {

    @NotNull
    DataView apply(@NotNull BitSequence input);

    @NotNull
    @Override
    DataView apply(@NotNull ByteSequence input);

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
