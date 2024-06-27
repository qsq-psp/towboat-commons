package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.api.SerializeFrom;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2022/9/1.
 */
@SuppressWarnings("unused")
public class FontValueSerializer implements ValueSerializer<Font>, JsonStructure {

    public static final FontValueSerializer INSTANCE = new FontValueSerializer();

    @Override
    public void serialize(String key, Font value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        String name = value.getFontName();
        if (name != null) {
            jc.key("name");
            jc.stringValue(name);
        } else if (cc.anySerializeConfig(SerializeFrom.NULL, true)) {
            jc.key("name");
            jc.nullValue();
        }
        String postscriptName = value.getPSName();
        if (postscriptName != null) {
            if (!postscriptName.equals(name) || !cc.anySerializeConfig(SerializeFrom.SPECIFIC, true)) {
                jc.key("postscriptName");
                jc.stringValue(postscriptName);
            }
        } else {
            jc.key("postscriptName");
            jc.nullValue();
        }
        String fontFaceName = value.getFontName();
        if (fontFaceName != null) {
            if (!fontFaceName.equals(name) || !cc.anySerializeConfig(SerializeFrom.SPECIFIC, true)) {
                jc.key("fontFaceName");
                jc.stringValue(fontFaceName);
            }
        } else {
            jc.key("fontFaceName");
            jc.nullValue();
        }
        String family = value.getFamily();
        if (family != null) {
            if (!family.equals(name) || !cc.anySerializeConfig(SerializeFrom.SPECIFIC, true)) {
                jc.key("family");
                jc.stringValue(family);
            }
        } else {
            jc.key("family");
            jc.nullValue();
        }
        jc.key("size");
        jc.numberValue(value.getSize());
        jc.key("size2D");
        jc.numberValue(value.getSize2D());
        jc.key("bold");
        jc.booleanValue(value.isBold());
        jc.key("italic");
        jc.booleanValue(value.isItalic());
        jc.closeObject();
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        final Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        final ConversionConfig cc = new ConversionConfig();
        final JsonSerializer js = new JsonSerializer();
        jc.openArray();
        for (Font font : fonts) {
            serialize(null, font, jc, cc, js);
        }
        jc.closeArray();
    }
}
