package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.CoderResult;

/**
 * Created on 2026/6/5.
 */
public class CoderResultTransformer implements JsonContextTransformer<CoderResult> {

    public static final CoderResultTransformer INSTANCE = new CoderResultTransformer();

    @Override
    public void transform(@NotNull CoderResult in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.stringKey("underflow");
            out.booleanValue(in.isUnderflow());
        }
        {
            out.stringKey("overflow");
            out.booleanValue(in.isOverflow());
        }
        {
            boolean malformed = in.isMalformed();
            out.stringKey("malformed");
            out.booleanValue(malformed);
            if (malformed) {
                out.stringKey("length");
                out.numberValue(in.length());
            }
        }
        {
            boolean unmappable = in.isUnmappable();
            out.stringKey("unmappable");
            out.booleanValue(unmappable);
            if (unmappable) {
                out.stringKey("length");
                out.numberValue(in.length());
            }
        }
        out.closeObject();
    }
}
