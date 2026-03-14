package mujica.io.fs;

import mujica.io.function.IOConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@CodeHistory(date = "2023/3/15", project = "Ultramarine")
@CodeHistory(date = "2024/1/23", project = "UltraIO")
@CodeHistory(date = "2026/2/28")
public final class Directories {

    private static final Logger LOGGER = LoggerFactory.getLogger(Directories.class);

    public static boolean isEmpty(@NotNull Path path) throws IOException {
        return isEmptyDirectory(path) || isEmptyRegularFile(path);
    }

    public static boolean isEmptyRegularFile(@NotNull Path path) throws IOException {
        return Files.isRegularFile(path) && Files.size(path) == 0;
    }

    public static boolean isEmptyDirectory(@NotNull Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            return false;
        }
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            return !directoryStream.iterator().hasNext();
        }
    }

    public static boolean containsRegularFile(@NotNull Path path) throws IOException {
        return Files.walk(path).anyMatch(path1 -> Files.isRegularFile(path1));
    }

    public static boolean containsNonEmptyRegularFile(@NotNull Path path) throws IOException {
        return Files.walk(path).anyMatch(containedPath -> {
            try {
                return Files.isRegularFile(containedPath) && Files.size(containedPath) > 0L;
            } catch (IOException e) {
                LOGGER.error("{}", containedPath, e);
                return false;
            }
        });
    }

    private static long uncheckedSize(@NotNull Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static long totalSize(@NotNull Path path) throws IOException {
        try {
            return Files.walk(path).filter(Files::isRegularFile).mapToLong(Directories::uncheckedSize).sum();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    @NotNull
    private static FileTime uncheckedModifiedTime(@NotNull Path path) {
        try {
            return Files.getLastModifiedTime(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @NotNull
    public static Optional<FileTime> latestModifiedTime(@NotNull Path path) throws IOException {
        try {
            return Files.walk(path).map(Directories::uncheckedModifiedTime).max(FileTime::compareTo);
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private static <E> void forEach(@NotNull Iterable<E> list, @NotNull IOConsumer<? super E> action) throws IOException { // add logger as parameter and move it to IOConsumer
        IOException firstException = null;
        IOException exceptionList = null;
        for (E item : list) {
            try {
                action.accept(item);
            } catch (IOException e) {
                LOGGER.error("{}", item, e);
                if (firstException == null) {
                    firstException = e;
                } else {
                    if (exceptionList == null) {
                        exceptionList = new IOException("multiple");
                    }
                    exceptionList.addSuppressed(firstException);
                    exceptionList.addSuppressed(e);
                }
            }
        }
        if (exceptionList != null) {
            throw exceptionList;
        }
        if (firstException != null) {
            throw firstException;
        }
    }

    public static void delete(@NotNull Path path, boolean breakOnFailure) throws IOException {
        final List<Path> pathList = Files.walk(path).collect(Collectors.toList());
        if (breakOnFailure) {
            for (Path subPath : pathList) {
                Files.delete(subPath);
            }
        } else {
            forEach(pathList, Files::delete);
        }
    }

    public static void copy(@NotNull Path src, @NotNull Path dst, boolean breakOnFailure, CopyOption... options) throws IOException {
        final List<Path> pathList = Files.walk(src).collect(Collectors.toList());
        if (breakOnFailure) {
            for (Path srcSubPath : pathList) {
                Path dstSubPath = dst.resolve(src.relativize(srcSubPath));
                Files.copy(srcSubPath, dstSubPath, options);
            }
        } else {
            forEach(pathList, srcSubPath -> {
                Path dstSubPath = dst.resolve(src.relativize(srcSubPath));
                Files.copy(srcSubPath, dstSubPath, options);
            });
        }
    }

    public static void move(@NotNull Path src, @NotNull Path dst, boolean breakOnFailure, CopyOption... options) throws IOException {
        if (dst.startsWith(src)) {
            throw new IOException("move into child");
        }
        final List<Path> pathList = Files.walk(src).collect(Collectors.toList());
        if (breakOnFailure) {
            for (Path srcSubPath : pathList) {
                Path dstSubPath = dst.resolve(src.relativize(srcSubPath));
                Files.move(srcSubPath, dstSubPath, options);
            }
            // todo: trim
        } else {
            forEach(pathList, srcSubPath -> {
                Path dstSubPath = dst.resolve(src.relativize(srcSubPath));
                Files.move(srcSubPath, dstSubPath, options);
            });
            // todo: trim
        }
    }

    public static void zip(@NotNull Path srcPath, @NotNull Path zipFile, OpenOption... options) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile, options))) {
            Files.walk(srcPath).forEach(subPath -> {
                if (!Files.isRegularFile(subPath)) {
                    return;
                }
                String name = srcPath.relativize(subPath).toString();
                if (File.separatorChar != '/') {
                    name = name.replace(File.separatorChar, '/');
                }
                if (name.startsWith("/")) {
                    name = name.substring(1);
                }
                if (name.startsWith("..")) {
                    return;
                }
                try {
                    ZipEntry entry = new ZipEntry(name);
                    entry.setLastAccessTime((FileTime) Files.getAttribute(subPath, "basic:lastAccessTime"));
                    entry.setCreationTime((FileTime) Files.getAttribute(subPath, "basic:lastModifiedTime"));
                    entry.setLastModifiedTime(Files.getLastModifiedTime(subPath));
                    zos.putNextEntry(entry);
                    Files.newInputStream(subPath).transferTo(zos);
                    zos.flush();
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public static void zip(@NotNull Path srcPath, @NotNull Path zipFile, boolean overwrite) throws IOException {
        if (overwrite) {
            zip(srcPath, zipFile, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } else {
            zip(srcPath, zipFile, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        }
    }

    public static void unzip(@NotNull Path zipFile, @NotNull Path dstPath, OpenOption... options) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile, StandardOpenOption.READ))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    return;
                }
                String name = entry.getName();
                if (File.separatorChar != '/') {
                    name = name.replace('/', File.separatorChar);
                }
                Path subPath = dstPath.resolve(name);
                Files.createDirectories(subPath.getParent());
                try (OutputStream os = Files.newOutputStream(subPath, options)) {
                    zis.transferTo(os);
                    os.flush();
                }
            }
        }
    }

    public static void unzip(@NotNull Path zipFile, @NotNull Path dstPath, boolean overwrite) throws IOException {
        if (overwrite) {
            unzip(zipFile, dstPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } else {
            unzip(zipFile, dstPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        }
    }

    private Directories() {
        super();
    }
}
