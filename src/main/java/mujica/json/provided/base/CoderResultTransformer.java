package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.CoderResult;

@CodeHistory(date = "2026/6/5")
public class CoderResultTransformer implements JsonContextTransformer<CoderResult> {

    public static final CoderResultTransformer INSTANCE = new CoderResultTransformer();

    @Override
    public void transform(@NotNull CoderResult in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("underflow");
            out.booleanValue(in.isUnderflow());
        }
        {
            out.key("overflow");
            out.booleanValue(in.isOverflow());
        }
        {
            boolean malformed = in.isMalformed();
            out.key("malformed");
            out.booleanValue(malformed);
            if (malformed) {
                out.key("length");
                out.numberValue(in.length());
            }
        }
        {
            boolean unmappable = in.isUnmappable();
            out.key("unmappable");
            out.booleanValue(unmappable);
            if (unmappable) {
                out.key("length");
                out.numberValue(in.length());
            }
        }
        out.closeObject();
    }
}
