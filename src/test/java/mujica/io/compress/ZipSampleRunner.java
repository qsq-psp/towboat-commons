package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * Created on 2025/11/15.
 */
@CodeHistory(date = "2025/11/15")
public class ZipSampleRunner extends Runner {

    @NotNull
    private final Class<?> clazz;

    @NotNull
    private final ZipSample[] samples;

    public ZipSampleRunner(@NotNull Class<?> clazz) throws ReflectiveOperationException {
        super();
        this.clazz = clazz;
        final String directory = (String) clazz.getDeclaredField("DIRECTORY").get(null);
        final String[] files = (String[]) clazz.getDeclaredField("FILES").get(null);
        final int fileCount = files.length;
        final ZipSample[] samples = new ZipSample[fileCount];
        for (int index = 0; index < fileCount; index++) {
            String fileName = files[index];
            samples[index] = new ZipSample(
                    Description.createTestDescription(clazz, fileName),
                    directory + fileName
            );
        }
        this.samples = samples;
    }

    @Override
    @NotNull
    public Description getDescription() {
        final Description description = Description.createSuiteDescription("zip");
        for (ZipSample child : samples) {
            description.addChild(child.description);
        }
        return description;
    }

    @Override
    public void run(@NotNull RunNotifier notifier) {
        for (ZipSample item : samples) {
            item.run(notifier, clazz);
        }
    }
}
