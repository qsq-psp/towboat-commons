package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2022/9/25", project = "nettyon", name = "PathTimeCache")
@CodeHistory(date = "2026/1/12", project = "Ultramarine")
@CodeHistory(date = "2026/3/18")
public class CachedPathLastModified implements CachedTime<Path> {

    @NotNull
    private final Path path;

    private final int hash;

    protected FileTime fileTime;

    public CachedPathLastModified(@NotNull Path path) {
        super();
        this.path = path;
        this.hash = path.hashCode();
        updateCache();
    }

    @NotNull
    @Override
    public Path getTarget() {
        return path;
    }

    @NotNull
    @Override
    public CachedPathLastModified updateCache() {
        try {
            fileTime = Files.getLastModifiedTime(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
        try {
            Files.setLastModifiedTime(path, newTime);
            fileTime = newTime;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @NotNull
    @Override
    public String toString() {
        return "CachedPathLastModified[path = " + path + ", fileTime = " + fileTime + "]";
    }
}
