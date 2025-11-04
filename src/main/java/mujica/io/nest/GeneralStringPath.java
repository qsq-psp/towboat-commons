package mujica.io.nest;

import mujica.ds.HealthAware;
import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.nio.file.*;
import java.util.function.Consumer;

@CodeHistory(date = "2022/12/12", project = "Ultramarine", name = "GeneralTextPath")
@CodeHistory(date = "2025/10/13")
public abstract class GeneralStringPath implements HealthAware, Path, Serializable {

    private static final long serialVersionUID = 0x2226edfcde046b6fL;

    @NotNull
    protected final String string;

    protected GeneralStringPath(@NotNull String string) {
        super();
        this.string = string;
    }

    @NotNull
    @Override
    public abstract FileSystem getFileSystem();

    @NotNull
    protected String getSeparator() {
        return getFileSystem().getSeparator();
    }

    @NotNull
    protected String getCurrentDirectorySymbol() {
        return ".";
    }

    @NotNull
    protected String getParentDirectorySymbol() {
        return "..";
    }

    protected void checkSegmentHealth(int start, int end) {
        if (start == end) {
            throw new InvariantException("empty segment");
        }
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final int len = string.length();
        final String sep = getSeparator();
        if (sep.isEmpty()) {
            throw new InvariantException("empty separator");
        }
        int start = 0;
        while (true) {
            int end = string.indexOf(sep, start);
            if (end == -1) {
                break;
            }
            if (end != 0) {
                checkSegmentHealth(start, end);
            }
            start = end + sep.length();
            if (start == len) {
                throw new InvariantException("separator at end");
            }
        }
    }

    @Override
    public boolean isAbsolute() {
        return string.startsWith(getSeparator());
    }

    @Override
    public Path getRoot() {
        return getFileSystem().getPath(getSeparator());
    }

    @Override
    public Path getFileName() {
        final String sep = getSeparator();
        final int div = string.lastIndexOf(sep);
        if (div <= 0) {
            return this;
        } else {
            return getFileSystem().getPath(string.substring(div + sep.length()));
        }
    }

    @Nullable
    @Override
    public Path getParent() {
        final int div = string.lastIndexOf(getSeparator());
        if (div <= 0) {
            return null;
        } else {
            return getFileSystem().getPath(string.substring(0, div));
        }
    }

    @Override
    public int getNameCount() {
        final String sep = getSeparator();
        int count = 0;
        int start = 0;
        while (true) {
            int end = string.indexOf(sep, start);
            if (end == -1) {
                return count;
            }
            if (end != 0) {
                count++;
            }
            start = end + sep.length();
        }
    }

    @NotNull
    @Override
    public Path getName(int index) {
        final String sep = getSeparator();
        int start = 0;
        while (true) {
            int end = string.indexOf(sep, start);
            if (end != 0 && index-- == 0) {
                if (end == -1) {
                    end = string.length();
                }
                return getFileSystem().getPath(string.substring(start, end));
            }
            if (end == -1) {
                throw new IndexOutOfBoundsException();
            }
            start = end + sep.length();
        }
    }

    @NotNull
    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public boolean startsWith(@NotNull Path other) {
        return string.startsWith(other.toString());
    }

    @Override
    public boolean endsWith(@NotNull Path other) {
        return string.endsWith(other.toString());
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
        final String sep = getSeparator();
        if (string.startsWith(sep)) {
            return this;
        } else {
            return getFileSystem().getPath(sep + string);
        }
    }

    @NotNull
    @Override
    public Path toRealPath(@NotNull LinkOption... options) {
        return this;
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) {
        return null;
    }

    @Override
    public int compareTo(@NotNull Path other) {
        return string.compareTo(other.toString());
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GeneralStringPath && this.string.equals(((GeneralStringPath) obj).string);
    }

    @NotNull
    @Override
    public String toString() {
        return string;
    }
}
