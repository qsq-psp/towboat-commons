package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ValueRange;

/**
 * Created on 2026/10/13.
 */
public class ValueRangeTransformer implements JsonContextTransformer<ValueRange> {

    public static final ValueRangeTransformer INSTANCE = new ValueRangeTransformer();

    @Override
    public void transform(ValueRange in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("fixed");
            out.booleanValue(in.isFixed());
            out.key("minSmallest");
            out.numberValue(in.getMinimum());
            out.key("minLargest");
            out.numberValue(in.getLargestMinimum());
            out.key("maxSmallest");
            out.numberValue(in.getSmallestMaximum());
            out.key("maxLargest");
            out.numberValue(in.getMaximum());
            out.key("int");
            out.booleanValue(in.isIntValue());
        }
        out.closeObject();
    }
}
