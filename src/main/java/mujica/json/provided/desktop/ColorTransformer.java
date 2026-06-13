package mujica.json.provided.desktop;

import mujica.io.codec.Base16Case;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on 2026/5/15.
 */
@CodeHistory(date = "2026/5/15")
public class ColorTransformer implements JsonContextTransformer<Color>, Base16Case {

    public static final ColorTransformer INSTANCE = new ColorTransformer();

    @Override
    public void transform(@NotNull Color in, @NotNull JsonHandler out, JsonContext context) {
        final int argb = in.getRGB();
        int charIndex;
        if ((argb & 0xff000000) == 0xff000000) {
            charIndex = 6;
        } else {
            charIndex = 8;
        }
        final char[] ca = new char[charIndex + 1];
        for (int bitIndex = 0; charIndex > 0; bitIndex += 4) {
            int nibble = 0xf & (argb >> bitIndex);
            if (nibble < 0xa) {
                nibble += '0';
            } else {
                nibble += LOWER_CONSTANT;
            }
            ca[charIndex--] = (char) nibble;
        }
        ca[0] = '#';
        out.stringValue(new String(ca));
    }
}
