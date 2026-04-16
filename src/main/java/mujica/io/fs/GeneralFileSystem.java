package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Created on 2026/3/26.
 */
@CodeHistory(date = "2026/3/26")
public abstract class GeneralFileSystem extends FileSystem {

    @NotNull
    protected String getUriScheme() {
        return "file";
    }

    @NotNull
    protected String getRootSymbol() {
        return ":";
    }

    @NotNull
    protected Path getRoot() {
        return getPath(getRootSymbol());
    }

    @NotNull
    protected String getCurrentDirectorySymbol() {
        return ".";
    }

    @NotNull
    protected Path getCurrentDirectory() {
        return getPath(getCurrentDirectorySymbol());
    }

    @NotNull
    protected String getParentDirectorySymbol() {
        return "..";
    }

    @NotNull
    protected Path getParentDirectory() {
        return getPath(getParentDirectorySymbol());
    }
}
