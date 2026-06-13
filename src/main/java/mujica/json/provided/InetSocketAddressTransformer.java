package mujica.json.provided;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

/**
 * Created on 2026/5/10.
 */
@CodeHistory(date = "2026/5/10")
@Deprecated // inline
public class InetSocketAddressTransformer implements JsonContextTransformer<InetSocketAddress> {

    public static final InetSocketAddressTransformer INSTANCE = new InetSocketAddressTransformer();

    static final FastString PORT = new FastString("port");

    static final FastString ADDRESS = new FastString("address");

    @Override
    public void transform(@NotNull InetSocketAddress in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(PORT);
            out.numberValue(in.getPort());
            out.stringKey(ADDRESS);
            out.stringValue(in.getAddress().toString());
        }
        {
            String name = in.getHostName();
            if (name != null) {
                out.stringKey(ClassLoaderTransformer.NAME);
                out.stringValue(name);
            }
        }
        out.closeObject();
    }
}
