package mujica.json.provided.management;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.lang.management.MemoryUsage;

/**
 * Created on 2026/5/3.
 */
public class MemoryUsageTransformer implements JsonContextTransformer<MemoryUsage> {

    public static final MemoryUsageTransformer INSTANCE = new MemoryUsageTransformer();

    static final FastString INIT = new FastString("init");

    static final FastString USED = new FastString("used");

    static final FastString COMMITTED = new FastString("committed");

    static final FastString MAX = new FastString("max");

    @Override
    public void transform(@NotNull MemoryUsage in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(INIT);
            out.numberValue(in.getInit());
            out.stringKey(USED);
            out.numberValue(in.getUsed());
            out.stringKey(COMMITTED);
            out.numberValue(in.getCommitted());
            out.stringKey(MAX);
            out.numberValue(in.getMax());
        }
        out.closeObject();
    }
}
