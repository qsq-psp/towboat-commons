package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.io.codec.RepeatCharIndentWriter;
import mujica.io.nest.BufferedLimitedDataInputStream;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("SameParameterValue")
@CodeHistory(date = "2025/9/22")
public final class JavaP {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaP.class);

    private static final String[][] LINKS = {
            {"D:\\Java\\CDP\\target\\classes", "D:\\Java\\CDP\\target\\javap"},
            {"D:\\Java\\coo\\out\\production", "D:\\Download\\GradleHome\\javap"},
            {"D:\\Java\\Ultramarine\\Platform\\target\\classes", "D:\\Java\\Ultramarine\\Platform\\target\\javap"}
    };

    public static void main(String[] args) {
        for (String[] link : LINKS) {
            if (link.length != 2) {
                continue;
            }
            Path srcPath = Path.of(link[0]);
            Path dstPath = Path.of(link[1]);
            if (Files.isDirectory(srcPath) && Files.isDirectory(dstPath)) {
                readRoot(srcPath, dstPath);
            }
        }
    }

    private static void readRoot(@NotNull Path src, @NotNull Path dst) {
        LOGGER.info("read root from {} to {}", src, dst);
        try {
            Files.walk(src).forEach(srcItem -> {
                Path dstItem = dst.resolve(src.relativize(srcItem));
                if (Files.isRegularFile(srcItem)) {
                    readFile(srcItem, dstItem);
                } else if (Files.isDirectory(srcItem)) {
                    readDirectory(srcItem, dstItem);
                }
            });
        } catch (IOException e) {
            LOGGER.error("walk {}", src, e);
        }
    }

    private static final String SUFFIX = ".class";

    private static void readFile(@NotNull Path src, @NotNull Path dst) {
        try {
            String fileName = src.getFileName().toString();
            if (!fileName.toLowerCase().endsWith(SUFFIX)) {
                return;
            }
            dst = Path.of(dst.getParent().toString(), fileName.substring(0, SUFFIX.length()) + ".javap.txt");
            if (Files.exists(dst)) {
                if (Files.getLastModifiedTime(src).compareTo(Files.getLastModifiedTime(dst)) <= 0) {
                    LOGGER.trace("ignore file from {} to {}", src, dst);
                    return;
                }
                LOGGER.info("overwrite file from {} to {}", src, dst);
            } else {
                LOGGER.info("write file from {} to {}", src, dst);
            }
            try (BufferedLimitedDataInputStream is = new BufferedLimitedDataInputStream(Files.newInputStream(src, StandardOpenOption.READ))) {
                ClassFile classFile = new ClassFile();
                classFile.read(is);
                try (IndentWriter iw = new RepeatCharIndentWriter(Files.newBufferedWriter(dst,
                        StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))) {
                    classFile.write(iw);
                }
            }
        } catch (Exception e) {
            LOGGER.error("handle file from {} to {}", src, dst, e);
        }
    }

    private static void readDirectory(@NotNull Path src, @NotNull Path dst) {
        if (Files.exists(dst)) {
            return;
        }
        try {
            Files.createDirectory(dst);
        } catch (IOException e) {
            LOGGER.error("read directory from {} to {}", src, dst, e);
        }
    }

    private static void transform(@NotNull String src, @NotNull String dst) {
        try (BufferedLimitedDataInputStream is = new BufferedLimitedDataInputStream(new FileInputStream(src))) {
            ClassFile classFile = new ClassFile();
            classFile.read(is);
            try (IndentWriter iw = new RepeatCharIndentWriter(new FileWriter(dst, StandardCharsets.UTF_8))) {
                classFile.write(iw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JavaP() {
        super();
    }
}
