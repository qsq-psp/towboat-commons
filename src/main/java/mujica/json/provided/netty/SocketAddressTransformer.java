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
            jh.key("type");
            if (inetAddress instanceof Inet4Address) {
                jh.stringValue("inet4");
            } else if (inetAddress instanceof Inet6Address) {
                jh.stringValue("inet6");
            } else {
                jh.stringValue("inet");
            }
            jh.key("port");
            jh.numberValue(inetSocketAddress.getPort());
            jh.key("address");
            jh.stringValue(inetAddress.toString());
            String name = inetSocketAddress.getHostName();
            if (name != null) {
                jh.key("name");
                jh.stringValue(name);
            }
        } else if (socketAddress instanceof DomainSocketAddress) {
            jh.key("type");
            jh.stringValue("domain");
            jh.key("path");
            jh.stringValue(((DomainSocketAddress) socketAddress).path());
        } else if (socketAddress instanceof LocalAddress) {
            jh.key("type");
            jh.stringValue("local");
            jh.key("id");
            jh.stringValue(((LocalAddress) socketAddress).id());
        } else {
            jh.key("type");
            jh.stringValue(socketAddress.getClass().getName());
        }
        jh.closeObject();
    }
}
