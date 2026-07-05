package mujica.json.provided.base;

import mujica.json.io.JsonCharStreamWriter;
import mujica.json.provided.base.*;
import mujica.json.reflect.JsonContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.math.MathContext;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.text.Bidi;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.UUID;
import java.util.concurrent.atomic.*;
import java.util.regex.Pattern;

/**
 * Created on 2026/5/27.
 */
@CodeHistory(date = "2026/5/27")
public class ProvidedTest {

    private static final String TARGET = "target\\json-provided";

    private static final JsonContext CONTEXT = new JsonContext();

    @BeforeClass
    public static void initContext() {
        CONTEXT.loadBasic().loadProvidedBase().logReflectCache();
    }

    private void caseObject(@NotNull String name, @NotNull Object object) throws IOException {
        final Path currentPath = Path.of("").toAbsolutePath();
        final Path targetPath = Path.of(TARGET).toAbsolutePath();
        if (!(Files.isDirectory(targetPath) && targetPath.startsWith(currentPath))) {
            Assert.fail("target directory");
        }
        try (Writer writer = Files.newBufferedWriter(Path.of(TARGET, name + ".json"), StandardCharsets.UTF_8,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            CONTEXT.transform(object, new JsonCharStreamWriter(writer));
            writer.flush();
        }
    }

    private void caseObject(@NotNull Object object) throws IOException {
        caseObject(object.getClass().getName(), object);
    }

    @Test
    public void caseAtomicBoolean() throws IOException {
        caseObject(new AtomicBoolean(false));
    }

    @Test
    public void caseAtomicIntegerArray() throws IOException {
        caseObject(new AtomicIntegerArray(new int[] {3, 4, 5}));
    }

    @Test
    public void caseAtomicInteger() throws IOException {
        caseObject(new AtomicInteger(8080));
    }

    @Test
    public void caseAtomicLongArray() throws IOException {
        caseObject(new AtomicLongArray(new long[] {-50L, 60L, 90L}));
    }

    @Test
    public void caseAtomicLong() throws IOException {
        caseObject(new AtomicLong(0xabcdef1234567890L));
    }

    @Test
    public void caseBidi() throws IOException {
        caseObject(new Bidi("lemon tree", Bidi.DIRECTION_LEFT_TO_RIGHT));
    }

    @Test
    public void caseCalendar() throws IOException {
        caseObject(Calendar.getInstance());
    }

    @Test
    public void caseCharset() throws IOException {
        caseObject(CharsetTransformer.INSTANCE);
    }

    @Test
    public void caseClassLoader() throws IOException {
        caseObject(ClassLoaderTransformer.INSTANCE);
    }

    @Test
    public void caseDecimalFormat() throws IOException {
        caseObject(new DecimalFormat("##0.##E0"));
    }

    @Test
    public void caseDoubleSummaryStatistics() throws IOException {
        final DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        statistics.accept(Math.sqrt(2));
        statistics.accept(Math.sqrt(5));
        statistics.accept(Math.cbrt(9));
        caseObject(statistics);
    }

    @Test
    public void caseDuration() throws IOException {
        caseObject(Duration.ofSeconds(114514));
    }

    @Test
    public void caseFileSystem() throws IOException {
        caseObject(FileSystemTransformer.INSTANCE);
    }

    @Test
    public void caseInstant() throws IOException {
        caseObject(InstantTransformer.INSTANCE);
    }

    @Test
    public void caseIntSummaryStatistics() throws IOException {
        final IntSummaryStatistics statistics = new IntSummaryStatistics();
        statistics.accept(2019);
        statistics.accept(2024);
        statistics.accept(2026);
        caseObject(statistics);
    }

    @Test
    public void caseLocale() throws IOException {
        caseObject(LocaleTransformer.INSTANCE);
    }

    @Test
    public void caseMatcher() throws IOException {
        caseObject(Pattern.compile("\\d{2,4}", Pattern.DOTALL).matcher("12345a987b000c66666666").find());
    }

    @Test
    public void caseMathContext() throws IOException {
        caseObject(MathContext.UNLIMITED);
    }

    @Test
    public void caseModule() throws IOException {
        caseObject(getClass().getModule());
    }

    @Test
    public void casePath() throws IOException {
        caseObject(Path.of("").toAbsolutePath());
    }

    @Test
    public void casePattern() throws IOException {
        caseObject(Pattern.compile("\\[\\d+]Az", Pattern.CASE_INSENSITIVE));
    }

    @Test
    public void caseProcessHandle() throws IOException {
        caseObject(ProcessHandleTransformer.INSTANCE);
    }

    @Test
    public void caseRuntime() throws IOException {
        caseObject(RuntimeTransformer.INSTANCE);
    }

    @Test
    public void caseSimpleDateFormat() throws IOException {
        caseObject(new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z"));
    }

    @Test
    public void caseSocket() throws IOException {
        caseObject(new Socket());
    }

    @Test
    public void caseThreadGroup() throws IOException {
        caseObject(ThreadGroupTransformer.INSTANCE);
    }

    @Test
    public void caseThread() throws IOException {
        caseObject(ThreadTransformer.INSTANCE);
    }

    @Test
    public void caseThrowable() throws IOException {
        caseObject(new ArithmeticException("test"));
    }

    @Test
    public void caseTimeZone() throws IOException {
        caseObject(TimeZoneTransformer.INSTANCE);
    }

    @Test
    public void caseUri() throws Exception {
        caseObject(new URI("https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/vendor/eclipsefdn-solstice-components/landing-well/eclipse-home-bg.jpg?281f3578e8c92caaa2691632d0fb4065"));
    }

    @Test
    public void caseUrl() throws IOException {
        caseObject(new URL("https://xiph.org/vorbis/doc/Vorbis_I_spec.html"));
    }

    @Test
    public void caseUuid() throws IOException {
        caseObject(UUID.randomUUID());
    }

    @Test
    public void caseWatchEventKind() throws IOException {
        caseObject(StandardWatchEventKinds.ENTRY_MODIFY);
    }

    @Test
    public void caseZoneId() throws IOException {
        caseObject(ZoneOffset.MAX);
        caseObject(ZoneId.of("Europe/Paris"));
    }
}
