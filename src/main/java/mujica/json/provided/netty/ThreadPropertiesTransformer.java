package mujica.json.provided.netty;

import io.netty.util.concurrent.ThreadProperties;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/22.
 */
public class ThreadPropertiesTransformer implements JsonContextTransformer<ThreadProperties> {

    public static final ThreadPropertiesTransformer INSTANCE = new ThreadPropertiesTransformer();

    @Override
    public void transform(ThreadProperties in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("state");
            out.stringValue(in.state().name()); // toString() is the same
            out.key("priority");
            out.numberValue(in.priority());
            out.key("interrupted");
            out.booleanValue(in.isInterrupted());
            out.key("daemon");
            out.booleanValue(in.isDaemon());
            out.key("name");
            out.stringValue(in.name());
            out.key("id");
            out.numberValue(in.id());
            out.key("alive");
            out.booleanValue(in.isAlive());
        }
        out.closeObject();
    }
}
