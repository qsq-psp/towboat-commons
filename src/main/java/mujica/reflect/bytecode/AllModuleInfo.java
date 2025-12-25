package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.io.codec.RepeatCharIndentWriter;
import mujica.io.nest.BufferedLimitedDataInputStream;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@CodeHistory(date = "2025/10/28", project = "LeetInAction")
@CodeHistory(date = "2025/12/5")
public final class AllModuleInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllModuleInfo.class);

    private static final String[] TARGETS = {
            "D:\\Java\\CDP\\target\\javap-module-info"
    };

    public static void main(String[] args) {
        final Path currentPath = Path.of("").toAbsolutePath();
        for (String targetName : TARGETS) {
            Path targetPath = Path.of(targetName);
            if (!(Files.isDirectory(targetPath) && targetPath.startsWith(currentPath))) {
                continue;
            }
            transform(ClassLoader.getPlatformClassLoader(), Path.of(targetName, "platform"));
            transform(ClassLoader.getSystemClassLoader(), Path.of(targetName, "system"));
        }
    }

    private static void transform(@NotNull ClassLoader classLoader, @NotNull Path directory) {
        try {
            if (!Files.exists(directory)) {
                Files.createDirectory(directory);
            }
            Object[] urls = classLoader.resources("module-info.class").toArray();
            for (Object url : urls) {
                transform((URL) url, directory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void transform(@NotNull URL url, @NotNull Path directory) {
        try (BufferedLimitedDataInputStream is = new BufferedLimitedDataInputStream(url.openStream())) {
            ClassFile classFile = new ClassFile();
            classFile.read(is);
            directory = Path.of(directory.toString(), classFile.getModuleName() + ".javap.txt");
            LOGGER.info("transform file from {} to {}", url, directory);
            try (IndentWriter iw = new RepeatCharIndentWriter(Files.newBufferedWriter(directory,
                    StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE))) {
                classFile.writeModule(iw);
            }
        } catch (Exception e) {
            LOGGER.error("handle file from {} to {}", url, directory, e);
        }
    }
}
