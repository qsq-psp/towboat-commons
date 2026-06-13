package mujica.io.hash;

import mujica.ds.bit.list.BooleanSequence;
import mujica.ds.i8.list.ByteSequence;
import mujica.ds.i8.view.DataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/1/27", project = "OSHI", name = "BitsDigest")
@CodeHistory(date = "2025/4/11", name = "HashOfBits")
public interface BitsHash extends BytesHash {

    @NotNull
    DataView apply(@NotNull BooleanSequence input);

    @NotNull
    @Override
    DataView apply(@NotNull ByteSequence input);

    @NotNull
    @Override
    DataView apply(@NotNull DataView input);
}
