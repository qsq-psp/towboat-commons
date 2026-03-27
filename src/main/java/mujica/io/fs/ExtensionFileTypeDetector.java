package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.HashMap;

@CodeHistory(date = "2026/3/3")
public abstract class ExtensionFileTypeDetector extends FileTypeDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionFileTypeDetector.class);

    @Nullable
    public abstract String getContentType(@NotNull String extension);

    @Override
    @Nullable
    public String probeContentType(Path path) {
        final String string = path.getFileName().toString();
        final int index = string.lastIndexOf('.') + 1;
        if (index == 0 || index == string.length()) {
            return null;
        }
        return getContentType(string.substring(index));
    }

    @CodeHistory(date = "2026/3/14")
    public static class Cached extends ExtensionFileTypeDetector {

        private WeakReference<FullMap> fullMap;

        public Cached() {
            super();
        }

        @Nullable
        @Override
        public String getContentType(@NotNull String extension) {
            return null;
        }
    }

    @CodeHistory(date = "2026/3/17")
    public static class FullMap extends ExtensionFileTypeDetector {

        private final HashMap<String, String> map = new HashMap<>();

        public FullMap() {
            super();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(ExtensionFileTypeDetector.class.getResourceAsStream("mime-type.txt"), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    int div = line.indexOf(' ');
                    if (div == -1) {
                        continue;
                    }
                    map.put(line.substring(0, div), line.substring(div + 1));
                }
            } catch (Exception e) {
                LOGGER.error("", e);
            }
        }

        @Nullable
        @Override
        public String getContentType(@NotNull String extension) {
            return map.get(extension);
        }
    }
}
