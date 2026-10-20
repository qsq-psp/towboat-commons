package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

@CodeHistory(date = "2026/4/21")
public class PathTransformer implements JsonContextTransformer<Path> {

    public static final PathTransformer INSTANCE = new PathTransformer();

    @Override
    public void transform(Path path, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.stringValue(path.toString());
    }
}
