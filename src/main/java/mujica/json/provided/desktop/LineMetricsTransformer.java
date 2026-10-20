package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.font.LineMetrics;

@CodeHistory(date = "2026/9/2")
public class LineMetricsTransformer implements JsonContextTransformer<LineMetrics> {

    static final FastString CHAR_COUNT = new FastString("charCount");

    static final FastString BASELINE_INDEX = new FastString("baselineIndex");

    static final FastString BASELINE_OFFSETS = new FastString("baselineOffsets");

    static final FastString STRIKE_THROUGH_OFFSET = new FastString("strikeThroughOffset");

    static final FastString STRIKE_THROUGH_THICKNESS = new FastString("strikeThroughThickness");

    static final FastString UNDERLINE_OFFSET = new FastString("underlineOffset");

    static final FastString UNDERLINE_THICKNESS = new FastString("underlineThickness");

    @Override
    public void transform(LineMetrics lineMetrics, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(CHAR_COUNT);
            out.numberValue(lineMetrics.getNumChars());
            out.key(FontMetricsTransformer.ASCENT);
            out.numberValue(lineMetrics.getAscent());
            out.key(FontMetricsTransformer.DESCENT);
            out.numberValue(lineMetrics.getDescent());
            out.key(FontMetricsTransformer.LEADING);
            out.numberValue(lineMetrics.getLeading());
            out.key(FontMetricsTransformer.HEIGHT);
            out.numberValue(lineMetrics.getHeight());
            out.key(BASELINE_INDEX);
            out.numberValue(lineMetrics.getBaselineIndex());
            out.key(BASELINE_OFFSETS);
            out.arrayValue(lineMetrics.getBaselineOffsets());
            out.key(STRIKE_THROUGH_OFFSET);
            out.numberValue(lineMetrics.getStrikethroughOffset());
            out.key(STRIKE_THROUGH_THICKNESS);
            out.numberValue(lineMetrics.getStrikethroughThickness());
            out.key(UNDERLINE_OFFSET);
            out.numberValue(lineMetrics.getUnderlineOffset());
            out.key(UNDERLINE_THICKNESS);
            out.numberValue(lineMetrics.getUnderlineThickness());
        }
        out.closeObject();
    }
}
