package mujica.text.format;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

@CodeHistory(date = "2026/2/3", name = "UniversalStringifierUsingByteBuf")
@CodeHistory(date = "2026/3/7")
public class AppenderToByteBuf implements Function<Object, byte[]>, BiConsumer<Object, ByteBuf> {

    @Override
    @NotNull
    public byte[] apply(@Nullable Object object) {
        final ByteBuf buf = Unpooled.buffer();
        try {
            accept(object, buf);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            return data;
        } finally {
            buf.release();
        }
    }

    @Override
    public void accept(@Nullable Object object, @NotNull ByteBuf out) {
        // ...
    }
}