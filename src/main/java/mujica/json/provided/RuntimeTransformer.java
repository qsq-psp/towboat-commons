package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "RuntimeValueSerializer")
@CodeHistory(date = "2026/5/12")
public class RuntimeTransformer implements JsonContextTransformer<Runtime>, JsonStructure {

    @Override
    public void transform(@NotNull Runtime in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("availableProcessors");
            out.numberValue(in.availableProcessors());
            out.stringKey("freeMemory");
            out.numberValue(in.freeMemory());
            out.stringKey("totalMemory");
            out.numberValue(in.totalMemory());
            out.stringKey("maxMemory");
            out.numberValue(in.maxMemory());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Runtime.getRuntime(), jh, null);
    }
}
