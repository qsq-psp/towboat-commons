package mujica.json.provided.netty;

import io.netty.util.concurrent.ThreadProperties;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/22.
 */
@CodeHistory(date = "2026/6/22")
public class ThreadPropertiesTransformer implements JsonContextTransformer<ThreadProperties> {

    public static final ThreadPropertiesTransformer INSTANCE = new ThreadPropertiesTransformer();

    @Override
    public void transform(ThreadProperties threadProperties, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("state");
            out.stringValue(threadProperties.state().name()); // toString() is the same
            out.key("priority");
            out.numberValue(threadProperties.priority());
            out.key("interrupted");
            out.booleanValue(threadProperties.isInterrupted());
            out.key("daemon");
            out.booleanValue(threadProperties.isDaemon());
            out.key("name");
            out.stringValue(threadProperties.name());
            out.key("id");
            out.numberValue(threadProperties.id());
            out.key("alive");
            out.booleanValue(threadProperties.isAlive());
        }
        out.closeObject();
    }
}
