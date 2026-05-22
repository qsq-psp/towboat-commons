package mujica.json.provided.desktop;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.awt.font.GlyphVector;

/**
 * Created on 2026/5/21.
 */
public class GlyphVectorTransformer implements JsonContextTransformer<GlyphVector> {

    static final FastString FONT = new FastString("font");

    static final FastString GLYPH_COUNT = new FastString("glyphCount");

    static final FastString LAYOUT_FLAGS = new FastString("layoutFlags");

    @Override
    public void transform(@NotNull GlyphVector in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(FONT);
            FontTransformer.INSTANCE.transform(in.getFont(), out, context);
            out.stringKey(GLYPH_COUNT);
            out.numberValue(in.getNumGlyphs());
            out.stringKey(LAYOUT_FLAGS);
            out.numberValue(in.getLayoutFlags());
        }
        out.closeObject();
    }
}
