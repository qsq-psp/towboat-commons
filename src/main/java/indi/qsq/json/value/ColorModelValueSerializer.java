package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;

/**
 * Created on 2022/9/1.
 */
public class ColorModelValueSerializer implements ValueSerializer<ColorModel>, JsonStructure {

    public static final ColorModelValueSerializer INSTANCE = new ColorModelValueSerializer();

    public static final EnumMapping TRANSPARENCY = (new EnumMapping())
            .addDefault("opaque", Transparency.OPAQUE)
            .add("bit-mask", Transparency.BITMASK)
            .add("translucent", Transparency.TRANSLUCENT);

    public static final EnumMapping TRANSFER_TYPE = (new EnumMapping())
            .add("byte", DataBuffer.TYPE_BYTE)
            .add("unsigned-short", DataBuffer.TYPE_USHORT)
            .add("short", DataBuffer.TYPE_SHORT)
            .add("int", DataBuffer.TYPE_INT)
            .add("float", DataBuffer.TYPE_FLOAT)
            .add("double", DataBuffer.TYPE_DOUBLE)
            .add("undefined", DataBuffer.TYPE_UNDEFINED);

    void serialize(String key, ColorModel value, @NotNull JsonConsumer jc) {
        if (value == null) {
            return; // this ValueSerializer is special; it checks null value; null to undefined
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("alpha");
        jc.booleanValue(value.hasAlpha());
        jc.key("alphaPremultiplied");
        jc.booleanValue(value.isAlphaPremultiplied());
        jc.key("transfer-type");
        jc.stringValue(TRANSFER_TYPE.forKey(value.getTransferType()));
        jc.key("pixel-size");
        jc.numberValue(value.getPixelSize());
        jc.key("component-size");
        jc.arrayValue(value.getComponentSize());
        jc.key("transparency");
        jc.stringValue(TRANSPARENCY.forKey(value.getTransparency()));
        ColorSpaceValueSerializer.INSTANCE.serialize("colorSpace", value.getColorSpace(), jc);
        if (value instanceof DirectColorModel) {
            DirectColorModel direct = (DirectColorModel) value;
            jc.key("redMask");
            jc.numberValue(direct.getRedMask());
            jc.key("greenMask");
            jc.numberValue(direct.getGreenMask());
            jc.key("blueMask");
            jc.numberValue(direct.getBlueMask());
            jc.key("alphaMask");
            jc.numberValue(direct.getAlphaMask());
        }
        jc.closeObject();
    }

    @Override
    public void serialize(String key, ColorModel value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        INSTANCE.serialize(null, ColorModel.getRGBdefault(), jc);
    }
}
