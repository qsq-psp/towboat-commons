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
public class SelectableChannelTransformer implements JsonContextTransformer<SelectableChannel> {

    public static final SelectableChannelTransformer INSTANCE = new SelectableChannelTransformer();

    static void transformFlags(int flags, @NotNull JsonHandler out) {
        out.openArray();
        if ((flags & SelectionKey.OP_READ) != 0) {
            out.stringValue("read");
        }
        if ((flags & SelectionKey.OP_WRITE) != 0) {
            out.stringValue("write");
        }
        if ((flags & SelectionKey.OP_CONNECT) != 0) {
            out.stringValue("connect");
        }
        if ((flags & SelectionKey.OP_ACCEPT) != 0) {
            out.stringValue("accept");
        }
        out.closeArray();
    }

    @Override
    public void transform(SelectableChannel in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("open");
            out.booleanValue(in.isOpen());
            out.key("registered");
            out.booleanValue(in.isRegistered());
            out.key("blocking");
            out.booleanValue(in.isBlocking());
        }
        {
            int flags = in.validOps();
            if (flags != 0) {
                out.key("supportedOperations");
                transformFlags(flags, out);
            }
        }
        out.closeObject();
    }
}
