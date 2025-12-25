package mujica.reflect.source;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@CodeHistory(date = "2025/4/4", project = "LeetInAction", name = "ReformatJavaSource")
@CodeHistory(date = "2025/12/1", name = "SynchronizeChain")
public final class Distribute {

    private static final Logger LOGGER = LoggerFactory.getLogger(Distribute.class);

    private static final String[][] LINKS = {
            {"D:\\Java\\CDP\\src", "D:\\Java\\CRYCHIC\\src"},
            {"D:\\Java\\CRYCHIC\\src", "D:\\Java\\MyGO\\src"},
            {"D:\\Java\\MyGO\\src", "D:\\Java\\Mujica\\src"},
            {"D:\\Java\\Mujica\\src", "D:\\Java\\AveMujica\\src"}
    };

    public static void main(String[] args) {
        final Path currentPath = Path.of("").toAbsolutePath();
        LOGGER.info("current {}", currentPath);
        for (String[] link : LINKS) {
            int size = link.length;
            if (size <= 1) {
                continue;
            }
            Path srcPath = Path.of(link[0]);
            if (!(Files.isDirectory(srcPath) && srcPath.startsWith(currentPath))) {
                continue;
            }
            for (int index = 1; index < size; index++) {
                Path dstPath = Path.of(link[index]);
                if (!Files.isDirectory(dstPath)) {
                    continue;
                }
                mergeRoot(srcPath, dstPath);
            }
        }
    }

    private static void mergeRoot(@NotNull Path src, @NotNull Path dst) {
        LOGGER.info("merge root from {} to {}", src, dst);
        try {
            Files.walk(src).forEach(srcItem -> {
                Path dstItem = dst.resolve(src.relativize(srcItem));
                if (Files.isRegularFile(srcItem)) {
                    mergeFile(srcItem, dstItem);
                } else if (Files.isDirectory(srcItem)) {
                    mergeDirectory(srcItem, dstItem);
                }
            });
        } catch (IOException e) {
            LOGGER.error("walk {}", src, e);
        }
    }

    private static void mergeFile(@NotNull Path src, @NotNull Path dst) {
        try {
            if (Files.exists(dst)) {
                if (Files.getLastModifiedTime(src).compareTo(Files.getLastModifiedTime(dst)) > 0) {
                    LOGGER.info("overwrite file from {} to {}", src, dst);
                    Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                } else {
                    LOGGER.trace("ignore file from {} to {}", src, dst);
                }
            } else {
                LOGGER.info("copy file from {} to {}", src, dst);
                Files.copy(src, dst, StandardCopyOption.COPY_ATTRIBUTES);
            }
        } catch (IOException e) {
            LOGGER.error("merge file from {} to {}", src, dst, e);
        }
    }

    private static void mergeDirectory(@NotNull Path src, @NotNull Path dst) {
        if (Files.exists(dst)) {
            return;
        }
        try {
            Files.createDirectory(dst);
        } catch (IOException e) {
            LOGGER.error("merge directory from {} to {}", src, dst, e);
        }
    }

    private Distribute() {
        super();
    }
}
