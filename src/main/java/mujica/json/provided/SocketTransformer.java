package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created on 2026/5/17.
 */
public class SocketTransformer implements JsonContextTransformer<Socket> {

    public static final SocketTransformer INSTANCE = new SocketTransformer();

    static final FastString CONNECTED = new FastString("connected");

    static final FastString BOUND = new FastString("bound");

    static final FastString CLOSED = new FastString("closed");

    static final FastString INPUT_SHUTDOWN = new FastString("inputShutdown");

    static final FastString OUTPUT_SHUTDOWN = new FastString("outputShutdown");

    static final FastString REMOTE = new FastString("remote");

    static final FastString LOCAL = new FastString("local");

    @Override
    public void transform(@NotNull Socket in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(CONNECTED);
            out.booleanValue(in.isConnected());
            out.stringKey(BOUND);
            out.booleanValue(in.isBound());
            out.stringKey(CLOSED);
            out.booleanValue(in.isClosed());
            out.stringKey(INPUT_SHUTDOWN);
            out.booleanValue(in.isInputShutdown());
            out.stringKey(OUTPUT_SHUTDOWN);
            out.booleanValue(in.isOutputShutdown());
        }
        {
            SocketAddress remote = in.getRemoteSocketAddress();
            if (remote != null) {
                out.stringKey(REMOTE);
                if (remote instanceof InetSocketAddress) {
                    InetSocketAddressTransformer.INSTANCE.transform((InetSocketAddress) remote, out, context);
                } else {
                    out.stringValue(remote.getClass().getName());
                }
            }
        }
        {
            SocketAddress local = in.getLocalSocketAddress();
            if (local != null) {
                out.stringKey(LOCAL);
                if (local instanceof InetSocketAddress) {
                    InetSocketAddressTransformer.INSTANCE.transform((InetSocketAddress) local, out, context);
                } else {
                    out.stringValue(local.getClass().getName());
                }
            }
        }
        out.closeObject();
    }
}
