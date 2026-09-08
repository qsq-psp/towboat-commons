package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * Created on 2026/8/22.
 */
public class SelectionKeyTransformer implements JsonContextTransformer<SelectionKey> {

    public static final SelectionKeyTransformer INSTANCE = new SelectionKeyTransformer();

    @Override
    public void transform(SelectionKey in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            SelectableChannel channel = in.channel();
            if (channel != null) {
                out.key("channel");
                SelectableChannelTransformer.INSTANCE.transform(channel, out, context);
            }
        }
        {
            out.key("valid");
            out.booleanValue(in.isValid());
        }
        {
            int flags = in.interestOps();
            if (flags != 0) {
                out.key("interestedOperations");
                SelectableChannelTransformer.transformFlags(flags, out);
            }
        }
        {
            int flags = in.readyOps();
            if (flags != 0) {
                out.key("readyOperations");
                SelectableChannelTransformer.transformFlags(flags, out);
            }
        }
        out.closeObject();
    }
}
