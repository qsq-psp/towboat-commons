package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import indi.um.util.text.HexCodec;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/17.
 */
public class NettyEventLoopGroupValueSerializer implements ValueSerializer<EventLoopGroup> {

    public static final NettyEventLoopGroupValueSerializer INSTANCE = new NettyEventLoopGroupValueSerializer();

    @Override
    public void serialize(String key, EventLoopGroup value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("class");
        jc.stringValue(value.getClass().getName());
        jc.key("hash");
        jc.stringValue(HexCodec.HEX_LOWER.hex32(System.identityHashCode(value)));
        jc.key("shuttingDown");
        jc.booleanValue(value.isShuttingDown());
        jc.key("shutDown");
        jc.booleanValue(value.isShutdown());
        jc.key("terminated");
        jc.booleanValue(value.isTerminated());
        if (value instanceof EventLoop) {
            EventLoop eventLoop = (EventLoop) value;
            jc.key("inEventLoop");
            jc.booleanValue(eventLoop.inEventLoop());
            EventLoopGroup parent = eventLoop.parent();
            if (parent != null) {
                serialize("parent", parent, jc, cc, js);
            }
        }
        jc.closeObject();
    }
}
