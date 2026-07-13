package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.UIResource;
import java.awt.*;

@CodeHistory(date = "2022/9/1", project = "Ultramarine", name = "FontValueSerializer")
@CodeHistory(date = "2026/4/24")
public class FontTransformer implements JsonContextTransformer<Font>, JsonStructure {

    public static final FontTransformer INSTANCE = new FontTransformer();

    static final FastString POSTSCRIPT_NAME = new FastString("postscriptName");

    static final FastString FONT_FACE_NAME = new FastString("fontFaceName");

    static final FastString FAMILY = new FastString("family");

    static final FastString SIZE = new FastString("size");

    static final FastString SIZE_2D = new FastString("size2D");

    static final FastString BOLD = new FastString("bold");

    static final FastString ITALIC = new FastString("italic");

    @Override
    public void transform(@NotNull Font font, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            String name = font.getFontName();
            if (name != null) {
                out.key(DataFlavorTransformer.NAME);
                out.stringValue(name);
            }
            name = font.getPSName();
            if (name != null) {
                out.key(POSTSCRIPT_NAME);
                out.stringValue(name);
            }
            name = font.getFontName();
            if (name != null) {
                out.key(FONT_FACE_NAME);
                out.stringValue(name);
            }
            name = font.getFamily();
            if (name != null) {
                out.key(FAMILY);
                out.stringValue(name);
            }
            out.key(SIZE);
            out.numberValue(font.getSize());
            out.key(SIZE_2D);
            out.numberValue(font.getSize2D());
            out.key(BOLD);
            out.booleanValue(font.isBold());
            out.key(ITALIC);
            out.booleanValue(font.isItalic());
        }
        if (font instanceof UIResource) {
            out.key(InsetsTransformer.UI_RESOURCE);
            out.booleanValue(true);
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
