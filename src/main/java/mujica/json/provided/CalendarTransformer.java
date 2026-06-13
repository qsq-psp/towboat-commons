package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

@SuppressWarnings("MagicConstant")
@CodeHistory(date = "2022/9/4", project = "Ultramarine", name = "CalendarValueSerializer")
@CodeHistory(date = "2026/5/7")
public class CalendarTransformer implements JsonContextTransformer<Calendar>, JsonStructure {

    public static final CalendarTransformer INSTANCE = new CalendarTransformer();

    static final FastString TIME = new FastString("time");

    static final FastString[] FIELDS = new FastString[Calendar.FIELD_COUNT];

    static {
        FIELDS[Calendar.ERA] = new FastString("era");
        FIELDS[Calendar.YEAR] = new FastString("year");
        FIELDS[Calendar.MONTH] = new FastString("month");
        FIELDS[Calendar.WEEK_OF_YEAR] = new FastString("weekOfYear");
        FIELDS[Calendar.WEEK_OF_MONTH] = new FastString("weekOfMonth");
        FIELDS[Calendar.DAY_OF_MONTH] = new FastString("dayOfMonth");
        FIELDS[Calendar.DAY_OF_YEAR] = new FastString("dayOfYear");
        FIELDS[Calendar.DAY_OF_WEEK] = new FastString("dayOfWeek");
        FIELDS[Calendar.DAY_OF_WEEK_IN_MONTH] = new FastString("dayOfWeekInMonth");
        FIELDS[Calendar.AM_PM] = new FastString("AM/PM");
        FIELDS[Calendar.HOUR] = new FastString("hour");
        FIELDS[Calendar.HOUR_OF_DAY] = new FastString("hourOfDay");
        FIELDS[Calendar.MINUTE] = new FastString("minute");
        FIELDS[Calendar.SECOND] = new FastString("second");
        FIELDS[Calendar.MILLISECOND] = new FastString("millisecond");
        FIELDS[Calendar.ZONE_OFFSET] = new FastString("zoneOffset");
        FIELDS[Calendar.DST_OFFSET] = new FastString("dstOffset");
    }

    static final FastString[] MONTH = new FastString[Calendar.UNDECIMBER + 1];

    static {
        MONTH[Calendar.JANUARY] = new FastString("January");
        MONTH[Calendar.FEBRUARY] = new FastString("February");
        MONTH[Calendar.MARCH] = new FastString("March");
        MONTH[Calendar.APRIL] = new FastString("April");
        MONTH[Calendar.MAY] = new FastString("May");
        MONTH[Calendar.JUNE] = new FastString("June");
        MONTH[Calendar.JULY] = new FastString("July");
        MONTH[Calendar.AUGUST] = new FastString("August");
        MONTH[Calendar.SEPTEMBER] = new FastString("September");
        MONTH[Calendar.OCTOBER] = new FastString("October");
        MONTH[Calendar.NOVEMBER] = new FastString("November");
        MONTH[Calendar.DECEMBER] = new FastString("December");
        MONTH[Calendar.UNDECIMBER] = new FastString("Undecimber");
    }

    static final FastString[] WEEK = new FastString[Calendar.SATURDAY + 1];

    static {
        WEEK[Calendar.SUNDAY] = new FastString("Sunday");
        WEEK[Calendar.MONDAY] = new FastString("Monday");
        WEEK[Calendar.TUESDAY] = new FastString("Tuesday");
        WEEK[Calendar.WEDNESDAY] = new FastString("Wednesday");
        WEEK[Calendar.THURSDAY] = new FastString("Thursday");
        WEEK[Calendar.FRIDAY] = new FastString("Friday");
        WEEK[Calendar.SATURDAY] = new FastString("Saturday");
    }

    static final FastString[] AM_PM = new FastString[Calendar.PM + 1];

    static {
        AM_PM[Calendar.AM] = new FastString("AM");
        AM_PM[Calendar.PM] = new FastString("PM");
    }

    @Override
    public void transform(@NotNull Calendar in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(TIME);
            out.numberValue(in.getTimeInMillis());
            for (int fieldIndex = 0; fieldIndex < Calendar.FIELD_COUNT; fieldIndex++) {
                out.stringKey(FIELDS[fieldIndex]);
                long fieldValue = in.get(fieldIndex);
                switch (fieldIndex) {
                    case Calendar.MONTH:
                        out.stringValue(MONTH[(int) fieldValue]);
                        break;
                    case Calendar.DAY_OF_WEEK:
                        out.stringValue(WEEK[(int) fieldValue]);
                        break;
                    case Calendar.AM_PM:
                        out.stringValue(AM_PM[(int) fieldValue]);
                        break;
                    default:
                        out.numberValue(fieldValue);
                        break;
                }
            }
            out.stringKey(SimpleDateFormatTransformer.TIME_ZONE);
            TimeZoneTransformer.INSTANCE.transform(in.getTimeZone(), out, context);
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Calendar.getInstance(), jh, null);
    }
}
