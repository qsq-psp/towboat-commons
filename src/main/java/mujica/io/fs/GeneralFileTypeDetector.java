package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

/**
 * Created on 2026/3/4.
 */
@CodeHistory(date = "2026/3/4")
public abstract class GeneralFileTypeDetector extends FileTypeDetector {

    @Override
    public String probeContentType(Path path) throws IOException {
        return null;
    }
}
