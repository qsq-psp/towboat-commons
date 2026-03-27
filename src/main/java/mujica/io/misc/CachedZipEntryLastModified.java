package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;

@CodeHistory(date = "2026/1/13", project = "Ultramarine")
@CodeHistory(date = "2026/3/20")
public class CachedZipEntryLastModified implements CachedTime<ZipEntry> {

    @NotNull
    protected final ZipEntry zipEntry;

    protected FileTime fileTime;

    public CachedZipEntryLastModified(@NotNull ZipEntry zipEntry) {
        super();
        this.zipEntry = zipEntry;
    }

    @NotNull
    @Override
    public ZipEntry getTarget() {
        return zipEntry;
    }

    @NotNull
    @Override
    public CachedTime<ZipEntry> updateCache() {
        fileTime = zipEntry.getLastModifiedTime();
        return this;
    }

    @Override
    public long getLong() {
        return fileTime.toMillis();
    }

    @Override
    public void setLong(long newTimeStamp) {
        setFileTime(FileTime.fromMillis(newTimeStamp));
    }

    @NotNull
    @Override
    public FileTime getFileTime() {
        return fileTime;
    }

    @Override
    public void setFileTime(@NotNull FileTime newTime) {
        fileTime = zipEntry.setLastModifiedTime(newTime).getLastModifiedTime();
    }

    @NotNull
    @Override
    public String toString() {
        return "CachedZipEntryLastModified[zipEntry = " + zipEntry + ", fileTime = " + fileTime + "]";
    }
}
