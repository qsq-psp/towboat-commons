package mujica.json.provided.netty;

import io.netty.util.NetUtil;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/13.
 */
public class NetUtilTransformer implements JsonContextTransformer<Class<NetUtil>>, JsonStructure {

    public static final NetUtilTransformer INSTANCE = new NetUtilTransformer();

    @Override
    public void transform(Class<NetUtil> ignore, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("SOMAXCONN"); // max backlog
            out.numberValue(NetUtil.SOMAXCONN);
            out.stringKey("IPv4StackPreferred");
            out.booleanValue(NetUtil.isIpV4StackPreferred());
            out.stringKey("IPv6AddressesPreferred");
            out.booleanValue(NetUtil.isIpV6AddressesPreferred());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(NetUtil.class, jh, null);
    }
}
