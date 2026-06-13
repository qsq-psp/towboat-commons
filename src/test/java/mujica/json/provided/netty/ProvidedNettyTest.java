package mujica.json.provided.netty;

import mujica.json.handler.UndoKeyJsonHandlerAdapter;
import mujica.json.io.JsonCharStreamWriter;
import mujica.json.reflect.JsonContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Created on 2026/6/7.
 */
public class ProvidedNettyTest {

    private static final String TARGET = "target\\json-provided";

    private static final JsonContext CONTEXT = new JsonContext();

    @BeforeClass
    public static void initContext() {
        CONTEXT.loadBasic().loadProvidedDesktop();
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
}
