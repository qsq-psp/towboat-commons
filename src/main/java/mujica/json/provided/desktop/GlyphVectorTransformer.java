package mujica.json.provided.desktop;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.font.GlyphVector;

@CodeHistory(date = "2026/5/21")
public class GlyphVectorTransformer implements JsonContextTransformer<GlyphVector> {

    static final FastString FONT = new FastString("font");

    static final FastString GLYPH_COUNT = new FastString("glyphCount");

    static final FastString LAYOUT_FLAGS = new FastString("layoutFlags");

    @Override
    public void transform(@NotNull GlyphVector glyphVector, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(FONT);
            FontTransformer.INSTANCE.transform(glyphVector.getFont(), out, context);
            out.key(GLYPH_COUNT);
            out.numberValue(glyphVector.getNumGlyphs());
            out.key(LAYOUT_FLAGS);
            out.numberValue(glyphVector.getLayoutFlags());
        }
        out.closeObject();
    }
}
