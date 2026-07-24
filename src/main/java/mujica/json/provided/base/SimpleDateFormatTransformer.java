package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

@CodeHistory(date = "2026/5/9")
public class SimpleDateFormatTransformer implements JsonContextTransformer<SimpleDateFormat> {

    public static final SimpleDateFormatTransformer INSTANCE = new SimpleDateFormatTransformer();

    static final FastString TIME_ZONE = new FastString("timeZone");

    @Override
    public void transform(@NotNull SimpleDateFormat format, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.CLASS);
            out.stringValue(format.getClass().getName());
            out.key(TIME_ZONE);
            TimeZoneTransformer.INSTANCE.transform(format.getTimeZone(), out, context);
            out.key("pattern");
            out.stringValue(format.toPattern());
        }
        out.closeObject();
    }
}
