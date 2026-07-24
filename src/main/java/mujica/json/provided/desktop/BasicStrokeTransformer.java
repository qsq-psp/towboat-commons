package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

@CodeHistory(date = "2026/4/28")
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
    public void transform(@NotNull BasicStroke basicStroke, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(WIDTH);
            out.numberValue(basicStroke.getLineWidth());
        }
        {
            int cap = basicStroke.getEndCap();
            out.key(CAP);
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
            int join = basicStroke.getLineJoin();
            out.key(JOIN);
            switch (join) {
                case BasicStroke.JOIN_MITER:
                    out.stringValue(MITER);
                    out.key(MITER_LIMIT);
                    out.numberValue(basicStroke.getMiterLimit());
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
            float[] dash = basicStroke.getDashArray();
            if (dash != null) {
                out.key(DASH);
                out.arrayValue(dash);
                out.key(DASH_PHASE);
                out.numberValue(basicStroke.getDashPhase());
            }
        }
        out.closeObject();
    }
}
