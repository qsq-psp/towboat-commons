package mujica.io.misc;

import mujica.ds.of_long.LongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2026/1/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/15")
public interface CachedTime<T> extends LongSlot {

    @NotNull
    T getTarget();

    @NotNull
    CachedTime<T> updateCache();

    @Override
    long getLong();

    @Override
    void setLong(long newTimeStamp);

    @NotNull
    FileTime getFileTime();

    void setFileTime(@NotNull FileTime newTime);
}
