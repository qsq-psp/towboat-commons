package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "FontValueSerializer")
@CodeHistory(date = "2026/4/24")
public class FontTransformer implements JsonContextTransformer<Font>, JsonStructure {

    public static final FontTransformer INSTANCE = new FontTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString POSTSCRIPT_NAME = new FastString("postscriptName");

    static final FastString FONT_FACE_NAME = new FastString("fontFaceName");

    static final FastString FAMILY = new FastString("family");

    static final FastString SIZE = new FastString("size");

    static final FastString SIZE_2D = new FastString("size2D");

    static final FastString BOLD = new FastString("bold");

    static final FastString ITALIC = new FastString("italic");

    @Override
    public void transform(Font in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            String name = in.getFontName();
            if (name != null) {
                out.stringKey(NAME);
                out.stringValue(name);
            }
            name = in.getPSName();
            if (name != null) {
                out.stringKey(POSTSCRIPT_NAME);
                out.stringValue(name);
            }
            name = in.getFontName();
            if (name != null) {
                out.stringKey(FONT_FACE_NAME);
                out.stringValue(name);
            }
            name = in.getFamily();
            if (name != null) {
                out.stringKey(FAMILY);
                out.stringValue(name);
            }
            out.stringKey(SIZE);
            out.numberValue(in.getSize());
            out.stringKey(SIZE_2D);
            out.numberValue(in.getSize2D());
            out.stringKey(BOLD);
            out.booleanValue(in.isBold());
            out.stringKey(ITALIC);
            out.booleanValue(in.isItalic());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        final Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        jh.openArray();
        for (Font font : fonts) {
            transform(font, jh, null);
        }
        jh.closeArray();
    }
}
