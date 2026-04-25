package mujica.json.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.DefaultByteBufHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/25.
 */
public class ByteBufAsJsonArray implements ByteBufHolder {

    @NotNull
    protected final ByteBuf data;

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
}
