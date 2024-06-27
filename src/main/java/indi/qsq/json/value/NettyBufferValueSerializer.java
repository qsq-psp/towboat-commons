package indi.qsq.json.value;

import indi.qsq.json.api.JsonConsumer;
import indi.qsq.json.api.ValueSerializer;
import indi.qsq.json.reflect.ConversionConfig;
import indi.qsq.json.reflect.JsonSerializer;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/16.
 */
@SuppressWarnings("unused")
public class NettyBufferValueSerializer implements ValueSerializer<ByteBuf> {

    @Override
    public void serialize(String key, ByteBuf value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        ClassValueSerializer.INSTANCE.serialize("class", value.getClass(), jc, cc, js);
        jc.key("capacity");
        jc.numberValue(value.capacity());
        jc.key("maxCapacity");
        jc.numberValue(value.maxCapacity());
        jc.key("direct");
        jc.booleanValue(value.isDirect());
        jc.key("readOnly");
        jc.booleanValue(value.isReadOnly());
        jc.key("readerIndex");
        jc.numberValue(value.readerIndex());
        jc.key("writerIndex");
        jc.numberValue(value.writerIndex());
        jc.key("nioBufferCount");
        jc.numberValue(value.nioBufferCount());
        jc.key("hasArray");
        jc.booleanValue(value.hasArray());
        jc.key("hasMemoryAddress");
        jc.booleanValue(value.hasMemoryAddress());
        jc.key("contiguous");
        jc.booleanValue(value.isContiguous());
        jc.key("referenceCount");
        jc.numberValue(value.refCnt());
        jc.closeObject();
    }
}
