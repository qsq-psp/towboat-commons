package mujica.io.nest;

import mujica.ds.HealthAware;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.*;
import java.util.function.Consumer;

@CodeHistory(date = "2025/10/15")
public abstract class GeneralPrefixPath<P extends GeneralPrefixPath<P>> implements HealthAware, Path, Serializable {

    private static final long serialVersionUID = 0x9e00691fa3997846L;

    @Nullable
    protected final P parent;

    @NotNull
    protected final String segment;

    protected GeneralPrefixPath(@Nullable P parent, @NotNull String segment) {
        super();
        this.parent = parent;
        this.segment = segment;
    }

    @NotNull
    @Override
    public abstract FileSystem getFileSystem();

    @NotNull
    protected String getRootString() {
        return ":";
    }

    @NotNull
    protected String getSeparator() {
        return getFileSystem().getSeparator();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {

    }

    @Override
    public boolean isAbsolute() {
        if (parent != null) {
            return parent.isAbsolute();
        } else {
            return segment.equals(getRootString());
        }
    }

    @Override
    public Path getRoot() {
        return null;
    }

    @Override
    public Path getFileName() {
        return getFileSystem().getPath(segment);
    }

    @Override
    @Nullable
    public P getParent() {
        return parent;
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
