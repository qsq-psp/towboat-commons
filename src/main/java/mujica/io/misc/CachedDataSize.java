package mujica.io.misc;

import mujica.ds.of_long.LongSlot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@CodeHistory(date = "2026/2/18")
@DirectSubclass({CachedFileLength.class, CachedPathSize.class, CachedFileStoreAttribute.class, CachedChannelSize.class})
public interface CachedDataSize<T> extends LongSlot {

    @NotNull
    T getTarget();

    @NotNull
    CachedDataSize<T> updateCache() throws IOException;

    @Override
    long getLong();

    @Override
    void setLong(long newValue);
}
