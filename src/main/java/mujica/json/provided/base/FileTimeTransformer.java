package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.FileTime;

/**
 * Created on 2026/8/17.
 */
public class FileTimeTransformer implements JsonContextTransformer<FileTime> {

    public static final FileTimeTransformer INSTANCE = new FileTimeTransformer();

    @Override
    public void transform(FileTime in, @NotNull JsonHandler out, JsonContext context) {
        out.numberValue(in.toMillis());
    }
}
