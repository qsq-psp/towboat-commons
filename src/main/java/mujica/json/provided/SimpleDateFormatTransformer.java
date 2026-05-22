package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

/**
 * Created on 2026/5/9.
 */
public class SimpleDateFormatTransformer implements JsonContextTransformer<SimpleDateFormat> {

    public static final SimpleDateFormatTransformer INSTANCE = new SimpleDateFormatTransformer();

    static final FastString TIME_ZONE = new FastString("timeZone");

    @Override
    public void transform(@NotNull SimpleDateFormat in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.CLASS);
            out.stringValue(in.getClass().getName());
            out.stringKey(TIME_ZONE);
            TimeZoneTransformer.INSTANCE.transform(in.getTimeZone(), out, context);
            out.stringKey("pattern");
            out.stringValue(in.toPattern());
        }
        out.closeObject();
    }
}
