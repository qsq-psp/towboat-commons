package mujica.json.provided.management;

import mujica.json.handler.UndoKeyJsonHandlerAdapter;
import mujica.json.io.JsonCharStreamWriter;
import mujica.json.reflect.JsonContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created on 2026/5/28.
 */
@CodeHistory(date = "2026/5/28")
public class ProvidedManagementTest {

    private static final String TARGET = "target\\json-provided";

    private static final JsonContext CONTEXT = new JsonContext();

    @BeforeClass
    public static void initContext() {
        CONTEXT.loadBasic().loadProvidedManagement();
    }

    private void caseObject(@NotNull String name, @NotNull Object object) throws IOException {
        final Path currentPath = Path.of("").toAbsolutePath();
        final Path targetPath = Path.of(TARGET).toAbsolutePath();
        if (!(Files.isDirectory(targetPath) && targetPath.startsWith(currentPath))) {
            Assert.fail("target directory");
        }
        try (Writer writer = Files.newBufferedWriter(Path.of(TARGET, name + ".json"), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            CONTEXT.transform(object, new UndoKeyJsonHandlerAdapter<>(new JsonCharStreamWriter(writer)));
            writer.flush();
        }
    }

    private void caseObject(@NotNull Object object) throws IOException {
        caseObject(object.getClass().getName(), object);
    }

    @Test
    public void caseClassLoading() throws IOException {
        caseObject(ManagementFactory.getClassLoadingMXBean());
    }

    @Test
    public void caseCompilation() throws IOException {
        caseObject(ManagementFactory.getCompilationMXBean());
    }

    @Test
    public void caseGarbageCollector() throws IOException {
        caseObject(ManagementFactory.getGarbageCollectorMXBeans()); // ArrayList
    }

    @Test
    public void caseMemoryPool() throws IOException {
        caseObject(ManagementFactory.getMemoryPoolMXBeans());
    }

    @Test
    public void caseOperatingSystem() throws IOException {
        caseObject(ManagementFactory.getOperatingSystemMXBean());
    }

    @Test
    public void caseRuntime() throws IOException {
        caseObject(ManagementFactory.getRuntimeMXBean());
    }

    @Test
    public void caseThread() throws IOException {
        caseObject(ManagementFactory.getThreadMXBean());
    }
}
