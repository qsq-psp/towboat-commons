package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.nio.file.attribute.FileTime;

@CodeHistory(date = "2025/11/11")
public class GzipFileAttributes extends EmptyFileAttributes {

    @Nullable
    final FileTime lastModifiedTime;

    final int operatingSystem;

    final boolean isText;

    public GzipFileAttributes(@Nullable FileTime lastModifiedTime, int operatingSystem, boolean isText) {
        super();
        this.lastModifiedTime = lastModifiedTime;
        this.operatingSystem = operatingSystem;
        this.isText = isText;
    }
}
