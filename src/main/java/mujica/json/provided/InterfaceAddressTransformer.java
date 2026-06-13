package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

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
    public void transform(InterfaceAddress in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            InetAddress inetAddress = in.getAddress();
            if (inetAddress != null) {
                out.stringKey(ADDRESS);
                out.stringValue(inetAddress.toString());
            }
            inetAddress = in.getBroadcast();
            if (inetAddress != null) {
                out.stringKey(BROADCAST);
                out.stringValue(inetAddress.toString());
            }
            out.stringKey(PREFIX_LENGTH);
            out.numberValue(in.getNetworkPrefixLength());
        }
        out.closeObject();
    }
}
