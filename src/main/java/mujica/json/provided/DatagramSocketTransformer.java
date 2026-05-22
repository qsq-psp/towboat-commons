package mujica.json.provided;

import mujica.json.entity.JsonHandler;
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
    public void transform(@NotNull DatagramSocket in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(SocketTransformer.CONNECTED);
            out.booleanValue(in.isConnected());
            out.stringKey(SocketTransformer.BOUND);
            out.booleanValue(in.isBound());
            out.stringKey(SocketTransformer.CLOSED);
            out.booleanValue(in.isClosed());
        }
        {
            SocketAddress remote = in.getRemoteSocketAddress();
            if (remote != null) {
                out.stringKey(SocketTransformer.REMOTE);
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
                out.stringKey(SocketTransformer.LOCAL);
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
