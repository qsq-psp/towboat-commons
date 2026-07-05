package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.util.IntSummaryStatistics;

/**
 * Created on 2026/5/3.
 */
public class IntSummaryStatisticsTransformer implements JsonContextTransformer<IntSummaryStatistics> {

    public static final IntSummaryStatisticsTransformer INSTANCE = new IntSummaryStatisticsTransformer();

    @Override
    public void transform(@NotNull IntSummaryStatistics in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("count");
            out.numberValue(in.getCount());
            out.stringKey("sum");
            out.numberValue(in.getSum());
            out.stringKey("min");
            out.numberValue(in.getMin());
            out.stringKey("max");
            out.numberValue(in.getMax());
            out.stringKey("average");
            out.numberValue(in.getAverage());
        }
        out.closeObject();
    }
}
