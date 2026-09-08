package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

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
    public void transform(LineMetrics in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(CHAR_COUNT);
            out.numberValue(in.getNumChars());
            out.key(FontMetricsTransformer.ASCENT);
            out.numberValue(in.getAscent());
            out.key(FontMetricsTransformer.DESCENT);
            out.numberValue(in.getDescent());
            out.key(FontMetricsTransformer.LEADING);
            out.numberValue(in.getLeading());
            out.key(FontMetricsTransformer.HEIGHT);
            out.numberValue(in.getHeight());
            out.key(BASELINE_INDEX);
            out.numberValue(in.getBaselineIndex());
            out.key(BASELINE_OFFSETS);
            out.arrayValue(in.getBaselineOffsets());
            out.key(STRIKE_THROUGH_OFFSET);
            out.numberValue(in.getStrikethroughOffset());
            out.key(STRIKE_THROUGH_THICKNESS);
            out.numberValue(in.getStrikethroughThickness());
            out.key(UNDERLINE_OFFSET);
            out.numberValue(in.getUnderlineOffset());
            out.key(UNDERLINE_THICKNESS);
            out.numberValue(in.getUnderlineThickness());
        }
        out.closeObject();
    }
}
