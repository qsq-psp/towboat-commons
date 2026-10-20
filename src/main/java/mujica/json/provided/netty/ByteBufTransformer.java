package mujica.json.provided.netty;

import io.netty.buffer.ByteBuf;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2022/1/9", project = "infrastructure", name = "NettyBufferValue")
@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "NettyBufferValueSerializer")
@CodeHistory(date = "2026/5/28")
public class ByteBufTransformer implements JsonContextTransformer<ByteBuf> {

    public static final ByteBufTransformer INSTANCE = new ByteBufTransformer();

    @Override
    public void transform(@NotNull ByteBuf byteBuf, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key("class");
            out.stringValue(byteBuf.getClass().getName());
            out.key("capacity");
            out.numberValue(byteBuf.capacity());
            out.key("maxCapacity");
            out.numberValue(byteBuf.maxCapacity());
            out.key("direct");
            out.booleanValue(byteBuf.isDirect());
            out.key("readOnly");
            out.booleanValue(byteBuf.isReadOnly());
            out.key("readerIndex");
            out.numberValue(byteBuf.readerIndex());
            out.key("writerIndex");
            out.numberValue(byteBuf.writerIndex());
            out.key("nioBufferCount");
            out.numberValue(byteBuf.nioBufferCount());
            out.key("hasArray");
            out.booleanValue(byteBuf.hasArray());
            out.key("hasMemoryAddress");
            out.booleanValue(byteBuf.hasMemoryAddress());
            out.key("isContiguous");
            out.booleanValue(byteBuf.isContiguous());
            out.key("referenceCount");
            out.numberValue(byteBuf.refCnt());
        }
        out.closeObject();
    }
}
