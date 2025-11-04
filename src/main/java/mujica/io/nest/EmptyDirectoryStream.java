package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created in Ultramarine on 2022/12/15.
 * Recreated on 2025/3/3.
 */
@CodeHistory(date = "2022/12/15", project = "Ultramarine")
@CodeHistory(date = "2025/3/3")
public class EmptyDirectoryStream implements DirectoryStream<Path>, Iterator<Path> {

    static final EmptyDirectoryStream INSTANCE = new EmptyDirectoryStream();

    @Override
    @NotNull
    public Iterator<Path> iterator() {
        return this;
    }

    @Override
    public void close() {
        // pass
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Path next() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Path> action) {
        // pass
    }

    @Override
    public void forEachRemaining(Consumer<? super Path> action) {
        // pass
    }
}
