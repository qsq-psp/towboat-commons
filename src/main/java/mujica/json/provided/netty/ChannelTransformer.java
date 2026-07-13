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
            jh.key("id");
            jh.stringValue(channel.id().asLongText());
        }
        {
            Channel parent = channel.parent();
            if (parent != null) {
                jh.key("parent");
                jh.stringValue(parent.id().asLongText());
            }
        }
        {
            jh.key("config");
            ChannelConfigTransformer.INSTANCE.transform(channel.config(), jh, context);
            jh.key("open");
            jh.booleanValue(channel.isOpen());
            jh.key("registered");
            jh.booleanValue(channel.isRegistered());
            jh.key("active");
            jh.booleanValue(channel.isActive());
        }
        {
            ChannelMetadata metadata = channel.metadata();
            if (metadata != null) {
                jh.key("hasDisconnect");
                jh.booleanValue(metadata.hasDisconnect());
                jh.key("defaultMaxMessagesPerRead");
                jh.numberValue(metadata.defaultMaxMessagesPerRead());
            }
        }
        {
            jh.key("local");
            SocketAddressTransformer.INSTANCE.transform(channel.localAddress(), jh, context);
            jh.key("remote");
            SocketAddressTransformer.INSTANCE.transform(channel.remoteAddress(), jh, context);
        }
        {
            boolean writeable = channel.isWritable();
            jh.key("writeable");
            jh.booleanValue(channel.isWritable());
            if (writeable) {
                jh.key("bytesToWrite");
                jh.numberValue(channel.bytesBeforeUnwritable());
            } else {
                jh.key("bytesToDrain");
                jh.numberValue(channel.bytesBeforeWritable());
            }
        }
        jh.closeObject();
    }
}
