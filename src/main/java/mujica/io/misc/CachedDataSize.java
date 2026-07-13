package mujica.io.misc;

import mujica.ds.i64.I64Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@CodeHistory(date = "2026/2/18")
@DirectSubclass({CachedFileLength.class, CachedPathSize.class, CachedFileStoreAttribute.class, CachedChannelSize.class})
public interface CachedDataSize<T> extends I64Slot {

    @NotNull
    T getTarget();

    @NotNull
    CachedDataSize<T> updateCache() throws IOException;

    @Override
    long getI64();

    @Override
    void setI64(long newValue);
}
