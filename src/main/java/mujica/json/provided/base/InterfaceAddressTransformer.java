package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InterfaceAddress;

@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "InterfaceAddressValueSerializer")
@CodeHistory(date = "2026/4/27")
public class InterfaceAddressTransformer implements JsonContextTransformer<InterfaceAddress> {

    public static final InterfaceAddressTransformer INSTANCE = new InterfaceAddressTransformer();

    static final FastString ADDRESS = new FastString("address");

    static final FastString BROADCAST = new FastString("broadcast");

    static final FastString PREFIX_LENGTH = new FastString("prefixLength");

    @Override
    public void transform(@NotNull InterfaceAddress interfaceAddress, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            InetAddress inetAddress = interfaceAddress.getAddress();
            if (inetAddress != null) {
                out.key(ADDRESS);
                out.stringValue(inetAddress.toString());
            }
            inetAddress = interfaceAddress.getBroadcast();
            if (inetAddress != null) {
                out.key(BROADCAST);
                out.stringValue(inetAddress.toString());
            }
            out.key(PREFIX_LENGTH);
            out.numberValue(interfaceAddress.getNetworkPrefixLength());
        }
        out.closeObject();
    }
}
