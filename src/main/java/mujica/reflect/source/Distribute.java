package mujica.reflect.source;

import mujica.ds.bit.list.BooleanList;
import mujica.ds.bit.list.CopyOnResizeBooleanList;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CodeHistory(date = "2025/12/1", name = "SynchronizeChain")
@CodeHistory(date = "2026/1/4")
public final class Distribute implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Distribute.class);

    public static void main(String[] args) {
        (new Distribute("D:\\Java\\CRYCHIC\\src", "D:\\Java\\MyGO\\src"))
                .includeDst("main\\java\\mujica\\math")
                .run();
        (new Distribute("D:\\Java\\MyGO\\src", "D:\\Java\\Mujica\\src"))
                .excludeSrc("main\\java\\mujica\\geometry")
                .excludeSrc("test\\java\\mujica\\geometry")
                .run();
        (new Distribute("D:\\Java\\Mujica\\src\\main", "D:\\Java\\AveMujica\\src\\main"))
                .excludeSrc("java\\json\\provided\\desktop")
                .excludeSrc("java\\json\\provided\\xml")
                .run();
    }

    @NotNull
    final Path srcRoot;

    @NotNull
    final Path dstRoot;

    @NotNull
    final HashSet<Path> excludedSrc = new HashSet<>();

    @NotNull
    final HashSet<Path> includedDst = new HashSet<>();

    Distribute(@NotNull String srcRoot, @NotNull String dstRoot) {
        super();
        this.srcRoot = Path.of(srcRoot).toAbsolutePath();
        this.dstRoot = Path.of(dstRoot).toAbsolutePath();
    }

    @NotNull
    Distribute excludeSrc(@NotNull String pathString) {
        Path path = Path.of(pathString);
        if (!path.isAbsolute()) {
            path = srcRoot.resolve(path);
        }
        excludedSrc.add(path);
        return this;
    }

    @NotNull
    Distribute includeDst(@NotNull String pathString) {
        Path path = Path.of(pathString);
        if (!path.isAbsolute()) {
            path = dstRoot.resolve(path);
        }
        includedDst.add(path);
        return this;
    }

    @Override
    public void run() {
        try {
            {
                Path currentPath = Path.of("").toAbsolutePath();
                if (!srcRoot.startsWith(currentPath)) {
                    return;
                }
            }
            {
                MergeVisitor visitor = new MergeVisitor();
                Files.walkFileTree(srcRoot, visitor);
            }
            {
                CleanVisitor visitor = new CleanVisitor();
                Files.walkFileTree(dstRoot, visitor);
                visitor.doDelete();
            }
        } catch (IOException e) {
            LOGGER.error("{}", this, e);
        }
    }

    @CodeHistory(date = "2026/5/1")
    private class MergeVisitor implements FileVisitor<Path> {

        final ReformatJava reformatJava = new ReformatJava();

        @Override
        public FileVisitResult preVisitDirectory(Path src, BasicFileAttributes attrs) throws IOException {
            if (excludedSrc.contains(src)) {
                return FileVisitResult.SKIP_SUBTREE;
            }
            final Path dst = dstRoot.resolve(srcRoot.relativize(src));
            if (Files.exists(dst)) {
                if (!Files.isDirectory(dst)) {
                    LOGGER.warn("expect a directory {}", dst);
                }
            } else {
                Files.createDirectory(dst);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path src, BasicFileAttributes attrs) {
            final Path dst = dstRoot.resolve(srcRoot.relativize(src));
            includedDst.add(dst);
            reformatJava.mergeFile(src, dst);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            LOGGER.warn("{}", file, exc);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            if (exc != null) {
                LOGGER.warn("{}", dir, exc);
            }
            return FileVisitResult.CONTINUE;
        }
    }

    @CodeHistory(date = "2026/4/25")
    private class CleanVisitor implements FileVisitor<Path> {

        @NotNull
        final ArrayList<Path> toDelete = new ArrayList<>();

        final BooleanList isDirectoryEmptyStack = new CopyOnResizeBooleanList(null);

        boolean isDirectoryEmpty;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            isDirectoryEmptyStack.offerLast(isDirectoryEmpty);
            if (includedDst.contains(dir)) {
                isDirectoryEmpty = false;
                return FileVisitResult.SKIP_SUBTREE;
            } else {
                isDirectoryEmpty = true;
                return FileVisitResult.CONTINUE;
            }
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (includedDst.contains(file)) {
                isDirectoryEmpty = false;
            } else {
                toDelete.add(file);
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            LOGGER.warn("{}", file, exc);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            if (exc != null) {
                LOGGER.warn("{}", dir, exc);
            }
            if (isDirectoryEmpty) {
                toDelete.add(dir);
                isDirectoryEmpty = isDirectoryEmptyStack.getLast();
            }
            isDirectoryEmptyStack.removeLast();
            return FileVisitResult.CONTINUE;
        }

        void doDelete() {
            LOGGER.info("{} path(s) to delete", toDelete.size());
            for (Path path : toDelete) {
                try {
                    // Files.getLastModifiedTime(path);
                    Files.delete(path);
                    LOGGER.info("deleted {}", path);
                } catch (IOException e) {
                    LOGGER.warn("deleting {}", path, e);
                }
            }
        }
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
            DirectSubclass.class,
            FieldOrder.class,
            Nullable.class,
            NotNull.class,
            DataType.class
    );

    @CodeHistory(date = "2025/4/4", project = "LeetInAction", name = "ReformatJavaSource")
    @CodeHistory(date = "2026/3/23")
    private static class ReformatJava {

        static final String NEGATIVE_FILE_END = "//:~";

        static final Pattern SERIAL_UID_PATTERN = Pattern.compile("\\s*private static final long serialVersionUID = 0([Xx][0-9A-Fa-f]{1,16}[Ll]);\\s*");

        static final Pattern LINE_ANNOTATION_PATTERN = Pattern.compile("\\s*@([A-Z][0-9A-Za-z]*)(\\([^)]*\\))?\\s*"); // do not match @interface, which is annotation declaration

        final HashMap<String, Integer> annotationOrderMap = new HashMap<>();

        int defaultAnnotationOrder = -1;

        final ArrayList<AnnotationInOrder> annotationList = new ArrayList<>();

        ReformatJava() {
            super();
            final List<Object> list = ANNOTATION_IN_ORDER;
            final int size = list.size();
            for (int index = 0; index < size; index++) {
                Object item = list.get(index);
                String name;
                if (item instanceof String) {
                    name = (String) item;
                } else if (item instanceof Class) {
                    name = ((Class<?>) item).getSimpleName();
                } else {
                    continue;
                }
                if (name.isEmpty()) {
                    defaultAnnotationOrder = index;
                } else {
                    annotationOrderMap.put(name, index);
                }
            }
        }

        int annotationOrder(@NotNull String annotationName) {
            return Objects.requireNonNullElseGet(annotationOrderMap.get(annotationName), () -> defaultAnnotationOrder);
        }

        void reformatJava(@NotNull List<String> lineList) {
            int size;
            while (true) {
                size = lineList.size();
                if (size == 0) {
                    break;
                }
                String lastLine = lineList.get(size - 1);
                if (lastLine.isBlank()) {
                    lineList.remove(size - 1);
                } else {
                    if (lastLine.endsWith(NEGATIVE_FILE_END)) {
                        lastLine = lastLine.substring(0, lastLine.length() - NEGATIVE_FILE_END.length());
                        lineList.set(size - 1, lastLine);
                    }
                    break;
                }
            }
            for (int lineNumber = 0; lineNumber < size; lineNumber++) {
                String line = lineList.get(lineNumber);
                Matcher matcher = SERIAL_UID_PATTERN.matcher(line);
                if (matcher.matches()) {
                    line = line.substring(0, matcher.start(1)) + matcher.group(1).toLowerCase().replace('l', 'L') + line.substring(matcher.end(1));
                    lineList.set(lineNumber, line);
                }
                matcher = LINE_ANNOTATION_PATTERN.matcher(line);
                if (matcher.matches()) {
                    annotationList.add(new AnnotationInOrder(line, lineNumber, annotationOrder(matcher.group(1))));
                } else {
                    int annotationListSize = annotationList.size();
                    if (annotationListSize > 0) {
                        if (annotationListSize > 1) {
                            annotationList.sort(null);
                            int baseLineNumber = lineNumber - annotationListSize;
                            for (int index = 0; index < annotationListSize; index++) {
                                lineList.set(baseLineNumber + index, annotationList.get(index).line);
                            }
                        }
                        annotationList.clear();
                    }
                }
            }
        }

        void transformJava(@NotNull Path src, Path dst, OpenOption... options) throws IOException {
            final List<String> lineList = Files.readAllLines(src, StandardCharsets.UTF_8);
            reformatJava(lineList);
            Files.write(dst, lineList, StandardCharsets.UTF_8, options);
            Files.setLastModifiedTime(dst, Files.getLastModifiedTime(src));
        }

        boolean isJava(@NotNull Path path) {
            return path.getFileName().toString().toLowerCase().endsWith(".java");
        }

        void mergeFile(@NotNull Path src, @NotNull Path dst) {
            try {
                if (Files.exists(dst)) {
                    if (Files.getLastModifiedTime(src).compareTo(Files.getLastModifiedTime(dst)) > 0) {
                        if (isJava(src)) {
                            LOGGER.info("overwrite java file from {} to {}", src, dst);
                            transformJava(src, dst, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                        } else {
                            LOGGER.info("overwrite file from {} to {}", src, dst);
                            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                        }
                    } else {
                        LOGGER.trace("ignore file from {} to {}", src, dst);
                    }
                } else {
                    if (isJava(src)) {
                        LOGGER.info("transform java file from {} to {}", src, dst);
                        transformJava(src, dst, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
                    } else {
                        LOGGER.info("copy file from {} to {}", src, dst);
                        Files.copy(src, dst, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("merge file from {} to {}", src, dst, e);
            }
        }

        void mergeDirectory(@NotNull Path src, @NotNull Path dst) {
            if (Files.exists(dst)) {
                return;
            }
            try {
                Files.createDirectory(dst);
            } catch (IOException e) {
                LOGGER.error("merge directory from {} to {}", src, dst, e);
            }
        }

        void mergeRoot(@NotNull Path src, @NotNull Path dst) {
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
    }
}
