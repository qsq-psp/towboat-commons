package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@CodeHistory(date = "2026/4/26")
public class DurationTransformer implements JsonContextTransformer<Duration> {

    public static final DurationTransformer INSTANCE = new DurationTransformer();

    static final FastString SECONDS = new FastString("seconds");

    static final FastString NANO = new FastString("nano");

    @Override
    public void transform(Duration in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(SECONDS);
            out.numberValue(in.getSeconds());
            out.key(NANO);
            out.numberValue(in.getNano());
        }
        out.closeObject();
    }
}
