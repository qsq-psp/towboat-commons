package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
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
    public void transform(Locale.LanguageRange in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(RANGE);
            out.stringValue(in.getRange());
            out.stringKey(WEIGHT);
            out.numberValue(in.getWeight());
        }
        out.closeObject();
    }
}
