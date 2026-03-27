package mujica.io.fs;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;

/**
 * Created on 2026/3/26.
 */
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
    protected String getCurrentDirectorySymbol() {
        return ".";
    }

    @NotNull
    protected String getParentDirectorySymbol() {
        return "..";
    }
}
