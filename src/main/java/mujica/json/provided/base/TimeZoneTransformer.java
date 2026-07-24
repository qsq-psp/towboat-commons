package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.TimeZone;

@CodeHistory(date = "2022/1/10", project = "infrastructure", name = "TimeZoneValue")
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
    public void transform(@NotNull TimeZone zone, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(RAW_OFFSET);
            out.numberValue(zone.getRawOffset());
            out.key(ZoneIdTransformer.ID);
            out.stringValue(zone.getID());
            out.key(ZONE_ID);
            ZoneIdTransformer.INSTANCE.transform(zone.toZoneId(), out, context);
            out.key(DISPLAY_NAME);
            out.stringValue(zone.getDisplayName());
            out.key(DST_SAVINGS);
            out.numberValue(zone.getDSTSavings());
            out.key(USE_DAYLIGHT);
            out.booleanValue(zone.useDaylightTime());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(TimeZone.getDefault(), jh, null);
    }
}
