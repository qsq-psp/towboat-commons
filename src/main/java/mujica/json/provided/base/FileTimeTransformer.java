package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.attribute.FileTime;

/**
 * Created on 2026/8/17.
 */
public class FileTimeTransformer implements JsonContextTransformer<FileTime> {

    public static final FileTimeTransformer INSTANCE = new FileTimeTransformer();

    @Override
    public void transform(FileTime fileTime, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.numberValue(fileTime.toMillis());
    }
}
