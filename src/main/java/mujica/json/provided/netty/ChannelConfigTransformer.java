package mujica.json.provided.netty;

import io.netty.channel.ChannelConfig;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DuplexChannelConfig;
import io.netty.channel.socket.ServerSocketChannelConfig;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Created on 2026/5/23.
 */
@CodeHistory(date = "2026/5/23")
public class ChannelConfigTransformer implements JsonContextTransformer<ChannelConfig> {

    public static final ChannelConfigTransformer INSTANCE = new ChannelConfigTransformer();

    @Override
    public void transform(@NotNull ChannelConfig channelConfig, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("timeout");
            out.numberValue(channelConfig.getConnectTimeoutMillis());
            out.key("writeSpinCount");
            out.numberValue(channelConfig.getWriteSpinCount());
            out.key("autoRead");
            out.booleanValue(channelConfig.isAutoRead());
            out.key("autoClose");
            out.booleanValue(channelConfig.isAutoRead());
            out.key("writeBufferLow");
            out.numberValue(channelConfig.getWriteBufferLowWaterMark());
            out.key("writeBufferHigh");
            out.numberValue(channelConfig.getWriteBufferHighWaterMark());
        }
        if (channelConfig instanceof DuplexChannelConfig) {
            out.key("allowHalfClosure");
            out.booleanValue(((DuplexChannelConfig) channelConfig).isAllowHalfClosure());
        }
        if (channelConfig instanceof ServerSocketChannelConfig) {
            ServerSocketChannelConfig serverSocketChannelConfig = (ServerSocketChannelConfig) channelConfig;
            out.key("backlog");
            out.numberValue(serverSocketChannelConfig.getBacklog());
            out.key("reuseAddress");
            out.booleanValue(serverSocketChannelConfig.isReuseAddress());
            out.key("receiveBufferSize");
            out.numberValue(serverSocketChannelConfig.getReceiveBufferSize());
        }
        if (channelConfig instanceof DatagramChannelConfig) {
            DatagramChannelConfig datagramChannelConfig = (DatagramChannelConfig) channelConfig;
            out.key("sendBufferSize");
            out.numberValue(datagramChannelConfig.getSendBufferSize());
            out.key("receiveBufferSize");
            out.numberValue(datagramChannelConfig.getReceiveBufferSize());
            out.key("trafficClass");
            out.numberValue(datagramChannelConfig.getTrafficClass());
            out.key("reuseAddress");
            out.booleanValue(datagramChannelConfig.isReuseAddress());
            out.key("broadcast");
            out.booleanValue(datagramChannelConfig.isBroadcast());
            out.key("loopbackDisabled");
            out.booleanValue(datagramChannelConfig.isLoopbackModeDisabled());
            out.key("timeToLive");
            out.numberValue(datagramChannelConfig.getTimeToLive());
            {
                InetAddress inetAddress = datagramChannelConfig.getInterface();
                if (inetAddress != null) {
                    out.key("interface");
                    out.stringValue(inetAddress.toString());
                }
            }
            {
                NetworkInterface networkInterface = datagramChannelConfig.getNetworkInterface();
                if (networkInterface != null) {
                    out.key("networkInterface");
                    out.stringValue(networkInterface.getName());
                }
            }
        }
        out.closeObject();
    }
}
