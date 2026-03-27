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

@CodeHistory(date = "2025/10/15")
public abstract class GeneralPrefixPath<P extends GeneralPrefixPath<P>> implements HealthAware, Path, Serializable {

    private static final long serialVersionUID = 0x9e00691fa3997846L;

    @Nullable
    protected final P parent; // the prefix

    @NotNull
    protected final String segment;

    protected final int hash;

    protected GeneralPrefixPath(@Nullable P parent, @NotNull String segment) {
        super();
        this.parent = parent;
        this.segment = segment;
        this.hash = parent == null ? segment.hashCode() : parent.hash * 79 + segment.hashCode();
    }

    @NotNull
    @Override
    public abstract GeneralFileSystem getFileSystem();

    @NotNull
    protected String getSeparator() {
        return getFileSystem().getSeparator();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // Floyd loop algorithm
        GeneralPrefixPath<P> slow = this;
        GeneralPrefixPath<P> fast = this.parent;
        while (slow != fast) {
            if (fast == null || fast.parent == null) {
                return;
            }
            assert slow != null;
            slow = slow.parent;
            fast = fast.parent.parent;
        }
        consumer.accept(new InvariantException("loop"));
    }

    @Override
    public boolean isAbsolute() {
        if (parent != null) {
            return parent.isAbsolute();
        } else {
            return segment.equals(getFileSystem().getRootSymbol());
        }
    }

    @Override
    public Path getRoot() {
        GeneralPrefixPath<P> node = this;
        while (node.parent != null) {
            node = node.parent;
        }
        if (node.segment.equals(getFileSystem().getRootSymbol())) {
            return node;
        } else {
            return null;
        }
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
        int count = 0;
        GeneralPrefixPath<P> node = this;
        do {
            node = node.parent;
            count++;
        } while (node != null);
        return count;
    }

    @NotNull
    @Override
    public Path getName(int index) {
        return null;
    }

    @NotNull
    @Override
    public Path subpath(int startIndex, int endIndex) {
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

    @Override
    public int hashCode() {
        return hash;
    }

    private boolean equals(@NotNull GeneralPrefixPath<?> a, @NotNull GeneralPrefixPath<?> b) {
        if (a == b) {
            return true;
        }
        while (true) {
            if (!a.segment.equals(b.segment)) {
                return false;
            }
            a = a.parent;
            b = b.parent;
            if (a == b) {
                return true;
            }
            if (a == null || b == null) {
                return false;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GeneralPrefixPath && equals(this, (GeneralPrefixPath<?>) obj);
    }

    protected void append(@NotNull String separator, @NotNull StringBuilder out) {
        if (parent != null) {
            parent.append(separator, out);
            out.append(separator);
        }
        out.append(segment);
    }

    @NotNull
    @Override
    public String toString() {
        if (parent == null) {
            return segment;
        }
        final StringBuilder sb = new StringBuilder();
        append(getFileSystem().getSeparator(), sb);
        return sb.toString();
    }
}
