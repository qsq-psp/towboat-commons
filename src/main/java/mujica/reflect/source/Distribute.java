package mujica.reflect.source;

import mujica.reflect.modifier.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CodeHistory(date = "2025/12/1", name = "SynchronizeChain")
@CodeHistory(date = "2026/1/4")
public final class Distribute {

    private static final Logger LOGGER = LoggerFactory.getLogger(Distribute.class);

    @NotNull
    final String includedRoot;

    @NotNull
    final List<String> excludedList = new ArrayList<>();

    Distribute(@NotNull String includedRoot) {
        super();
        this.includedRoot = includedRoot;
    }

    @NotNull
    static Distribute include(@NotNull String path) {
        return new Distribute(path);
    }

    @NotNull
    Distribute exclude(@NotNull String path) {
        excludedList.add(path);
        return this;
    }

    @Nullable
    Path getSourceRoot() {
        final Path currentPath = Path.of("").toAbsolutePath();
        final Path sourceRoot = Path.of(includedRoot).toAbsolutePath();
        if (Files.isDirectory(sourceRoot) && sourceRoot.startsWith(currentPath)) {
            return sourceRoot;
        } else {
            return null;
        }
    }

    void merge(@NotNull String target) {
        //
    }

    void overwrite(@NotNull String target) {
        //
    }

    void cleanAndWrite(@NotNull String target) {
        //
    }

    @CodeHistory(date = "2025/10/11", project = "LeetInAction", name = "PriorityAnnotation")
    @CodeHistory(date = "2026/3/23")
    private static class AnnotationInOrder implements Comparable<AnnotationInOrder> {

        @NotNull
        final String line;

        final int lineNumber;

        final int order;

        AnnotationInOrder(@NotNull String line, int lineNumber, int order) {
            super();
            this.line = line;
            this.lineNumber = lineNumber;
            this.order = order;
        }

        @Override
        public int compareTo(@NotNull AnnotationInOrder that) {
            final int result = Integer.compare(this.order, that.order);
            if (result != 0) {
                return result;
            } else {
                return Integer.compare(this.lineNumber, that.lineNumber);
            }
        }
    }

    private static final List<Object> ANNOTATION_IN_ORDER = List.of(
            CodeHistory.class,
            ReferencePage.class,
            ReferenceCode.class,
            Name.class,
            Documented.class,
            "BeforeClass",
            "AfterClass",
            "Before",
            "After",
            "RunWith",
            "Test",
            "Ignore",
            "Sharable",
            "ChannelHandler.Sharable",
            ConstantInterface.class,
            Retention.class,
            Target.class,
            Repeatable.class,
            "", // other annotations not listed here
            Nullable.class,
            NotNull.class,
            DataType.class
    );

    @CodeHistory(date = "2025/4/4", project = "LeetInAction", name = "ReformatJavaSource")
    @CodeHistory(date = "2026/3/23")
    private static class ReformatJava {

        final HashMap<String, Integer> annotationOrderMap = new HashMap<>();

        int defaultAnnotationOrder = -1;

        ReformatJava() {
            super();
        }
    }

    private static final String[][] LINKS = {
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
}
