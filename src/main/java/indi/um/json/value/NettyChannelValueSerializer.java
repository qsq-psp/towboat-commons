package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelId;
import io.netty.channel.EventLoop;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

/**
 * Created on 2022/7/16.
 */
@SuppressWarnings("unused")
public class NettyChannelValueSerializer implements ValueSerializer<Channel> {

    @Override
    public void serialize(String key, Channel value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        serialize("id", value.id(), jc);
        serialize("config", value.config(), jc);
        jc.key("open");
        jc.booleanValue(value.isOpen());
        jc.key("registered");
        jc.booleanValue(value.isRegistered());
        jc.key("active");
        jc.booleanValue(value.isActive());
        {
            SocketAddress address = value.localAddress();
            if (address != null) {
                SocketAddressValueSerializer.INSTANCE.serialize("local", address, jc, cc, js);
            }
            address = value.remoteAddress();
            if (address != null) {
                SocketAddressValueSerializer.INSTANCE.serialize("remote", address, jc, cc, js);
            }
        }
        {
            EventLoop eventLoop = value.eventLoop();
            if (eventLoop != null) {
                NettyEventLoopGroupValueSerializer.INSTANCE.serialize("eventLoop", eventLoop, jc, cc, js);
            }
        }
        value = value.parent();
        if (value != null) {
            serialize("parent", value.id(), jc);
        }
        jc.closeObject();
    }

    private void serialize(String key, ChannelId id, @NotNull JsonConsumer jc) {
        if (id == null) {
            return;
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.objectEntry("short", id.asShortText());
        jc.objectEntry("long", id.asLongText());
        jc.closeObject();
    }

    @SuppressWarnings("SameParameterValue")
    private void serialize(String key, ChannelConfig config, @NotNull JsonConsumer jc) {
        if (config == null) {
            return;
        }
        jc.optionalKey(key);
        jc.openObject();
        jc.key("timeout");
        jc.numberValue(config.getConnectTimeoutMillis());
        jc.key("writeSpinCount");
        jc.numberValue(config.getWriteSpinCount());
        jc.key("autoRead");
        jc.booleanValue(config.isAutoRead());
        jc.key("autoClose");
        jc.booleanValue(config.isAutoClose());
        jc.key("writeBufferLow");
        jc.numberValue(config.getWriteBufferLowWaterMark());
        jc.key("writeBufferHigh");
        jc.numberValue(config.getWriteBufferHighWaterMark());
        jc.closeObject();
    }
}
