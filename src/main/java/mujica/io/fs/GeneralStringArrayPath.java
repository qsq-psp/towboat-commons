package mujica.io.fs;

import mujica.ds.HealthAware;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.*;
import java.util.function.Consumer;

@CodeHistory(date = "2026/3/27")
public abstract class GeneralStringArrayPath implements HealthAware, Path, Serializable {

    @NotNull
    protected final String[] segments;

    protected GeneralStringArrayPath(@NotNull String[] segments) {
        super();
        this.segments = segments;
    }

    @NotNull
    @Override
    public abstract GeneralFileSystem getFileSystem();

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (segments.length == 0) {
            consumer.accept(new RuntimeException("no segment"));
        }
        for (String segment : segments) {
            if (segment == null) {
                consumer.accept(new RuntimeException("null segment"));
            }
        }
    }

    @Override
    public boolean isAbsolute() {
        return getFileSystem().getRootSymbol().equals(segments[0]);
    }

    @Override
    public Path getRoot() {
        if (isAbsolute()) {
            return getFileSystem().getRoot();
        } else {
            return null;
        }
    }

    @Override
    public Path getFileName() {
        final int index = segments.length - 1;
        if (index == 0) {
            return this;
        }
        return getFileSystem().getPath(segments[index]);
    }

    @Override
    public Path getParent() {
        return null;
    }

    @Override
    public int getNameCount() {
        return 0;
    }

    @NotNull
    @Override
    public Path getName(int index) {
        return null;
    }

    @NotNull
    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public boolean startsWith(@NotNull Path other) {
        return false;
    }

    @Override
    public boolean endsWith(@NotNull Path other) {
        return false;
    }

    @Override
    public Path normalize() {
        return null;
    }

    @NotNull
    @Override
    public Path resolve(@NotNull Path other) {
        return null;
    }

    @NotNull
    @Override
    public Path relativize(@NotNull Path other) {
        return null;
    }

    @NotNull
    @Override
    public URI toUri() {
        return null;
    }

    @NotNull
    @Override
    public Path toAbsolutePath() {
        return null;
    }

    @NotNull
    @Override
    public Path toRealPath(@NotNull LinkOption... options) throws IOException {
        return null;
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return null;
    }

    @Override
    public int compareTo(Path other) {
        return 0;
    }
}
