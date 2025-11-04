package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.Nullable;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Created on 2025/10/10.
 */
@CodeHistory(date = "2025/10/10")
public class EmptyFileAttributeView implements BasicFileAttributeView {

    @Override
    public String name() {
        return "empty";
    }

    @Override
    public BasicFileAttributes readAttributes() {
        return EmptyFileAttributes.INSTANCE;
    }

    @Override
    public void setTimes(@Nullable FileTime lastModifiedTime, @Nullable FileTime lastAccessTime, @Nullable FileTime createTime) {
        // It may or not fail by throwing an IOException
    }
}
