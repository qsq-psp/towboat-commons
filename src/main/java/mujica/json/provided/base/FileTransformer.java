package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@CodeHistory(date = "2026/4/21")
public class FileTransformer implements JsonContextTransformer<File> {

    @Override
    public void transform(@NotNull File file, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.stringValue(file.getPath()); // toString() is the same
    }
}
