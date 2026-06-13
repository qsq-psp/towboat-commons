package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.WatchEvent;

/**
 * Created on 2026/5/20.
 */
public class WatchEventKindTransformer implements JsonContextTransformer<WatchEvent.Kind<?>> {

    @Override
    public void transform(@NotNull WatchEvent.Kind<?> in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(in.name());
            out.stringKey(TypeTransformer.TYPE);
            out.stringValue(in.type().getName());
        }
        out.closeObject();
    }
}
