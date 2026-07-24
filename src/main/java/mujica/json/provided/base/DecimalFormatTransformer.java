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
    public void transform(@NotNull DecimalFormat in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("decimalFormatSymbols");
            DecimalFormatSymbolsTransformer.INSTANCE.transform(in.getDecimalFormatSymbols(), out, context);
            out.key("pattern");
            out.stringValue(in.toPattern());
            out.key("localizedPattern");
            out.stringValue(in.toLocalizedPattern());
        }
        out.closeObject();
    }
}
