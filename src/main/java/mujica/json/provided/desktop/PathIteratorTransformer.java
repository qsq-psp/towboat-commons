package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.geom.PathIterator;

/**
 * Created on 2026/5/22.
 */
@CodeHistory(date = "2026/5/22")
public class PathIteratorTransformer implements JsonContextTransformer<PathIterator> {

    public static final PathIteratorTransformer INSTANCE = new PathIteratorTransformer();

    static final FastString WINDING_RULE = new FastString("windingRule");

    static final FastString EVEN_ODD = new FastString("evenOdd");

    static final FastString NON_ZERO = new FastString("nonZero");

    static final FastString SEGMENTS = new FastString("segments");

    static final FastString TYPE = new FastString("type");

    static final FastString MOVE_TO = new FastString("moveTo");

    static final FastString LINE_TO = new FastString("lineTo");

    static final FastString QUAD_TO = new FastString("quadTo");

    static final FastString X1 = new FastString("x1");

    static final FastString Y1 = new FastString("y1");

    static final FastString X2 = new FastString("x2");

    static final FastString Y2 = new FastString("y2");

    static final FastString CUBIC_TO = new FastString("cubicTo");

    static final FastString X3 = new FastString("x3");

    static final FastString Y3 = new FastString("y3");

    static final FastString CLOSE = new FastString("close");

    static void transformExposed(@NotNull PathIterator in, @NotNull JsonHandler out) {
        {
            int windingRule = in.getWindingRule();
            out.key(WINDING_RULE);
            switch (windingRule) {
                case PathIterator.WIND_EVEN_ODD:
                    out.stringValue(EVEN_ODD);
                    break;
                case PathIterator.WIND_NON_ZERO:
                    out.stringValue(NON_ZERO);
                    break;
                default:
                    out.numberValue(windingRule);
                    break;
            }
        }
        {
            float[] coords = new float[6];
            out.key(SEGMENTS);
            out.openArray();
            while (!in.isDone()) {
                int segmentType = in.currentSegment(coords);
                out.openObject();
                out.key(TYPE);
                switch (segmentType) {
                    case PathIterator.SEG_MOVETO:
                        out.stringValue(MOVE_TO);
                        out.key(RectangleTransformer.X);
                        out.numberValue(coords[0]);
                        out.key(RectangleTransformer.Y);
                        out.numberValue(coords[1]);
                        break;
                    case PathIterator.SEG_LINETO:
                        out.stringValue(LINE_TO);
                        out.key(RectangleTransformer.X);
                        out.numberValue(coords[0]);
                        out.key(RectangleTransformer.Y);
                        out.numberValue(coords[1]);
                        break;
                    case PathIterator.SEG_QUADTO:
                        out.stringValue(QUAD_TO);
                        out.key(X1);
                        out.numberValue(coords[0]);
                        out.key(Y1);
                        out.numberValue(coords[1]);
                        out.key(X2);
                        out.numberValue(coords[2]);
                        out.key(Y2);
                        out.numberValue(coords[3]);
                        break;
                    case PathIterator.SEG_CUBICTO:
                        out.stringValue(CUBIC_TO);
                        out.key(X1);
                        out.numberValue(coords[0]);
                        out.key(Y1);
                        out.numberValue(coords[1]);
                        out.key(X2);
                        out.numberValue(coords[2]);
                        out.key(Y2);
                        out.numberValue(coords[3]);
                        out.key(X3);
                        out.numberValue(coords[4]);
                        out.key(Y3);
                        out.numberValue(coords[5]);
                        break;
                    case PathIterator.SEG_CLOSE:
                        out.stringValue(CLOSE);
                        break;
                    default:
                        out.numberValue(segmentType);
                        break;
                }
                out.closeObject();
                in.next();
            }
            out.closeArray();
        }
    }

    @Override
    public void transform(@NotNull PathIterator in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        transformExposed(in, out);
        out.closeObject();
    }
}
