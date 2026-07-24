package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.WatchEvent;

@CodeHistory(date = "2026/5/20")
public class WatchEventKindTransformer implements JsonContextTransformer<WatchEvent.Kind<?>> {

    @Override
    public void transform(@NotNull WatchEvent.Kind<?> kind, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(kind.name());
            out.key(TypeTransformer.TYPE);
            out.stringValue(kind.type().getName());
        }
        out.closeObject();
    }
}
