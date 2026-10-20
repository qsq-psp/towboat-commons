package mujica.json.provided.netty;

import io.netty.channel.ChannelHandlerContext;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/6/10")
public class ChannelHandlerContextTransformer implements JsonContextTransformer<ChannelHandlerContext> {

    public static final ChannelHandlerContextTransformer INSTANCE = new ChannelHandlerContextTransformer();

    @Override
    public void transform(@NotNull ChannelHandlerContext channelHandlerContext, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("channel");
            ChannelTransformer.INSTANCE.transform(channelHandlerContext.channel(), out, context);
            out.key("name");
            out.stringValue(channelHandlerContext.name());
            out.key("removed");
            out.booleanValue(channelHandlerContext.isRemoved());
        }
        out.closeObject();
    }
}
