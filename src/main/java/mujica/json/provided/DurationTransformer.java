package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Created on 2026/4/26.
 */
public class DurationTransformer implements JsonContextTransformer<Duration> {

    public static final DurationTransformer INSTANCE = new DurationTransformer();

    static final FastString SECONDS = new FastString("seconds");

    static final FastString NANO = new FastString("nano");

    @Override
    public void transform(Duration in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(SECONDS);
            out.numberValue(in.getSeconds());
            out.stringKey(NANO);
            out.numberValue(in.getNano());
        }
        out.closeObject();
    }
}
