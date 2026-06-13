package mujica.json.provided.netty;

import io.netty.channel.ChannelConfig;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/5/23.
 */
public class ChannelConfigTransformer implements JsonContextTransformer<ChannelConfig> {

    public static final ChannelConfigTransformer INSTANCE = new ChannelConfigTransformer();

    @Override
    public void transform(@NotNull ChannelConfig in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey("timeout");
            out.numberValue(in.getConnectTimeoutMillis());
            out.stringKey("writeSpinCount");
            out.numberValue(in.getWriteSpinCount());
            out.stringKey("autoRead");
            out.booleanValue(in.isAutoRead());
            out.stringKey("autoClose");
            out.booleanValue(in.isAutoRead());
            out.stringKey("writeBufferLow");
            out.numberValue(in.getWriteBufferLowWaterMark());
            out.stringKey("writeBufferHigh");
            out.numberValue(in.getWriteBufferHighWaterMark());
        }
        out.closeObject();
    }
}
