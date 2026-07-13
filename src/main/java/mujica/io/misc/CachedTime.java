package mujica.io.misc;

import mujica.ds.i64.I64Slot;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2026/1/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/15")
@DirectSubclass({CachedFileLastModified.class, CachedPathLastModified.class, CachedPathTimeAttribute.class, CachedZipEntryLastModified.class})
public interface CachedTime<T> extends I64Slot {

    @NotNull
    T getTarget();

    @NotNull
    CachedTime<T> updateCache();

    @Override
    long getI64();

    @Override
    void setI64(long newTimeStamp);

    @NotNull
    FileTime getFileTime();

    void setFileTime(@NotNull FileTime newTime);
}
