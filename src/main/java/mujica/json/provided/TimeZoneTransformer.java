package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "TimeZoneValueSerializer")
@CodeHistory(date = "2026/4/12")
public class TimeZoneTransformer implements JsonContextTransformer<TimeZone>, JsonStructure {

    public static final TimeZoneTransformer INSTANCE = new TimeZoneTransformer();

    static final FastString RAW_OFFSET = new FastString("rawOffset");

    static final FastString ZONE_ID = new FastString("zoneId");

    static final FastString DISPLAY_NAME = new FastString("displayName");

    static final FastString DST_SAVINGS = new FastString("dstSavings");

    static final FastString USE_DAYLIGHT = new FastString("useDaylight");

    @Override
    public void transform(TimeZone in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(RAW_OFFSET);
            out.numberValue(in.getRawOffset());
            out.stringKey(ZoneIdTransformer.ID);
            out.stringValue(in.getID());
            out.stringKey(ZONE_ID);
            ZoneIdTransformer.INSTANCE.transform(in.toZoneId(), out, context);
            out.stringKey(DISPLAY_NAME);
            out.stringValue(in.getDisplayName());
            out.stringKey(DST_SAVINGS);
            out.numberValue(in.getDSTSavings());
            out.stringKey(USE_DAYLIGHT);
            out.booleanValue(in.useDaylightTime());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(TimeZone.getDefault(), jh, null);
    }
}
