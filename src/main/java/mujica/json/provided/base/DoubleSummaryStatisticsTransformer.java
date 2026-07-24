package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.DoubleSummaryStatistics;

@CodeHistory(date = "2026/5/4")
public class DoubleSummaryStatisticsTransformer implements JsonContextTransformer<DoubleSummaryStatistics> {

    public static final DoubleSummaryStatisticsTransformer INSTANCE = new DoubleSummaryStatisticsTransformer();

    @Override
    public void transform(@NotNull DoubleSummaryStatistics statistics, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("count");
            out.numberValue(statistics.getCount());
            out.key("sum");
            out.numberValue(statistics.getSum());
            out.key("min");
            out.numberValue(statistics.getMin());
            out.key("max");
            out.numberValue(statistics.getMax());
            out.key("average");
            out.numberValue(statistics.getAverage());
        }
        out.closeObject();
    }
}
