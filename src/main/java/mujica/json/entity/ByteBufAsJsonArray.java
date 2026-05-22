package mujica.json.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import mujica.json.reflect.ContainerConfig;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2026/4/25.
 */
@CodeHistory(date = "2026/4/25")
public class ByteBufAsJsonArray extends JsonArray implements ByteBufHolder {

    @NotNull
    protected ByteBuf data;

    public ByteBufAsJsonArray(@NotNull ByteBuf data) {
        super();
        this.data = data;
    }

    @Override
    public ByteBuf content() {
        return ByteBufUtil.ensureAccessible(data);
    }

    @Override
    public ByteBufHolder copy() {
        return replace(data.copy());
    }

    @Override
    public ByteBufHolder duplicate() {
        return replace(data.duplicate());
    }

    @Override
    public ByteBufHolder retainedDuplicate() {
        return replace(data.retainedDuplicate());
    }

    @Override
    public ByteBufHolder replace(ByteBuf content) {
        return new DefaultByteBufHolder(content);
    }

    @Override
    public int refCnt() {
        return data.refCnt();
    }

    @Override
    public ByteBufHolder retain() {
        data.retain();
        return this;
    }

    @Override
    public ByteBufHolder retain(int increment) {
        data.retain(increment);
        return this;
    }

    @Override
    public ByteBufHolder touch() {
        data.touch();
        return this;
    }

    @Override
    public ByteBufHolder touch(Object hint) {
        data.touch(hint);
        return this;
    }

    @Override
    public boolean release() {
        return data.release();
    }

    @Override
    public boolean release(int decrement) {
        return data.release(decrement);
    }

    @Override
    public int size() {
        return data.writerIndex();
    }

    @Override
    public Byte getObject(int index) {
        return data.getByte(index);
    }

    @Override
    public void setObject(int index, Object value) {
        data.setByte(index, (Byte) value);
    }

    @NotNull
    @Override
    public Consumer<Object> consumer(@NotNull ContainerConfig.ArrayAction action) {
        switch (action) {
            case NEW: {
                ByteBuf newData = data.alloc().buffer();
                data.release();
                data = newData;
                return new ConsumerImpl(0);
            }
            case CLEAR:
                data.clear();
                return new ConsumerImpl(0);
            case COVER:
                return new ConsumerImpl(0);
            case APPEND:
                return new ConsumerImpl(data.writerIndex());
            default:
                throw new IllegalArgumentException();
        }
    }

    @CodeHistory(date = "2026/4/27")
    private class ConsumerImpl implements Consumer<Object> {

        int index;

        ConsumerImpl(int index) {
            super();
            this.index = index;
        }

        @Override
        public void accept(Object value) {
            if (index < data.writerIndex()) {
                data.setByte(index, (Byte) value);
            } else {
                data.writeByte((Byte) value);
            }
            index++;
        }
    }
}
