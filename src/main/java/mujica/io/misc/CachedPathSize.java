package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CodeHistory(date = "2026/2/18")
public class CachedPathSize implements CachedDataSize<Path> {

    @NotNull
    private final Path path;

    private final int hash;

    private transient long value;

    public CachedPathSize(@NotNull Path path) throws IOException {
        super();
        this.path = path;
        this.hash = path.hashCode();
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
    public Path getTarget() {
        return path;
    }

    @NotNull
    @Override
    public CachedPathSize updateCache() throws IOException {
        value = Files.size(path);
        return this;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof CachedPathSize && this.path.equals(((CachedPathSize) obj).path);
    }
}
