package mujica.json.provided.management;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.management.MemoryUsage;

@CodeHistory(date = "2026/5/3")
public class MemoryUsageTransformer implements JsonContextTransformer<MemoryUsage> {

    public static final MemoryUsageTransformer INSTANCE = new MemoryUsageTransformer();

    static final FastString INIT = new FastString("init");

    static final FastString USED = new FastString("used");

    static final FastString COMMITTED = new FastString("committed");

    static final FastString MAX = new FastString("max");

    @Override
    public void transform(@NotNull MemoryUsage in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(INIT);
            out.numberValue(in.getInit());
            out.key(USED);
            out.numberValue(in.getUsed());
            out.key(COMMITTED);
            out.numberValue(in.getCommitted());
            out.key(MAX);
            out.numberValue(in.getMax());
        }
        out.closeObject();
    }
}
