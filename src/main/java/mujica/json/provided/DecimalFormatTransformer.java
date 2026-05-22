package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

/**
 * Created on 2026/4/26.
 */
public class DecimalFormatTransformer implements JsonContextTransformer<DecimalFormat> {

    @Override
    public void transform(DecimalFormat in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("decimalFormatSymbols");
            DecimalFormatSymbolsTransformer.INSTANCE.transform(in.getDecimalFormatSymbols(), out, context);
            out.stringKey("pattern");
            out.stringValue(in.toPattern());
            out.stringKey("localizedPattern");
            out.stringValue(in.toLocalizedPattern());
        }
        out.closeObject();
    }
}
