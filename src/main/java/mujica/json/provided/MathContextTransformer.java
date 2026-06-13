package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.MathContext;

/**
 * Created on 2026/5/16.
 */
@CodeHistory(date = "2026/5/16")
public class MathContextTransformer implements JsonContextTransformer<MathContext> {

    public static final MathContextTransformer INSTANCE = new MathContextTransformer();

    @Override
    public void transform(@NotNull MathContext in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("precision");
            out.numberValue(in.getPrecision());
            out.stringKey("roundingMode");
            out.stringValue(in.getRoundingMode().toString()); // name() is also OK
        }
        out.closeObject();
    }
}
