package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.MathContext;

@CodeHistory(date = "2026/5/16")
public class MathContextTransformer implements JsonContextTransformer<MathContext> {

    public static final MathContextTransformer INSTANCE = new MathContextTransformer();

    @Override
    public void transform(@NotNull MathContext mc, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("precision");
            out.numberValue(mc.getPrecision());
            out.key("roundingMode");
            out.stringValue(mc.getRoundingMode().toString()); // mathContext.getRoundingMode().name() is the same
        }
        out.closeObject();
    }
}
