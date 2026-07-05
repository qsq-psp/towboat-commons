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
            out.stringKey("class");
            out.stringValue(in.getClass().getName());
            out.stringKey("capacity");
            out.numberValue(in.capacity());
            out.stringKey("maxCapacity");
            out.numberValue(in.maxCapacity());
            out.stringKey("direct");
            out.booleanValue(in.isDirect());
            out.stringKey("readOnly");
            out.booleanValue(in.isReadOnly());
            out.stringKey("readerIndex");
            out.numberValue(in.readerIndex());
            out.stringKey("writerIndex");
            out.numberValue(in.writerIndex());
            out.stringKey("nioBufferCount");
            out.numberValue(in.nioBufferCount());
            out.stringKey("hasArray");
            out.booleanValue(in.hasArray());
            out.stringKey("hasMemoryAddress");
            out.booleanValue(in.hasMemoryAddress());
            out.stringKey("isContiguous");
            out.booleanValue(in.isContiguous());
            out.stringKey("referenceCount");
            out.numberValue(in.refCnt());
        }
        out.closeObject();
    }
}
