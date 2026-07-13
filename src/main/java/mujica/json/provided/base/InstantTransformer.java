package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@CodeHistory(date = "2026/4/24")
public class InstantTransformer implements JsonContextTransformer<Instant>, JsonStructure {

    public static final InstantTransformer INSTANCE = new InstantTransformer();

    static final FastString EPOCH_SECOND = new FastString("epochSecond");

    static final FastString NANO = new FastString("nano");

    static final FastString EPOCH_MILLI = new FastString("epochMilli");

    static final FastString ISO = new FastString("iso");

    @Override
    public void transform(@NotNull Instant instant, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(EPOCH_SECOND);
            out.numberValue(instant.getEpochSecond());
            out.key(NANO);
            out.numberValue(instant.getNano());
            out.key(EPOCH_MILLI);
            out.numberValue(instant.toEpochMilli());
            out.key(ISO);
            out.stringValue(instant.toString());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Instant.now(), jh, null);
    }
}
