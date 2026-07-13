package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
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

    static final FastString PORT = new FastString("port");

    static final FastString ADDRESS = new FastString("address");

    static void transformInetSocketAddress(@NotNull InetSocketAddress address, @NotNull JsonHandler out) {
        out.openObject();
        {
            out.key(PORT);
            out.numberValue(address.getPort());
            out.key(ADDRESS);
            out.stringValue(address.getAddress().toString());
        }
        {
            String name = address.getHostName();
            if (name != null) {
                out.key(ClassLoaderTransformer.NAME);
                out.stringValue(name);
            }
        }
        out.closeObject();
    }

    @Override
    public void transform(@NotNull Socket socket, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(CONNECTED);
            out.booleanValue(socket.isConnected());
            out.key(BOUND);
            out.booleanValue(socket.isBound());
            out.key(CLOSED);
            out.booleanValue(socket.isClosed());
            out.key(INPUT_SHUTDOWN);
            out.booleanValue(socket.isInputShutdown());
            out.key(OUTPUT_SHUTDOWN);
            out.booleanValue(socket.isOutputShutdown());
        }
        {
            SocketAddress remote = socket.getRemoteSocketAddress();
            if (remote != null) {
                out.key(REMOTE);
                if (remote instanceof InetSocketAddress) {
                    transformInetSocketAddress((InetSocketAddress) remote, out);
                } else {
                    out.stringValue(remote.getClass().getName());
                }
            }
        }
        {
            SocketAddress local = socket.getLocalSocketAddress();
            if (local != null) {
                out.key(LOCAL);
                if (local instanceof InetSocketAddress) {
                    transformInetSocketAddress((InetSocketAddress) local, out);
                } else {
                    out.stringValue(local.getClass().getName());
                }
            }
        }
        out.closeObject();
    }
}
