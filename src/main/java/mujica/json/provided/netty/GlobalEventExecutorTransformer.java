package mujica.json.provided.netty;

import io.netty.util.concurrent.GlobalEventExecutor;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/27")
public class GlobalEventExecutorTransformer implements JsonContextTransformer<GlobalEventExecutor>, JsonStructure {

    public static final GlobalEventExecutorTransformer INSTANCE = new GlobalEventExecutorTransformer();

    @Override
    public void transform(GlobalEventExecutor in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("pendingTasks");
            out.numberValue(in.pendingTasks());
            out.key("inEventLoop");
            out.booleanValue(in.inEventLoop());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(GlobalEventExecutor.INSTANCE, jh, null);
    }
}
