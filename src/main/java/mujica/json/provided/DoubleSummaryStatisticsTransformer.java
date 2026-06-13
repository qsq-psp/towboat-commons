package mujica.json.provided;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.DoubleSummaryStatistics;

/**
 * Created on 2026/5/4.
 */
@CodeHistory(date = "2026/5/4")
public class DoubleSummaryStatisticsTransformer implements JsonContextTransformer<DoubleSummaryStatistics> {

    public static final DoubleSummaryStatisticsTransformer INSTANCE = new DoubleSummaryStatisticsTransformer();

    @Override
    public void transform(@NotNull DoubleSummaryStatistics in, @NotNull JsonHandler out, JsonContext context) {
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
