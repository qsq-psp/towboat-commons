package mujica.json.provided.desktop;

import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "ColorModelValueSerializer")
@CodeHistory(date = "2026/4/25")
public class ColorModelTransformer implements JsonContextTransformer<ColorModel>, JsonStructure {

    public static final ColorModelTransformer INSTANCE = new ColorModelTransformer();

    @Override
    public void transform(ColorModel in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            boolean alpha = in.hasAlpha();
            out.key("alpha");
            out.booleanValue(alpha);
            if (alpha) {
                out.key("alphaPremultiplied");
                out.booleanValue(in.isAlphaPremultiplied());
            }
        }
        {
            int transferType = in.getTransferType();
            out.key("transferType");
            switch (transferType) {
                case DataBuffer.TYPE_BYTE:
                    out.stringValue("byte");
                    break;
                case DataBuffer.TYPE_USHORT:
                    out.stringValue("unsignedShort");
                    break;
                case DataBuffer.TYPE_SHORT:
                    out.stringValue("short");
                    break;
                case DataBuffer.TYPE_INT:
                    out.stringValue("int");
                    break;
                case DataBuffer.TYPE_FLOAT:
                    out.stringValue("float");
                    break;
                case DataBuffer.TYPE_DOUBLE:
                    out.stringValue("double");
                    break;
                case DataBuffer.TYPE_UNDEFINED:
                    out.stringValue("undefined");
                    break;
                default:
                    out.numberValue(transferType);
                    break;
            }
        }
        {
            out.key("pixelSize");
            out.numberValue(in.getPixelSize());
            out.key("componentCount");
            out.numberValue(in.getNumComponents());
            out.key("colorComponentCount");
            out.numberValue(in.getNumColorComponents());
            out.key("componentSizes");
            out.arrayValue(in.getComponentSize());
        }
        {
            int transparency = in.getTransparency();
            out.key("transparency");
            switch (transparency) {
                case Transparency.OPAQUE:
                    out.stringValue("opaque");
                    break;
                case Transparency.BITMASK:
                    out.stringValue("bitmask");
                    break;
                case Transparency.TRANSLUCENT:
                    out.stringValue("translucent");
                    break;
                default:
                    out.numberValue(transparency);
                    break;
            }
        }
        {
            out.key("colorSpace");
            ColorSpaceTransformer.INSTANCE.transform(in.getColorSpace(), out, context);
        }
        if (in instanceof DirectColorModel) {
            DirectColorModel direct = (DirectColorModel) in;
            out.key("redMask");
            out.numberValue(direct.getRedMask());
            out.key("greenMask");
            out.numberValue(direct.getGreenMask());
            out.key("blueMask");
            out.numberValue(direct.getBlueMask());
            out.key("alphaMask");
            out.numberValue(direct.getAlphaMask());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ColorModel.getRGBdefault(), jh, null);
    }
}
