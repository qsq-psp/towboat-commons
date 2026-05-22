package mujica.json.provided;

import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

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
    public void transform(@NotNull NetworkInterface in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(ClassLoaderTransformer.NAME);
            out.stringValue(in.getName());
        }
        {
            out.stringKey("inetAddress");
            out.openArray();
            Enumeration<InetAddress> enumeration = in.getInetAddresses();
            while (enumeration.hasMoreElements()) {
                out.stringValue(enumeration.nextElement().toString());
            }
            out.closeArray();
        }
        {
            out.stringKey("interfaceAddress");
            out.openArray();
            for (InterfaceAddress interfaceAddress : in.getInterfaceAddresses()) {
                InterfaceAddressTransformer.INSTANCE.transform(interfaceAddress, out, context);
            }
            out.closeArray();
        }
        {
            NetworkInterface parent = in.getParent();
            if (parent != null) {
                out.stringKey("parent");
                out.stringValue(parent.getName());
            }
        }
        {
            out.stringKey("index");
            out.numberValue(in.getIndex());
        }
        {
            String displayName = in.getDisplayName();
            if (displayName != null) {
                out.stringKey("displayName");
                out.stringValue(displayName);
            }
        }
        {
            out.stringKey("virtual");
            out.booleanValue(in.isVirtual());
        }
        out.closeObject();
    }
}
