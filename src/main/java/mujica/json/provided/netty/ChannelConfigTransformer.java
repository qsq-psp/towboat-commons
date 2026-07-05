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
            out.stringKey("timeout");
            out.numberValue(channelConfig.getConnectTimeoutMillis());
            out.stringKey("writeSpinCount");
            out.numberValue(channelConfig.getWriteSpinCount());
            out.stringKey("autoRead");
            out.booleanValue(channelConfig.isAutoRead());
            out.stringKey("autoClose");
            out.booleanValue(channelConfig.isAutoRead());
            out.stringKey("writeBufferLow");
            out.numberValue(channelConfig.getWriteBufferLowWaterMark());
            out.stringKey("writeBufferHigh");
            out.numberValue(channelConfig.getWriteBufferHighWaterMark());
        }
        if (channelConfig instanceof DuplexChannelConfig) {
            out.stringKey("allowHalfClosure");
            out.booleanValue(((DuplexChannelConfig) channelConfig).isAllowHalfClosure());
        }
        if (channelConfig instanceof ServerSocketChannelConfig) {
            ServerSocketChannelConfig serverSocketChannelConfig = (ServerSocketChannelConfig) channelConfig;
            out.stringKey("backlog");
            out.numberValue(serverSocketChannelConfig.getBacklog());
            out.stringKey("reuseAddress");
            out.booleanValue(serverSocketChannelConfig.isReuseAddress());
            out.stringKey("receiveBufferSize");
            out.numberValue(serverSocketChannelConfig.getReceiveBufferSize());
        }
        if (channelConfig instanceof DatagramChannelConfig) {
            DatagramChannelConfig datagramChannelConfig = (DatagramChannelConfig) channelConfig;
            out.stringKey("sendBufferSize");
            out.numberValue(datagramChannelConfig.getSendBufferSize());
            out.stringKey("receiveBufferSize");
            out.numberValue(datagramChannelConfig.getReceiveBufferSize());
            out.stringKey("trafficClass");
            out.numberValue(datagramChannelConfig.getTrafficClass());
            out.stringKey("reuseAddress");
            out.booleanValue(datagramChannelConfig.isReuseAddress());
            out.stringKey("broadcast");
            out.booleanValue(datagramChannelConfig.isBroadcast());
            out.stringKey("loopbackDisabled");
            out.booleanValue(datagramChannelConfig.isLoopbackModeDisabled());
            out.stringKey("timeToLive");
            out.numberValue(datagramChannelConfig.getTimeToLive());
            {
                InetAddress inetAddress = datagramChannelConfig.getInterface();
                if (inetAddress != null) {
                    out.stringKey("interface");
                    out.stringValue(inetAddress.toString());
                }
            }
            {
                NetworkInterface networkInterface = datagramChannelConfig.getNetworkInterface();
                if (networkInterface != null) {
                    out.stringKey("networkInterface");
                    out.stringValue(networkInterface.getName());
                }
            }
        }
        out.closeObject();
    }
}
