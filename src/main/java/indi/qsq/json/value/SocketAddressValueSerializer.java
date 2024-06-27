package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import io.netty.channel.local.LocalAddress;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created in infrastructure on 2022/3/5, named NetworkAddressValue.
 * Recreated on 2022/7/29.
 */
public class SocketAddressValueSerializer implements ValueSerializer<SocketAddress> {

    public static final SocketAddressValueSerializer INSTANCE = new SocketAddressValueSerializer();

    @Override
    public void serialize(String key, SocketAddress value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        if (value instanceof InetSocketAddress) {
            jc.key("type");
            jc.stringValue("inet");
            InetSocketAddress inetSocketAddress = (InetSocketAddress) value;
            jc.objectEntry("hostname", inetSocketAddress.getHostName());
            jc.objectEntry("address", inetSocketAddress.getAddress());
            jc.key("port");
            jc.numberValue(inetSocketAddress.getPort());
        } else if (value instanceof LocalAddress) {
            jc.key("type");
            jc.stringValue("local");
            jc.objectEntry("id", ((LocalAddress) value).id());
        } else {
            jc.key("type");
            jc.stringValue(value.getClass().getName());
        }
        jc.closeObject();
    }
}
