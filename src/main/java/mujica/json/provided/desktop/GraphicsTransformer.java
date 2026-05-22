package mujica.json.provided.desktop;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created on 2026/5/14.
 */
public class GraphicsTransformer implements JsonContextTransformer<Graphics> {

    public static final GraphicsTransformer INSTANCE = new GraphicsTransformer();

    @Override
    public void transform(@NotNull Graphics in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            Color color = in.getColor();
            if (color != null) {
                out.stringKey("color");
                ColorTransformer.INSTANCE.transform(color, out, context);
            }
        }
        {
            Font font = in.getFont();
            if (font != null) {
                out.stringKey(GlyphVectorTransformer.FONT);
                FontTransformer.INSTANCE.transform(font, out, context);
            }
        }
        {
            Rectangle clipBounds = in.getClipBounds();
            if (clipBounds != null) {
                out.stringKey("clipBounds");
                RectangleTransformer.INSTANCE.transform(clipBounds, out, context);
            }
        }
        if (in instanceof Graphics2D) {
            transformExposed2D((Graphics2D) in, out, context);
        }
        out.closeObject();
    }

    private void transformExposed2D(@NotNull Graphics2D in, @NotNull JsonHandler out, JsonContext context) {
        out.stringKey("configuration");
        GraphicsConfigurationTransformer.INSTANCE.transform(in.getDeviceConfiguration(), out, context);
        transformExposedRenderingHints(in, out, context);
        {
            AffineTransform transform = in.getTransform();
            if (transform != null && !transform.isIdentity()) {
                out.stringKey("transform");
                AffineTransformTransformer.INSTANCE.transform(transform, out, context);
            }
        }
        {
            Composite composite = in.getComposite();
            if (composite != null) {
                out.stringKey("composite");
                if (composite instanceof AlphaComposite) {
                    AlphaCompositeTransformer.INSTANCE.transform((AlphaComposite) composite, out, context);
                } else {
                    out.stringValue(composite.getClass().getName());
                }
            }
        }
        {
            Color background = in.getBackground();
            if (background != null) {
                out.stringKey("background");
                ColorTransformer.INSTANCE.transform(background, out, context);
            }
        }
        {
            Stroke stroke = in.getStroke();
            if (stroke != null) {
                out.stringKey("stroke");
                if (stroke instanceof BasicStroke) {
                    BasicStrokeTransformer.INSTANCE.transform((BasicStroke) stroke, out, context);
                } else {
                    out.stringValue(stroke.getClass().getName());
                }
            }
        }
    }

    private void transformExposedRenderingHints(@NotNull Graphics2D in, @NotNull JsonHandler out, JsonContext context) {
        {
            Object value = in.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            out.stringKey("antialiasing");
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
            out.stringKey("rendering");
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
            out.stringKey("dithering");
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
            out.stringKey("textAntialiasing");
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
            out.stringKey("fractionalMetrics");
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
            out.stringKey("fractionalMetrics");
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
            out.stringKey("alphaInterpolation");
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
            out.stringKey("colorRendering");
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
            out.stringKey("strokeControl");
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
            out.stringKey("resolutionVariant");
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
