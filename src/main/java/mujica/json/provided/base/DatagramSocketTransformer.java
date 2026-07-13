package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created on 2026/5/18.
 */
public class DatagramSocketTransformer implements JsonContextTransformer<DatagramSocket> {

    public static final DatagramSocketTransformer INSTANCE = new DatagramSocketTransformer();

    @Override
    public void transform(@NotNull DatagramSocket socket, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(SocketTransformer.CONNECTED);
            out.booleanValue(socket.isConnected());
            out.key(SocketTransformer.BOUND);
            out.booleanValue(socket.isBound());
            out.key(SocketTransformer.CLOSED);
            out.booleanValue(socket.isClosed());
        }
        {
            SocketAddress remote = socket.getRemoteSocketAddress();
            if (remote != null) {
                out.key(SocketTransformer.REMOTE);
                if (remote instanceof InetSocketAddress) {
                    SocketTransformer.transformInetSocketAddress((InetSocketAddress) remote, out);
                } else {
                    out.stringValue(remote.getClass().getName());
                }
            }
        }
        {
            SocketAddress local = socket.getLocalSocketAddress();
            if (local != null) {
                out.key(SocketTransformer.LOCAL);
                if (local instanceof InetSocketAddress) {
                    SocketTransformer.transformInetSocketAddress((InetSocketAddress) local, out);
                } else {
                    out.stringValue(local.getClass().getName());
                }
            }
        }
        out.closeObject();
    }
}
