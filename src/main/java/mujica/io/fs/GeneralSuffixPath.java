package mujica.io.fs;

import mujica.ds.HealthAware;
import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.*;
import java.util.function.Consumer;

@CodeHistory(date = "2026/4/11")
public abstract class GeneralSuffixPath<P extends GeneralSuffixPath<P>> implements HealthAware, Path, Serializable {

    @Nullable
    protected final P child;

    protected final String segment;

    protected final int hash;

    protected GeneralSuffixPath(@Nullable P child, @NotNull String segment) {
        super();
        this.child = child;
        this.segment = segment;
        this.hash = child == null ? segment.hashCode() : child.hash * 81 + segment.hashCode();
    }

    @NotNull
    @Override
    public abstract GeneralFileSystem getFileSystem();

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // Floyd loop algorithm
        GeneralSuffixPath<P> slow = this;
        GeneralSuffixPath<P> fast = this.child;
        while (slow != fast) {
            if (fast == null || fast.child == null) {
                return;
            }
            assert slow != null;
            slow = slow.child;
            fast = fast.child.child;
        }
        consumer.accept(new InvariantException("loop"));
    }

    @Override
    public boolean isAbsolute() {
        return segment.equals(getFileSystem().getRootSymbol());
    }

    @Nullable
    @Override
    public Path getRoot() {
        if (isAbsolute()) {
            return getFileSystem().getRoot();
        } else {
            return null;
        }
    }

    @Override
    @NotNull
    public GeneralSuffixPath<P> getFileName() {
        GeneralSuffixPath<P> node = this;
        while (node.child != null) {
            node = node.child;
        }
        return node;
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
