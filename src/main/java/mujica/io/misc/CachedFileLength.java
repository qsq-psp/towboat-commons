package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@CodeHistory(date = "2026/2/19")
public class CachedFileLength implements CachedDataSize<File> {

    @NotNull
    private final File file;

    private final int hash;

    private transient long value;

    public CachedFileLength(@NotNull File file) {
        super();
        this.file = file;
        this.hash = file.hashCode();
        updateCache();
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setLong(long newValue) {
        value = newValue;
    }

    @NotNull
    @Override
    public File getTarget() {
        return file;
    }

    @NotNull
    @Override
    public CachedFileLength updateCache() {
        value = file.length();
        return this;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof CachedFileLength && this.file.equals(((CachedFileLength) obj).file);
    }
}
