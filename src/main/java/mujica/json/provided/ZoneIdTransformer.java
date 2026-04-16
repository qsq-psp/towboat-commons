package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.zone.ZoneRules;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "ZoneIdValueSerializer")
@CodeHistory(date = "2026/4/9")
public class ZoneIdTransformer implements JsonContextTransformer<ZoneId>, JsonStructure {

    public static final ZoneIdTransformer INSTANCE = new ZoneIdTransformer();

    static final FastString ID = new FastString("id");

    static final FastString FIXED_OFFSET = new FastString("fixedOffset");
    
    @Override
    public void transform(ZoneId in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ID);
            out.stringValue(in.getId());
            final ZoneRules zoneRules = in.getRules();
            out.stringKey(FIXED_OFFSET);
            out.booleanValue(zoneRules.isFixedOffset());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(ZoneId.systemDefault(), jh, null);
    }
}
