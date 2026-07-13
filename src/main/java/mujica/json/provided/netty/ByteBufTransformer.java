package mujica.json.provided.netty;

import io.netty.buffer.ByteBuf;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/1/9", project = "infrastructure", name = "NettyBufferValue")
@CodeHistory(date = "2022/7/16", project = "Ultramarine", name = "NettyBufferValueSerializer")
@CodeHistory(date = "2026/5/28")
public class ByteBufTransformer implements JsonContextTransformer<ByteBuf> {

    public static final ByteBufTransformer INSTANCE = new ByteBufTransformer();

    @Override
    public void transform(@NotNull ByteBuf in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key("class");
            out.stringValue(in.getClass().getName());
            out.key("capacity");
            out.numberValue(in.capacity());
            out.key("maxCapacity");
            out.numberValue(in.maxCapacity());
            out.key("direct");
            out.booleanValue(in.isDirect());
            out.key("readOnly");
            out.booleanValue(in.isReadOnly());
            out.key("readerIndex");
            out.numberValue(in.readerIndex());
            out.key("writerIndex");
            out.numberValue(in.writerIndex());
            out.key("nioBufferCount");
            out.numberValue(in.nioBufferCount());
            out.key("hasArray");
            out.booleanValue(in.hasArray());
            out.key("hasMemoryAddress");
            out.booleanValue(in.hasMemoryAddress());
            out.key("isContiguous");
            out.booleanValue(in.isContiguous());
            out.key("referenceCount");
            out.numberValue(in.refCnt());
        }
        out.closeObject();
    }
}
