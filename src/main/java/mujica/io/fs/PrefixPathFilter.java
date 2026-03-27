package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.DirectoryStream;
import java.nio.file.Path;

@CodeHistory(date = "2026/3/5")
public class PrefixPathFilter implements DirectoryStream.Filter<Path> {

    @Nullable
    final PrefixPathFilter next;

    @NotNull
    final Path prefix;

    final boolean positive;

    public PrefixPathFilter(@Nullable PrefixPathFilter next, @NotNull Path prefix, boolean positive) {
        super();
        this.next = next;
        this.prefix = prefix;
        this.positive = positive;
    }

    @NotNull
    public PrefixPathFilter include(@NotNull Path path) {
        return new PrefixPathFilter(this, path, true);
    }

    @NotNull
    public PrefixPathFilter exclude(@NotNull Path path) {
        return new PrefixPathFilter(this, path, false);
    }

    @Override
    public boolean accept(@NotNull Path entry) {
        if (entry.startsWith(prefix)) {
            return positive;
        }
        if (next != null) {
            return next.accept(entry);
        } else {
            return !positive;
        }
    }
}
