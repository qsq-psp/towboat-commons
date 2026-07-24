package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;
import java.time.zone.ZoneRules;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "ZoneIdValueSerializer")
@CodeHistory(date = "2026/4/9")
public class ZoneIdTransformer implements JsonContextTransformer<ZoneId>, JsonStructure {

    public static final ZoneIdTransformer INSTANCE = new ZoneIdTransformer();

    static final FastString ID = new FastString("id");

    static final FastString FIXED_OFFSET = new FastString("fixedOffset");
    
    @Override
    public void transform(@NotNull ZoneId zoneId, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ID);
            out.stringValue(zoneId.getId());
            final ZoneRules zoneRules = zoneId.getRules();
            out.key(FIXED_OFFSET);
            out.booleanValue(zoneRules.isFixedOffset());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ZoneId.systemDefault(), jh, null);
    }
}
