package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.color.ColorSpace;

/**
 * Created on 2026/5/3.
 */
@CodeHistory(date = "2026/5/3")
@SuppressWarnings("SpellCheckingInspection")
public class ColorSpaceTransformer implements JsonContextTransformer<ColorSpace> {

    public static final ColorSpaceTransformer INSTANCE = new ColorSpaceTransformer();

    @Override
    public void transform(ColorSpace in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            int type = in.getType();
            out.stringKey("type");
            switch (type) {
                case ColorSpace.TYPE_XYZ:
                    out.stringValue("XYZ");
                    break;
                case ColorSpace.TYPE_Lab:
                    out.stringValue("Lab");
                    break;
                case ColorSpace.TYPE_Luv:
                    out.stringValue("Luv");
                    break;
                case ColorSpace.TYPE_YCbCr:
                    out.stringValue("YCbCr");
                    break;
                case ColorSpace.TYPE_Yxy:
                    out.stringValue("Yxy");
                    break;
                case ColorSpace.TYPE_RGB:
                    out.stringValue("RGB");
                    break;
                case ColorSpace.TYPE_GRAY:
                    out.stringValue("GRAY");
                    break;
                case ColorSpace.TYPE_HSV:
                    out.stringValue("HSV");
                    break;
                case ColorSpace.TYPE_HLS:
                    out.stringValue("HLS");
                    break;
                case ColorSpace.TYPE_CMYK:
                    out.stringValue("CMYK");
                    break;
                case ColorSpace.TYPE_CMY:
                    out.stringValue("CMY");
                    break;
                case ColorSpace.TYPE_2CLR:
                    out.stringValue("2CLR");
                    break;
                case ColorSpace.TYPE_3CLR:
                    out.stringValue("3CLR");
                    break;
                case ColorSpace.TYPE_4CLR:
                    out.stringValue("4CLR");
                    break;
                case ColorSpace.TYPE_5CLR:
                    out.stringValue("5CLR");
                    break;
                case ColorSpace.TYPE_6CLR:
                    out.stringValue("6CLR");
                    break;
                case ColorSpace.TYPE_7CLR:
                    out.stringValue("7CLR");
                    break;
                case ColorSpace.TYPE_8CLR:
                    out.stringValue("8CLR");
                    break;
                case ColorSpace.TYPE_9CLR:
                    out.stringValue("9CLR");
                    break;
                case ColorSpace.TYPE_ACLR:
                    out.stringValue("ACLR");
                    break;
                case ColorSpace.TYPE_BCLR:
                    out.stringValue("BCLR");
                    break;
                case ColorSpace.TYPE_CCLR:
                    out.stringValue("CCLR");
                    break;
                case ColorSpace.TYPE_DCLR:
                    out.stringValue("DCLR");
                    break;
                case ColorSpace.TYPE_ECLR:
                    out.stringValue("ECLR");
                    break;
                case ColorSpace.TYPE_FCLR:
                    out.stringValue("FCLR");
                    break;
                default:
                    out.numberValue(type);
                    break;
            }
            out.stringKey("componentCount");
            out.numberValue(in.getNumComponents());
        }
        out.closeObject();
    }
}
