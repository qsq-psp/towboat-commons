package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "RuntimeValueSerializer")
@CodeHistory(date = "2026/5/12")
public class RuntimeTransformer implements JsonContextTransformer<Runtime>, JsonStructure {

    public static final RuntimeTransformer INSTANCE = new RuntimeTransformer();

    @Override
    public void transform(@NotNull Runtime runtime, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("availableProcessors");
            out.numberValue(runtime.availableProcessors());
            out.key("freeMemory");
            out.numberValue(runtime.freeMemory());
            out.key("totalMemory");
            out.numberValue(runtime.totalMemory());
            out.key("maxMemory");
            out.numberValue(runtime.maxMemory());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Runtime.getRuntime(), jh, null);
    }
}
