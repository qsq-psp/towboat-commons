package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2022/9/25", project = "nettyon", name = "FileTimeCache")
@CodeHistory(date = "2026/1/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/17")
public class CachedFileLastModified implements CachedTime<File> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedFileLastModified.class);

    @NotNull
    private final File file;

    private final int hash;

    private long timeStamp;

    public CachedFileLastModified(@NotNull File file) {
        super();
        this.file = file;
        this.hash = file.hashCode();
        updateCache();
    }

    @NotNull
    @Override
    public File getTarget() {
        return file;
    }

    @NotNull
    @Override
    public CachedFileLastModified updateCache() {
        timeStamp = file.lastModified();
        if (timeStamp == 0L) {
            throw new RuntimeException();
        }
        return this;
    }

    @Override
    public long getLong() {
        return timeStamp;
    }

    @Override
    public void setLong(long newTimeStamp) {
        if (file.setLastModified(newTimeStamp)) {
            timeStamp = newTimeStamp;
        } else {
            LOGGER.warn("{} set last modified {}", file, newTimeStamp);
        }
    }

    @NotNull
    @Override
    public FileTime getFileTime() {
        return FileTime.fromMillis(timeStamp);
    }

    @Override
    public void setFileTime(@NotNull FileTime newTime) {
        setLong(newTime.toMillis());
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @NotNull
    @Override
    public String toString() {
        return "CachedFileLastModified[file = " + file + ", timeStamp = " + timeStamp + "]";
    }
}
