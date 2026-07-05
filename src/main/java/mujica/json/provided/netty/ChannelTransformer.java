package mujica.json.provided.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "NettyChannelValueSerializer")
@CodeHistory(date = "2026/5/24")
public class ChannelTransformer implements JsonContextTransformer<Channel> {

    public static final ChannelTransformer INSTANCE = new ChannelTransformer();

    @Override
    public void transform(@NotNull Channel channel, @NotNull JsonHandler jh, JsonContext context) {
        jh.openObject();
        {
            jh.stringKey("id");
            jh.stringValue(channel.id().asLongText());
        }
        {
            Channel parent = channel.parent();
            if (parent != null) {
                jh.stringKey("parent");
                jh.stringValue(parent.id().asLongText());
            }
        }
        {
            jh.stringKey("config");
            ChannelConfigTransformer.INSTANCE.transform(channel.config(), jh, context);
            jh.stringKey("open");
            jh.booleanValue(channel.isOpen());
            jh.stringKey("registered");
            jh.booleanValue(channel.isRegistered());
            jh.stringKey("active");
            jh.booleanValue(channel.isActive());
        }
        {
            ChannelMetadata metadata = channel.metadata();
            if (metadata != null) {
                jh.stringKey("hasDisconnect");
                jh.booleanValue(metadata.hasDisconnect());
                jh.stringKey("defaultMaxMessagesPerRead");
                jh.numberValue(metadata.defaultMaxMessagesPerRead());
            }
        }
        {
            jh.stringKey("local");
            SocketAddressTransformer.INSTANCE.transform(channel.localAddress(), jh, context);
            jh.stringKey("remote");
            SocketAddressTransformer.INSTANCE.transform(channel.remoteAddress(), jh, context);
        }
        {
            boolean writeable = channel.isWritable();
            jh.stringKey("writeable");
            jh.booleanValue(channel.isWritable());
            if (writeable) {
                jh.stringKey("bytesToWrite");
                jh.numberValue(channel.bytesBeforeUnwritable());
            } else {
                jh.stringKey("bytesToDrain");
                jh.numberValue(channel.bytesBeforeWritable());
            }
        }
        jh.closeObject();
    }
}
