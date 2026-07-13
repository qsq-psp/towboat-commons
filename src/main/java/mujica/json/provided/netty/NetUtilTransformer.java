package mujica.json.provided.netty;

import io.netty.util.NetUtil;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/13")
public final class NetUtilTransformer implements JsonContextTransformer<Class<NetUtil>>, JsonStructure {

    public static final NetUtilTransformer INSTANCE = new NetUtilTransformer();

    @Override
    public void transform(@Nullable Class<NetUtil> ignore, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("SOMAXCONN"); // max backlog
            out.numberValue(NetUtil.SOMAXCONN);
            out.key("IPv4StackPreferred");
            out.booleanValue(NetUtil.isIpV4StackPreferred());
            out.key("IPv6AddressesPreferred");
            out.booleanValue(NetUtil.isIpV6AddressesPreferred());
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(NetUtil.class, jh, null);
    }
}
