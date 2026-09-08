package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.font.FontRenderContext;

@CodeHistory(date = "2026/9/1")
public class FontMetricsTransformer implements JsonContextTransformer<FontMetrics> {

    static final FastString TRANSFORM = new FastString("transform");

    static final FastString TEXT_ANTIALIASING = new FastString("textAntialiasing");

    static final FastString FRACTIONAL_METRICS = new FastString("fractionalMetrics");

    static final FastString LEADING = new FastString("leading");

    static final FastString ASCENT = new FastString("ascent");

    static final FastString DESCENT = new FastString("descent");

    static final FastString HEIGHT = new FastString("height");

    static final FastString MAX_ASCENT = new FastString("maxAscent");

    static final FastString MAX_DESCENT = new FastString("maxDescent");

    static final FastString MAX_ADVANCE = new FastString("maxAdvance");

    static final FastString HAS_UNIFORM_LINE_METRICS = new FastString("hasUniformLineMetrics");

    static void transformExposed(FontRenderContext frc, @NotNull JsonHandler out, JsonContext context) {
        if (frc.isTransformed()) {
            out.key(TRANSFORM);
            AffineTransformTransformer.INSTANCE.transform(frc.getTransform(), out, context);
        }
        if (frc.isAntiAliased()) {
            out.key(TEXT_ANTIALIASING);
            Object value = frc.getAntiAliasingHint();
            if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_ON) {
                out.stringValue("on");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF) {
                out.stringValue("off");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
                out.stringValue("default");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_GASP) {
                out.stringValue("gasp");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB) {
                out.stringValue("LCD-HRGB");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR) {
                out.stringValue("LCD-HBGR");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB) {
                out.stringValue("LCD-VRGB");
            } else if (value == RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR) {
                out.stringValue("LCD-VBGR");
            } else if (value == RenderingHints.KEY_TEXT_LCD_CONTRAST) {
                out.stringValue("LCD-contrast");
            } else {
                out.stringValue("unknown");
            }
        }
        out.key(FRACTIONAL_METRICS);
        out.booleanValue(frc.usesFractionalMetrics());
    }

    @Override
    public void transform(FontMetrics in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(GlyphVectorTransformer.FONT);
            FontTransformer.INSTANCE.transform(in.getFont(), out, context);
            transformExposed(in.getFontRenderContext(), out, context);
            out.key(LEADING);
            out.numberValue(in.getLeading());
            out.key(ASCENT);
            out.numberValue(in.getAscent());
            out.key(DESCENT);
            out.numberValue(in.getDescent());
            out.key(HEIGHT);
            out.numberValue(in.getHeight());
            out.key(MAX_ASCENT);
            out.numberValue(in.getMaxAscent());
            out.key(MAX_DESCENT);
            out.numberValue(in.getMaxDescent());
            out.key(MAX_ADVANCE);
            out.numberValue(in.getMaxAdvance());
            out.key(HAS_UNIFORM_LINE_METRICS);
            out.booleanValue(in.hasUniformLineMetrics());
        }
        out.closeObject();
    }
}
