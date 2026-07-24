package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.AffineTransform;

@CodeHistory(date = "2026/5/14")
public class GraphicsTransformer implements JsonContextTransformer<Graphics> {

    public static final GraphicsTransformer INSTANCE = new GraphicsTransformer();

    @Override
    public void transform(@NotNull Graphics graphics, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            Color color = graphics.getColor();
            if (color != null) {
                out.key("color");
                ColorTransformer.INSTANCE.transform(color, out, context);
            }
        }
        {
            Font font = graphics.getFont();
            if (font != null) {
                out.key(GlyphVectorTransformer.FONT);
                FontTransformer.INSTANCE.transform(font, out, context);
            }
        }
        {
            Rectangle clipBounds = graphics.getClipBounds();
            if (clipBounds != null) {
                out.key("clipBounds");
                RectangleTransformer.INSTANCE.transform(clipBounds, out, context);
            }
        }
        if (graphics instanceof Graphics2D) {
            transformExposed2D((Graphics2D) graphics, out, context);
        }
        out.closeObject();
    }

    private void transformExposed2D(@NotNull Graphics2D graphics2D, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.key("configuration");
        GraphicsConfigurationTransformer.INSTANCE.transform(graphics2D.getDeviceConfiguration(), out, context);
        transformExposedRenderingHints(graphics2D, out);
        {
            AffineTransform transform = graphics2D.getTransform();
            if (transform != null && !transform.isIdentity()) {
                out.key("transform");
                AffineTransformTransformer.INSTANCE.transform(transform, out, context);
            }
        }
        {
            Composite composite = graphics2D.getComposite();
            if (composite != null) {
                out.key("composite");
                if (composite instanceof AlphaComposite) {
                    AlphaCompositeTransformer.INSTANCE.transform((AlphaComposite) composite, out, context);
                } else {
                    out.stringValue(composite.getClass().getName());
                }
            }
        }
        {
            Color background = graphics2D.getBackground();
            if (background != null) {
                out.key("background");
                ColorTransformer.INSTANCE.transform(background, out, context);
            }
        }
        {
            Stroke stroke = graphics2D.getStroke();
            if (stroke != null) {
                out.key("stroke");
                if (stroke instanceof BasicStroke) {
                    BasicStrokeTransformer.INSTANCE.transform((BasicStroke) stroke, out, context);
                } else {
                    out.stringValue(stroke.getClass().getName());
                }
            }
        }
    }

    private void transformExposedRenderingHints(@NotNull Graphics2D in, @NotNull JsonHandler out) {
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            out.key("antialiasing");
            if (value == RenderingHints.VALUE_ANTIALIAS_ON) {
                out.stringValue("on");
            } else if (value == RenderingHints.VALUE_ANTIALIAS_OFF) {
                out.stringValue("off");
            } else if (value == RenderingHints.VALUE_ANTIALIAS_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_RENDERING);
            out.key("rendering");
            if (value == RenderingHints.VALUE_RENDER_SPEED) {
                out.stringValue("speed");
            } else if (value == RenderingHints.VALUE_RENDER_QUALITY) {
                out.stringValue("quality");
            } else if (value == RenderingHints.VALUE_RENDER_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_DITHERING);
            out.key("dithering");
            if (value == RenderingHints.VALUE_DITHER_DISABLE) {
                out.stringValue("disable");
            } else if (value == RenderingHints.VALUE_DITHER_ENABLE) {
                out.stringValue("enable");
            } else if (value == RenderingHints.VALUE_DITHER_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
            out.key("textAntialiasing");
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
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS);
            out.key("fractionalMetrics");
            if (value == RenderingHints.VALUE_FRACTIONALMETRICS_OFF) {
                out.stringValue("off");
            } else if (value == RenderingHints.VALUE_FRACTIONALMETRICS_ON) {
                out.stringValue("on");
            } else if (value == RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
            out.key("interpolation");
            if (value == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
                out.stringValue("nearestNeighbor");
            } else if (value == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
                out.stringValue("bilinear");
            } else if (value == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
                out.stringValue("bicubic");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION);
            out.key("alphaInterpolation");
            if (value == RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED) {
                out.stringValue("speed");
            } else if (value == RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY) {
                out.stringValue("quality");
            } else if (value == RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_COLOR_RENDERING);
            out.key("colorRendering");
            if (value == RenderingHints.VALUE_COLOR_RENDER_SPEED) {
                out.stringValue("speed");
            } else if (value == RenderingHints.VALUE_COLOR_RENDER_QUALITY) {
                out.stringValue("quality");
            } else if (value == RenderingHints.VALUE_COLOR_RENDER_DEFAULT) {
                out.stringValue("default");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);
            out.key("strokeControl");
            if (value == RenderingHints.VALUE_STROKE_DEFAULT) {
                out.stringValue("default");
            } else if (value == RenderingHints.VALUE_STROKE_NORMALIZE) {
                out.stringValue("normalize");
            } else if (value == RenderingHints.VALUE_STROKE_PURE) {
                out.stringValue("pure");
            } else {
                out.stringValue("unknown");
            }
        }
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT);
            out.key("resolutionVariant");
            if (value == RenderingHints.VALUE_RESOLUTION_VARIANT_DEFAULT) {
                out.stringValue("default");
            } else if (value == RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT) {
                out.stringValue("sizeFit");
            } else if (value == RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT) {
                out.stringValue("dpiFit");
            } else {
                out.stringValue("unknown");
            }
        }
    }
}
