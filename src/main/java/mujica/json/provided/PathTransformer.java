package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@CodeHistory(date = "2026/4/21")
public class PathTransformer implements JsonContextTransformer<Path> {

    @Override
    public void transform(Path in, @NotNull JsonHandler out, JsonContext context) {
        out.stringValue(in.toString());
    }
}
