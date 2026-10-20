package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

@CodeHistory(date = "2026/4/26")
public class DecimalFormatTransformer implements JsonContextTransformer<DecimalFormat> {

    @Override
    public void transform(@NotNull DecimalFormat format, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("decimalFormatSymbols");
            DecimalFormatSymbolsTransformer.INSTANCE.transform(format.getDecimalFormatSymbols(), out, context);
            out.key("pattern");
            out.stringValue(format.toPattern());
            out.key("localizedPattern");
            out.stringValue(format.toLocalizedPattern());
        }
        out.closeObject();
    }
}
