package mujica.io.fs;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;

/**
 * Created in Ultramarine on 2023/11/9, named Zips.
 * Recreated on 2025/4/6.
 */
public final class ZipUtil {

    public static void copyAttributes(@NotNull ZipEntry src, @NotNull Path dst) throws IOException {
        Files.setAttribute(dst, "basic:lastModifiedTime", src.getLastModifiedTime());
        Files.setAttribute(dst, "basic:creationTime", src.getCreationTime());
        Files.setAttribute(dst, "basic:lastAccessTime", src.getLastAccessTime());
    }

    public static void copyAttributes(@NotNull BasicFileAttributes src, @NotNull ZipEntry dst) {
        dst.setLastModifiedTime(src.lastModifiedTime());
        dst.setCreationTime(src.creationTime());
        dst.setLastAccessTime(src.lastAccessTime());
    }

    public static void copyAttributes(@NotNull Path src, @NotNull ZipEntry dst) throws IOException {
        copyAttributes(Files.readAttributes(src, BasicFileAttributes.class), dst);
    }

    @NotNull
    public static ZipEntry createZipEntry(@NotNull Path path) throws IOException {
        final ZipEntry entry = new ZipEntry(path.toString());
        copyAttributes(path, entry);
        return entry;
    }
}
