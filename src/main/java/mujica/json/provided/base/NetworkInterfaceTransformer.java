package mujica.json.provided.base;

import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@CodeHistory(date = "2022/1/10", project = "infrastructure", name = "NetworkInterfaceValue")
@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "NetworkInterfaceValueSerializer")
@CodeHistory(date = "2026/5/9")
public class NetworkInterfaceTransformer implements JsonContextTransformer<NetworkInterface> {

    public static final NetworkInterfaceTransformer INSTANCE = new NetworkInterfaceTransformer();

    @Override
    public void transform(@NotNull NetworkInterface nif, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(nif.getName());
        }
        {
            out.key("inetAddress");
            out.openArray();
            Enumeration<InetAddress> enumeration = nif.getInetAddresses();
            while (enumeration.hasMoreElements()) {
                out.stringValue(enumeration.nextElement().toString());
            }
            out.closeArray();
        }
        {
            out.key("interfaceAddress");
            out.openArray();
            for (InterfaceAddress interfaceAddress : nif.getInterfaceAddresses()) {
                InterfaceAddressTransformer.INSTANCE.transform(interfaceAddress, out, context);
            }
            out.closeArray();
        }
        {
            NetworkInterface parent = nif.getParent();
            if (parent != null) {
                out.key("parent");
                out.stringValue(parent.getName());
            }
        }
        {
            out.key("index");
            out.numberValue(nif.getIndex());
        }
        {
            String displayName = nif.getDisplayName();
            if (displayName != null) {
                out.key("displayName");
                out.stringValue(displayName);
            }
        }
        {
            out.key("virtual");
            out.booleanValue(nif.isVirtual());
        }
        out.closeObject();
    }
}
