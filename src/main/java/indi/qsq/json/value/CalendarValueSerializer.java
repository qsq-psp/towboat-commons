package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.value.EnumMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * Created on 2022/9/4.
 */
@SuppressWarnings("unused")
public class CalendarValueSerializer implements ValueSerializer<Calendar>, JsonStructure {

    public static final CalendarValueSerializer INSTANCE = new CalendarValueSerializer();

    public static final EnumMapping FIELDS = (new EnumMapping())
            .add("era", Calendar.ERA)
            .add("year", Calendar.YEAR)
            .add("month", Calendar.MONTH)
            .add("week-of-year", Calendar.WEEK_OF_YEAR)
            .add("week-of-month", Calendar.WEEK_OF_MONTH)
            .add("date", Calendar.DATE)
            .add("day-of-month", Calendar.DAY_OF_MONTH)
            .add("day-of-year", Calendar.DAY_OF_YEAR)
            .add("day-of-week", Calendar.DAY_OF_WEEK)
            .add("day-of-week-in-month", Calendar.DAY_OF_WEEK_IN_MONTH)
            .add("AM/PM", Calendar.AM_PM)
            .add("hour-of-day", Calendar.HOUR_OF_DAY)
            .add("minute", Calendar.MINUTE)
            .add("second", Calendar.SECOND)
            .addDefault("millisecond", Calendar.MILLISECOND)
            .add("zone-offset", Calendar.ZONE_OFFSET)
            .add("DST-offset", Calendar.DST_OFFSET);

    public static final EnumMapping WEEK = (new EnumMapping())
            .addDefault("Sunday", Calendar.SUNDAY)
            .add("Monday", Calendar.MONDAY)
            .add("Tuesday", Calendar.TUESDAY)
            .add("Wednesday", Calendar.WEDNESDAY)
            .add("Thursday", Calendar.THURSDAY)
            .add("Friday", Calendar.FRIDAY)
            .add("Saturday", Calendar.SATURDAY);

    public static final EnumMapping MONTH = (new EnumMapping())
            .addDefault("January", Calendar.JANUARY)
            .add("February", Calendar.FEBRUARY)
            .add("March", Calendar.MARCH)
            .add("April", Calendar.APRIL)
            .add("May", Calendar.MAY)
            .add("June", Calendar.JUNE)
            .add("July", Calendar.JULY)
            .add("August", Calendar.AUGUST)
            .add("September", Calendar.SEPTEMBER)
            .add("October", Calendar.OCTOBER)
            .add("November", Calendar.NOVEMBER)
            .add("December", Calendar.DECEMBER)
            .add("Undecimber", Calendar.UNDECIMBER);

    void serialize(String key, Calendar value, @NotNull JsonConsumer jc) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("time");
        jc.numberValue(value.getTimeInMillis());
        for (int fieldIndex : FIELDS.values()) {
            long fieldValue = value.get(fieldIndex);
            jc.key(FIELDS.forKey(fieldIndex));
            switch (fieldIndex) {
                case Calendar.MONTH:
                    jc.stringValue(MONTH.forKey((int) fieldValue));
                    break;
                case Calendar.DAY_OF_WEEK:
                    jc.stringValue(WEEK.forKey((int) fieldValue));
                    break;
                case Calendar.AM_PM:
                    jc.stringValue(fieldValue == Calendar.AM ? "AM" : "PM");
                    break;
                default:
                    jc.numberValue(fieldValue);
                    break;
            }
        }
        TimeZoneValueSerializer.INSTANCE.serialize("timeZone", value.getTimeZone(), jc);
        jc.closeObject();
    }

    @Override
    public void serialize(String key, Calendar value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        serialize(key, value, jc);
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, Calendar.getInstance(), jc);
    }
}
