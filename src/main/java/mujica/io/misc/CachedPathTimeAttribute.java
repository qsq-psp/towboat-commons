package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2026/1/13", project = "Ultramarine")
@CodeHistory(date = "2026/3/19")
public class CachedPathTimeAttribute implements CachedTime<Path> {

    @NotNull
    protected final Path path;

    @NotNull
    protected final String attributeName;

    protected FileTime fileTime;

    public CachedPathTimeAttribute(@NotNull Path path, @NotNull String attributeName) {
        super();
        this.path = path;
        this.attributeName = attributeName;
        updateCache();
    }

    @NotNull
    public static CachedPathTimeAttribute creationTimeOf(@NotNull Path path) {
        return new CachedPathTimeAttribute(path, "basic:creationTime");
    }

    @NotNull
    public static CachedPathTimeAttribute lastModifiedTimeOf(@NotNull Path path) {
        return new CachedPathTimeAttribute(path, "basic:lastModifiedTime");
    }

    @NotNull
    public static CachedPathTimeAttribute lastAccessTimeOf(@NotNull Path path) {
        return new CachedPathTimeAttribute(path, "basic:lastAccessTime");
    }

    @Override
    @NotNull
    public Path getTarget() {
        return path;
    }

    @NotNull
    @Override
    public CachedPathTimeAttribute updateCache() {
        try {
            fileTime = (FileTime) Files.getAttribute(path, attributeName);
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
            Files.setAttribute(path, attributeName, newTime);
            fileTime = newTime;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @NotNull
    @Override
    public String toString() {
        return "CachedPathTimeAttribute[path = " + path + ", attributeName = " + attributeName + ", fileTime = " + fileTime + "]";
    }
}
