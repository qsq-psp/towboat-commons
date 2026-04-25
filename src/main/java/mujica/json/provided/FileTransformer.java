package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@CodeHistory(date = "2026/4/21")
public class FileTransformer implements JsonContextTransformer<File> {

    @Override
    public void transform(File in, @NotNull JsonHandler out, JsonContext context) {
        out.stringValue(in.getPath()); // toString() is also OK
    }
}
