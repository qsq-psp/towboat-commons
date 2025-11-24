package mujica.io.nest;

import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2025/10/10.
 */
public class EmptyFileAttributes implements BasicFileAttributes {

    public static final FileTime EPOCH = FileTime.from(0L, TimeUnit.SECONDS);

    public static final EmptyFileAttributes INSTANCE = new EmptyFileAttributes();

    public EmptyFileAttributes() {
        super();
    }

    @NotNull
    @Override
    public FileTime lastModifiedTime() {
        return EPOCH;
    }

    @NotNull
    @Override
    public FileTime lastAccessTime() {
        return EPOCH;
    }

    @NotNull
    @Override
    public FileTime creationTime() {
        return EPOCH;
    }

    @Override
    public boolean isRegularFile() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isSymbolicLink() {
        return false;
    }

    @Override
    public boolean isOther() {
        return false;
    }

    @Override
    public long size() {
        return 0L;
    }

    @Override
    public Object fileKey() {
        return null;
    }
}
