package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/5/16.
 */
public class AlphaCompositeTransformer implements JsonContextTransformer<AlphaComposite> {

    public static final AlphaCompositeTransformer INSTANCE = new AlphaCompositeTransformer();

    @Override
    public void transform(@NotNull AlphaComposite in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("alpha");
            out.numberValue(in.getAlpha());
        }
        {
            int rule = in.getRule();
            out.key("rule");
            switch (rule) {
                case AlphaComposite.CLEAR:
                    out.stringValue("clear");
                    break;
                case AlphaComposite.SRC:
                    out.stringValue("src");
                    break;
                case AlphaComposite.DST:
                    out.stringValue("dst");
                    break;
                case AlphaComposite.SRC_OVER:
                    out.stringValue("src-over");
                    break;
                case AlphaComposite.DST_OVER:
                    out.stringValue("dst-over");
                    break;
                case AlphaComposite.SRC_IN:
                    out.stringValue("src-in");
                    break;
                case AlphaComposite.DST_IN:
                    out.stringValue("dst-in");
                    break;
                case AlphaComposite.SRC_OUT:
                    out.stringValue("src-out");
                    break;
                case AlphaComposite.DST_OUT:
                    out.stringValue("dst-out");
                    break;
                case AlphaComposite.SRC_ATOP:
                    out.stringValue("src-atop");
                    break;
                case AlphaComposite.DST_ATOP:
                    out.stringValue("dst-atop");
                    break;
                case AlphaComposite.XOR:
                    out.stringValue("xor");
                    break;
                default:
                    out.numberValue(rule);
                    break;
            }
        }
        out.closeObject();
    }
}
