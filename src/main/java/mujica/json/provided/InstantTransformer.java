package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Created on 2026/4/24.
 */
public class InstantTransformer implements JsonContextTransformer<Instant>, JsonStructure {

    public static final InstantTransformer INSTANCE = new InstantTransformer();

    static final FastString EPOCH_SECOND = new FastString("epochSecond");

    static final FastString NANO = new FastString("nano");

    static final FastString EPOCH_MILLI = new FastString("epochMilli");

    static final FastString ISO = new FastString("iso");

    @Override
    public void transform(Instant in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(EPOCH_SECOND);
            out.numberValue(in.getEpochSecond());
            out.stringKey(NANO);
            out.numberValue(in.getNano());
            out.stringKey(EPOCH_MILLI);
            out.numberValue(in.toEpochMilli());
            out.stringKey(ISO);
            out.stringValue(in.toString());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(Instant.now(), jh, null);
    }
}
