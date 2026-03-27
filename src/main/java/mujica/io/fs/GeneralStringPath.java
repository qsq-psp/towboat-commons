package mujica.io.fs;

import mujica.ds.HealthAware;
import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.format.TowboatCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
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
    public abstract GeneralFileSystem getFileSystem();

    protected void checkSegmentHealth(int currentSeparatorIndex, int nextSeparatorIndex) {
        if (currentSeparatorIndex == nextSeparatorIndex) {
            throw new InvariantException("empty segment");
        }
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        final String separator = getFileSystem().getSeparator();
        if (separator.isEmpty()) {
            throw new InvariantException("empty separator");
        }
        int currentSeparatorIndex, nextSeparatorIndex;
        if (string.startsWith(separator)) {
            currentSeparatorIndex = separator.length(); // the initial separator marking absolute is not counted
        } else {
            currentSeparatorIndex = 0;
        }
        while (true) {
            nextSeparatorIndex = string.indexOf(separator, currentSeparatorIndex);
            if (nextSeparatorIndex == -1) {
                break;
            }
            checkSegmentHealth(currentSeparatorIndex, nextSeparatorIndex);
            currentSeparatorIndex = nextSeparatorIndex + separator.length();
        }
        // string == "" or string == separator is not permitted
        checkSegmentHealth(currentSeparatorIndex, string.length());
        // string ending with separator is not permitted
    }

    @Override
    public boolean isAbsolute() {
        return string.startsWith(getFileSystem().getSeparator());
    }

    @Override
    public Path getRoot() {
        return getFileSystem().getPath(getFileSystem().getSeparator());
    }

    @Override
    public Path getFileName() {
        final String separator = getFileSystem().getSeparator();
        final int separatorIndex = string.lastIndexOf(separator);
        if (separatorIndex <= 0) { // separatorIndex == -1 for absolute and separatorIndex == 0 for relative
            return this;
        } else {
            return getFileSystem().getPath(string.substring(separatorIndex + separator.length()));
        }
    }

    @Nullable
    @Override
    public Path getParent() {
        final int separatorIndex = string.lastIndexOf(getFileSystem().getSeparator());
        if (separatorIndex <= 0) { // separatorIndex == -1 for absolute and separatorIndex == 0 for relative
            return null;
        } else {
            return getFileSystem().getPath(string.substring(0, separatorIndex));
        }
    }

    @Override
    public int getNameCount() {
        final String separator = getFileSystem().getSeparator();
        int currentSeparatorIndex, nextSeparatorIndex;
        if (string.startsWith(separator)) {
            currentSeparatorIndex = separator.length(); // the initial separator marking absolute is not counted
        } else {
            currentSeparatorIndex = 0;
        }
        int count = 1; // string == "" or string == separator is not permitted
        while (true) {
            nextSeparatorIndex = string.indexOf(separator, currentSeparatorIndex);
            if (nextSeparatorIndex == -1) {
                break;
            }
            currentSeparatorIndex = nextSeparatorIndex + separator.length();
            count++;
        }
        return count;
    }

    @NotNull
    @Override
    public Path getName(int segmentIndex) {
        if (segmentIndex < 0) {
            throw new IllegalArgumentException(); // not IndexOutOfBoundsException
        }
        final String separator = getFileSystem().getSeparator();
        int currentSeparatorIndex, nextSeparatorIndex;
        if (string.startsWith(separator)) {
            currentSeparatorIndex = separator.length(); // the initial separator marking absolute is not counted
        } else {
            currentSeparatorIndex = 0;
        }
        while (segmentIndex-- > 0) {
            nextSeparatorIndex = string.indexOf(separator, currentSeparatorIndex);
            if (nextSeparatorIndex == -1) {
                throw new IllegalArgumentException(); // not IndexOutOfBoundsException
            }
            currentSeparatorIndex = nextSeparatorIndex + separator.length();
        }
        nextSeparatorIndex = string.indexOf(separator, currentSeparatorIndex);
        if (nextSeparatorIndex == -1) {
            nextSeparatorIndex = string.length();
        }
        return getFileSystem().getPath(string.substring(currentSeparatorIndex, nextSeparatorIndex));
    }

    @NotNull
    @Override
    public Path subpath(int startSegmentIndex, int endSegmentIndex) {
        if (!(0 <= startSegmentIndex && startSegmentIndex <= endSegmentIndex)) {
            throw new IllegalArgumentException(); // not IndexOutOfBoundsException
        }
        if (startSegmentIndex == endSegmentIndex) {
            if (!(endSegmentIndex < getNameCount())) {
                throw new IllegalArgumentException(); // not IndexOutOfBoundsException
            }
            String currentDirectorySymbol = getFileSystem().getCurrentDirectorySymbol();
            if (string.equals(currentDirectorySymbol)) {
                return this;
            } else {
                return getFileSystem().getPath(currentDirectorySymbol);
            }
        }
        int startIndex = -1;
        final String separator = getFileSystem().getSeparator();
        int currentSeparatorIndex, nextSeparatorIndex;
        if (startSegmentIndex == 0) {
            startIndex = 0;
        }
        if (string.startsWith(separator)) {
            currentSeparatorIndex = separator.length(); // the initial separator marking absolute is not counted
        } else {
            currentSeparatorIndex = 0;
        }
        int index = 1; // string == "" or string == separator is not permitted
        while (true) {
            nextSeparatorIndex = string.indexOf(separator, currentSeparatorIndex);
            if (nextSeparatorIndex == -1) {
                if (endSegmentIndex == index) {
                    return getFileSystem().getPath(string.substring(startIndex));
                }
                break;
            }
            if (endSegmentIndex == index) {
                return getFileSystem().getPath(string.substring(startIndex, nextSeparatorIndex));
            }
            currentSeparatorIndex = nextSeparatorIndex + separator.length();
            if (startSegmentIndex == index) {
                startIndex = currentSeparatorIndex;
            } else
            index++;
        }
        throw new IllegalArgumentException(); // not IndexOutOfBoundsException
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
        final String currentDirectorySymbol = getFileSystem().getCurrentDirectorySymbol();
        if (other.isAbsolute() || this.string.equals(currentDirectorySymbol)) {
            return other;
        }
        if (this.getFileSystem() != other.getFileSystem()) {
            throw new IllegalArgumentException();
        }
        final String otherString = other.toString();
        if (otherString.equals(currentDirectorySymbol)) {
            return this;
        }
        return getFileSystem().getPath(this.string, otherString);
    }

    @NotNull
    @Override
    public Path relativize(@NotNull Path other) {
        final String separator = getFileSystem().getSeparator();
        final String currentDirectorySymbol = getFileSystem().getCurrentDirectorySymbol();
        if (this.string.startsWith(separator)) { // is absolute
            if (!other.isAbsolute()) {
                throw new IllegalArgumentException();
            }
            if (this.string.length() == separator.length() + currentDirectorySymbol.length() && this.string.endsWith(currentDirectorySymbol)) {
                return other;
            }
        } else {
            if (other.isAbsolute()) {
                throw new IllegalArgumentException();
            }
            if (this.string.equals(currentDirectorySymbol)) {
                return other;
            }
        }
        if (this.getFileSystem() != other.getFileSystem()) {
            throw new IllegalArgumentException();
        }
        final String otherString = other.toString();
        final int commonPrefixLength = TowboatCharSequence.commonPrefixLength(this.string, otherString);
        return this; // todo
    }

    @NotNull
    @Override
    public URI toUri() {
        try {
            return new URI(getFileSystem().getUriScheme(), null, string, null);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    @NotNull
    @Override
    public Path toAbsolutePath() {
        final String separator = getFileSystem().getSeparator();
        if (string.startsWith(separator)) {
            return this;
        } else {
            return getFileSystem().getPath(separator + string);
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
