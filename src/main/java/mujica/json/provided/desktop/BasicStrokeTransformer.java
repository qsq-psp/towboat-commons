package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/4/28.
 */
public class BasicStrokeTransformer implements JsonContextTransformer<BasicStroke> {

    public static final BasicStrokeTransformer INSTANCE = new BasicStrokeTransformer();

    static final FastString WIDTH = new FastString("width");

    static final FastString CAP = new FastString("cap");

    static final FastString BUTT = new FastString("butt");

    static final FastString ROUND = new FastString("round");

    static final FastString SQUARE = new FastString("square");

    static final FastString JOIN = new FastString("join");

    static final FastString BEVEL = new FastString("bevel");

    static final FastString MITER = new FastString("miter");

    static final FastString MITER_LIMIT = new FastString("miterlimit");

    static final FastString DASH = new FastString("dash");

    static final FastString DASH_PHASE = new FastString("dashPhase");

    @Override
    public void transform(BasicStroke in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(WIDTH);
            out.numberValue(in.getLineWidth());
        }
        {
            int cap = in.getEndCap();
            out.stringKey(CAP);
            switch (cap) {
                case BasicStroke.CAP_BUTT:
                    out.stringValue(BUTT);
                    break;
                case BasicStroke.CAP_ROUND:
                    out.stringValue(ROUND);
                    break;
                case BasicStroke.CAP_SQUARE:
                    out.stringValue(SQUARE);
                    break;
                default:
                    // unexpected cap value
                    out.numberValue(cap);
                    break;
            }
        }
        {
            int join = in.getLineJoin();
            out.stringKey(JOIN);
            switch (join) {
                case BasicStroke.JOIN_MITER:
                    out.stringValue(MITER);
                    out.stringKey(MITER_LIMIT);
                    out.numberValue(in.getMiterLimit());
                    break;
                case BasicStroke.JOIN_ROUND:
                    out.stringValue(ROUND);
                    break;
                case BasicStroke.JOIN_BEVEL:
                    out.stringValue(BEVEL);
                    break;
                default:
                    // unexpected join value
                    out.numberValue(join);
                    break;
            }
        }
        {
            float[] dash = in.getDashArray();
            if (dash != null) {
                out.stringKey(DASH);
                out.arrayValue(dash);
                out.stringKey(DASH_PHASE);
                out.numberValue(in.getDashPhase());
            }
        }
        out.closeObject();
    }
}
