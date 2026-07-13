package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CodeHistory(date = "2022/7/19", project = "Ultramarine", name = "LanguageRangeValueSerializer")
@CodeHistory(date = "2026/4/22")
public class LanguageRangeTransformer implements JsonContextTransformer<Locale.LanguageRange> {

    public static final LanguageRangeTransformer INSTANCE = new LanguageRangeTransformer();

    static final FastString RANGE = new FastString("range");

    static final FastString WEIGHT = new FastString("weight");

    @Override
    public void transform(@NotNull Locale.LanguageRange range, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(RANGE);
            out.stringValue(range.getRange());
            out.key(WEIGHT);
            out.numberValue(range.getWeight());
        }
        out.closeObject();
    }
}
