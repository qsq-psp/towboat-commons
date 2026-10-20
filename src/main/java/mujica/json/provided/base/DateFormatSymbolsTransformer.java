package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormatSymbols;

/**
 * Created on 2026/10/13.
 */
public class DateFormatSymbolsTransformer implements JsonContextTransformer<DateFormatSymbols> {

    public static final DateFormatSymbolsTransformer INSTANCE = new DateFormatSymbolsTransformer();

    @Override
    public void transform(DateFormatSymbols in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("eras");
            out.arrayValue(in.getEras());
            out.key("months");
            out.arrayValue(in.getMonths());
            out.key("shortMonths");
            out.arrayValue(in.getShortMonths());
            out.key("weekdays");
            out.arrayValue(in.getWeekdays());
            out.key("shortWeekdays");
            out.arrayValue(in.getShortWeekdays());
            out.key("AM/PM");
            out.arrayValue(in.getAmPmStrings());
        }
        out.closeObject();
    }
}
