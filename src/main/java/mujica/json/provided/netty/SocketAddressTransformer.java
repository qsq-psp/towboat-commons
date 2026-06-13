package mujica.json.provided.netty;

import io.netty.channel.local.LocalAddress;
import io.netty.channel.unix.DomainSocketAddress;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.*;

/**
 * Created on 2026/6/9.
 */
@CodeHistory(date = "2026/6/9")
public class SocketAddressTransformer implements JsonContextTransformer<SocketAddress> {

    public static final SocketAddressTransformer INSTANCE = new SocketAddressTransformer();

    @Override
    public void transform(@NotNull SocketAddress socketAddress, @NotNull JsonHandler jh, @Nullable JsonContext context) {
        jh.openObject();
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            InetAddress inetAddress = inetSocketAddress.getAddress();
            jh.stringKey("type");
            if (inetAddress instanceof Inet4Address) {
                jh.stringValue("inet4");
            } else if (inetAddress instanceof Inet6Address) {
                jh.stringValue("inet6");
            } else {
                jh.stringValue("inet");
            }
            jh.stringKey("port");
            jh.numberValue(inetSocketAddress.getPort());
            jh.stringKey("address");
            jh.stringValue(inetAddress.toString());
            String name = inetSocketAddress.getHostName();
            if (name != null) {
                jh.stringKey("name");
                jh.stringValue(name);
            }
        } else if (socketAddress instanceof DomainSocketAddress) {
            jh.stringKey("type");
            jh.stringValue("domain");
            jh.stringKey("path");
            jh.stringValue(((DomainSocketAddress) socketAddress).path());
        } else if (socketAddress instanceof LocalAddress) {
            jh.stringKey("type");
            jh.stringValue("local");
            jh.stringKey("id");
            jh.stringValue(((LocalAddress) socketAddress).id());
        } else {
            jh.stringKey("type");
            jh.stringValue(socketAddress.getClass().getName());
        }
        jh.closeObject();
    }
}
